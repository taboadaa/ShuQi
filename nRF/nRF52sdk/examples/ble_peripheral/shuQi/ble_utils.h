/**
 * @name	ble_utils.h
 * @authors	Taboada Adrien, Collet Axel
 * @date	16 juin 2017
 * @brief	
 */
#ifndef BLE_UTILS_H_
#define BLE_UTILS_H_

#include <stdint.h>
#include <stdlib.h>

#include "constant.h"
#include "app.h"
#include "data_management.h"

//enum_state_t state_change(enum_state_t currentState, enum_mode_t mode);

/**
 *
 * @param entry
 * @return
 */
void get_rfid_id(int entry);

#endif /* BLE_UTILS_H_ */
