package org.firstinspires.ftc.teamcode.dogecommander;

import com.disnodeteam.dogecommander.examples.subsystems.NavigationHardware;
import com.disnodeteam.dogecommander.examples.subsystems.REVImu;
import com.disnodeteam.dogecommander.examples.subsystems.TankDriveSubsystem;
import com.disnodeteam.dogecommander.hardware.DogeBot;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class UWUBot extends DogeBot {
    public TankDriveSubsystem tankDrive;
    public REVImu navigationHardware;
    public MineralArmSubsystem mineralArmSubsystem;
    public UWUBot(HardwareMap hwd) {
        super(hwd);
        String driveTrainName = "DriveTrain";
        String navName = "Navigation";

        String[] leftMotors = {"lf", "lr"};
        String[] rightMotors = {"rf", "rr"};
        tankDrive = new TankDriveSubsystem(driveTrainName, leftMotors, rightMotors);
        navigationHardware = new REVImu(navName,"imu1");
        mineralArmSubsystem = new MineralArmSubsystem("MineralArm", "mineral");

        addSubsystem(tankDrive);
        addSubsystem(navigationHardware);
        addSubsystem(mineralArmSubsystem);

        ticksPerInch = (int) (537.6 /(3.14 * 4));
    }
}
