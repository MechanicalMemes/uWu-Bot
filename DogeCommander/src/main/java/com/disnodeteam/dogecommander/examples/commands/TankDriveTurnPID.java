package com.disnodeteam.dogecommander.examples.commands;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.examples.subsystems.NavigationHardware;
import com.disnodeteam.dogecommander.examples.subsystems.TankDriveSubsystem;
import com.disnodeteam.dogecommander.utils.PIDController;
import com.disnodeteam.dogecommander.utils.PIDSettings;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class TankDriveTurnPID extends DogeCommand {
    private TankDriveSubsystem tankDrive;
    private boolean enabled = false;
    private double angle, speed;
    private PIDController pidController;
    private NavigationHardware navigationHardware;

    public TankDriveTurnPID(TankDriveSubsystem tankDrive, NavigationHardware navSubsystem, double angle, double speed, PIDSettings pidSettings) {
        this.tankDrive = tankDrive;
        this.navigationHardware = navSubsystem;
        this.angle = -angle;
        this.speed = speed;
        this.pidController = new PIDController(pidSettings);
    }

    @Override
    public void start() {

        tankDrive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        commander.linearOpMode.telemetry.addData("Status","Starting PID");
        commander.linearOpMode.telemetry.update();
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        tankDrive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        tankDrive.setPower(0,0);
        commander.linearOpMode.telemetry.addData("Status","Stopped PID");
        commander.linearOpMode.telemetry.update();
    }

    @Override
    public boolean isTaskRunning() {
        return !onHeading(speed, angle, (int) navigationHardware.getHeading());
    }

    boolean onHeading(double speed, double target, int heading) {

        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error

        if (Math.abs(heading - target) <= 0.5) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            double nrmTarget = (AngleUnit.normalizeDegrees(target) / 180);
            double nrmHeading = (AngleUnit.normalizeDegrees(heading) / 180);
            double finalError = pidController.run(nrmTarget,nrmHeading);
            UniLogger.Log("TankDriveTurnPID", "Running Loop: \n\tCurrent:" + nrmHeading +"\n\t Target: " + nrmTarget +"\n\tError: " + finalError );
            steer = Range.clip(finalError, -1, 1);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;


        }

        tankDrive.setPower(leftSpeed, rightSpeed);



        return onTarget;
    }
}
