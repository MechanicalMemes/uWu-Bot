package com.disnodeteam.dogecommander.examples.commands;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.examples.subsystems.TankDriveSubsystem;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TankDriveToDistance extends DogeCommand {
    TankDriveSubsystem tankDrive;

    boolean enabled = false;
    int target;
    double speed;
    public TankDriveToDistance(TankDriveSubsystem tankDrive, double distanceInInches, double speed) {
        this.tankDrive = tankDrive;
        this.target = (int) distanceInInches * bot.ticksPerInch;
        this.speed = speed;
    }

    @Override
    public void start() {


        tankDrive.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        tankDrive.setTargetPosition(target, target);
        tankDrive.setPower(speed, speed);
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
