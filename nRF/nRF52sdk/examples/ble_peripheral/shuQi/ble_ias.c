/* This file was generated by plugin 'Nordic Semiconductor nRF5x v.1.2.2' (BDS version 1.1.3135.0) */

#include "ble_ias.h"
#include <string.h>
#include "nordic_common.h"
#include "ble_srv_common.h"
#include "app_util.h"
#include "app_util_bds.h"

#define OPCODE_LENGTH 1 /**< Length of opcode inside Immediate Alert packet. */
#define HANDLE_LENGTH 2 /**< Length of handle inside Immediate Alert packet. */

/* TODO Consider changing the max values if encoded data for characteristic/descriptor is fixed length */
#define MAX_ALERT_LEVEL_LEN (BLE_L2CAP_MTU_DEF - OPCODE_LENGTH - HANDLE_LENGTH) /**< Maximum size of a transmitted Alert Level. */ 

/**@brief Function for encoding Alert Level.
 *
 * @param[in]   p_alert_level              Alert Level structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t alert_level_alert_level_encode(alert_level_alert_level_t * p_alert_level, uint8_t * encoded_buffer) {
	uint8_t alert_level;
	alert_level = p_alert_level->alert_level;
	encoded_buffer[0] = alert_level;
	return 1;
}

/**@brief Function for encoding Alert Level.
 *
 * @param[in]   p_alert_level              Alert Level characteristic structure to be encoded.
 * @param[out]  p_encoded_buffer   Buffer where the encoded data will be written.
 *
 * @return      Size of encoded data.
 */
static uint8_t alert_level_encode(ble_ias_alert_level_t * p_alert_level, uint8_t * encoded_buffer) {
	uint8_t len = 0;
	len += alert_level_alert_level_encode(&p_alert_level->alert_level, &encoded_buffer[len]);
	return len;
}

/**@brief Function for decoding Alert Level.
 *
 * @param[in]   data_len              Length of the field to be decoded.
 * @param[in]   p_data                Buffer where the encoded data is stored.
 * @param[out]  p_write_val           Decoded data.
 *
 * @return      Length of the decoded field.
 */
static uint8_t alert_level_alert_level_decode(uint8_t data_len, uint8_t * p_data,
		alert_level_alert_level_t * p_write_val) {
	uint8_t pos = 0;
	p_write_val->alert_level = (enum_alert_level_t) p_data[pos++];
	return pos;
}

/**@brief Function for decoding Alert Level.
 *
 * @param[in]   data_len              Length of the field to be decoded.
 * @param[in]   p_data                Buffer where the encoded data is stored.
 * @param[out]  p_write_val           Decoded data.
 *
 * @return      Length of the decoded field.
 */
static uint8_t alert_level_decode(uint8_t data_len, uint8_t * p_data, ble_ias_alert_level_t * p_write_val) {
	uint8_t pos = 0;
	pos += alert_level_alert_level_decode((data_len - pos), &p_data[pos], &p_write_val->alert_level);

	return pos;
}

