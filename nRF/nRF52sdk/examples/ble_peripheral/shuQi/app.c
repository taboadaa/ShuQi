/**
 * @name	app.c
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 * @brief	Main application.
 */

#define NRF_LOG_MODULE_NAME "       APP"

#include "app.h"


/**
 * @brief Initialize the application.
 */
void app_init() {
	rfid_ids[0].size = 0;
	current_state = STATE_SLEEP;

	ble_entry_selection_write = false;
	ble_manager_mode_write = false;
}

/**
 * @brief Change the state of the application.
 */
void state_change(enum_state_t currentState, enum_mode_t mode) {
	if(currentState == STATE_SLEEP) {
		if (mode == MODE_READ) {
			currentState = STATE_READ;
		} else if(mode == MODE_RECOGNITION) {
			currentState = STATE_RECOGNITION;
		}
	} else if(currentState == STATE_READ) {
		if(mode == MODE_SLEEP) {
			currentState = STATE_SLEEP;
		}
	} else if(currentState == STATE_RECOGNITION) {
		if(mode == MODE_SLEEP) {
			currentState = STATE_SLEEP;
		}
	}

	//log
	if(currentState == STATE_READ) {
		NRF_LOG_INFO("State READ\n");
	} else if(currentState == STATE_RECOGNITION) {
		NRF_LOG_INFO("State RECOGNITION\n");
	} else {NRF_LOG_INFO("State SLEEP or UNKNOWN\n");}
}

/**
 * @brief Function for application main entry.
 */
int main(void) {
	device_init();
	app_init();

	NRF_LOG_INFO("Init complete\n");

	// Enter main loop.
	for (;;) {
		//State change
		if (ble_manager_mode_write == true) {
			state_change(current_state, get_stuff_manager_manager_mode());
			ble_manager_mode_write = false;
		}

		if (current_state == STATE_SLEEP) {
			power_manage();
		} else if (current_state == STATE_READ) {

		} else if (current_state == STATE_RECOGNITION) {

		}





	}
}
