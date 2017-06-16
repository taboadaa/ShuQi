/**
 * @name	ble_utils.c
 * @authors	Taboada Adrien, Collet Axel
 * @date	16 juin 2017
 * @brief	
 */



/**
 *
 */
void set_state_new() {

}

/**
 *
 * @param entry
 * @return
 */
void get_rfid_id(int entry) {
	set_stuff_manager_entry_value(rfid_ids_get(rfid_ids, entry));
}



