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
#include "init.h"
#include "service_if.h"
#include "data_management.h"

#include "nrf_delay.h"
#include "nrf_gpio.h"

#include "app_uart.h"

uint8_array_t** rfid_ids;

typedef enum
{
    STATE_SLEEP = 0,
	STATE_READ = 1,
	STATE_RECOGNITION = 2,
} enum_state_t;

void app_init();

#endif /* APP_H_ */
