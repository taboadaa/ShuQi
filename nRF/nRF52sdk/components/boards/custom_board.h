#ifndef CUSTOM_BOARD_H
#define CUSTOM_BOARD_H

// LEDs definitions for SHUQI_BOARD

#define LEDS_ACTIVE_STATE 0

#define LEDS_NUMBER    1
#define LED_START      27
#define LED_1          27
#define LED_STOP       27

#define LEDS_LIST { LED_1 }

#define BSP_LED_0      LED_1

#define LED_TOP 11
/* all LEDs are lit when GPIO is low */
#define LEDS_INV_MASK  LEDS_MASK

#define BUTTONS_NUMBER 2

#define BUTTON_START   12
#define BUTTON_1       12
#define BUTTON_2       13
#define BUTTON_STOP    13
#define BUTTON_PULL    NRF_GPIO_PIN_PULLUP

#define BUTTONS_LIST { BUTTON_1, BUTTON_2 }

#define BSP_BUTTON_0   BUTTON_1
#define BSP_BUTTON_1   BUTTON_2

#define BUTTONS_ACTIVE_STATE 0


#define RX_PIN_NUMBER  6
#define TX_PIN_NUMBER  8
#define RFID_ENABLE_PIN_NUMBER 7

#define RTS_PIN_NUMBER 9
#define CTS_PIN_NUMBER 10
#define HWFC           true

#define SK6812_DATA_PIN 17

#define SPIS_MISO_PIN   28  // SPI MISO signal.
#define SPIS_CSN_PIN    14  // SPI CSN signal.
#define SPIS_MOSI_PIN   17  // SPI MOSI signal.
#define SPIS_SCK_PIN    29  // SPI SCK signal.

#define SPIM0_SCK_PIN   29  // SPI clock GPIO pin number.
#define SPIM0_MOSI_PIN  17  // SPI Master Out Slave In GPIO pin number.
#define SPIM0_MISO_PIN  28  // SPI Master In Slave Out GPIO pin number.
#define SPIM0_SS_PIN    14  // SPI Slave Select GPIO pin number.

// serialization APPLICATION board - temp. setup for running serialized MEMU tests
#define SER_APP_RX_PIN              31    // UART RX pin number.
#define SER_APP_TX_PIN              30    // UART TX pin number.
#define SER_APP_CTS_PIN             28    // UART Clear To Send pin number.
#define SER_APP_RTS_PIN             29    // UART Request To Send pin number.

#define SER_APP_SPIM0_SCK_PIN        2     // SPI clock GPIO pin number.
#define SER_APP_SPIM0_MOSI_PIN       4     // SPI Master Out Slave In GPIO pin number
#define SER_APP_SPIM0_MISO_PIN       3     // SPI Master In Slave Out GPIO pin number
#define SER_APP_SPIM0_SS_PIN        31     // SPI Slave Select GPIO pin number
#define SER_APP_SPIM0_RDY_PIN       29     // SPI READY GPIO pin number
#define SER_APP_SPIM0_REQ_PIN       30     // SPI REQUEST GPIO pin number

// serialization CONNECTIVITY board
#define SER_CON_RX_PIN              13    // UART RX pin number.
#define SER_CON_TX_PIN              12    // UART TX pin number.
#define SER_CON_CTS_PIN             14    // UART Clear To Send pin number. Not used if HWFC is set to false.
#define SER_CON_RTS_PIN             15    // UART Request To Send pin number. Not used if HWFC is set to false.

#define SER_CON_SPIS_SCK_PIN        29    // SPI SCK signal.
#define SER_CON_SPIS_MOSI_PIN       25    // SPI MOSI signal.
#define SER_CON_SPIS_MISO_PIN       28    // SPI MISO signal.
#define SER_CON_SPIS_CSN_PIN        12    // SPI CSN signal.
#define SER_CON_SPIS_RDY_PIN        14    // SPI READY GPIO pin number.
#define SER_CON_SPIS_REQ_PIN        13    // SPI REQUEST GPIO pin number.

#define SER_CONN_CHIP_RESET_PIN     27    // Pin used to reset connectivity chip

#define NRF_CLOCK_LFCLKSRC      {.source        = NRF_CLOCK_LF_SRC_XTAL,            \
                                 .rc_ctiv       = 0,                                \
                                 .rc_temp_ctiv  = 0,                                \
                                 .xtal_accuracy = NRF_CLOCK_LF_XTAL_ACCURACY_20_PPM}

#ifdef __cplusplus
}
#endif

#endif // CUSTOM_BOARD_H
