package com.disnodeteam.dogecv.detectors;


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

public class MineralRowDetector extends OpenCVPipeline {


    public enum MineralRowLocation {
        UNKNOWN,
        LEFT,
        CENTER,
        RIGHT
    }

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
    public boolean             stretch          = true;

    public DogeCVColorFilter   colorFilerYellow   = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW);
    public DogeCVColorFilter   colorFilterWhite  = new HSVColorFilter(new Scalar(40,25,200), new Scalar(40,40,50));


    private MineralRowLocation currentOrder = MineralRowLocation.UNKNOWN;
    private MineralRowLocation lastOrder    = MineralRowLocation.UNKNOWN;


    private Mat workingMat = new Mat();
    private Mat blurredMat  = new Mat();
    private Mat maskYellow = new Mat();
    private Mat maskWhite = new Mat();
    private Mat hiarchy  = new Mat();
    private Mat structure = new Mat();
    public Size stretchKernal = new Size(10,10);
    private Size newSize = new Size();

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {

        Size initSize= rgba.size();
        newSize  = new Size(initSize.width * downScaleFactor, initSize.height * downScaleFactor);
        rgba.copyTo(workingMat);

        Imgproc.resize(workingMat, workingMat,newSize);

        if(rotateMat){
            Mat tempBefore = workingMat.t();

            Core.flip(tempBefore, workingMat, -1); //mRgba.t() is the transpose

            tempBefore.release();
        }


        Mat yellowConvert = workingMat.clone();
        Mat whiteConvert = workingMat.clone();

        colorFilerYellow.process(yellowConvert, maskYellow);
        colorFilterWhite.process(whiteConvert, maskWhite);
        if(stretch){
            structure = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,stretchKernal);
            Imgproc.morphologyEx(maskWhite,maskWhite,Imgproc.MORPH_CLOSE,structure,new Point(-1,-1), 3);
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

        List<MatOfPoint> contoursWhite = new ArrayList<>();

        Imgproc.findContours(maskWhite, contoursWhite,hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursWhite,-1,new Scalar(70,130,230),2);
        List<Rect> whiteFound = new ArrayList<>();
        List<Double> whiteScores = new ArrayList<>();
        for(MatOfPoint c : contoursWhite) {
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
            double ratioDiffrence = Math.abs(cubeRatio - 1);

            double finalDiffrence = (ratioDiffrence * ratioWeight) + (areaDiffrence * areaWeight);



            // Update the chosen rect if the diffrence is lower then the curreny chosen
            // Also can add a condition for min diffrence to filter out VERY wrong answers
            // Think of diffrence as score. 0 = perfect
            if(finalDiffrence < maxDiffrence && area > minArea){
                boolean good = true;
                for(Rect checkRect : whiteFound){
                    boolean inX = ( rect.x > (checkRect.x - (checkRect.width / 2))) && rect.x < (checkRect.x + (checkRect.width / 2));
                    boolean inY = ( rect.y > (checkRect.y - (checkRect.height / 2))) && rect.y < (checkRect.y + (checkRect.height / 2));
                    if(inX && inY){
                        good = false;
                    }
                }
                if(good){
                    whiteFound.add(rect);
                    whiteScores.add(finalDiffrence);
                }
            }

            if(debugContours && area > 100){
                Imgproc.circle(workingMat,centerPoint,3,new Scalar(0,255,255),3);
                Imgproc.putText(workingMat,"Area: " + area,centerPoint,0,0.5,new Scalar(0,255,255));
            }

        }



        if(chosenYellowRect != null){
            Imgproc.rectangle(workingMat,
                    new Point(chosenYellowRect.x, chosenYellowRect.y),
                    new Point(chosenYellowRect.x + chosenYellowRect.width, chosenYellowRect.y + chosenYellowRect.height),
                    new Scalar(255, 0, 0), 2);

            Imgproc.putText(workingMat,
                    "Gold: " + String.format("%.2f X=%.2f", chosenYellowScore, (double)chosenYellowRect.x),
                    new Point(chosenYellowRect.x - 5, chosenYellowRect.y - 10),
                    Core.FONT_HERSHEY_PLAIN,
                    1.3,
                    new Scalar(0, 255, 255),
                    2);
        }

        if(whiteFound != null){
            for(int i=0;i<whiteFound.size();i++){
                Rect rect = whiteFound.get(i);
                double score = whiteScores.get(i);
                Imgproc.rectangle(workingMat,
                        new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(255, 255, 255), 2);
                Imgproc.putText(workingMat,
                        "Silver: " + String.format("%.2f X=%.2f", chosenYellowScore, (double)rect.x),
                        new Point(rect.x - 5, rect.y - 10),
                        Core.FONT_HERSHEY_PLAIN,
                        1.3,
                        new Scalar(255, 255, 255),
                        2);

            }
        }


        if(whiteFound.size() >= 2 && chosenYellowRect != null){
            int leftCount = 0;
            for(int i=0;i<whiteFound.size();i++){
                Rect rect = whiteFound.get(i);
                if(chosenYellowRect.x > rect.x){
                    leftCount++;
                }
            }
            if(leftCount == 0){
                currentOrder = MineralRowLocation.LEFT;
            }

            if(leftCount == 1){
                currentOrder = MineralRowLocation.CENTER;
            }

            if(leftCount >= 2){
                currentOrder = MineralRowLocation.RIGHT;
            }

            lastOrder = currentOrder;

        }else{
            currentOrder = MineralRowLocation.UNKNOWN;
        }


        Imgproc.putText(workingMat,"Result: " + lastOrder.toString(),new Point(10,newSize.height - 30),0,1, new Scalar(255,255,0),1);
        Imgproc.putText(workingMat,"Current Track: " + currentOrder.toString(),new Point(10,newSize.height - 10),0,0.5, new Scalar(255,255,255),1);

        Imgproc.resize(workingMat,workingMat,initSize);

        yellowConvert.release();
        whiteConvert.release();
        Imgproc.putText(workingMat,"DogeCV 2.1 Mineral Row: " + newSize.toString() + " - " + speed.toString() + " - " + detectionMode.toString() ,new Point(5,30),0,1.2,new Scalar(0,255,255),2);

        return workingMat;
    }

    public MineralRowLocation getCurrentOrder() {
        return currentOrder;
    }

    public MineralRowLocation getLastOrder() {
        return lastOrder;
    }
}
