/**
 * @name	app.h
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 * @brief	Main application.
 */

#ifndef APP_H_
#define APP_H_

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "app_util.h"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "constant.h"
#include "init.h"
#include "service_if.h"
#include "data_management.h"

static uint8_array_t rfid_ids[RFID_ID_ARRAY_SIZE];
enum_state_t current_state;

/**
 * @brief Change the state of the application.
 * @param currentState Current state of the application
 * @param mode New state to go
 * @return Return the current state.
 */
void state_change(enum_state_t currentState, enum_mode_t mode);



/**
 * @brief Function for application main entry.
 */
void app_init();

#endif /* APP_H_ */
