package com.disnodeteam.dogecommander.utils;


public class PIDController {
    private double kP;
    private double kI;
    private double kD;
    private double lastTime;
    private int integral = 0;
    private double previousError = 0;

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

        double angleError = (targetValue - position);

        double error = angleError;

        integral += kI * error * dt;

        double u = (kP * error + integral + kD * (error - previousError) / dt);

        previousError = error;

        return u;
    }
}