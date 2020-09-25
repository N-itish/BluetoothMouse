package Handlers;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import bluetooth.ConnectionStatus;

public class PingDetector implements Runnable{
    private InputStream inputStream;
    private ConnectionStatus connectionStatus;
    public PingDetector(InputStream inputStream, ConnectionStatus connectionStatus){
        this.inputStream = inputStream;
    }
    @Override
    public void run() {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        while(true){
            try {
                if (dataInputStream.available() > 0 ) {
                    Log.v("Datahandler",dataInputStream.readUTF());
                }
            }catch (IOException ioe){
                connectionStatus.setConnectionStatus(false);
                ioe.printStackTrace();
            }
        }

    }
}
