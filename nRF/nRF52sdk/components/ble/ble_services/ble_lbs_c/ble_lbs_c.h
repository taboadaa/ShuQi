/*
 * Copyright (c) 2016 Nordic Semiconductor. All Rights Reserved.
 *
 * The information contained herein is confidential property of Nordic Semiconductor. The use,
 * copying, transfer or disclosure of such information is prohibited except by express written
 * agreement with Nordic Semiconductor.
 *
 */

/**@file
 *
 * @defgroup ble_lbs_c LED Button Service Client
 * @{
 * @ingroup  ble_sdk_srv
 * @brief    The LED Button Service client can be used to set a LED, and read a button state on a
 *           LED button service server.
 *
 * @details  This module contains the APIs and types exposed by the LED Button Service Client
 *           module. These APIs and types can be used by the application to perform discovery of
 *           LED Button Service at the peer and interact with it.
 *
 * @note     The application must propagate BLE stack events to this module by calling
 *           ble_lbs_c_on_ble_evt().
 *
 */

#ifndef BLE_LBS_C_H__
#define BLE_LBS_C_H__

#include <stdint.h>
#include "ble.h"
#include "ble_db_discovery.h"

#ifdef __cplusplus
extern "C" {
#endif

#define LBS_UUID_BASE        {0x23, 0xD1, 0xBC, 0xEA, 0x5F, 0x78, 0x23, 0x15, \
                              0xDE, 0xEF, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00}
#define LBS_UUID_SERVICE     0x1523
#define LBS_UUID_BUTTON_CHAR 0x1524
#define LBS_UUID_LED_CHAR    0x1525

/**@brief LBS Client event type. */
typedef enum
{
    BLE_LBS_C_EVT_DISCOVERY_COMPLETE = 1,  /**< Event indicating that the LED Button Service has been discovered at the peer. */
    BLE_LBS_C_EVT_BUTTON_NOTIFICATION      /**< Event indicating that a notification of the LED Button Button characteristic has been received from the peer. */
} ble_lbs_c_evt_type_t;


/**@brief Structure containing the Button value received from the peer. */
typedef struct
{
    uint8_t button_state;  /**< Button Value. */
} ble_button_t;


/**@brief Structure containing the handles related to the LED Button Service found on the peer. */
typedef struct
{
    uint16_t button_cccd_handle;  /**< Handle of the CCCD of the Button characteristic. */
    uint16_t button_handle;       /**< Handle of the Button characteristic as provided by the SoftDevice. */
    uint16_t led_handle;          /**< Handle of the LED characteristic as provided by the SoftDevice. */
} lbs_db_t;


/**@brief LED Button Event structure. */
typedef struct
{
    ble_lbs_c_evt_type_t evt_type;    /**< Type of the event. */
    uint16_t             conn_handle; /**< Connection handle on which the event occured.*/
    union
    {
        ble_button_t button;          /**< Button Value received. This will be filled if the evt_type is @ref BLE_LBS_C_EVT_BUTTON_NOTIFICATION. */
        lbs_db_t     peer_db;         /**< LED Button Service related handles found on the peer device. This will be filled if the evt_type is @ref BLE_LBS_C_EVT_DISCOVERY_COMPLETE.*/
    } params;
} ble_lbs_c_evt_t;


// Forward declaration of the ble_lbs_c_t type.
typedef struct ble_lbs_c_s ble_lbs_c_t;

/**@brief   Event handler type.
 *
 * @details This is the type of the event handler that should be provided by the application
 *          of this module in order to receive events.
 */
typedef void (* ble_lbs_c_evt_handler_t) (ble_lbs_c_t * p_ble_lbs_c, ble_lbs_c_evt_t * p_evt);


/**@brief LED Button Client structure. */
struct ble_lbs_c_s
{
    uint16_t                conn_handle;  /**< Connection handle as provided by the SoftDevice. */
    lbs_db_t                peer_lbs_db;  /**< Handles related to LBS on the peer*/
    ble_lbs_c_evt_handler_t evt_handler;  /**< Application event handler to be called when there is an event related to the LED Button service. */
    uint8_t                 uuid_type;    /**< UUID type. */
};

/**@brief LED Button Client initialization structure. */
typedef struct
{
    ble_lbs_c_evt_handler_t evt_handler;  /**< Event handler to be called by the LED Button Client module whenever there is an event related to the LED Button Service. */
} ble_lbs_c_init_t;


