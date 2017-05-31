/* This file was generated by plugin 'Nordic Semiconductor nRF5x v.1.2.2' (BDS version 1.1.3139.0) */

#ifndef BLE_STUFF_MANAGER_H__
#define BLE_STUFF_MANAGER_H__

#include <stdint.h>
#include <stdbool.h>
#include "ble.h"
#include "ble_srv_common.h"
#include "app_util_bds.h"



/**@brief Stuff Manager event type. */
typedef enum
{ 
    BLE_STUFF_MANAGER_ENTRY_VALUE_EVT_NOTIFICATION_ENABLED,  /**< Entry Value value notification enabled event. */
    BLE_STUFF_MANAGER_ENTRY_VALUE_EVT_NOTIFICATION_DISABLED, /**< Entry Value value notification disabled event. */
    BLE_STUFF_MANAGER_ENTRY_SELECTION_EVT_NOTIFICATION_ENABLED,  /**< Entry Selection value notification enabled event. */
    BLE_STUFF_MANAGER_ENTRY_SELECTION_EVT_NOTIFICATION_DISABLED, /**< Entry Selection value notification disabled event. */
    BLE_STUFF_MANAGER_ENTRY_SELECTION_EVT_WRITE, /**< Entry Selection write event. */
    BLE_STUFF_MANAGER_MANAGER_MODE_EVT_NOTIFICATION_ENABLED,  /**< Manager Mode value notification enabled event. */
    BLE_STUFF_MANAGER_MANAGER_MODE_EVT_NOTIFICATION_DISABLED, /**< Manager Mode value notification disabled event. */
    BLE_STUFF_MANAGER_MANAGER_MODE_EVT_WRITE, /**< Manager Mode write event. */
    BLE_STUFF_MANAGER_ENTRY_NUMBER_EVT_NOTIFICATION_ENABLED,  /**< Entry Number value notification enabled event. */
    BLE_STUFF_MANAGER_ENTRY_NUMBER_EVT_NOTIFICATION_DISABLED, /**< Entry Number value notification disabled event. */
} ble_stuff_manager_evt_type_t;

// Forward declaration of the ble_stuff_manager_t type.
typedef struct ble_stuff_manager_s ble_stuff_manager_t;






typedef enum
{ 
    MODE_SLEEP = 0, 
    MODE_READ = 1, 
    MODE_RECOGNITION = 2, 
} enum_mode_t; 
typedef struct
{
    enum_mode_t mode; 
} manager_mode_mode_t; 


/**@brief Entry Value structure. */
typedef struct
{
    uint8_array_t id;
} ble_stuff_manager_entry_value_t;
/**@brief Entry Selection structure. */
typedef struct
{
    uint16_t entry_number;
} ble_stuff_manager_entry_selection_t;
/**@brief Manager Mode structure. */
typedef struct
{
    manager_mode_mode_t mode;
} ble_stuff_manager_manager_mode_t;
/**@brief Entry Number structure. */
typedef struct
{
    int32_t entry_number;
} ble_stuff_manager_entry_number_t;

/**@brief Stuff Manager Service event. */
typedef struct
{
    ble_stuff_manager_evt_type_t evt_type;    /**< Type of event. */
    union {
        uint16_t cccd_value; /**< Holds decoded data in Notify and Indicate event handler. */
        ble_stuff_manager_entry_selection_t entry_selection; /**< Holds decoded data in Write event handler. */
        ble_stuff_manager_manager_mode_t manager_mode; /**< Holds decoded data in Write event handler. */
    } params;
} ble_stuff_manager_evt_t;

/**@brief Stuff Manager Service event handler type. */
typedef void (*ble_stuff_manager_evt_handler_t) (ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_evt_t * p_evt);

/**@brief Stuff Manager Service init structure. This contains all options and data needed for initialization of the service */
typedef struct
{
    ble_stuff_manager_evt_handler_t     evt_handler; /**< Event handler to be called for handling events in the Stuff Manager Service. */
    ble_stuff_manager_entry_value_t ble_stuff_manager_entry_value_initial_value; /**< If not NULL, initial value of the Entry Value characteristic. */ 
    ble_stuff_manager_entry_selection_t ble_stuff_manager_entry_selection_initial_value; /**< If not NULL, initial value of the Entry Selection characteristic. */ 
    ble_stuff_manager_manager_mode_t ble_stuff_manager_manager_mode_initial_value; /**< If not NULL, initial value of the Manager Mode characteristic. */ 
    ble_stuff_manager_entry_number_t ble_stuff_manager_entry_number_initial_value; /**< If not NULL, initial value of the Entry Number characteristic. */ 
} ble_stuff_manager_init_t;

/**@brief Stuff Manager Service structure. This contains various status information for the service.*/
struct ble_stuff_manager_s
{
    ble_stuff_manager_evt_handler_t evt_handler; /**< Event handler to be called for handling events in the Stuff Manager Service. */
    uint16_t service_handle; /**< Handle of Stuff Manager Service (as provided by the BLE stack). */
    ble_gatts_char_handles_t entry_value_handles; /**< Handles related to the Entry Value characteristic. */
    ble_gatts_char_handles_t entry_selection_handles; /**< Handles related to the Entry Selection characteristic. */
    ble_gatts_char_handles_t manager_mode_handles; /**< Handles related to the Manager Mode characteristic. */
    ble_gatts_char_handles_t entry_number_handles; /**< Handles related to the Entry Number characteristic. */
    uint16_t conn_handle; /**< Handle of the current connection (as provided by the BLE stack, is BLE_CONN_HANDLE_INVALID if not in a connection). */
};

/**@brief Function for initializing the Stuff Manager.
 *
 * @param[out]  p_stuff_manager       Stuff Manager Service structure. This structure will have to be supplied by
 *                          the application. It will be initialized by this function, and will later
 *                          be used to identify this particular service instance.
 * @param[in]   p_stuff_manager_init  Information needed to initialize the service.
 *
 * @return      NRF_SUCCESS on successful initialization of service, otherwise an error code.
 */
uint32_t ble_stuff_manager_init(ble_stuff_manager_t * p_stuff_manager, const ble_stuff_manager_init_t * p_stuff_manager_init);

/**@brief Function for handling the Application's BLE Stack events.*/
void ble_stuff_manager_on_ble_evt(ble_stuff_manager_t * p_stuff_manager, ble_evt_t * p_ble_evt);

/**@brief Function for setting the Entry Value.
 *
 * @details Sets a new value of the Entry Value characteristic. The new value will be sent
 *          to the client the next time the client reads the Entry Value characteristic.
 *          This function is only generated if the characteristic's Read property is not 'Excluded'.
 *
 * @param[in]   p_stuff_manager                 Stuff Manager Service structure.
 * @param[in]   p_entry_value  New Entry Value.
 *
 * @return      NRF_SUCCESS on success, otherwise an error code.
 */
uint32_t ble_stuff_manager_entry_value_set(ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_entry_value_t * p_entry_value);

/**@brief Function for setting the Entry Number.
 *
 * @details Sets a new value of the Entry Number characteristic. The new value will be sent
 *          to the client the next time the client reads the Entry Number characteristic.
 *          This function is only generated if the characteristic's Read property is not 'Excluded'.
 *
 * @param[in]   p_stuff_manager                 Stuff Manager Service structure.
 * @param[in]   p_entry_number  New Entry Number.
 *
 * @return      NRF_SUCCESS on success, otherwise an error code.
 */
uint32_t ble_stuff_manager_entry_number_set(ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_entry_number_t * p_entry_number);

#endif //_BLE_STUFF_MANAGER_H__
