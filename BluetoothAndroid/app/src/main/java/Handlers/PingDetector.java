package Handlers;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import bluetooth.ConnectionStatus;

public class PingDetector implements Runnable{
    private InputStream inputStream;
    private ConnectionStatus connectionStatus;
    private long minutesPassed;
    private long currentTime;
    public PingDetector(InputStream inputStream, ConnectionStatus connectionStatus){
        this.connectionStatus = connectionStatus;
        this.inputStream = inputStream;
        this.minutesPassed = 0;
    }
    @Override
    public void run() {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        while(true){
            if(minutesPassed != 0){
                if(currentTime - minutesPassed >= 3)
                {
                    Log.v("PingHandler","timeout occured!!!");
                    connectionStatus.setConnectionStatus(false);
                    break;
                }
            }
            currentTime = (System.currentTimeMillis()/1000)/60;
                try {
                    if (dataInputStream.available() > 0) {
                        Log.v("PingHandler", dataInputStream.readUTF());
                        minutesPassed = (System.currentTimeMillis()/1000)/60;
                    }
                } catch (IOException ioe) {
                    connectionStatus.setConnectionStatus(false);
                    ioe.printStackTrace();
                }
            }


    }
}
