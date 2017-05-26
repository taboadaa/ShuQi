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
	//rfidIds = calloc(sizeof(uint8_array_t*) * RFID_ID_ARRAY_SIZE, 1);

	for (int ii = 0; ii < RFID_ID_ARRAY_SIZE; ++ii) {
		rfidIds[ii] = NULL;
	}
}

/**
 *
 * @param idTag
 * @return
 */
int rfid_ids_add(uint8_array_t** rfidIds, uint8_array_t* idTag) {
	int ii = 0;
	bool same = false;

	uint8_array_t* temp = rfidIds[ii];

	// 96 bits idTag (96/8=12)
	if (idTag->size == RFID_ID_SIZE) {
		// Check every entry if tag is already here
		while (temp != NULL) {
			temp = rfidIds[ii];
			same = true;

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
		rfidIds[ii] = idTag;

		return ii;
	}
}

/**
 *
 */
void app_init() {
	rfid_ids = calloc(sizeof(uint8_array_t*) * RFID_ID_ARRAY_SIZE, 1);
	rfid_id_init(rfid_ids);

	/*dbg*//*uint8_array_t* tag = malloc(sizeof(uint8_array_t));
	tag->size = RFID_ID_SIZE; tag->p_data = malloc(sizeof(uint8_t)*RFID_ID_SIZE);
	int echo = rfid_ids_add(rfid_ids, tag);*//*/dbg/*/
	NRF_LOG_INFO("App init\n");
}











