package com.disnodeteam.dogecommander.examples.subsystems;

import com.disnodeteam.dogecommander.hardware.DogeSubsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;

public interface NavigationHardware {
    double getHeading();
    double getError(double targetAngle);
    void recalibrate();
    boolean isReady();
}
