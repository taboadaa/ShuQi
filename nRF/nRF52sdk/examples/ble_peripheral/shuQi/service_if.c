/* This file was generated by plugin 'Nordic Semiconductor nRF5x v.1.2.2' (BDS version 1.1.3139.0) */

#define NRF_LOG_MODULE_NAME "SERVICE_IF"
#include "service_if.h"

static ble_lls_t    m_lls; 
static ble_stuff_manager_t    m_stuff_manager;

uint8_t m_stuff_manager_entry_value_initial_value_id_arr[1];


/**@brief Function for handling the Link Loss events.
 *
 * @details This function will be called for all Link Loss events which are passed to
 *          the application.
 *
 * @param[in]   p_link_loss   Link Loss structure.
 * @param[in]   p_evt   Event received from the Link Loss.
 */
static void on_lls_evt(ble_lls_t * p_lls, ble_lls_evt_t * p_evt)
{
    switch (p_evt->evt_type)
    { 
        case BLE_LLS_ALERT_LEVEL_EVT_WRITE:
        	NRF_LOG_INFO("LLS_ALERT_LEVEL evt WRITE.\n");
            break; 
        default:
            // No implementation needed.
            break;
    }
}


/**@brief Function for handling the Stuff Manager events.
 *
 * @details This function will be called for all Stuff Manager events which are passed to
 *          the application.
 *
 * @param[in]   p_stuff_manager   Stuff Manager structure.
 * @param[in]   p_evt   Event received from the Stuff Manager.
 */
static void on_stuff_manager_evt(ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_evt_t * p_evt)
{
    switch (p_evt->evt_type)
    { 
        case BLE_STUFF_MANAGER_ENTRY_SELECTION_EVT_WRITE:
        	NRF_LOG_INFO("STUFF_MANAGER_ENTRY_SELECTION evt WRITE.\n");

        	get_rfid_id(get_stuff_manager_entry_selection());

            break; 
        case BLE_STUFF_MANAGER_MANAGER_MODE_EVT_WRITE:
        	NRF_LOG_INFO("STUFF_MANAGER_MANAGER_MODE evt WRITE.\n");

        	state_change(currentState, get_stuff_manager_manager_mode());

            break; 
        default:
            // No implementation needed.
            break;
    }
}


/**@brief Function for initializing the Services generated by Bluetooth Developer Studio.
 *
 *
 * @return      NRF_SUCCESS on successful initialization of services, otherwise an error code.
 */
uint32_t bluetooth_init(void)
{
    uint32_t    err_code; 
    ble_lls_init_t    lls_init; 
    ble_stuff_manager_init_t    stuff_manager_init; 
    

    // Initialize Link Loss.
    memset(&lls_init, 0, sizeof(lls_init));

    lls_init.evt_handler = on_lls_evt; 
    lls_init.ble_lls_alert_level_initial_value.alert_level.alert_level = ALERT_LEVEL_NO_ALERT; 

    err_code = ble_lls_init(&m_lls, &lls_init);
    if (err_code != NRF_SUCCESS)
    {
        return err_code;
    } 

    // Initialize Stuff Manager.
    memset(&stuff_manager_init, 0, sizeof(stuff_manager_init));

    stuff_manager_init.evt_handler = on_stuff_manager_evt; 
    stuff_manager_init.ble_stuff_manager_entry_value_initial_value.id.size = 1;
    stuff_manager_init.ble_stuff_manager_entry_value_initial_value.id.p_data = m_stuff_manager_entry_value_initial_value_id_arr; 
    memset(&stuff_manager_init.ble_stuff_manager_entry_selection_initial_value.entry_number,
           0x00,
           sizeof(stuff_manager_init.ble_stuff_manager_entry_selection_initial_value.entry_number));
    stuff_manager_init.ble_stuff_manager_manager_mode_initial_value.mode.mode = MODE_SLEEP; 
    memset(&stuff_manager_init.ble_stuff_manager_entry_number_initial_value.entry_number,
           0x00,
           sizeof(stuff_manager_init.ble_stuff_manager_entry_number_initial_value.entry_number));

    err_code = ble_stuff_manager_init(&m_stuff_manager, &stuff_manager_init);
    if (err_code != NRF_SUCCESS)
    {
        return err_code;
    } 

    return NRF_SUCCESS;
}

/**@brief Function for handling the Application's BLE Stack events.
 *
 * @details Handles all events from the BLE stack of interest to all Bluetooth Developer Studio generated Services.
 *
 * @param[in]   p_ble_evt  Event received from the BLE stack.
 */
void bluetooth_on_ble_evt(ble_evt_t * p_ble_evt)
{ 
    ble_lls_on_ble_evt(&m_lls, p_ble_evt); 
    ble_stuff_manager_on_ble_evt(&m_stuff_manager, p_ble_evt); 
}

/*
 * Getter / Setter
 */

/**
 *
 */
void set_stuff_manager_entry_number(uint16_t value) {
	ble_stuff_manager_entry_number_t * p_entry_number = (ble_stuff_manager_entry_number_t*) malloc(
			sizeof(ble_stuff_manager_entry_number_t));
	p_entry_number->entry_number = value;

	ble_stuff_manager_entry_number_set(&m_stuff_manager, p_entry_number);

	free(p_entry_number);
}

/**
 * Setter for the characteristic Manager Mode.
 * @param value
 */
void set_stuff_manager_entry_value(uint8_array_t value) {
	ble_stuff_manager_entry_value_t * p_entry_value = (ble_stuff_manager_entry_value_t *) malloc(
			sizeof(ble_stuff_manager_entry_value_t));
	p_entry_value->id = value;

	ble_stuff_manager_entry_value_set(&m_stuff_manager, p_entry_value);

	free(p_entry_value);
}

/**
 * Getter for the characteristic Entry Selection.
 * @return Return the value of the characteristic
 */
uint16_t get_stuff_manager_entry_selection() {
	uint16_t result = m_stuff_manager.entry_selection_handles.value_handle;
	return result;
}

/**
 * Getter for the characteristic Manager Mode.
 * @return Return the value of the characteristic
 */
uint8_t get_stuff_manager_manager_mode() {
	uint8_t result = m_stuff_manager.manager_mode_handles.value_handle;
	return result;
}


