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
    for (;;) {

    	Buffer_tag_UHF = (Buffer_tag_UHF_t*) malloc(sizeof(Buffer_tag_UHF_t));
		if (Buffer_tag_UHF == NULL){
			nrf_gpio_pin_set(LED_TOP);
			while(1);
		}
		i++;
		if ( i >8){
			reset = true;
			i = 0;
		}else{
			reset = false;
		}
        inventaire(Buffer_tag_UHF,true);

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
