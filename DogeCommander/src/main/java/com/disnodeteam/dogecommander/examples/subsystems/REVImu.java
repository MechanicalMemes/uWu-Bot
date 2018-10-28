package com.disnodeteam.dogecommander.examples.subsystems;

import com.disnodeteam.dogecommander.hardware.DogeSubsystem;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class REVImu extends DogeSubsystem implements NavigationHardware {
    private BNO055IMU imu;
    private String imuName;
    public REVImu(String name, String revIMUName) {
        super(name);
        this.imuName = revIMUName;
    }

    @Override
    public void hardwareInit(HardwareMap map) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();


        imu = map.get(BNO055IMU.class, imuName);
        imu.initialize(parameters);
    }

    @Override
    public double getHeading() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return (angles.firstAngle);
    }

    @Override
    public double getError(double targetAngle){
        double angleError = 0;

        angleError = (targetAngle - getHeading());
        angleError -= (360*Math.floor(0.5+((angleError)/360.0)));

        return angleError;

    }

    @Override
    public void recalibrate() {
    }

    @Override
    public boolean isReady() {
        return imu.isGyroCalibrated();
    }


}
