package com.disnodeteam.dogecommander.examples.commands;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.examples.subsystems.TankDriveSubsystem;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDriveToDistance extends DogeCommand {
    TankDriveSubsystem tankDrive;
    String tankDriveName;
    boolean enabled = false;
    int target;
    public TankDriveToDistance(String tankDriveName, double distanceInInches) {
        this.tankDriveName = tankDriveName;
        this.target = (int) distanceInInches * bot.ticksPerInch;
    }

    @Override
    public void start() {
        tankDrive = (TankDriveSubsystem)bot.getSubSystem(tankDriveName);
        if(tankDrive != null){
            enabled = true;
        }else{
            UniLogger.Log("DogeCommander", "Cant run TankDriveToDistance. Cannot find subsystem "+ tankDriveName);
            return;
        }

        tankDrive.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        tankDrive.setTargetPosition(target, target);
        tankDrive.setPower(1.0, 1.0);
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        tankDrive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tankDrive.setPower(0,0);
    }

    @Override
    public boolean isTaskRunning() {
        return tankDrive.isMotorsBusy();
    }
}
