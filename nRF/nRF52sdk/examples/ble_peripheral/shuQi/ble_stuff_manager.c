/* This file was generated by plugin 'Nordic Semiconductor nRF5x v.1.2.2' (BDS version 1.1.3139.0) */

#define NRF_LOG_MODULE_NAME "BLE_STUFF_MANAGER"

#include "ble_stuff_manager.h"

#define OPCODE_LENGTH 1 /**< Length of opcode inside Stuff Manager packet. */
#define HANDLE_LENGTH 2 /**< Length of handle inside Stuff Manager packet. */

/* TODO Consider changing the max values if encoded data for characteristic/descriptor is fixed length */
#define MAX_ENTRY_VALUE_LEN (BLE_L2CAP_MTU_DEF - OPCODE_LENGTH - HANDLE_LENGTH) /**< Maximum size of a transmitted Entry Value. */ 
#define MAX_ENTRY_SELECTION_LEN (BLE_L2CAP_MTU_DEF - OPCODE_LENGTH - HANDLE_LENGTH) /**< Maximum size of a transmitted Entry Selection. */ 
#define MAX_MANAGER_MODE_LEN (BLE_L2CAP_MTU_DEF - OPCODE_LENGTH - HANDLE_LENGTH) /**< Maximum size of a transmitted Manager Mode. */ 
#define MAX_ENTRY_NUMBER_LEN (BLE_L2CAP_MTU_DEF - OPCODE_LENGTH - HANDLE_LENGTH) /**< Maximum size of a transmitted Entry Number. */ 

/**@brief Function for encoding Entry Value.
 *
 * @param[in]   p_entry_value              Entry Value characteristic structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t entry_value_encode(ble_stuff_manager_entry_value_t * p_entry_value, uint8_t * encoded_buffer) {
	uint8_t len = 0;
	len += bds_uint8_array_encode(&p_entry_value->id, &encoded_buffer[len]);
	return len;
}

/**@brief Function for encoding Entry Selection.
 *
 * @param[in]   p_entry_selection              Entry Selection characteristic structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t entry_selection_encode(ble_stuff_manager_entry_selection_t * p_entry_selection, uint8_t * encoded_buffer) {
	uint8_t len = 0;
	len += bds_uint16_encode(&p_entry_selection->entry_number, &encoded_buffer[len]);
	return len;
}

/**@brief Function for decoding Entry Selection.
 *
 * @param[in]   data_len              Length of the field to be decoded.
 * @param[in]   p_data                Buffer where the encoded data is stored.
 * @param[out]  p_write_val           Decoded data.
 *
 * @return      Length of the decoded field.
 */
static uint8_t entry_selection_decode(uint8_t data_len, uint8_t * p_data, ble_stuff_manager_entry_selection_t * p_write_val) {
	uint8_t pos = 0;
	pos += bds_uint16_decode((data_len - pos), &p_data[pos], &p_write_val->entry_number);

	return pos;
}
/**@brief Function for encoding Mode.
 *
 * @param[in]   p_mode              Mode structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t manager_mode_mode_encode(manager_mode_mode_t * p_mode, uint8_t * encoded_buffer) {
	uint8_t mode;
	mode = p_mode->mode;
	encoded_buffer[0] = mode;
	return 1;
}

/**@brief Function for encoding Manager Mode.
 *
 * @param[in]   p_manager_mode              Manager Mode characteristic structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t manager_mode_encode(ble_stuff_manager_manager_mode_t * p_manager_mode, uint8_t * encoded_buffer) {
	uint8_t len = 0;
	len += manager_mode_mode_encode(&p_manager_mode->mode, &encoded_buffer[len]);
	return len;
}

/**@brief Function for decoding Mode.
 *
 * @param[in]   data_len              Length of the field to be decoded.
 * @param[in]   p_data                Buffer where the encoded data is stored.
 * @param[out]  p_write_val           Decoded data.
 *
 * @return      Length of the decoded field.
 */
static uint8_t manager_mode_mode_decode(uint8_t data_len, uint8_t * p_data, manager_mode_mode_t * p_write_val) {
	uint8_t pos = 0;
	p_write_val->mode = (enum_mode_t) p_data[pos++];
	return pos;
}

/**@brief Function for decoding Manager Mode.
 *
 * @param[in]   data_len              Length of the field to be decoded.
 * @param[in]   p_data                Buffer where the encoded data is stored.
 * @param[out]  p_write_val           Decoded data.
 *
 * @return      Length of the decoded field.
 */
