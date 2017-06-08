/*
 * init.h
 *
 *  Created on: 2 juin 2017
 *      Author: tab
 */

#ifndef INIT_H_
#define INIT_H_

#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include "nordic_common.h"
#include "nrf.h"
#include "app_error.h"
#include "ble.h"
#include "ble_hci.h"
#include "ble_srv_common.h"
#include "ble_advdata.h"
#include "ble_advertising.h"
#include "ble_conn_params.h"
#include "boards.h"
#include "softdevice_handler.h"
#include "app_timer.h"
#include "peer_manager.h"
#include "fds.h"
#include "fstorage.h"
#include "ble_conn_state.h"
#include "bsp_btn_ble.h"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "nrf_ble_qwr.h"

#include "constant.h"
#include "service_if.h"


void power_manage(void);
void bsp_event_handler(bsp_event_t event);
void assert_nrf_callback(uint16_t line_num,const uint8_t *p_file_name);

void device_init(void);

#endif /* INIT_H_ */