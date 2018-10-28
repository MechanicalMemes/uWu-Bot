package com.disnodeteam.dogecommander.examples.subsystems;

import android.graphics.Path;

import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.hardware.DogeSubsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.List;

public class TankDriveSubsystem extends DogeSubsystem {

    private double speed = 1.0;
    private DcMotor.RunMode   runMode        = DcMotor.RunMode.RUN_USING_ENCODER;
    private DcMotor.Direction leftDirection  = DcMotor.Direction.REVERSE;
    private DcMotor.Direction rightDirection = DcMotor.Direction.FORWARD;
    private List<DcMotor> leftMotors  = new ArrayList<>();
    private List<DcMotor> rightMotors = new ArrayList<>();
    private String[] leftMotorNames;
    private String[] rightMotorNames;

    public TankDriveSubsystem(String name, String[] leftMotorName, String[] rightMotorName) {
        super(name);
        this.leftMotorNames = leftMotorName;
        this.rightMotorNames = rightMotorName;

    }

    @Override
    public void hardwareInit(HardwareMap map) {
        leftMotors.clear();
        rightMotors.clear();
        for(String name : leftMotorNames){
            DcMotor motor = map.dcMotor.get(name);
            motor.setMode(runMode);
            motor.setDirection(leftDirection);
            leftMotors.add(motor);
        }

        for(String name : rightMotorNames){
            DcMotor motor = map.dcMotor.get(name);
            motor.setMode(runMode);
            motor.setDirection(rightDirection);
            rightMotors.add(motor);
        }
    }

    public void setDirections(DcMotor.Direction left, DcMotor.Direction right){
        leftDirection = left;
        rightDirection = right;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }


    public void setRunMode(DcMotor.RunMode runMode) {
        this.runMode = runMode;
    }

    public DcMotor.RunMode getRunMode() {
        return runMode;
    }

    public void setPower(double left, double right){
        for(DcMotor motor : leftMotors){
            motor.setPower(left * speed);
        }

        for(DcMotor motor : rightMotors){
            motor.setPower(right * speed);
        }
    }

    public void setTargetPosition(int left, int right){
        for(DcMotor motor : leftMotors){
            motor.setTargetPosition(left);
        }

        for(DcMotor motor : rightMotors){
            motor.setTargetPosition(right);
        }
    }

    public List<DcMotor> getLeftMotorsPower(){
        return leftMotors;
    }

    public List<DcMotor> getRightMotorsPower() {
        return rightMotors;
    }

    public int getLeftMotorsPosition(int index){
        return leftMotors.get(index).getCurrentPosition();
    }

    public int getRightMotorsPositionAvg(int index) {
        return rightMotors.get(index).getCurrentPosition();
    }

    public boolean isMotorsBusy(){
        boolean isBusy = false;
        for(DcMotor motor: leftMotors){
            if(motor.isBusy()){
                isBusy = true;
            }
        }

        for(DcMotor motor: rightMotors){
            if(motor.isBusy()){
                isBusy = true;
            }
        }
        return isBusy;
    }
}
