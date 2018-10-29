package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
public class RobotConstants {

    public static PIDCoefficients TURNING_PID = new PIDCoefficients(1.3,0,0.5 );
    // other constants
}
