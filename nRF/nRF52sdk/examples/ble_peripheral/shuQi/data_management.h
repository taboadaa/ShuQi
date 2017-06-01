/*
 * data_management.h
 *
 *  Created on: 1 juin 2017
 *      Author: tab
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

void rfid_ids_init(uint8_array_t** rfidIds);
int rfid_ids_add(uint8_array_t** rfidIds, uint8_array_t* idTag);

#endif /* DATA_MANAGEMENT_H_ */
