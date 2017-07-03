/*
 * rfid.h
 *
 *  Created on: 9 juin 2017
 *      Author: colleta
 */

#ifndef RFID_H_
#define RFID_H_

#define ERR_NO_DATA 1
#define ERR_DATA_FALSE 2
#define ERR_SIZE_EPC_ID 3
#define UART_TX_BUF_SIZE 32 /**< UART TX buffer size. */
#define UART_RX_BUF_SIZE 256*8 /**< UART RX buffer size. */

#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include "app_uart.h"
#include "app_error.h"
#include "nrf_delay.h"
#include "nrf.h"
#include "app_util.h"
#include "boards.h"
#include "sk6812.h"


#define MAX_TEST_DATA_BYTES     (15U)                /**< max number of test bytes to be used for tx and rx. */
#define SIZE_BUFFER_UART 32

#define ERR_NO_DATA 1
#define ERR_DATA_FALSE 2
#define ERR_SIZE_EPC_ID 3
#define MALLOC_ERROR 4
#define ERR_BUFFER_TAILLE 5

#define DATA_FINISH 0
#define RECEIVE_IN_PROGRESS -1
#define NMB_SCAN_BEFORE_READ_BUFFER_YR903 20


#define YR903_ERROR_BUFFER_IS_EMPTY 0x38
#define YR903_ERROR_UNKNOWN -1

bool flag_data_receive ;
/**
 * @struct structure d'un tag UHF
 * @brief stock les informations PC, EPC, CRC, RSSI
 *
 */

typedef struct uart_buffer_t uart_buffer_t;
struct uart_buffer_t
{
	uint8_t i ;
	uint8_t size_data;
	uint8_t data[SIZE_BUFFER_UART];
};
typedef struct TagUHF_t TagUHF_t;

struct TagUHF_t
{
	uint16_t PC;
	uint8_t EPC[12];
	uint16_t CRC;
	uint16_t RSSI;
};
typedef struct Buffer_tag_UHF_t Buffer_tag_UHF_t;

struct Buffer_tag_UHF_t
{
	TagUHF_t* TagUHF;
	uint8_t size;
};

uart_buffer_t* uart_buffer_rx;
uart_buffer_t uart_buffer_tx;

void uart_send_next_byte(uart_buffer_t* buffer);

uint8_t uart_receive_byte(uart_buffer_t* buffer, uint8_t data);

void uart_handle(app_uart_evt_t * p_event);


/** @brief r�cup�re une trame de donn�e dans le fifo
 *
 * @fn uint8_t * read_data(uint8_t* data){
 * @param uint8_t* data : pointeur sur les donn�e � remplire
 *
 */
uint8_t read_data(uint8_t* data);
/**
 * @fn uint8_t read_tag(uint8_t* data, TagUHF_t* tag){
 * @brief r�cupere un tags a partir des donn�e r�cup�r�e
 *
 * @param uint8_t* data : une trame binaire recu du moduel YR903
 * @param TagUHF_t* tag : pointeur vers le tags, sera remplis selon les informations de la trame
 *
 * @return return 0 si aucune erreur
 * @note pr�vu pour un tag epc de 96 bits
 */
uint8_t read_tag(uint8_t* data, TagUHF_t* tag);
/**
 * @fn tag_present ( Buffer_t* buffer, uint32_t EPC0,uint32_t EPC1,uint32_t EPC2)
 * @brief regarde si un id epc est pr�sent dans le buffer
 *
 * @param Buffer_t* buffer poiteur sur un buffer � remplir (ensemble des tags lus)
 * @param uint32_t EPC0 4 premiers byte de l'id EPC (MSB)
 * @param uint32_t EPC1 4 bytes suivant
 * @param uint32_t EPC2 4 premiers byte de l'id EPC (LSB)
 * @return true si le tag est pr�sent , false si le tag n'est pas pr�sent
 */
bool tag_present (Buffer_tag_UHF_t* buffer, uint8_t* EPC);


/**
 * @fn send_data(uint8_t* data, uint32_t nmb)
 * @brief enveoi la trame data en uart, utilise le fifo. G�re la checksum
 *
 * @param data : chaine hexad�cimal � envoy�
 * @param uint32_t nmb : taille de la chaine hexad�cimal � envoyer
 *
 */
void send_data(uint8_t* data, uint32_t nmb,uart_buffer_t* buffer);


uart_buffer_t* allocate_buffer_uart();



/**
 * @fn inventaire(Buffer_t *buffer)
 * @brief �xecute en boucle :
									- commande pour lecture RFID par le module YR903
									- lecture et reset du buffer du module YR903
									- d�codage des trames re�u pour lister les tags RFID
 *
 *
 * @param Buffer_t *buffer pointeur sur un buffer
 */
uint32_t inventaire(Buffer_tag_UHF_t *buffer, bool reset);

void wait_flag ( bool* flag);
void init_rfid();
uint32_t add_tag_buffer_ble(uint8_t* buffer_ble,TagUHF_t *tag);
uint32_t tag_rfid_to_format_ble(uint8_array_t* buffer_ble,Buffer_tag_UHF_t *buffer_tag_uhf);

void read_tags(uint8_array_t buffer_ble);

#endif /* RFID_H_ */
