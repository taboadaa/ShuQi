/*
 * app.c
 *
 *  Created on: 19 mai 2017
 *      Author: tab
 */

#define NRF_LOG_MODULE_NAME "       APP"

#include "app.h"


/**
 *
 */
void app_init() {
	rfid_ids = calloc(sizeof(uint8_array_t*) * RFID_ID_ARRAY_SIZE, 1);
	rfid_ids_init(rfid_ids);

	NRF_LOG_INFO("App init\n");
}


enum_state_t state_change(enum_state_t currentState, enum_mode_t mode) {
	if(currentState == STATE_SLEEP) {
		if (mode == MODE_READ) {
			return STATE_READ;
		} else if(mode == MODE_RECOGNITION) {
			return STATE_RECOGNITION;
		}
	} else if(currentState == STATE_READ) {
		if(mode == MODE_SLEEP) {
			return STATE_SLEEP;
		}
	} else if(currentState == STATE_RECOGNITION) {
		if(mode == MODE_SLEEP) {
			return STATE_SLEEP;
		}
	}
	return currentState;
}

/** @brief Function for application main entry.
 */
int main(void) {
	device_init();
	app_init();

	NRF_LOG_INFO("Init complete\n");

	// Enter main loop.
	for (;;) {
		NRF_LOG_INFO("Power manage\n\r");
		power_manage();

		NRF_LOG_INFO("Exit power manage\n\r");
	}
}