static uint8_t manager_mode_decode(uint8_t data_len, uint8_t * p_data, ble_stuff_manager_manager_mode_t * p_write_val) {
	uint8_t pos = 0;
	pos += manager_mode_mode_decode((data_len - pos), &p_data[pos], &p_write_val->mode);

	return pos;
}
/**@brief Function for encoding Entry Number.
 *
 * @param[in]   p_entry_number              Entry Number characteristic structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t entry_number_encode(ble_stuff_manager_entry_number_t * p_entry_number, uint8_t * encoded_buffer) {
	uint8_t len = 0;
	len += bds_uint16_encode(&p_entry_number->entry_number, &encoded_buffer[len]);
	return len;
}

/**@brief Function for handling the Connect event.
 *
 * @param[in]   p_stuff_manager       Stuff Manager Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
static void on_connect(ble_stuff_manager_t * p_stuff_manager, ble_evt_t * p_ble_evt) {
	p_stuff_manager->conn_handle = p_ble_evt->evt.gap_evt.conn_handle;
}

/**@brief Function for handling the Disconnect event.
 *
 * @param[in]   p_stuff_manager       Stuff Manager Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
static void on_disconnect(ble_stuff_manager_t * p_stuff_manager, ble_evt_t * p_ble_evt) {
	UNUSED_PARAMETER(p_ble_evt);
	p_stuff_manager->conn_handle = BLE_CONN_HANDLE_INVALID;
}

/**@brief Function for handling the Write event.
 *
 * @param[in]   p_stuff_manager       Stuff Manager Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
static void on_write(ble_stuff_manager_t * p_stuff_manager, ble_gatts_evt_write_t * p_ble_evt) {

	if (p_ble_evt->handle == p_stuff_manager->entry_selection_handles.value_handle) {
		if (p_stuff_manager->evt_handler != NULL) {
			ble_stuff_manager_evt_t evt;
			evt.evt_type = BLE_STUFF_MANAGER_ENTRY_SELECTION_EVT_WRITE;
			entry_selection_decode(p_ble_evt->len, p_ble_evt->data, &evt.params.entry_selection);
			p_stuff_manager->evt_handler(p_stuff_manager, &evt);
		}
	}
	if (p_ble_evt->handle == p_stuff_manager->manager_mode_handles.value_handle) {
		if (p_stuff_manager->evt_handler != NULL) {
			ble_stuff_manager_evt_t evt;
			evt.evt_type = BLE_STUFF_MANAGER_MANAGER_MODE_EVT_WRITE;
			manager_mode_decode(p_ble_evt->len, p_ble_evt->data, &evt.params.manager_mode);
			p_stuff_manager->evt_handler(p_stuff_manager, &evt);
		}
	}
}

/**@brief Authorize WRITE request event handler.
 *
 * @details Handles WRITE events from the BLE stack.
 *
 * @param[in]   p_sc_ctrlpt  SC Ctrlpt structure.
 * @param[in]   p_gatts_evt  GATTS Event received from the BLE stack.
 *
 */
static void on_rw_authorize_request(ble_stuff_manager_t * p_stuff_manager, ble_gatts_evt_t * p_gatts_evt) {
	ble_gatts_evt_rw_authorize_request_t * p_auth_req = &p_gatts_evt->params.authorize_request;
	if (p_auth_req->type == BLE_GATTS_AUTHORIZE_TYPE_WRITE) {
		if ((p_gatts_evt->params.authorize_request.request.write.op != BLE_GATTS_OP_PREP_WRITE_REQ)
				&& (p_gatts_evt->params.authorize_request.request.write.op != BLE_GATTS_OP_EXEC_WRITE_REQ_NOW)
				&& (p_gatts_evt->params.authorize_request.request.write.op != BLE_GATTS_OP_EXEC_WRITE_REQ_CANCEL)) {

			if (p_auth_req->request.write.handle == p_stuff_manager->entry_selection_handles.value_handle) {
				on_write(p_stuff_manager, &p_auth_req->request.write);
			}
			if (p_auth_req->request.write.handle == p_stuff_manager->manager_mode_handles.value_handle) {
				on_write(p_stuff_manager, &p_auth_req->request.write);
			}
		}
	}
}

