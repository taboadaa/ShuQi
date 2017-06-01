/*
 * app.h
 *
 *  Created on: 19 mai 2017
 *      Author: tab
 */

#ifndef APP_H_
#define APP_H_

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include "app_util.h"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "constant.h"
#include "data_management.h"


uint8_array_t** rfid_ids;

void app_init();

#endif /* APP_H_ */
