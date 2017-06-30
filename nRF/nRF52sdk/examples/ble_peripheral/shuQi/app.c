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


#if defined(BOARD_PCA10040)
#error "PCA10040"
#endif


/** @brief Function for application main entry.
 */
int main(void) {
    device_init();

    //app_init();
	nrf_gpio_cfg_output(RFID_ENABLE_PIN_NUMBER);
	nrf_gpio_pin_set(RFID_ENABLE_PIN_NUMBER);

	nrf_gpio_cfg_output(LED_TOP);
	nrf_gpio_pin_clear(LED_TOP);


    const app_uart_comm_params_t comm_params =
          {
              RX_PIN_NUMBER,
              TX_PIN_NUMBER,
              RTS_PIN_NUMBER,
              CTS_PIN_NUMBER,
              APP_UART_FLOW_CONTROL_DISABLED,
              false,
              UART_BAUDRATE_BAUDRATE_Baud115200
          };

    app_uart_init(&comm_params,NULL,uart_handle,APP_IRQ_PRIORITY_LOW );
    //UART RX is enabled




    APP_ERROR_CHECK(err_code);


		Buffer_t* buffer;
		uint8_t i = 0;
		bool reset;
    for (;;) {

		buffer = (Buffer_t*) malloc(sizeof(Buffer_t));
		if (buffer == NULL){
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
        inventaire(buffer,true);


		free(buffer);
    }


    // Enter main loop.
	for (;;) {
		NRF_LOG_INFO("Power manage\n\r");
		power_manage();

		NRF_LOG_INFO("Exit power manage\n\r");
	}
}