/**@brief Function for handling BLE events.
 *
 * @param[in]   p_stuff_manager       Stuff Manager Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
void ble_stuff_manager_on_ble_evt(ble_stuff_manager_t * p_stuff_manager, ble_evt_t * p_ble_evt) {
	switch (p_ble_evt->header.evt_id) {
	case BLE_GAP_EVT_CONNECTED:
		on_connect(p_stuff_manager, p_ble_evt);
		break;
	case BLE_GAP_EVT_DISCONNECTED:
		on_disconnect(p_stuff_manager, p_ble_evt);
		break;
	case BLE_GATTS_EVT_WRITE:
		on_write(p_stuff_manager, &p_ble_evt->evt.gatts_evt.params.write);
		break;
	case BLE_GATTS_EVT_RW_AUTHORIZE_REQUEST:
		on_rw_authorize_request(p_stuff_manager, &p_ble_evt->evt.gatts_evt);
		break;
	default:
		//No implementation needed.
		break;
	}
}

/**@brief Function for initializing the Stuff Manager. */
uint32_t ble_stuff_manager_init(ble_stuff_manager_t * p_stuff_manager, const ble_stuff_manager_init_t * p_stuff_manager_init) {
	uint32_t err_code;
	ble_uuid_t ble_uuid;

	// Initialize service structure
	p_stuff_manager->evt_handler = p_stuff_manager_init->evt_handler;
	p_stuff_manager->conn_handle = BLE_CONN_HANDLE_INVALID;

	// Add a custom base UUID.
	ble_uuid128_t bds_base_uuid = { { 0x1A, 0xE7, 0xF5, 0x9D, 0xA2, 0x98, 0xBF, 0xBB, 0x89, 0x4A, 0xD9, 0x6E, 0x00, 0x00, 0x1A, 0xE7 } };
	uint8_t uuid_type;
	err_code = sd_ble_uuid_vs_add(&bds_base_uuid, &uuid_type);
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}
	ble_uuid.type = uuid_type;
	ble_uuid.uuid = 0xE71A;

	// Add service
	err_code = sd_ble_gatts_service_add(BLE_GATTS_SRVC_TYPE_PRIMARY, &ble_uuid, &p_stuff_manager->service_handle);
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	// Add Entry Value characteristic
	ble_stuff_manager_entry_value_t entry_value_initial_value = p_stuff_manager_init->ble_stuff_manager_entry_value_initial_value;

	uint8_t entry_value_encoded_value[MAX_ENTRY_VALUE_LEN];
	ble_add_char_params_t add_entry_value_params;
	memset(&add_entry_value_params, 0, sizeof(add_entry_value_params));

	add_entry_value_params.uuid = 0xE71D;
	add_entry_value_params.uuid_type = ble_uuid.type;
	add_entry_value_params.max_len = MAX_ENTRY_VALUE_LEN;
	add_entry_value_params.init_len = entry_value_encode(&entry_value_initial_value, entry_value_encoded_value);
	add_entry_value_params.p_init_value = entry_value_encoded_value;
	add_entry_value_params.char_props.read = 1;
	add_entry_value_params.read_access = SEC_OPEN;
	// 1 for variable length and 0 for fixed length.
	add_entry_value_params.is_var_len = 1;

	err_code = characteristic_add(p_stuff_manager->service_handle, &add_entry_value_params, &(p_stuff_manager->entry_value_handles));
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	// Add Entry Selection characteristic
	ble_stuff_manager_entry_selection_t entry_selection_initial_value = p_stuff_manager_init->ble_stuff_manager_entry_selection_initial_value;

	uint8_t entry_selection_encoded_value[MAX_ENTRY_SELECTION_LEN];
	ble_add_char_params_t add_entry_selection_params;
	memset(&add_entry_selection_params, 0, sizeof(add_entry_selection_params));

	add_entry_selection_params.uuid = 0xE71C;
	add_entry_selection_params.uuid_type = ble_uuid.type;
	add_entry_selection_params.max_len = MAX_ENTRY_SELECTION_LEN;
	add_entry_selection_params.init_len = entry_selection_encode(&entry_selection_initial_value, entry_selection_encoded_value);
	add_entry_selection_params.p_init_value = entry_selection_encoded_value;
	add_entry_selection_params.char_props.write = 1;
	add_entry_selection_params.write_access = SEC_OPEN;
	// 1 for variable length and 0 for fixed length.
	add_entry_selection_params.is_var_len = 1;

	err_code = characteristic_add(p_stuff_manager->service_handle, &add_entry_selection_params, &(p_stuff_manager->entry_selection_handles));
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	// Add Manager Mode characteristic
	ble_stuff_manager_manager_mode_t manager_mode_initial_value = p_stuff_manager_init->ble_stuff_manager_manager_mode_initial_value;

	uint8_t manager_mode_encoded_value[MAX_MANAGER_MODE_LEN];
	ble_add_char_params_t add_manager_mode_params;
	memset(&add_manager_mode_params, 0, sizeof(add_manager_mode_params));

	add_manager_mode_params.uuid = 0xE71F;
	add_manager_mode_params.uuid_type = ble_uuid.type;
	add_manager_mode_params.max_len = MAX_MANAGER_MODE_LEN;
	add_manager_mode_params.init_len = manager_mode_encode(&manager_mode_initial_value, manager_mode_encoded_value);
	add_manager_mode_params.p_init_value = manager_mode_encoded_value;
	add_manager_mode_params.char_props.write = 1;
	add_manager_mode_params.write_access = SEC_OPEN;
	// 1 for variable length and 0 for fixed length.
	add_manager_mode_params.is_var_len = 1;

	err_code = characteristic_add(p_stuff_manager->service_handle, &add_manager_mode_params, &(p_stuff_manager->manager_mode_handles));
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	// Add Entry Number characteristic
	ble_stuff_manager_entry_number_t entry_number_initial_value = p_stuff_manager_init->ble_stuff_manager_entry_number_initial_value;

	uint8_t entry_number_encoded_value[MAX_ENTRY_NUMBER_LEN];
	ble_add_char_params_t add_entry_number_params;
	memset(&add_entry_number_params, 0, sizeof(add_entry_number_params));

	add_entry_number_params.uuid = 0xE710;
	add_entry_number_params.uuid_type = ble_uuid.type;
	add_entry_number_params.max_len = MAX_ENTRY_NUMBER_LEN;
	add_entry_number_params.init_len = entry_number_encode(&entry_number_initial_value, entry_number_encoded_value);
	add_entry_number_params.p_init_value = entry_number_encoded_value;
	add_entry_number_params.char_props.read = 1;
	add_entry_number_params.read_access = SEC_OPEN;
	// 1 for variable length and 0 for fixed length.
	add_entry_number_params.is_var_len = 1;

	err_code = characteristic_add(p_stuff_manager->service_handle, &add_entry_number_params, &(p_stuff_manager->entry_number_handles));
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	return NRF_SUCCESS;
}

