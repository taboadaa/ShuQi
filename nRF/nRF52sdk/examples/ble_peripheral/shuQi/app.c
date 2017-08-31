/*
 * app.c
 *
 *  Created on: 19 mai 2017
 *      Author: tab
 */

#define NRF_LOG_MODULE_NAME "       APP"

#include "app.h"
#include "sk6812.h"
#include "app_uart.h"
#include "nrf_drv_uart.h"
#include "app_error.h"
#include "nrf_delay.h"
#include "nrf.h"
#include "bsp.h"
#include "rfid.h"

#if defined(BOARD_PCA10040)
#error "PCA10040"
#endif


#define TAILLE_ID_EPC_BYTE 12 // une id epx fait 12 byte

#define BUFFER_BLE_MALLOC_ERROR  1
#define BUFFER_BLE_MALLOC_SUCCESFULL 0
uint32_t err_code;
uint8_array_t buffer_ble[MAX_TAGS];

uint32_t init_buffer_ble(void){
	for (uint32_t i =0;i<MAX_TAGS;i++){
		buffer_ble[i].size = TAILLE_ID_EPC_BYTE;
		buffer_ble[i].p_data =(uint8_t *) malloc(sizeof(uint8_t ) * TAILLE_ID_EPC_BYTE);

		if (buffer_ble[i].p_data== NULL){
			return BUFFER_BLE_MALLOC_ERROR;
		}
	}
	return BUFFER_BLE_MALLOC_SUCCESFULL;
}
uint32_t uninit_buffer_ble(void){

	for (uint32_t i =0;i<MAX_TAGS;i++){
		free(buffer_ble[i].p_data);
	}
	return 0;
}

/** @brief Function for application main entry.
 *
 *
 */
int main(void) {
    device_init();

    //app_init();
	nrf_gpio_cfg_output(RFID_ENABLE_PIN_NUMBER);
	nrf_gpio_pin_set(RFID_ENABLE_PIN_NUMBER);

	nrf_gpio_cfg_output(LED_TOP);
	nrf_gpio_pin_clear(LED_TOP);

	init_rfid();

	if ( init_buffer_ble() != BUFFER_BLE_MALLOC_SUCCESFULL){
		sk6812_set_color( 255,255,255);
		while(1);
	}




    APP_ERROR_CHECK(err_code);


    Buffer_tag_UHF_t Buffer_tag_UHF;
	uint8_t i = 0;
	bool reset;

	sk6812_set_color( 255,0,0);

	nrf_delay_ms(500);

	sk6812_change_mode(BLUE_EFFECT);
    for (;;) {

		i++;
		if ( i >8){
			reset = true;
			i = 0;
		}else{
			reset = false;
		}

        nrf_gpio_pin_clear(LED_TOP);

        if (inventaire(&Buffer_tag_UHF,true)){
        	sk6812_change_mode(0);
        	sk6812_set_color( 255,255,255);


        }
        nrf_gpio_pin_set(LED_TOP);


        tag_rfid_to_format_ble (buffer_ble,&Buffer_tag_UHF);
        set_stuff_manager_entry_value(buffer_ble[0]);
        set_stuff_manager_entry_number(1);


        nrf_delay_ms(1000);


    }


    // Enter main loop.
	for (;;) {
		NRF_LOG_INFO("Power manage\n\r");
		power_manage();

		NRF_LOG_INFO("Exit power manage\n\r");
	}
}
