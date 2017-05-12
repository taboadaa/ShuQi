var readList =
[
// For example
//[ SERVICE_HEART_RATE, HEART_RATE_BODY_SENSOR_LOCATION ]
];

var writeList = 
[
// For example
//[ SERVICE_HEART_RATE, HEART_RATE_HEART_RATE_CONTROL_POINT, 1 ]
]

var notifyList =
[
// For example
//[ SERVICE_HEART_RATE, HEART_RATE_HEART_RATE_MEASUREMENT, [1,0]  ]
]

var logNotification = [];

function Initialize()
{
  // Initialize Data
  DeviceContext.FlushData();

  // Remind myself how I can add commands in this script.
  remindme();
  
  // To read characteristics, add entry into readList above.
  testRead();

  // To write to characteristics, add entry into writeList above.
  testWrite();

  // To enable/disable Notification or Indication, add entry into notifyIndicateList above.
  testNotify();
}

function testRead()
{
  for (i = 0; i< readList.length; i++)
  {
    testReadCharacteristic( readList[i][0], readList[i][1] );
  }
}

function testWrite()
{
  for (i = 0; i< writeList.length; i++)
  {
    testWriteCharacteristic( writeList[i][0], writeList[i][1], writeList[i][2] );
  }
}

function testNotify()
{
  for (i = 0; i< notifyList.length; i++)
  {
    testSetNotify( notifyList[i][0], notifyList[i][1], notifyList[i][2]);
  }
}


function testReadCharacteristic( servName, charName )
{
  log("");
  log( "Test: Read Characteristic(UUID=" + charName.toString() + ") from Service (UUID=" + servName.toString() + ")" );
  var service = DeviceContext.GetService(servName);
  if( service == null )
  {
  log("FAIL: Service does not exist");
  return;
  }
  var characteristic = service.GetCharacteristic(charName);
  if( characteristic == null )
  {
  log("FAIL: Characteristic does not exist");
  return;
  }

  DeviceContext.SendReadValue(characteristic);
  var charValue = DeviceContext.GetValue(characteristic);
  if (charValue == null )
  {
  log( "FAIL: Unable to read value from Characteristic");
  return;
  }

  log( "PASS: Characteristic has value: " + charValue);
  return;
}

function testWriteCharacteristic( servName, charName, writeValue )
{
  log("");
  log( "Test: Write value(" + writeValue.toString() + ") to Characteristic(UUID=" + charName.toString() + ") in Service (UUID=" + servName.toString() + ")" );
  var service = DeviceContext.GetService(servName);
  if( service == null )
  {
    log("FAIL: Service does not exist");
    return;
  }
  var characteristic = service.GetCharacteristic(charName);
  if( characteristic == null )
  {
    log("FAIL: Characteristic does not exist");
    return;
  }

  characteristic.Value = writeValue;
  DeviceContext.SendWriteValue(characteristic);

  log( "PASS: Successfully write through DeviceContext. Verify the value on server side.");
  return;
}

function testSetNotify( servName, charName, setValue )
{
  log("");
  log( "Test: Set new value[" + setValue.toString() + "] to Client Characteristic Configuration descriptor for Characteristic(UUID=" + charName.toString() + ") in Service (UUID=" + servName.toString() + ")" );
  var service = DeviceContext.GetService(servName);
  if( service == null )
  {
    log("FAIL: Service does not exist");
    return;
  }
  var characteristic = service.GetCharacteristic(charName);
  if( characteristic == null )
  {
    log("FAIL: Characteristic does not exist");
    return;
  }

  var descriptor = characteristic.GetDescriptor("2902");
  DeviceContext.SetValue(descriptor, setValue);
  DeviceContext.SendWriteValue(descriptor);

  // Register this characteristic so that notification will be logged.
  logNotification.push(characteristic);

  log( "PASS: Successfully send new value through DeviceContext.");
  return;
}

function CharacteristicValueChangedNotify(characteristic)
{
  for( i = 0; i < logNotification.length; i++ )
  {
    if(characteristic.UUID == logNotification[i].UUID)
    {
      log("PASS: A value (" + DeviceContext.GetValue(characteristic) + ") for Characteristic (UUID= " + characteristic.UUID + ") has been received after Notification is enabled.");
      // Comment out the following line if subsequent notification should still be logged.
      logNotification.splice(i, 1);
    }
  }
  return;
}

function remindme()
{
  if(readList.length == 0)
  {
    log("");
    log("To read characteristic, add an entry in the readList");
    log("Format: [ Service UUID, Characteristic UUID ]");
    log("");
  }

  if(writeList.length == 0)
  {
    log("");
    log("To write to characteristic, add an entry in the writeList");
    log("Format: [ Service UUID, Characteristic UUID, value to be written ]");
    log("");
  }

  if(notifyList.length == 0)
  {
    log("");
    log("To enable/disable notification, add an entry in the notifyList");
    log("Format: [ Service UUID, Characteristic UUID, [ x, 0 ] ] ");
    log("where (x = 1) => enable and (x = 0) => disable");
    log("");
  }
}