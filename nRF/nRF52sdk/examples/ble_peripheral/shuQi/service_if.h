/**
 * @name	service_if.h
 * @authors	Nordic Semiconductor ASA, Taboada Adrien, Collet Axel
 * @date	2017.05.30
 * @details	This file was generated by plugin 'Nordic Semiconductor nRF5x v.1.2.2' (BDS version 1.1.3139.0)
 * @copyright	Copyright (c) 2015 Nordic Semiconductor. All Rights Reserved.
 * @brief	Bluetooth services used in the application. Services given: Link Loss Service, Stuff Manager.
 */

/* Copyright (c) 2015 Nordic Semiconductor. All Rights Reserved.
 *
 * The information contained herein is property of Nordic Semiconductor ASA.
 * Terms and conditions of usage are described in detail in NORDIC
 * SEMICONDUCTOR STANDARD SOFTWARE LICENSE AGREEMENT.
 *
 * Licensees are granted free, non-transferable use of the information. NO
 * WARRANTY of ANY KIND is provided. This heading must NOT be removed from
 * the file.
 *
 */

#ifndef BLE_DS_IF_H__
#define BLE_DS_IF_H__

#include <stdint.h>
#include <stdlib.h>
#include "app_util.h"
#include "ble.h"
#include "nrf_log.h"

#include "constant.h"
#include "ble_lls.h"
#include "ble_stuff_manager.h"

// Global variables
bool ble_entry_selection_write;
bool ble_manager_mode_write;

uint16_t ble_entry_selection;
enum_mode_t ble_manager_mode;

/**@brief Function for initializing the Services generated by Bluetooth Developer Studio.
 *
 *
 * @return      NRF_SUCCESS on successful initialization of services, otherwise an error code.
 */
uint32_t bluetooth_init(void);

/**@brief Function for handling the Application's BLE Stack events.
 *
 * @details Handles all events from the BLE stack of interest to all Bluetooth Developer Studio generated Services.
 *
 * @param[in]   p_ble_evt  Event received from the BLE stack.
 */
void bluetooth_on_ble_evt(ble_evt_t * p_ble_evt);

/**
 * @brief Function for setting the characteristic Entry Number.
 * @param value	Value to put in the characteristic
 */
void ble_set_stuff_manager_entry_number(uint16_t value);

/**
 * @brief Function for setting the characteristic Entry Value.
 * @param value	Value to put in the characteristic
 */
void ble_set_stuff_manager_entry_value(uint8_array_t value);

#endif // BLE_DS_IF_H__

/** @} */
