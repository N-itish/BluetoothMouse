package com.nitish.Handlers;
import com.nitish.Service.Worker.WorkerService;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;



public class ReadHandler implements Runnable{
	
	private final InputStream inputStream;
	private final WorkerService worker;
	private  DataInputStream dataInputStream;

	public ReadHandler(InputStream inputStream,WorkerService worker) {
		this.inputStream = inputStream;
		this.worker = worker;
	}

	public void run() {
		dataInputStream = new DataInputStream(inputStream);
		System.out.println("Started reading data from the client!!");
		try {
			readInput();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}finally {
			try {
				System.out.println("Closing the streams...");
				dataInputStream.close();
				System.out.println("Streams closed");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	private void readInput() throws IOException {

		while(true){
			if(dataInputStream.available() > 0) {
				readInputByteStream();
			}
		}

	}

	private void readInputByteStream() throws IOException{
		// protocol used:
		// first the length of the message is sent
		// then the byte array is sent
		int messageLength  = dataInputStream.readInt();
		byte[] message = new byte[messageLength];
		dataInputStream.read(message);
		executeWorker(message);
	}

	public void executeWorker(byte[] message)  {
		worker.execute(message);
	}

}
