package org.firstinspires.ftc.teamcode.dogecommander;

import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Victo on 11/7/2018.
 */

public class MineralScoreCommand extends DogeCommand {
    private MineralArmSubsystem mineralArmSubsystem;
    private GoldAlignDetector goldAlignDetector;
    private boolean scored = false;
    private boolean waiting = false;
    private ElapsedTime timer;
    private double timeDown;
    public MineralScoreCommand(MineralArmSubsystem mineralArmSubsystem, double time){
        this.mineralArmSubsystem = mineralArmSubsystem;
        this.goldAlignDetector = new GoldAlignDetector();
        this.timeDown = time;
        goldAlignDetector.init(bot.hardwareMap.appContext, CameraViewDisplay.getInstance());

        goldAlignDetector.enable();
    }
    @Override
    public void start() {
        mineralArmSubsystem.setUp();
    }

    @Override
    public void loop() {
        if(goldAlignDetector.getAligned()){
            mineralArmSubsystem.setDown();
            timer.startTime();
            waiting = true;
        }
        if(waiting && timer.time() >= timeDown){
            scored = true;
            waiting = false;
            mineralArmSubsystem.setUp();
        }
    }

    @Override
    public void stop() {
        goldAlignDetector.disable();
    }

    @Override
    public boolean isTaskRunning() {
        return !scored && !waiting;
    }
}
