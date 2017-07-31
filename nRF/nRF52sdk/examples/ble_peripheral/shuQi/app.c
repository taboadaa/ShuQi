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

uint32_t err_code;
uint8_array_t buffer_ble;

/** @brief Function for application main entry.
 */
int main(void) {
    device_init();

    //app_init();
	nrf_gpio_cfg_output(RFID_ENABLE_PIN_NUMBER);
	nrf_gpio_pin_set(RFID_ENABLE_PIN_NUMBER);

	nrf_gpio_cfg_output(LED_TOP);
	nrf_gpio_pin_clear(LED_TOP);

	init_rfid();



    //UART RX is enabled




    APP_ERROR_CHECK(err_code);


    Buffer_tag_UHF_t* Buffer_tag_UHF;
	uint8_t i = 0;
	bool reset;

	sk6812_set_color( 255,0,0);

	nrf_delay_ms(500);

	sk6812_change_mode(BLUE_EFFECT);
    for (;;) {

    	Buffer_tag_UHF = (Buffer_tag_UHF_t*) malloc(sizeof(Buffer_tag_UHF_t));
		if (Buffer_tag_UHF == NULL){
			sk6812_change_mode(0);
			sk6812_set_color( 0,255,255);
		}
		i++;
		if ( i >8){
			reset = true;
			i = 0;
		}else{
			reset = false;
		}
        if (inventaire(Buffer_tag_UHF,true)){
        	sk6812_change_mode(0);
        	sk6812_set_color( 255,255,255);


        }



        free(buffer_ble.p_data);
        buffer_ble.size = 12 * Buffer_tag_UHF->size;
        buffer_ble.p_data = (uint8_t*)malloc(sizeof(uint8_t)* buffer_ble.size);
        if (buffer_ble.p_data== NULL){
			sk6812_change_mode(0);
			sk6812_set_color( 255,255,0);
		}

        tag_rfid_to_format_ble (&buffer_ble,Buffer_tag_UHF);
        set_stuff_manager_entry_value(buffer_ble);
        set_stuff_manager_entry_number(1);

        free(Buffer_tag_UHF->TagUHF);
		free(Buffer_tag_UHF);
    }


    // Enter main loop.
	for (;;) {
		NRF_LOG_INFO("Power manage\n\r");
		power_manage();

		NRF_LOG_INFO("Exit power manage\n\r");
	}
}
