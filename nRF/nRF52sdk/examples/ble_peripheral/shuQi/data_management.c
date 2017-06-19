/**
 * @name	data_management.h
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 * @brief	Data management.
 */

#include "data_management.h"

/**
 *
 * @param rfidId
 */
void rfid_ids_init(uint8_array_t* rfidIds) {
	rfidIds = calloc(sizeof(uint8_array_t) * RFID_ID_ARRAY_SIZE, 1);

	/*for (int ii = 0; ii < RFID_ID_ARRAY_SIZE; ++ii) {
		rfidIds[ii] = NULL;
	}*/
}

/**
 *
 * @param idTag
 * @return
 */
int rfid_ids_add(uint8_array_t* rfidIds, uint8_array_t idTag) {
	int ii = 0;
	bool same = false;

	uint8_array_t temp = rfidIds[ii];

	// 96 bits idTag (96/8=12)
	if (idTag.size == RFID_ID_SIZE) {
		// Check every entry if tag is already here
		while (temp.size != 0) {
			temp = rfidIds[ii];
			same = true;

			// Check every byte of tag
			for (int jj = 0; jj < RFID_ID_SIZE; ++jj) {
				if (temp.p_data[jj] != idTag.p_data[ii]) {
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
 * @param rfidIds
 * @param entry
 * @return
 */
uint8_array_t rfid_ids_get(uint8_array_t* rfidIds, int entry) {
	return rfidIds[entry];
}

