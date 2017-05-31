var notifyList = [];

function Initialize()
{
  DeviceContext.FlushData();

  // Check every 1 second and send notification if needed.
  setInterval(SendNotification, 1000, 1000);
}

function OnReadValue( characteristic )
{
  log("Received Read command on characteristic " + characteristic.Name + " = " + characteristic.UUID);
}

function OnWriteValue( characteristic, newValue )
{
  log("");
  if( characteristic.UUID == "2902" )
  {
    if(newValue[0] == 1)
    {
      log("Received Write command with value:[" + newValue.toString() + "] to enable notification of " + characteristic.Name + " descriptor of " + characteristic.Characteristic.Name + " = " +  characteristic.Characteristic.UUID);
      notifyList.push(characteristic.Characteristic);
    }
    else if (newValue[1] == 1)
    {
      log("Received Write command with value:[" + newValue.toString() + "] to enable indication of " + characteristic.Name + " descriptor of " + characteristic.Characteristic.Name + " = " +  characteristic.Characteristic.UUID);
    }
    else
    {
      log("Received Write command with value:[" + newValue.toString() + "] to disable indication or notification of " + characteristic.Name + " descriptor of " + characteristic.Characteristic.Name + " = " +  characteristic.Characteristic.UUID);
    }
  }
  else
  {
    log("Received Write command on characteristic " + characteristic.Name + " = " + characteristic.UUID + " with value:" + newValue.toString());
  }
}

function SendNotification()
{
  for( i = 0; i < notifyList.length; i++ )
  {
    DeviceContext.NotifyValue(notifyList[i]);
  }
}