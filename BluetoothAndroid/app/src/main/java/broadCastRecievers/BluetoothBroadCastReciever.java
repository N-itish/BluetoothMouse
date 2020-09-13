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
    private ArrayAdapter arrayAdapter;
    private ArrayList<BluetoothDevice> foundDevices;
    private ArrayList<String> presentDevices;
    private boolean isListReady;
    private String previous;
    public BluetoothBroadCastReciever(ArrayAdapter arrayAdapter){
        isListReady = false;
        this.arrayAdapter = arrayAdapter;
        this.foundDevices = new ArrayList<>();
        this.presentDevices = new ArrayList<>();
    }


    public BluetoothDevice getDevices(String deviceName) {
        for(BluetoothDevice device: foundDevices ){
            if(device.getName().equalsIgnoreCase(deviceName)){
                return device;
            }
        }
        return null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            arrayAdapter.clear();
            Log.v("BroadcastReceiver", "Bluetooth discovery started");
        }

        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.v("BroadcastReceiver", "Bluetooth discovery finished");
            isListReady = true;

        }
        //Detect when a device has been found
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.v("BroadcastReceiver", "device Found:"+device.getName());
            if(!foundDevices.contains(device)) {
                foundDevices.add(device);
            }
            if(!presentDevices.contains(device.getName())) {
                arrayAdapter.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
            presentDevices.add(device.getName());

        }
    }


}
