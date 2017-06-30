/*
 * rfid.c
 *
 *  Created on: 9 juin 2017
 *      Author: colleta
 */
#include "rfid.h"

void uart_send_next_byte(uart_buffer_t* buffer){
	if ( buffer->i <buffer->size_data ){
		app_uart_put(buffer->data[buffer->i]);
		buffer->i++;
	}
}

uint8_t uart_receive_byte(uart_buffer_t* buffer, uint8_t data){
	buffer->data[buffer->i] = data;
	if ( buffer->i ==0 ){
		if (data != 0xA0 ){
			return ERR_DATA_FALSE;
		}
	}else if ( buffer->i ==1 ){
		buffer->size_data = data+1;
	}else if ( buffer->i < buffer->size_data ){
		//nothing
	}else if ( buffer->i == buffer->size_data ){
		uint8_t check =0;
		for( uint8_t i; i<( buffer->size_data+1);i++){
			check += buffer->data[i];
		}
		buffer->i = 0;
		if (check){
			return ERR_DATA_FALSE;
		}else{
			// data correct
			return DATA_FINISH;
		}

	}else{
		return ERR_DATA_FALSE;
	}
	buffer->i++;
	return RECEIVE_IN_PROGRESS;
}


void uart_handle(app_uart_evt_t * p_event)
{
    if (p_event->evt_type == APP_UART_COMMUNICATION_ERROR)
    {
        APP_ERROR_HANDLER(p_event->data.error_communication);
    }
    else if (p_event->evt_type == APP_UART_FIFO_ERROR)
    {
        APP_ERROR_HANDLER(p_event->data.error_code);
    }else if (p_event->evt_type == APP_UART_DATA){

    	uint8_t data = p_event->data.value;
    	uint8_t err_code = uart_receive_byte(uart_buffer_rx,data);
    	if (err_code >0 ){
    		// erreur
    		nrf_gpio_pin_set(LED_TOP);
    	}else if ( err_code == DATA_FINISH){
    		flag_data_receive = true;
    		nrf_gpio_pin_clear(LED_TOP);
    	}
	}else if (APP_UART_TX_EMPTY){
		 uart_send_next_byte(&uart_buffer_tx);

	}
}

/**
 * @fn uint8_t read_tag(uint8_t* data, TagUHF_t* tag){
 * @brief récupere un tags a partir des donnée récupérée
 *
 * @param uint8_t* data : une trame binaire recu du moduel YR903
 * @param TagUHF_t* tag : pointeur vers le tags, sera remplis selon les informations de la trame
 *
 * @return return 0 si aucune erreur
 * @note prévu pour un tag epc de 96 bits
 */
uint8_t read_tag(uint8_t* data, TagUHF_t* tag){
	if (data[6] != 0x10 )
		return ERR_SIZE_EPC_ID;

	tag->PC = (data[7]<<8) + data[8];

	tag->EPC[0] = (data[9]<<24) + (data[10]<<16) + (data[11]<<8) + data[12];
	tag->EPC[1] = (data[13]<<24) + (data[14]<<16) + (data[15]<<8) + data[16];
	tag->EPC[2] = (data[17]<<24) + (data[18]<<16) + (data[19]<<8) + data[20];

	tag->CRC = (data[21] <<8) + data[22];
	tag->RSSI = data[23];

	return 0;
}
/**
 * @fn tag_present ( Buffer_t* buffer, uint32_t EPC0,uint32_t EPC1,uint32_t EPC2)
 * @brief regarde si un id epc est présent dans le buffer
 *
 * @param Buffer_t* buffer poiteur sur un buffer à remplir (ensemble des tags lus)
 * @param uint32_t EPC0 4 premiers byte de l'id EPC (MSB)
 * @param uint32_t EPC1 4 bytes suivant
 * @param uint32_t EPC2 4 premiers byte de l'id EPC (LSB)
 * @return true si le tag est présent , false si le tag n'est pas présent
 */
bool tag_present ( Buffer_t* buffer, uint32_t EPC0,uint32_t EPC1,uint32_t EPC2){

	return false;
}

/**
 * @fn send_data(uint8_t* data, uint32_t nmb)
 * @brief enveoi la trame data en uart, utilise le fifo. Gère la checksum
 *
 * @param data : chaine hexadécimal à envoyé
 * @param uint32_t nmb : taille de la chaine hexadécimal à envoyer
 *
 */
