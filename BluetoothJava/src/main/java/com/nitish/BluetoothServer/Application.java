package com.nitish.BluetoothServer;
import com.nitish.Service.Worker.Impl.ConsolePrinter;
import com.nitish.Service.Worker.Impl.MouseMover;
import com.nitish.Service.Worker.WorkerService;
import java.io.IOException;

public class Application
{

	public static void main( String[] args )
    {
    	try{
    		//the worker interface is used to do something after the
			//data has been received from the client
			WorkerService worker = new MouseMover();
			BluetoothServer server = new BluetoothServer(worker);
			server.startServer();
    	}catch(IOException ioe) 
    	{
    		ioe.printStackTrace();
    	}
    }
}