/**@brief Function for initializing the LED Button client module.
 *
 * @details This function will register with the DB Discovery module. There it registers for the
 *          LED Button Service. Doing so will make the DB Discovery module look for the presence
 *          of a LED Button Service instance at the peer when a discovery is started.
 *
 * @param[in] p_ble_lbs_c      Pointer to the LED Button client structure.
 * @param[in] p_ble_lbs_c_init Pointer to the LED Button initialization structure containing the
 *                             initialization information.
 *
 * @retval    NRF_SUCCESS On successful initialization. Otherwise an error code. This function
 *                        propagates the error code returned by the Database Discovery module API
 *                        @ref ble_db_discovery_evt_register.
 */
uint32_t ble_lbs_c_init(ble_lbs_c_t * p_ble_lbs_c, ble_lbs_c_init_t * p_ble_lbs_c_init);

/**@brief Function for handling BLE events from the SoftDevice.
 *
 * @details This function will handle the BLE events received from the SoftDevice. If a BLE event
 *          is relevant to the LED Button Client module, then it uses it to update interval
 *          variables and, if necessary, send events to the application.
 *
 * @param[in] p_ble_lbs_c Pointer to the LED button client structure.
 * @param[in] p_ble_evt   Pointer to the BLE event.
 */
void ble_lbs_c_on_ble_evt(ble_lbs_c_t * p_ble_lbs_c, const ble_evt_t * p_ble_evt);


/**@brief Function for requesting the peer to start sending notification of the Button
 *        Characteristic.
 *
 * @details This function will enable to notification of the Button at the peer
 *          by writing to the CCCD of the Button Characteristic.
 *
 * @param[in] p_ble_lbs_c Pointer to the LED Button Client structure.
 *
 * @retval  NRF_SUCCESS If the SoftDevice has been requested to write to the CCCD of the peer.
 *                      Otherwise, an error code. This function propagates the error code returned
 *                      by the SoftDevice API @ref sd_ble_gattc_write.
 *          NRF_ERROR_INVALID_STATE if no connection handle has been assigned (@ref ble_lbs_c_handles_assign)
 *          NRF_ERROR_NULL if the given parameter is NULL
 */
uint32_t ble_lbs_c_button_notif_enable(ble_lbs_c_t * p_ble_lbs_c);


/**@brief Function for handling events from the database discovery module.
 *
 * @details Call this function when getting a callback event from the DB discovery module. This
 *          function will handle an event from the database discovery module, and determine if it
 *          relates to the discovery of LED Button service at the peer. If so, it will call the
 *          application's event handler indicating that the LED Button service has been discovered
 *          at the peer. It also populates the event with the service related information before
 *          providing it to the application.
 *
 * @param[in] p_ble_lbs_c Pointer to the LED Button client structure.
 * @param[in] p_evt Pointer to the event received from the database discovery module.
 */
void ble_lbs_on_db_disc_evt(ble_lbs_c_t * p_ble_lbs_c, const ble_db_discovery_evt_t * p_evt);


/**@brief     Function for assigning a Handles to this instance of lbs_c.
 *
 * @details Call this function when a link has been established with a peer to associate this link
 *          to this instance of the module. This makes it  possible to handle several links and
 *          associate each link to a particular instance of this module.
 *
 * @param[in] p_ble_lbs_c    Pointer to the LED Button client structure instance to associate.
 * @param[in] conn_handle    Connection handle to associate with the given LED Button Client Instance.
 * @param[in] p_peer_handles LED Button Service handles found on the peer (from @ref BLE_LBS_C_EVT_DISCOVERY_COMPLETE event).
 *
 */
uint32_t ble_lbs_c_handles_assign(ble_lbs_c_t *    p_ble_lbs_c,
                                  uint16_t         conn_handle,
                                  const lbs_db_t * p_peer_handles);


/**@brief Function for writing the LED status to the connected server.
 *
 * @param[in] p_ble_lbs_c Pointer to the LED Button client structure.
 * @param[in] status      LED status to send.
 *
 * @retval NRF_SUCCESS If the staus was sent successfully. Otherwise, an error code is returned.
 */
uint32_t ble_lbs_led_status_send(ble_lbs_c_t * p_ble_lbs_c, uint8_t status);


#ifdef __cplusplus
}
#endif

#endif // BLE_LBS_C_H__

/** @} */
