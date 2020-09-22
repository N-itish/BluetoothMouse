package com.nitish.BluetoothServer;
import com.nitish.Service.Worker.Impl.ConsolePrinter;
import com.nitish.Service.Worker.Impl.MouseMover;
import com.nitish.Service.Worker.WorkerService;
public class Application
{

	public static void main( String[] args )
    {
    		//the worker interface is used to do something after the
			//data has been received from the client
			WorkerService worker = new MouseMover();
			BluetoothServer server = new BluetoothServer(worker);
			server.startServer();

    }
}
