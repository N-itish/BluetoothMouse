package com.nitish.BluetoothServer;


import com.intel.bluetooth.BlueCoveImpl;
import com.nitish.Handlers.ReadHandler;
import com.nitish.Service.Worker.WorkerService;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;


public class BluetoothServer {
    private static final int UUIDVALUE = 80087355;
    private static final int BLUETOOTH_NOT_TURNED_ON = 1;
    private final WorkerService worker;
    private StreamConnection connection = null;


    public BluetoothServer(WorkerService worker){
        this.worker = worker;
    }

    public void startServer() throws IOException{
        if(isBluetoothOn()) {
            acceptConnection();
            showConnectedDevice(connection);
            readData(connection);
        }
        else
        {
            System.out.println("Bluetooth Is Not Turned On!!!");
            System.out.println("Turn On Bluetooth Manually!!!");
            System.exit(BLUETOOTH_NOT_TURNED_ON);
        }
    }

    private boolean isBluetoothOn(){
        return LocalDevice.isPowerOn();
    }



    private void acceptConnection() throws IOException{
        UUID uuid = new UUID(UUIDVALUE);
        String bluetoothURL = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
        StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(bluetoothURL);
        connection = service.acceptAndOpen();
    }

    private void showConnectedDevice(StreamConnection connection) throws IOException{
        RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(connection);
        System.out.println("Device: "+ remoteDevice.getFriendlyName(true)+" connected to the server!!");
    }

    private void readData(StreamConnection connection) throws IOException{
        ReadHandler runnableThread = new ReadHandler(connection.openInputStream(),worker);
        Thread messageHandler = new Thread(runnableThread);
        messageHandler.start();
    }

}
