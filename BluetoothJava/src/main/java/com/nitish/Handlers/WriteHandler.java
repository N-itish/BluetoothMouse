package com.nitish.Handlers;

import javafx.concurrent.Worker;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteHandler implements Runnable{
    private OutputStream outputStream;
    private Worker worker;
    public WriteHandler(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void run()  {
        String pingData = "ping";
        DataOutputStream dataOutputStream  = new DataOutputStream(outputStream);
        //pinging the client every 2 minutes
        while(true){
            try {
                dataOutputStream.write(pingData.getBytes());
                Thread.sleep(1000*60*2);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }catch(InterruptedException intr){
                intr.printStackTrace();
            }
        }
    }
}
