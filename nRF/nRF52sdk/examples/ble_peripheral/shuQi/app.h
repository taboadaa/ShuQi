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
#include "app_util.h"

#define RFID_ID_SIZE                     96/8                                       /**< Size of rfid id. */
#define RFID_ID_ARRAY_SIZE               20                                         /**< Size of rfid array. */

static uint8_array_t* rfid_ids;



#endif /* APP_H_ */
