package com.nitish.Handlers;

import java.io.*;
import java.util.Scanner;

public class WriteHandler implements Runnable{
    private BufferedWriter writer;
    private DataOutputStream dataOutputStream;
    private Scanner userInput;

    public WriteHandler(OutputStream outputStream){
        dataOutputStream = new DataOutputStream(outputStream);
        writer = new BufferedWriter(new OutputStreamWriter(dataOutputStream));
        userInput = new Scanner(System.in);

    }
    public void run() {
        try{
            sendStringMessasge();
        }catch (IOException ioe){
           ioe.printStackTrace();
        }finally
        {
            try{
                dataOutputStream.close();
                writer.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    public void sendStringMessasge() throws IOException {
        while(true){
            System.out.print("Enter message to send: ");
            String message = userInput.nextLine();
            writer.write(message);
            System.out.println("\n");
        }
    }
}
