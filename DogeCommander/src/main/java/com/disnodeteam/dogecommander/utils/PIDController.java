package com.disnodeteam.dogecommander.utils;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.disnodeteam.dogecommander.UniLogger;

public class PIDController {
    private double kP;
    private double kI;
    private double kD;
    private double lastTime;
    private int integral = 0;
    private double previousError = 0;
    private double errorSum = 0;

    public PIDController(PIDSettings settings) {
        this.kP = settings.getP();
        this.kI = settings.getI();
        this.kD = settings.getD();
    }

    public PIDController(){
    }



    public double run(double targetValue, double position) {
        double dt = (System.currentTimeMillis() - lastTime);
        lastTime = System.currentTimeMillis();

        double error = (targetValue - position);

        errorSum += error;

        double result = (kP * error) + (kI*dt*errorSum) + (kD/dt *(error-previousError));

        previousError = error;
        UniLogger.Log("DogeCommander-PID", "PID Update: \n\tTarget:" + targetValue +  "\n\tPosition: " + position+  "\n\tError: " + error + "\n\tResult: " + result);
        //FTC_DASHBOARD
        FtcDashboard dashboard = FtcDashboard.getInstance();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("pid_dt", dt);
        packet.put("pid_error", error);
        packet.put("pid_error_sum", errorSum);
        packet.put("pid_result", result);
        packet.put("pid_target", targetValue);
        packet.put("pid_pos", position);

        dashboard.sendTelemetryPacket(packet);
        return result;
    }
}