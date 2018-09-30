package org.firstinspires.ftc.teamcode.dogecommander;

import com.disnodeteam.dogecommander.hardware.DogeBot;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Victo on 9/16/2018.
 */

public class TestBot extends DogeBot {
    public TestBot(HardwareMap hwd) {
        super(hwd);
        addSubsystem(new TestSubsystem("Test Sub", "TestMotor"));
    }
}
