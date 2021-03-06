package com.nitish.BluetoothServer;

import com.nitish.Handlers.ReadHandler;
import com.nitish.Handlers.WriteHandler;
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

    public void startServer() {
        if(!isBluetoothOn()) {
            shutdownProgram();
        }
        else
        {
            try {
                acceptConnection();
                pingClient(connection);
                showConnectedDevice(connection);
                readData(connection);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

    }

    private boolean isBluetoothOn()  {
        return LocalDevice.isPowerOn();
    }

    private void shutdownProgram(){
        System.out.println("Bluetooth Is Not Turned On!!!");
        System.out.println("Turn On Bluetooth Manually!!!");
        System.exit(BLUETOOTH_NOT_TURNED_ON);
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

    public void pingClient(StreamConnection connection) throws IOException{
        WriteHandler writeHandler = new WriteHandler(connection.openOutputStream());
        Thread pingThread = new Thread(writeHandler);
        pingThread.start();

    }

}
