/**
 * @name	data_management.h
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 */

#include "data_management.h"

/**
 * @brief Initialize the variable who store tags.
 */
void rfid_ids_init(uint8_array_t* rfidIds) {
	for (int ii = 0; ii < RFID_ID_ARRAY_SIZE; ++ii) {
		rfidIds[ii].size = RFID_ID_SIZE;
		rfidIds[ii].p_data = calloc(sizeof(uint8_t)*RFID_ID_SIZE, 1);
	}
}

/**
 * @brief Add a tag to the variable who store tags.
 */
uint16_t rfid_ids_add(uint8_array_t* rfidIds, uint16_t nbRfidIds, uint8_array_t idTag) {
	rfidIds[nbRfidIds].size = idTag.size;

	for (int ii = 0; ii < idTag.size; ++ii) {
		rfidIds[nbRfidIds].p_data[ii] = idTag.p_data[ii];
	}

	return nbRfidIds;
}

/**
 * @brief Clear the array who store tags.
 */
void rfid_ids_clear(uint8_array_t* rfidIds, uint16_t nbRfidIds) {
	for (int ii = 0; ii < nbRfidIds; ++ii) {
		free(rfidIds->p_data);
		rfidIds->size = 0;
	}
}
