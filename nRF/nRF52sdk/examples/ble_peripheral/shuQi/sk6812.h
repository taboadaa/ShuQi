/*
 * sk6812.h
 *
 *  Created on: 8 juin 2017
 *      Author: colleta
 */

#ifndef SK6812_H_
#define SK6812_H_

#include "nrf_drv_spi.h"
#include "app_util_platform.h"
#include "nrf_gpio.h"
#include "nrf_delay.h"

#include "nrf_drv_timer.h"

#define SPI_INSTANCE  0 /**< SPI instance index. */
#define BLUE_EFFECT 1
#define WHITE_LEVEL_MINIMUM 10
#define TIME_MS 3
static const nrf_drv_timer_t TIMER =  NRF_DRV_TIMER_INSTANCE(1);


static const nrf_drv_spi_t spi = NRF_DRV_SPI_INSTANCE(SPI_INSTANCE);  /**< SPI instance. */
static volatile bool spi_xfer_done = true;  /**< Flag used to indicate that SPI instance completed the transfer. */

static uint8_t       m_tx_buf[12+35];           /**< TX buffer. */
static const uint8_t m_length = sizeof(m_tx_buf);        /**< Transfer length. */
static uint8_t mode =0 ; // 0 = one color // 1 = blue effect up // 2 = blue effect down

static uint8_t blue_level =0 ;


/**
 * @brief SPI user event handler.
 * @param event
 */
void spi_event_handler(nrf_drv_spi_evt_t const * p_event);

/**
 * @brief SPI user event handler.
 * @param uint8_t r,g,b : color
 * @param uint8_t* buffer ( buffer de 12 bytes )
 *
 *
*/

uint32_t data_led_rgb (uint8_t* color, uint8_t* buffer);

uint32_t sk6812_init ();

uint32_t sk6812_set_color( uint8_t r,uint8_t g,uint8_t b);

uint32_t  sk6812_change_mode(uint8_t new_mode);
uint32_t init_timer ();

#endif /* SK6812_H_ */
