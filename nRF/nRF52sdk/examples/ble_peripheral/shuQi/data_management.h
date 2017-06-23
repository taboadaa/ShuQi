/**
 * @name	data_management.h
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 * @brief	Data management.
 */

#ifndef DATA_MANAGEMENT_H_
#define DATA_MANAGEMENT_H_

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include "app_util.h"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "constant.h"

int rfid_ids_add(uint8_array_t* rfidIds, uint8_array_t idTag);
//uint8_array_t rfid_ids_get(uint8_array_t* rfidIds, int entry);

#endif /* DATA_MANAGEMENT_H_ */
