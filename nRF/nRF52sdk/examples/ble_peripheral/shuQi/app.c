/*
 * app.c
 *
 *  Created on: 19 mai 2017
 *      Author: tab
 */

#include "app.h"

/**
 *
 * @param rfidId
 */
void rfid_id_init(uint8_array_t** rfidIds) {
	rfidIds = calloc(sizeof(uint8_array_t) * RFID_ID_ARRAY_SIZE, 1);
}

/**
 *
 * @param idTag
 * @return
 */
int rfid_ids_add(uint8_array_t** rfidIds, uint8_array_t* idTag) {
	int ii = 0;
	bool same = true;

	uint8_array_t* temp = rfidIds[ii];

	// 96 bits idTag (96/8=12)
	if (idTag->size == RFID_ID_SIZE) {
		// Check every entry if tag is already here
		while (temp != NULL) {
			temp = rfidIds[ii];

			// Check every byte of tag
			for (int jj = 0; jj < RFID_ID_SIZE; ++jj) {
				if (temp->p_data[jj] != idTag->p_data[ii]) {
					same = false;
					break;
				}
			}

			if (same == true) {
				break;
			} else {
				ii++;
			}
		}
	}

	if (same == true) {
		return -1;
	} else {
		return ii;
	}
}

/**
 *
 */
void app_init() {
	rfid_id_init(rfid_ids);

	uint8_array_t* tag = malloc(sizeof(uint8_array_t));
	tag->size = RFID_ID_SIZE; tag->p_data = malloc(sizeof(uint8_t)*RFID_ID_SIZE);
	int echo = rfid_ids_add(rfid_ids, tag);

	char* str = NULL;
	sprintf(str, "retour rfid_add %d\n", echo);
	NRF_LOG_INFO("todo comment log un printf?");
}










