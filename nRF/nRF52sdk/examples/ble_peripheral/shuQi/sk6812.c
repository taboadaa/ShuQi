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

	init_timer ();
	nrf_drv_timer_disable(&TIMER);
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

void timer_handler(nrf_timer_event_t event_type, void* p_context)
{
    switch (event_type)
    {
        case NRF_TIMER_EVENT_COMPARE0:
            if (mode == 1){
            	if( blue_level == 254){
            		mode = 2;
            	}
            	blue_level++;
            	sk6812_set_color( 0,0,blue_level);


            }else if (mode == 2){
            	if( blue_level <33){
        			mode = 1;
        		}
        		blue_level -=1;
        		sk6812_set_color( 0,0,blue_level);
            }
            break;

        default:
            //Do nothing.
            break;
    }
}


uint32_t init_timer (){
	uint32_t err_code;
	nrf_drv_timer_config_t timer_cfg = NRF_DRV_TIMER_DEFAULT_CONFIG;
	err_code = nrf_drv_timer_init(&TIMER, &timer_cfg,timer_handler);
	APP_ERROR_CHECK(err_code);

	uint32_t time_ticks = nrf_drv_timer_ms_to_ticks(&TIMER,TIME_MS);

	nrf_drv_timer_extended_compare(
		 &TIMER, NRF_TIMER_CC_CHANNEL0, time_ticks, NRF_TIMER_SHORT_COMPARE0_CLEAR_MASK, true);

	return err_code;
}
uint32_t  sk6812_change_mode(uint8_t new_mode){
	sk6812_set_color( 0,0,0);
	if (new_mode == 0){
		mode = 0;
		nrf_drv_timer_disable(&TIMER);
	}else if(new_mode ==1){

		mode =1;
		blue_level  = 0;
		nrf_drv_timer_enable(&TIMER);
	}else{
		return 1;
	}

	return 0;
}
