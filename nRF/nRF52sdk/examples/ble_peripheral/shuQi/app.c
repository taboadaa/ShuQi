/*
 * app.c
 *
 *  Created on: 19 mai 2017
 *      Author: tab
 */

#define NRF_LOG_MODULE_NAME "APP"

#include "app.h"


/**
 *
 */
void app_init() {
	rfid_ids = calloc(sizeof(uint8_array_t*) * RFID_ID_ARRAY_SIZE, 1);
	rfid_ids_init(rfid_ids);

	NRF_LOG_INFO("App init\n");
}











