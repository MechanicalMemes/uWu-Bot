package com.disnodeteam.dogecv.detectors.roverrukus;


import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.HSVColorFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Victo on 11/5/2017.
 */

public class GoldAlignDetector extends OpenCVPipeline {

    public enum  MineralRowDetectionMode {
        PERFECT_AREA, MAX_AREA
    }

    public enum MineralRowDetectionSpeed {
        VERY_FAST, FAST, BALANCED, SLOW, VERY_SLOW
    }


    public MineralRowDetectionMode  detectionMode    = MineralRowDetectionMode.MAX_AREA;
    public double              downScaleFactor  = 0.4;
    public double              perfectRatio     = 1;
    public boolean             rotateMat        = false;
    public MineralRowDetectionSpeed speed            = MineralRowDetectionSpeed.BALANCED;
    public double              perfectArea      = 6500;
    public double              areaWeight       = 0.01; // Since we're dealing with 100's of pixels
    public double              minArea          = 1000;
    public double              ratioWeight      = 100; // Since most of the time the area diffrence is a decimal place
    public double              maxDiffrence     = 8; // Since most of the time the area diffrence is a decimal place
    public boolean             debugContours    = false;
    public boolean             debugAlignment   = true;
    public int                 alignSize        = 100;
    public int                 alignPosOffset   = 0;
    public boolean             stretch          = true;

    public DogeCVColorFilter   colorFilerYellow   = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW);

    private Mat workingMat = new Mat();
    private Mat blurredMat  = new Mat();
    private Mat maskYellow = new Mat();
    private Mat hiarchy  = new Mat();
    private Mat structure = new Mat();
    private Size stretchKernal = new Size(10,10);
    private Size newSize = new Size();

    private boolean aligned = false;

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {

        Size initSize= rgba.size();
        newSize  = new Size(initSize.width * downScaleFactor, initSize.height * downScaleFactor);
        rgba.copyTo(workingMat);


        Imgproc.resize(workingMat, workingMat,newSize);




        Mat yellowConvert = workingMat.clone();


        colorFilerYellow.process(yellowConvert, maskYellow);

        if(stretch){
            structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,stretchKernal);
            Imgproc.morphologyEx(maskYellow,maskYellow,Imgproc.MORPH_CLOSE,structure,new Point(-1,-1), 3);
        }
        List<MatOfPoint> contoursYellow = new ArrayList<>();

        Imgproc.findContours(maskYellow, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursYellow,-1,new Scalar(230,70,70),2);
        Rect chosenYellowRect = null;
        double chosenYellowScore = Integer.MAX_VALUE;

        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for(MatOfPoint c : contoursYellow) {
            MatOfPoint2f contour2f = new MatOfPoint2f(c.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            // You can find this by printing the area of each found rect, then looking and finding what u deem to be perfect.
            // Run this with the bot, on a balance board, with jewels in their desired location. Since jewels should mostly be
            // in the same position, this hack could work nicely.


            double area = Imgproc.contourArea(c);
            double areaDiffrence = 0;

            switch(detectionMode){
                case MAX_AREA:
                    areaDiffrence = -area * areaWeight;
                    break;
                case PERFECT_AREA:
                    areaDiffrence = Math.abs(perfectArea - area);
                    break;
            }

            // Just declaring vars to make my life eassy
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            Point centerPoint = new Point(x + ( w/2), y + (h/2));



            double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
            double ratioDiffrence = Math.abs(cubeRatio - perfectRatio);


            double finalDiffrence = (ratioDiffrence * ratioWeight) + (areaDiffrence * areaWeight);


            // Optional to ALWAYS return a result.

            // Update the chosen rect if the diffrence is lower then the curreny chosen
            // Also can add a condition for min diffrence to filter out VERY wrong answers
            // Think of diffrence as score. 0 = perfect
            if(finalDiffrence < chosenYellowScore && finalDiffrence < maxDiffrence && area > minArea){
                chosenYellowScore = finalDiffrence;
                chosenYellowRect = rect;
            }

            if(debugContours && area > 100){
                Imgproc.circle(workingMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(workingMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
            }

        }

        double alignX = (newSize.width / 2) + alignPosOffset;
        double alignXMin = alignX - (alignSize / 2);
        double alignXMax = alignX +(alignSize / 2);
        double xPos = 0;


        if(chosenYellowRect != null){
            xPos = chosenYellowRect.x + (chosenYellowRect.width / 2);
            Imgproc.circle(workingMat, new Point( xPos, chosenYellowRect.y + (chosenYellowRect.height / 2)), 5, new Scalar(0,255,0),2);
            if(xPos < alignXMax && xPos > alignXMin){
                aligned = true;
            }else{
                aligned = false;
            }
            Imgproc.line(workingMat,new Point(xPos, newSize.height), new Point(xPos, newSize.height - 30),new Scalar(255,255,0), 2);
        }else{
            aligned = false;
        }
        if(debugAlignment){

            Imgproc.line(workingMat,new Point(alignXMin, newSize.height), new Point(alignXMin, newSize.height - 40),new Scalar(0,255,0), 2);
            Imgproc.line(workingMat,new Point(alignXMax, newSize.height), new Point(alignXMax,newSize.height - 40),new Scalar(0,255,0), 2);
        }

        Imgproc.putText(workingMat,"Result: " + aligned,new Point(10,newSize.height - 30),0,1, new Scalar(255,255,0),1);
        //Imgproc.putText(workingMat,"Current X: " + chosenYellowRect.x,new Point(10,newSize.height - 10),0,0.5, new Scalar(255,255,255),1);

        Imgproc.resize(workingMat,workingMat,initSize);

        yellowConvert.release();

        Imgproc.putText(workingMat,"DogeCV 2.1 Mineral Row: " + newSize.toString() + " - " + speed.toString() + " - " + detectionMode.toString() ,new Point(5,30),0,1.2,new Scalar(0,255,255),2);

        return workingMat;
    }

}