static void send_data(uint8_t* data, uint32_t nmb,uart_buffer_t* buffer)
{

	uint8_t check = 0;
	uint32_t i ;
	for (i = 0; i < nmb; i++)
	{
		check =(uint8_t)(check +data[i]);
		buffer->data[i]= data[i];
	}


	buffer->size_data = nmb+1; // taille des donnée + check byte
	buffer->data[nmb] = (~check)+1;
	buffer->i = 0;
	uart_send_next_byte(buffer);
}
uart_buffer_t* allocate_buffer_uart(){

	uart_buffer_t* p = (uart_buffer_t*)malloc (sizeof(uart_buffer_t));
	if (p != NULL){
		p->i = 0;
	}
	return p;
}

uint32_t free_buffer_uart(uart_buffer_t* p){

	free(p);
	return 0;
}



/**
 * @fn inventaire(Buffer_t *buffer)
 * @brief éxecute en boucle :
									- commande pour lecture RFID par le module YR903
									- lecture et reset du buffer du module YR903
									- décodage des trames reçu pour lister les tags RFID
 *
 *
 * @param Buffer_t *buffer pointeur sur un buffer
 */
void scan_buffer(uint8_t nmb_scan){
	for (uint8_t i =0; i< nmb_scan;i++){
		send_data((uint8_t*)"\xA0\x04\x01\x80\x01", 5,&uart_buffer_tx);
	}
}

void wait_flag ( bool* flag){
	while (*flag == false);
	*flag = false;
}

void yr903_cmd_name_get_inventory_buffer(bool reset){
	if (reset){
		send_data((uint8_t*)"\xA0\x03\x01\x91", 4,&uart_buffer_tx);
	}else{
		send_data((uint8_t*)"\xA0\x03\x01\x90", 4,&uart_buffer_tx);
	}
}


uint16_t nmb_tag_in_the_buffer(uart_buffer_t* uart_buffer){
	if (uart_buffer_rx->size_data == 0x05){
		uint8_t erreur_code_yr903 = uart_buffer_rx->data[5];
		if (erreur_code_yr903 == YR903_ERROR_BUFFER_IS_EMPTY){
			nmb_tag = 0;
		}else{
			return -1;
		}
	}else{
		nmb_tag =( uart_buffer_rx->data[4]<<8 )+ uart_buffer_rx->data[5];
	}
	return nmb_tag;
}
uint32_t inventaire(Buffer_t *buffer, bool reset)
{
	clean_fifo_rx();
//   uint8_t tx_data[UART_TX_BUF_SIZE] ;
	uart_buffer_rx = allocate_buffer_uart();
	if (uart_buffer_rx ==NULL){
		return MALLOC_ERROR;
	}


	scan_buffer(NMB_SCAN_BEFORE_READ_BUFFER_YR903);


	yr903_cmd_name_get_inventory_buffer(reset);

	wait_flag(&flag_data_receive);



	// how many tag ?
	// 0 tag ?
	uint16_t nmb_tag = free_buffer_uart(tab_uart_buffer_rx[i]);

	// allocate memory
	uart_buffer_t* tab_uart_buffer_rx[nmb_tag];
	tab_uart_buffer_rx[0] = uart_buffer_rx;

	// allouer le nombre nécessaire pour la réception des messages du buffer uart
	for(uint16_t i =1; i< nmb_tag;i++){
		if ((tab_uart_buffer_rx[i] =allocate_buffer_uart()) == NULL){
			// free all tab
			for(uint16_t j =0; j< i;j++){
				free_buffer_uart(tab_uart_buffer_rx[j]);
			}
			return MALLOC_ERROR;
		}
	}

	// attendre la réception des paquets du module RFID (1 paquet par tag )
	for(uint16_t i =1; i< nmb_tag;i++){
		uart_buffer_rx = tab_uart_buffer_rx[i];
		wait_flag(&flag_data_receive);
	}




	// analyse buffer



	// free buffer
	free_buffer_uart(tab_uart_buffer_rx[0]); // existe même si aucun TAG
	for(uint16_t i =1; i< nmb_tag;i++){
		free_buffer_uart(tab_uart_buffer_rx[i]);
	}






	nrf_delay_ms(400);
	return 0;
}