/**@brief Function for handling the Connect event.
 *
 * @param[in]   p_ias       Immediate Alert Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
static void on_connect(ble_ias_t * p_ias, ble_evt_t * p_ble_evt) {
	p_ias->conn_handle = p_ble_evt->evt.gap_evt.conn_handle;
}

/**@brief Function for handling the Disconnect event.
 *
 * @param[in]   p_ias       Immediate Alert Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
static void on_disconnect(ble_ias_t * p_ias, ble_evt_t * p_ble_evt) {
	UNUSED_PARAMETER(p_ble_evt);
	p_ias->conn_handle = BLE_CONN_HANDLE_INVALID;
}

/**@brief Function for handling the Write event.
 *
 * @param[in]   p_ias       Immediate Alert Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
static void on_write(ble_ias_t * p_ias, ble_gatts_evt_write_t * p_ble_evt) {

	if (p_ble_evt->handle == p_ias->alert_level_handles.value_handle) {
		if (p_ias->evt_handler != NULL) {
			ble_ias_evt_t evt;
			evt.evt_type = BLE_IAS_ALERT_LEVEL_EVT_WRITE;
			alert_level_decode(p_ble_evt->len, p_ble_evt->data, &evt.params.alert_level);
			p_ias->evt_handler(p_ias, &evt);
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
static void on_rw_authorize_request(ble_ias_t * p_ias, ble_gatts_evt_t * p_gatts_evt) {
	ble_gatts_evt_rw_authorize_request_t * p_auth_req = &p_gatts_evt->params.authorize_request;
	if (p_auth_req->type == BLE_GATTS_AUTHORIZE_TYPE_WRITE) {
		if ((p_gatts_evt->params.authorize_request.request.write.op != BLE_GATTS_OP_PREP_WRITE_REQ)
				&& (p_gatts_evt->params.authorize_request.request.write.op != BLE_GATTS_OP_EXEC_WRITE_REQ_NOW)
				&& (p_gatts_evt->params.authorize_request.request.write.op != BLE_GATTS_OP_EXEC_WRITE_REQ_CANCEL)) {

		}
	}
}

/**@brief Function for handling BLE events.
 *
 * @param[in]   p_ias       Immediate Alert Service structure.
 * @param[in]   p_ble_evt   Event received from the BLE stack.
 */
void ble_ias_on_ble_evt(ble_ias_t * p_ias, ble_evt_t * p_ble_evt) {
	switch (p_ble_evt->header.evt_id) {
	case BLE_GAP_EVT_CONNECTED:
		on_connect(p_ias, p_ble_evt);
		break;
	case BLE_GAP_EVT_DISCONNECTED:
		on_disconnect(p_ias, p_ble_evt);
		break;
	case BLE_GATTS_EVT_WRITE:
		on_write(p_ias, &p_ble_evt->evt.gatts_evt.params.write);
		break;
	case BLE_GATTS_EVT_RW_AUTHORIZE_REQUEST:
		on_rw_authorize_request(p_ias, &p_ble_evt->evt.gatts_evt);
		break;
	default:
		//No implementation needed.
		break;
	}
}

/**@brief Function for initializing the Immediate Alert. */
uint32_t ble_ias_init(ble_ias_t * p_ias, const ble_ias_init_t * p_ias_init) {
	uint32_t err_code;
	ble_uuid_t ble_uuid;

	// Initialize service structure
	p_ias->evt_handler = p_ias_init->evt_handler;
	p_ias->conn_handle = BLE_CONN_HANDLE_INVALID;

	BLE_UUID_BLE_ASSIGN(ble_uuid, 0x1802);

	// Add service
	err_code = sd_ble_gatts_service_add(BLE_GATTS_SRVC_TYPE_PRIMARY, &ble_uuid, &p_ias->service_handle);
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	// Add Alert Level characteristic
	ble_ias_alert_level_t alert_level_initial_value = p_ias_init->ble_ias_alert_level_initial_value;

	uint8_t alert_level_encoded_value[MAX_ALERT_LEVEL_LEN];
	ble_add_char_params_t add_alert_level_params;
	memset(&add_alert_level_params, 0, sizeof(add_alert_level_params));

	add_alert_level_params.uuid = 0x2A06;
	add_alert_level_params.max_len = MAX_ALERT_LEVEL_LEN;
	add_alert_level_params.init_len = alert_level_encode(&alert_level_initial_value, alert_level_encoded_value);
	add_alert_level_params.p_init_value = alert_level_encoded_value;
	add_alert_level_params.char_props.write_wo_resp = 1;
	add_alert_level_params.write_access = SEC_OPEN;
	// 1 for variable length and 0 for fixed length.
	add_alert_level_params.is_var_len = 1;

	err_code = characteristic_add(p_ias->service_handle, &add_alert_level_params, &(p_ias->alert_level_handles));
	if (err_code != NRF_SUCCESS) {
		return err_code;
	}

	return NRF_SUCCESS;
}

