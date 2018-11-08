package org.firstinspires.ftc.teamcode.dogecommander;

import com.disnodeteam.dogecommander.hardware.DogeSubsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Victo on 11/7/2018.
 */

public class MineralArmSubsystem extends DogeSubsystem {
    private String servoName;
    private Servo servo;

    public final double UP_POS = 0;
    public final double DOWN_POS = 1.0;

    public MineralArmSubsystem(String name, String servoName) {
        super(name);
        this.servoName = servoName;
    }


    @Override
    public void hardwareInit(HardwareMap map) {
        servo = map.servo.get(servoName);
    }

    public void setUp(){
        servo.setPosition(UP_POS);
    }

    public void setDown(){
        servo.setPosition(DOWN_POS);
    }
}
