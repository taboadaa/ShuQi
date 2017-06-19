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

var ManagerMode = {
	sleep: 0,
	read: 1,
	recognition: 2
}

var StuffManager = {
	deviceId: "0",

	setMode: function(mode) {
		buffer = BleUtils.encode(mode);
		ble.write(this.deviceId, StuffManagerService.uuid, StuffManagerService.managerMode, mode);
	},

	readStuff: function() {
		//TODO Voir gestion struct du buffer
		//fncs
		function onEntryNumberChange(data) {
			console.log("    EntryNumber changed!");
			ble.read(this.deviceId, StuffManagerService.uuid, StuffManagerService.entryNumber, StuffManagerCallback);
		}
		
		function onEntryNumberRead(buffer) {
			console.log("    EntryNumber readed!");
			data = BleUtils.decode(buffer);
			if (data[0] !== 0) {
				ble.stopNotification(this.deviceId, StuffManagerService.uuid, StuffManagerService.entryNumber);
				ble.write(this.deviceId, StuffManagerService.uuid, StuffManagerService.entrySelection, BleUtils.encode(""), onEntrySelectionWrite());
			}
		}
		
		function onEntrySelectionWrite(data) {
			console.log("    EntrySelection writed!");
			
		}
		
		
		//fnc main
		this.setMode(ManagerMode.read);

		//wait for EntryNumber to change
		ble.startNotification(this.deviceId, StuffManagerService.uuid, StuffManagerService.entryNumber, onEntryNumberChange());

	},
	
	readStuffList: function() {
		
	}

};

var StuffManagerCallback = {
	readEntryNumber: function() {
		
	}
}

/**
 *
 * @type type
 */
var BleUtils = {
	/**
	 * Encode a string into Uint8Array.
	 * @param {type} string
	 * @returns {Object}
	 */
	encode: function(string) {
		var array = new Uint8Array(string.length);
		for (var ii = 0; ii < string.length; ii++) {
			array[ii] = string.charCodeAt(ii);
		}
		return array.buffer;
	},

	/**
	 * Decode
	 * @param {type} buffer
	 * @returns {unresolved}
	 */
	decode: function(buffer) {
		return String.fromCharCode.apply(null, new Uint8Array(buffer));
	}
}



