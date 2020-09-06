package com.nitish.Service.Worker.Impl;

import com.nitish.Service.Worker.WorkerService;

public class ConsolePrinter implements WorkerService {
    public void execute(byte[] data) {
        System.out.println("message from the client: " + new String(data));
    }
}
