package com.nitish.Service.Worker.Impl;

import com.nitish.Service.Worker.WorkerService;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Properties;



import static java.lang.Math.*;

public class MouseMover implements WorkerService {

    private  int xOffset;
    private  int yOffset;
    private static int xPrev;
    private static int yPrev;
    private  boolean releaseMouse = false;
    private Robot robot;

    public MouseMover(){
        xPrev = 0;
        yPrev = 0;
        loadProperties();
        initializeRobot();
    }

    private void loadProperties(){
        String filePath = "src/main/Resources/resolutions.properties";
        File file = new File(filePath);
        try {
            InputStream stream = new FileInputStream(file);
            Properties props = new Properties();
            props.load(stream);
            setOffsets(props);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void setOffsets(Properties props){
        double x = Double.parseDouble(props.getProperty("compX"))/Double.parseDouble(props.getProperty("andrX"));
        double y = Double.parseDouble(props.getProperty("compY"))/Double.parseDouble(props.getProperty("andrY"));
        xOffset =  (int) ceil(x);
        yOffset =  (int) ceil(y);
    }

    private void initializeRobot(){
        try{
            robot = new Robot();
        }catch (AWTException awt){
            awt.printStackTrace();
        }
    }

    public void execute(byte[] data){
        String message = new String(data);
        moveMouse(message);
    }

     private void moveMouse(String message){

         if(message.equalsIgnoreCase("left")){
             robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
             robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         }
         else if(message.equalsIgnoreCase("sTap")){
             robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
             robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
         }
         else if(message.equalsIgnoreCase("dTap")){
             if(!releaseMouse){
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                releaseMouse = true;
             }
             else
             {
                 robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                 releaseMouse = false;
             }
         }
         else if(message.equalsIgnoreCase("right")){
             robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
             robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
         }
         else {
             String[] coordinates = splitString(message,':');
             int xCoord = ((int) round(Double.parseDouble(coordinates[0]))) ;
             int yCoord = ((int) round(Double.parseDouble(coordinates[1]))) ;
             xPrev = xCoord;
             yPrev = yCoord;
             robot.mouseMove(xCoord+xPrev, yCoord+yPrev);
         }
     }

     private String SetMouseMotions(String message){
        HashMap<String,String> mouseMotion = new HashMap<String, String>();
        mouseMotion.put("left","clickLeft()");
        mouseMotion.put("right","clickRight()");
        mouseMotion.put("move","moveMouse()");
        mouseMotion.put("dtap","dragMouse()");
        mouseMotion.put("stap","clickLeft()");
        return mouseMotion.get(message);
     }
    private String[] splitString(String data,char splitBy){
        int count = 0;
        int divisions = 0;
        int position = 0;
        String[] splits;
        StringBuilder builder = new StringBuilder();
        while(count < data.length()){
            if(data.charAt(count) == splitBy){
                divisions++;
            }
            count++;
        }
        count = 0;
        splits = new String[divisions+1];
        while(count < data.length()){

            if(data.charAt(count) == splitBy)
            {
                splits[position] = builder.toString();
                builder.delete(0,builder.length());
                position++;
                count++;
            }
            builder.append(data.charAt(count));
            count++;
        }
        splits[position] = builder.toString();
        return splits;
    }
}