/**@brief Function for setting the Entry Value. */
uint32_t ble_stuff_manager_entry_value_set(ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_entry_value_t * p_entry_value) {
	ble_gatts_value_t gatts_value;
	uint8_t encoded_value[MAX_ENTRY_VALUE_LEN];

	// Initialize value struct.
	memset(&gatts_value, 0, sizeof(gatts_value));

	gatts_value.len = entry_value_encode(p_entry_value, encoded_value);
	gatts_value.offset = 0;
	gatts_value.p_value = encoded_value;

	return sd_ble_gatts_value_set(p_stuff_manager->conn_handle, p_stuff_manager->entry_value_handles.value_handle, &gatts_value);
}

/**@brief Function for setting the Entry Number. */
uint32_t ble_stuff_manager_entry_number_set(ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_entry_number_t * p_entry_number) {
	ble_gatts_value_t gatts_value;
	uint8_t encoded_value[MAX_ENTRY_NUMBER_LEN];

	// Initialize value struct.
	memset(&gatts_value, 0, sizeof(gatts_value));

	gatts_value.len = entry_number_encode(p_entry_number, encoded_value);
	gatts_value.offset = 0;
	gatts_value.p_value = encoded_value;

	return sd_ble_gatts_value_set(p_stuff_manager->conn_handle, p_stuff_manager->entry_number_handles.value_handle, &gatts_value);
}

/**@brief Function for getting the Manager Mode. */
uint32_t ble_stuff_manager_manager_mode_get(ble_stuff_manager_t * p_stuff_manager, ble_stuff_manager_manager_mode_t * p_manager_mode) {
	ble_gatts_value_t gatts_value;
	ble_stuff_manager_manager_mode_t* decoded_value = NULL;

	uint8_t get_result = sd_ble_gatts_value_get(p_stuff_manager->conn_handle, p_stuff_manager->entry_number_handles.value_handle, &gatts_value);

	manager_mode_decode(gatts_value.len, gatts_value.p_value, decoded_value);
	p_manager_mode = decoded_value;

	return get_result;
}

