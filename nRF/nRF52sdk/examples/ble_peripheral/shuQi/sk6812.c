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

    if (mode == 1){
    	if( blue_level == 254){
    		mode = 2;
    	}
    	blue_level++;
    	sk6812_set_color( 20,20,blue_level);
    	nrf_delay_ms(3);



    }else if (mode == 2){
    	if( blue_level <20){
			mode = 1;
		}
		blue_level -=2;
		nrf_delay_ms(3);
		sk6812_set_color( 20,20,blue_level);
    }

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
      for ( i =8; i>0;i-=2){
         tmp = (color[j]>>i) & 0x03;

         switch (tmp){
          case 0 :
             buffer[pos_buffer] = 0x88;
          break;
          case 1 :
             buffer[pos_buffer] = 0x8C;
          break;

          case 2:
             buffer[pos_buffer] = 0xC8;
          break;

          case 3:
             buffer[pos_buffer] = 0xCC;
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
	spi_config.mosi_pin = 17;
	spi_config.sck_pin  = 27;
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
	uint8_t color[3] = {g,r,b};
	if (data_led_rgb (color, m_tx_buf+35)){
	   return 1;
	}

	for(uint8_t i = 0; i<35;i++){
		m_tx_buf[i] = 0;
	}
	APP_ERROR_CHECK(nrf_drv_spi_transfer(&spi, m_tx_buf, m_length,NULL, m_length));
	return 0;
}

uint32_t change_mode(uint8_t new_mode){
	if (new_mode == 0){
		mode = 0;
	}else if(new_mode ==1){

		mode =1;
		blue_level  = 0;
	}else{
		return 1;
	}
	sk6812_set_color( 0,0,0);
	return 0;
}
