package broadCastRecievers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Lenovo on 05/07/2020.
 */

public class BluetoothBroadCastReciever extends BroadcastReceiver{
    private ArrayAdapter<String> adapter;
    private ArrayList<BluetoothDevice> foundDevices;
    public BluetoothBroadCastReciever(ArrayAdapter<String> adapter){
        this.adapter = adapter;
        this.foundDevices = new ArrayList<>();
    }

    public ArrayList<BluetoothDevice> getDevices(){
        return foundDevices;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Log.v("BroadcastReceiver", "Bluetooth discovery started");
        }

        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.v("BroadcastReceiver", "Bluetooth discovery finished");
        }
        //Detect when a device has been found
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.v("BroadcastReceiver", "device Found:"+device.getName());
            try {
                foundDevices.add(device);
            }catch (Exception e){
                e.printStackTrace();
            }
            adapter.add(device.getName());
        }
    }
}
