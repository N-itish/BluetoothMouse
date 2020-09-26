package bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Lenovo on 08/08/2020.
 */

public class ConnectedThread extends Thread {
    private InputStream input;
    private DataOutputStream output;
    private BluetoothSocket socket;
    private String message;
    public ConnectedThread(BluetoothSocket socket, String message){
        this.socket = socket;
        this.message = message;
        try{
            input = socket.getInputStream();
            output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        }catch (IOException ioe)
        {
            Log.v("connectedThread","Cannot get the output/input streams");
            ioe.printStackTrace();
        }
    }

    /*
        PROTOCOL:
        first send the length of the byte
        second send the bytearray
     */
    public void run(){
        try {
            byte[] messageInBytes = message.getBytes(StandardCharsets.UTF_8);
            output.writeInt(messageInBytes.length);
            output.write(messageInBytes);
            output.flush();
            Log.v("connectedThread","message Sent");

        }catch(IOException ioe)
        {
            Log.v("connectedThead","Cannot write the output");
            ioe.printStackTrace();
        }
    }

    public void cancel(){
        try{
            socket.close();
        }catch(IOException ioe)
        {
            Log.v("connectedThread","Cannot close the socket");
            ioe.printStackTrace();
        }
    }
}
