package org.firstinspires.ftc.teamcode.dogecommander;

import android.util.Log;

import com.disnodeteam.dogecommander.hardware.DogeSubsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Victo on 9/16/2018.
 */

public class TestSubsystem extends DogeSubsystem {
    public String testMotor = "";
    public double motorSpeed = 0;

    public TestSubsystem(String name, String testMotorName) {
        super(name);
        testMotor = testMotorName;
    }

    @Override
    public void hardwareInit(HardwareMap map) {
        testMotor += "-CREATED";
    }

    public void setPower(double power){
        motorSpeed = power;
        Log.d("DogeCommander", "Setting power: " + motorSpeed);
    }

    public double getPower(){
        return motorSpeed;
    }
}
