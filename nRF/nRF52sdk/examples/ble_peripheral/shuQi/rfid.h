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
 * @brief Structure du buffer YR903
 *
 * Stock la lecture UART du buffer. contient un tableau avec tout les tags
 */

typedef struct Buffer_t Buffer_t;
struct Buffer_t
{
		TagUHF_t tagUHF[10];
		uint16_t nmb_tag;
};

uint8_t read_tag(uint8_t* data, TagUHF_t* tag);
/**
 * @fn tag_present ( Buffer_t* buffer, uint32_t EPC0,uint32_t EPC1,uint32_t EPC2)
 * @brief regarde si un id epc est présent dans le buffer
 *
 * @param Buffer_t* buffer poiteur sur un buffer à remplir (ensemble des tags lus)
 * @param uint32_t EPC0 4 premiers byte de l'id EPC (MSB)
 * @param uint32_t EPC1 4 bytes suivant
 * @param uint32_t EPC2 4 derniers byte de l'id EPC (LSB)
 * @return true si le tag est présent , false si le tag n'est pas présent
 */
bool tag_present ( Buffer_t* buffer, uint32_t EPC0,uint32_t EPC1,uint32_t EPC2);


/**
 * @fn uint8_t analyse_buffer(Buffer_t* buffer)
 * @brief analyse le buffer du YR903,ce buffer inclus l'ensemble des tags lues
 *
 * @param Buffer_t* buffer poiteur sur un buffer à remplir (ensemble des tags lus)
 * @return 0 si tout c'est bien passé
 */
uint8_t analyse_buffer(Buffer_t* buffer);

/**
 * @fn send_data(uint8_t* data, uint32_t nmb)
 * @brief enveoi la trame data en uart, utilise le fifo. Gère la checksum
 *
 * @param data : chaine hexadécimal à envoyé
 * @param uint32_t nmb : taille de la chaine hexadécimal à envoyer
 *
 */
static void send_data(uint8_t* data, uint32_t nmb);

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
void inventaire(Buffer_t *buffer, bool reset);






#endif /* RFID_H_ */
