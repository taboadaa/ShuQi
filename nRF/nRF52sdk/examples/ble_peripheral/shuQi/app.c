/**
 * @name	app.c
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 */

#define NRF_LOG_MODULE_NAME "  APP"

#include "app.h"

//dbg
void debugdebug() {
	nb_rfid_ids = 7;
	for (int ii = 0; ii < nb_rfid_ids; ++ii) {
		rfid_ids[ii].size = RFID_ID_SIZE;
		for (int jj = 0; jj < RFID_ID_SIZE; ++jj) {
			rfid_ids[ii].p_data[jj] = ii;
		}
	}
	NRF_LOG_INFO("TAG READED\n");
}
///dbg

/**
 * @brief Initialize the application.
 */
void app_init() {
	rfid_ids_init(rfid_ids);

	nb_rfid_ids = 0;
	current_state = STATE_SLEEP;

	ble_entry_selection_write = false;
	ble_manager_mode_write = false;
}

/**
 * @brief Change the state of the application.
 */
enum_state_t state_change(enum_state_t currentState, enum_mode_t mode) {
	if (currentState == STATE_SLEEP) {
		if (mode == MODE_READ) {
			currentState = STATE_READ;
		} else if (mode == MODE_RECOGNITION) {
			currentState = STATE_RECOGNITION;
		}
	} else if (currentState == STATE_READ) {
		if (mode == MODE_SLEEP) {
			currentState = STATE_SLEEP;
		}
	} else if (currentState == STATE_RECOGNITION) {
		if (mode == MODE_SLEEP) {
			currentState = STATE_SLEEP;
		}
	}

	//log
	if (currentState == STATE_READ) {
		NRF_LOG_INFO("State READ\n");
	} else if (currentState == STATE_RECOGNITION) {
		NRF_LOG_INFO("State RECOGNITION\n");
	} else {
		NRF_LOG_INFO("State SLEEP or UNKNOWN\n");
	}

	return currentState;
}

/**
 * @brief Read tags
 */
void read_tags() {
	debugdebug(); //todo enable and scan RFID tags

	ble_set_stuff_manager_entry_number(nb_rfid_ids);

	NRF_LOG_INFO("Tags readed\n");
}

/**
 * @brief Update the Entry Value.
 */
void update_tags() {
	if (ble_entry_selection != 0) {
		uint8_array_t tmp = rfid_ids[ble_entry_selection - 1];
		ble_set_stuff_manager_entry_value(tmp);
	}

	NRF_LOG_INFO("Tag updated\n");
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
			current_state = state_change(current_state, ble_manager_mode);
			ble_manager_mode_write = false;
		}

		//State operation
		if (current_state == STATE_SLEEP) {
			nb_rfid_ids = 0;
			//todo delete rfid_ids
		} else if (current_state == STATE_READ) {
			if (nb_rfid_ids == 0) {
				read_tags();
			} else if (ble_entry_selection_write == true) {
				update_tags();
				ble_entry_selection_write = false;
			}
		}/* else if(current_state == STATE_RECOGNITION) {

		 }*/
		power_manage();
	}
}
