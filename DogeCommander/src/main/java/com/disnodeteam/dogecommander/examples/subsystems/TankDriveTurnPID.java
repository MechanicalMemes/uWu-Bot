package com.disnodeteam.dogecommander.examples.subsystems;

import com.disnodeteam.dogecommander.UniLogger;
import com.disnodeteam.dogecommander.auto.DogeCommand;
import com.disnodeteam.dogecommander.utils.PIDController;
import com.disnodeteam.dogecommander.utils.PIDSettings;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class TankDriveTurnPID extends DogeCommand {
    private TankDriveSubsystem tankDrive;
    private boolean enabled = false;
    private double angle, speed;
    private PIDController pidController;
    private NavigationHardware navigationHardware;

    public TankDriveTurnPID(TankDriveSubsystem tankDrive, NavigationHardware navSubsystem, double angle, double speed, PIDSettings pidSettings) {
        this.tankDrive = tankDrive;
        this.navigationHardware = navSubsystem;
        this.angle = angle;
        this.speed = speed;
        this.pidController = new PIDController(pidSettings);
    }

    @Override
    public void start() {

        tankDrive.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        return !onHeading(speed, angle, (int) navigationHardware.getHeading());
    }

    boolean onHeading(double speed, double target, int heading) {

        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error

        if (Math.abs(heading) <= 2) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            double finalError = pidController.run((int)angle,heading);
            if(commander.linearOpMode != null){
                commander.linearOpMode.telemetry.addData("Final error" ,finalError);
                commander.linearOpMode.telemetry.addData("Raw Angle" ,angle);
                commander.linearOpMode.telemetry.addData("Raw Angle" ,target);
                commander.linearOpMode.telemetry.update();
            }
            steer = Range.clip(finalError, -1, 1);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        tankDrive.setPower(leftSpeed, rightSpeed);



        return onTarget;
    }
}
