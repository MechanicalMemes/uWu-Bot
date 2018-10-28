package com.disnodeteam.dogecommander.examples.commands;

import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Victo on 10/27/2018.
 */

public class WaitForTime extends DogeCommand{
    ElapsedTime timer = new ElapsedTime();
    double time = 1.0;
    public WaitForTime(double time){
        this.time = time;
    }
    @Override
    public void start() {
        timer.startTime();
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        timer.reset();

    }

    @Override
    public boolean isTaskRunning() {
        return timer.time() < time;
    }
}
