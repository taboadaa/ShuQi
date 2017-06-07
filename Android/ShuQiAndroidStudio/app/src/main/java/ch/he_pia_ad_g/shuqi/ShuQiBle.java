package ch.he_pia_ad_g.shuqi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * Created by tab on 07.06.17.
 */

public class ShuQiBle {
    private BluetoothAdapter bleAdp;
    private BluetoothManager bleMng;

    public ShuQiBle() {
        bleMng = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdp = bleMng.getAdapter();
    }

}
