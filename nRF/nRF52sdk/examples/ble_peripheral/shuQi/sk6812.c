/*
 * sk6812.c
 *
 *  Created on: 8 juin 2017
 *      Author: colleta
 */
#include "sk6812.h"

/**
 * @brief SPI user event handler.
 * @param event
 */
void spi_event_handler(nrf_drv_spi_evt_t const * p_event){
    spi_xfer_done = true;
}

/**
 * @brief SPI user event handler.
 * @param uint8_t r,g,b : color
 * @param uint8_t* buffer ( buffer de 12 bytes )
 *
 *
*/

uint32_t data_led_rgb (uint8_t* color, uint8_t* buffer){
   uint8_t i,j;
   uint8_t tmp; // variable temporaire
   uint8_t pos_buffer=0;

   for ( j =0; j<3;j++){
      for ( i =0; i<8;i+=2){
         tmp = (color[j]>>i) & 0x03;

         switch (tmp){
          case 0 :
             buffer[pos_buffer] = 0xCC;
          break;
          case 1 :
             buffer[pos_buffer] = 0xCE;
          break;

          case 2:
             buffer[pos_buffer] = 0xEC;
          break;

          case 3:
             buffer[pos_buffer] = 0xEE;
          break;

          default :
            return 1;
         }
         pos_buffer++;
      }
   }

   return 0;

}

uint32_t sk6812_init (){

	nrf_drv_spi_config_t spi_config = NRF_DRV_SPI_DEFAULT_CONFIG;


	spi_config.frequency = (0x33333300UL);
	spi_config.mosi_pin = 4;
	spi_config.sck_pin  = 3;
	APP_ERROR_CHECK(nrf_drv_spi_init(&spi, &spi_config, spi_event_handler));
	return 0;

}

uint32_t sk6812_set_color( uint8_t r,uint8_t g,uint8_t b){


	static uint8_t sk6812_init_state = 0;
	if (!(sk6812_init_state)){
		sk6812_init ();
		sk6812_init_state = 1;
	}
	if (spi_xfer_done == false){
		return 2;
	}
	spi_xfer_done = false;
	uint8_t color[3] = {r,g,b};
	if (data_led_rgb (color, m_tx_buf)){
	   return 1;
	}

	for(uint8_t i = 12; i<m_length;i++){
		m_tx_buf[i] = 0;
	}
	APP_ERROR_CHECK(nrf_drv_spi_transfer(&spi, m_tx_buf, m_length,NULL, m_length));
	return 0;
}
