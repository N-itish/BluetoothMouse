package com.nitish.Service.Worker.Impl;

import com.nitish.Service.Worker.WorkerService;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.util.Properties;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

public class MouseMover implements WorkerService {
    private  int xOffset;
    private  int yOffset;
    private Robot robot;

    public MouseMover(){
        loadProperties();
        initializeRobot();
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
        if(message.equalsIgnoreCase("left")){
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        else if(message.equalsIgnoreCase("right")){
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
        else {
            String[] coordinates = message.split(":");
            int xCoord = ((int) round(Double.parseDouble(coordinates[0]))) * xOffset;
            int yCoord = ((int) round(Double.parseDouble(coordinates[1]))) * yOffset;
            robot.mouseMove(xCoord, yCoord);
        }
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
}
