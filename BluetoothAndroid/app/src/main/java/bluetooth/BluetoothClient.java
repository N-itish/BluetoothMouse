package bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Lenovo on 05/07/2020.
 */

public class BluetoothClient extends Thread{

    private BluetoothSocket clientScoket;
    private BluetoothDevice myDevice;
    private static final String myUUID = "04c6093b-0000-1000-8000-00805f9b34fb";

    public BluetoothClient(BluetoothDevice myDevice){
        try{
            this.myDevice = myDevice;
            clientScoket = myDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));
        }catch (IOException ioe){
            Log.v("BluetoothClient","Socket Creation failed");
        }
    }


    public BluetoothSocket getSocket(){
        return clientScoket;
    }

    public void run(){
        try {
            clientScoket.connect();
            Log.v("BluetoothClient","Connection succeeded");
        } catch (IOException ioe) {
            Log.v("BluetoothClient","Connection failed");
            try{
                clientScoket.close();
            }catch(IOException closeconnection){
                Log.v("BluetoothClient","closing socket failed");
                closeconnection.printStackTrace();
            }
            ioe.printStackTrace();
        }
    }

}
