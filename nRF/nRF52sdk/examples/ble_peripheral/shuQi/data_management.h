/**
 * @name	data_management.h
 * @authors	Taboada Adrien, Collet Axel
 * @date	2017.05.30
 * @brief	Functions to manage data in the application.
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


/**
 * @brief Initialize the variable who store the tags.
 * @param rfidIds	Variable to initialize
 */
void rfid_ids_init(uint8_array_t* rfidIds);

/**
 * @brief Add a tag to the variable who store the tags.
 * @param rfidIds	Variable who's storing the tags
 * @param nbRfidIds	Number of tags saved
 * @param idTag		New tag to add
 * @return Return the number of tags stored.
 */
uint16_t rfid_ids_add(uint8_array_t* rfidIds, uint16_t nbRfidIds, uint8_array_t idTag);

#endif /* DATA_MANAGEMENT_H_ */
