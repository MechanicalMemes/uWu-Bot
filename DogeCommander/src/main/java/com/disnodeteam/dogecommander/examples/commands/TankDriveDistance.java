package com.disnodeteam.dogecommander.examples.commands;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.examples.subsystems.NavigationHardware;
import com.disnodeteam.dogecommander.examples.subsystems.TankDriveSubsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class TankDriveDistance extends DogeCommand {
    TankDriveSubsystem tankDrive;

    boolean enabled = false;
    double distanceInInches = 0;
    int target;
    double speed;
    double ticksLeftAvg = 0;
    double startingTicks = 0;

    public TankDriveDistance(TankDriveSubsystem tankDrive, double distanceInInches, double speed) {
        this.tankDrive = tankDrive;
        this.distanceInInches = distanceInInches;

        this.speed = speed;
    }

    @Override
    public void start() {
        target = (int) distanceInInches * bot.ticksPerInch;
        ticksLeftAvg = target;
        startingTicks = (tankDrive.getLeftMotorsPosition(0) + tankDrive.getRightMotorsPositionAvg(0) )/ 2;
        tankDrive.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        tankDrive.setTargetPosition(target, target);
        tankDrive.setPower(speed, speed);
    }

    @Override
    public void loop() {
        double ticksTravled = ((tankDrive.getLeftMotorsPosition(0) + tankDrive.getRightMotorsPositionAvg(0) )/ 2) - startingTicks;
        double progress = Math.abs(target - ticksTravled) / ticksTravled;

        if(progress >= 0.7){
            double speedMultiplier = (1-progress) / 0.3 ;
            Range.clip(speedMultiplier,0.1,1.0);
            tankDrive.setPower(speed * speedMultiplier, speed * speedMultiplier);
        }


    }

    @Override
    public void stop() {
        tankDrive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tankDrive.setPower(0,0);
    }

    @Override
    public boolean isTaskRunning() {
        UniLogger.Log("DogeCommander-DistanceDrive", "IsMotorsBusy: " + tankDrive.isMotorsBusy());
        return tankDrive.isMotorsBusy();
    }
}
