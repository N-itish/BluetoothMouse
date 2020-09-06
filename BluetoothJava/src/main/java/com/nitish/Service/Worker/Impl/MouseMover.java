package com.nitish.Service.Worker.Impl;

import com.nitish.Service.Worker.WorkerService;

import java.awt.*;
import java.awt.event.InputEvent;

import static java.lang.Math.round;

public class MouseMover implements WorkerService {
    private int xOffset;
    private int yOffset;
    private int[] computerResolution = {1920,1080};
    private int[] androidResoultion = {1080,2400};
    private Robot robot;
    //TODO: need to change offset below depending upon the resolution provided by the user
    public MouseMover(){
        xOffset = 2;
        yOffset = 1;
    }
    public void execute(byte[] data) {
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
            String coordinates[] = new String(data).split(":");
            int xCoord = ((int) round(Double.parseDouble(coordinates[0]))) * xOffset;
            int yCoord = ((int) round(Double.parseDouble(coordinates[1]))) * yOffset;
            try {
                Robot robot = new Robot();
                robot.mouseMove(xCoord, yCoord);
            } catch (AWTException awt) {
                awt.printStackTrace();
            }
        }
     }
}
