/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//BLE struct
var StuffManagerService = {
	uuid: "E71AE71A6ED94A89BBBF98A29DF5E71A",
	entryValue: "E71AE71D6ED94A89BBBF98A29DF5E71A",
	entrySelection: "E71AE71C6ED94A89BBBF98A29DF5E71A",
	managerMode: "E71AE71F6ED94A89BBBF98A29DF5E71A",
	entryNumber: "E71AE7106ED94A89BBBF98A29DF5E71A"
};

var LinkLossService = {
	uuid: "1803",
	alertLevel: "2A06"
};

var StuffManager = {
	deviceId: "0",

	setMode: function() {
		ble.write(this.deviceId, StuffManagerService.uuid, StuffManagerService.managerMode, )
	}

};



