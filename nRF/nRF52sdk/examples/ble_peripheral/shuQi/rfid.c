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
    	uart_communication_status = UART_COMMUNICATION_ERROR;
        APP_ERROR_HANDLER(p_event->data.error_communication);
    }
    else if (p_event->evt_type == APP_UART_FIFO_ERROR)
    {
    	uart_communication_status = UART_ERREUR_FIFO;
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

	}else{
		uart_communication_status = UART_ERREUR_UNKNOWN;
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


	for (uint8_t i=0;i<12;i++){
		tag->EPC[i]= data[9+i];
	}

	tag->CRC = (data[21] <<8) + data[22];
	tag->RSSI = data[23];
	return 0;
}

/**
 * @fn send_data(uint8_t* data, uint32_t nmb)
 * @brief enveoi la trame data en uart, utilise le fifo. Gère la checksum
 *
 * @param data : chaine hexadécimal à envoyé
 * @param uint32_t nmb : taille de la chaine hexadécimal à envoyer
 *
 */
void send_data(uint8_t* data, uint32_t nmb,uart_buffer_t* buffer)
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

		wait_flag(&flag_data_receive);
	}
}

void wait_flag (bool* flag){
	sk6812_change_mode(0);
	while (*flag == false){
		if (  uart_communication_status != UART_CORRECT){
			break;
		}
	}
	*flag = false;
	sk6812_change_mode(1);
}

void yr903_cmd_name_get_inventory_buffer(bool reset){
	if (reset){
		send_data((uint8_t*)"\xA0\x03\x01\x91", 4,&uart_buffer_tx);
	}else{
		send_data((uint8_t*)"\xA0\x03\x01\x90", 4,&uart_buffer_tx);
	}
}


uint16_t nmb_tag_in_the_buffer(uart_buffer_t* uart_buffer){
	uint16_t nmb_tag;
	if (uart_buffer_rx->size_data == 0x05){
		uint8_t erreur_code_yr903 = uart_buffer_rx->data[4];
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
uint32_t inventaire(Buffer_tag_UHF_t *buffer_tag_uhf, bool reset)
{
//   uint8_t tx_data[UART_TX_BUF_SIZE] ;
	uart_buffer_rx = &tab_uart_buffer_rx[0];
	if (uart_buffer_rx ==NULL){
		return MALLOC_ERROR;
	}


	scan_buffer(NMB_SCAN_BEFORE_READ_BUFFER_YR903);


	yr903_cmd_name_get_inventory_buffer(reset);




	// how many tag ?
	// 0 tag ?
	wait_flag(&flag_data_receive);

	int16_t nmb_tag =  nmb_tag_in_the_buffer(uart_buffer_rx );




	// attendre la réception des paquets du module RFID (1 paquet par tag )
	for(uint16_t i =1; i< nmb_tag;i++){
		uart_buffer_rx = &tab_uart_buffer_rx[i];
		wait_flag(&flag_data_receive);
		if ( uart_communication_status != UART_CORRECT){
			return ERR_COMMUNICATION_UART;
		}

	}


	// allouer buffer_tag rfid
	buffer_tag_uhf->size = nmb_tag;
	buffer_tag_uhf->TagUHF = (TagUHF_t*)malloc(sizeof(TagUHF_t)*nmb_tag);
	if ( buffer_tag_uhf->TagUHF == NULL){
		return MALLOC_ERROR;
	}


	// analyse buffer
	for(uint16_t i =0; i< nmb_tag;i++){
		read_tag(tab_uart_buffer_rx[i].data, &buffer_tag_uhf->TagUHF[i]);
	}


	return 0;
}

void init_rfid(){

	flag_data_receive= false;
	const app_uart_comm_params_t comm_params =
		   {
			   RX_PIN_NUMBER,
			   TX_PIN_NUMBER,
			   RTS_PIN_NUMBER,
			   CTS_PIN_NUMBER,
			   APP_UART_FLOW_CONTROL_DISABLED,
			   false,
			   UART_BAUDRATE_BAUDRATE_Baud115200
		   };

	 app_uart_init(&comm_params,NULL,uart_handle,APP_IRQ_PRIORITY_LOW );

	nrf_gpio_cfg_input(RX_PIN_NUMBER, NRF_GPIO_PIN_PULLUP);
	nrf_gpio_cfg_input(TX_PIN_NUMBER, NRF_GPIO_PIN_PULLUP);

}
uint32_t add_tag_buffer_ble(uint8_t* buffer_ble,TagUHF_t *tag){

	for(uint8_t i =0; i<12;i++){
		// le format de tag est stocker en MSB en premier mais est envoyé par BLE avec LSB en premier
		buffer_ble[i]= tag->EPC[i];
	}
	return 0;
}

uint32_t tag_rfid_to_format_ble(uint8_array_t* buffer_ble,Buffer_tag_UHF_t *buffer_tag_uhf){

	if ( buffer_tag_uhf->size > 10){
		return ERR_BUFFER_TAILLE;
	}

	for (uint8_t i =0; i< buffer_tag_uhf->size; i++){
		add_tag_buffer_ble(buffer_ble[i].p_data,&buffer_tag_uhf->TagUHF[i]);
	}
	return 0;
}
