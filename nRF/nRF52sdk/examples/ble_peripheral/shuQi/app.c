/*
 * app.c
 *
 *  Created on: 19 mai 2017
 *      Author: tab
 */

#define NRF_LOG_MODULE_NAME "       APP"

#include "app.h"
#include "sk6812.h"
#include "app_uart.h"
#include "app_error.h"
#include "nrf_delay.h"
#include "nrf.h"
#include "bsp.h"


#if defined(BOARD_PCA10040)
#error "PCA10040"
#endif

#define MAX_TEST_DATA_BYTES     (15U)                /**< max number of test bytes to be used for tx and rx. */
#define UART_TX_BUF_SIZE 32                         /**< UART TX buffer size. */
#define UART_RX_BUF_SIZE 256*8                           /**< UART RX buffer size. */

#define ERR_NO_DATA 1
#define ERR_DATA_FALSE 2
#define ERR_SIZE_EPC_ID 3


uint8_t  tx_data_start_inventory[5] =("\xA0\x04\x80\xFF\xDD");

/**
 * @struct structure d'un tag UHF
 * @brief stock les informations PC, EPC, CRC, RSSI
 *
 */

typedef struct TagUHF_t TagUHF_t;
struct TagUHF_t
{
    uint16_t                 PC;
    uint32_t                 EPC[3];
    uint16_t                 CRC;
    uint16_t                 RSSI;
};

/**
 * @struct Buffer_t Buffer_t
 * @brief Strucur du buffer YR903
 *
 * Stock la lecture UART du buffer. contient un tableau avec tout les tags
 */

typedef struct Buffer_t Buffer_t;
struct Buffer_t
{
		TagUHF_t tagUHF[10];
		uint16_t nmb_tag;
};




void uart_error_handle(app_uart_evt_t * p_event)
{
    if (p_event->evt_type == APP_UART_COMMUNICATION_ERROR)
    {
        APP_ERROR_HANDLER(p_event->data.error_communication);
    }
    else if (p_event->evt_type == APP_UART_FIFO_ERROR)
    {
        APP_ERROR_HANDLER(p_event->data.error_code);
    }
}
void clean_fifo_rx(void){
	uint8_t data;
	while (app_uart_get(&(data)) == NRF_SUCCESS);

}

/** @brief récupère une trame de donnée dans le fifo
 *
 * @fn uint8_t * read_data(uint8_t* data){
 * @param uint8_t* data : pointeur sur les donnée à remplire
 *
 */
uint8_t read_data(uint8_t* data){

	uint8_t len;
	uint8_t header;
	uint8_t i =0;


	while (app_uart_get(data[i]) == NRF_SUCCESS){
		i++;
	}
	header = data[i-1];
	if (header != 0xA0){
		app_uart_flush();
		return ERR_DATA_FALSE;
	}
	return 0;


	/*

	while (app_uart_get(&(len)) != NRF_SUCCESS);


	data[0] = header;
	data[1] = len;

	for (uint8_t i = 0; i<len; i++){
		if(app_uart_get(&(data[i+2])) != NRF_SUCCESS){
			app_uart_flush();
			return ERR_DATA_FALSE;
		}
	}

	return 0;

	*/
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
	for (uint32_t i = 0; i< buffer->nmb_tag; i++){
		if ((buffer->tagUHF[i].EPC[0] == EPC0)&&(buffer->tagUHF[i].EPC[1] == EPC1)&&(buffer->tagUHF[i].EPC[2] == EPC2))
			return true;
	}
	return false;

}


/**
 * @fn uint8_t analyse_buffer(Buffer_t* buffer)
 * @brief analyse le buffer du YR903,ce buffer inclus l'ensemble des tags lues
 *
 * @param Buffer_t* buffer poiteur sur un buffer à remplir (ensemble des tags lus)
 * @return 0 si tout c'est bien passé
 */
uint8_t analyse_buffer(Buffer_t* buffer){

		static uint8_t data[256];
		if (read_data(data))
			return ERR_NO_DATA;
		if (data[0] != 0xA0)
			return ERR_DATA_FALSE;

		if ((data[3] != 0x91)&&(data[3] != 0x90))
			return ERR_DATA_FALSE;
		if (data[1] == 0x04){ // erreur
			if (data[4] == 0x38){ // buffer vide
				buffer->nmb_tag = 0;
				return 0;
			}else{ // si autre erreur renvoyé le numéro de l'erreur ( détaillé page 36 du protocol YR903)
				return data[4];
			}
		}

		uint16_t i=0;
		uint16_t nmb_tag =  data[5];
		buffer->nmb_tag =  nmb_tag;
		while (i < buffer->nmb_tag){
			if (read_tag(data,&buffer->tagUHF[i]))
				return ERR_DATA_FALSE;
			i++;
			if (i < buffer->nmb_tag){
				if (read_data(data))
					return ERR_NO_DATA;
			}
		}
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
static void send_data(uint8_t* data, uint32_t nmb)
{

	uint8_t check = 0;
	for (uint32_t i = 0; i < nmb; i++)
	{
		check =(uint8_t)(check +data[i]);
		while(app_uart_put(data[i]) != NRF_SUCCESS);

	}

	check = (~check)+1;
	while(app_uart_put(check) != NRF_SUCCESS);

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
uint32_t inventaire(Buffer_t *buffer, bool reset)
{
	clean_fifo_rx();
//   uint8_t tx_data[UART_TX_BUF_SIZE] ;
	static uint8_t data[20];
	send_data((uint8_t*)"\xA0\x04\x01\x80\x01", 5);
	nrf_delay_ms(300);
	if (read_data(data)){
		nrf_gpio_pin_set(LED_TOP);
		return 1;
	}

	if (reset){
		send_data((uint8_t*)"\xA0\x03\x01\x91", 4);
	}else{
		send_data((uint8_t*)"\xA0\x03\x01\x90", 4);
	}



	nrf_delay_ms(40);


	if (analyse_buffer(buffer)){
		nrf_gpio_pin_set(LED_TOP);
		return 2;
	}
	return 0;
}

/** @brief Function for application main entry.
 */
int main(void) {
    device_init();

    //app_init();
	nrf_gpio_cfg_output(RFID_ENABLE_PIN_NUMBER);
	nrf_gpio_pin_set(RFID_ENABLE_PIN_NUMBER);

	nrf_gpio_cfg_output(LED_TOP);
	nrf_gpio_pin_clear(LED_TOP);
    uint32_t err_code;
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

    APP_UART_FIFO_INIT(&comm_params,
						 UART_RX_BUF_SIZE,
						 UART_TX_BUF_SIZE,
						 uart_error_handle,
						 APP_IRQ_PRIORITY_LOW,
						 err_code);

    APP_ERROR_CHECK(err_code);


		Buffer_t* buffer;
		uint8_t i = 0;
		bool reset;
    for (;;) {

		buffer = (Buffer_t*) malloc(sizeof(Buffer_t));
		if (buffer == NULL){
			nrf_gpio_pin_set(LED_TOP);
			while(1);
		}
		i++;
		if ( i >8){
			reset = true;
			i = 0;
		}else{
			reset = false;
		}
        inventaire(buffer,reset);


		free(buffer);
    }


    // Enter main loop.
	for (;;) {
		NRF_LOG_INFO("Power manage\n\r");
		power_manage();

		NRF_LOG_INFO("Exit power manage\n\r");
	}
}
