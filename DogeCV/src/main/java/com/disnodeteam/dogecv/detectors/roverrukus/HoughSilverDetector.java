package com.disnodeteam.dogecv.detectors.roverrukus;

import android.util.Log;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.math.Circle;
import com.disnodeteam.dogecv.scoring.ColorDevScorer;
import com.disnodeteam.dogecv.scoring.DogeCVScorer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public class HoughSilverDetector extends DogeCVDetector {

    public DogeCVScorer stdDevScorer = new ColorDevScorer();

    public double sensitivity = 1.4; //Sensitivity of circle detector; between about 1.2 and 2.1;
    public double minDistance = 60; //Adjust with frame size! This is the minimum distance between circles

    private Mat whiteMask = new Mat();
    private Mat workingMat = new Mat();
    private Mat displayMat = new Mat();
    private int results;
    private Circle foundCircle;
    private boolean isFound = false;
    private boolean showMask = false;

    public HoughSilverDetector() {
        super();
        this.detectorName = "Accurate Silver Detector";
    }

    @Override
    public Mat process(Mat input) {
        if(input.channels() < 0 || input.cols() <= 0){
            Log.e("DogeCV", "Bad INPUT MAT!");
        }
        input.copyTo(workingMat);
        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGBA2RGB);
        displayMat = new Mat();
        Imgproc.bilateralFilter(workingMat, displayMat, 5, 175, 175);
        displayMat.copyTo(workingMat);
        Imgproc.cvtColor(workingMat, workingMat, Imgproc.COLOR_RGB2Lab);

        Imgproc.erode(workingMat, workingMat, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3)));
        Imgproc.GaussianBlur(workingMat, workingMat, new Size(3,3), 0);
        List<Mat> channels = new ArrayList<Mat>();
        Core.split(workingMat, channels);

        Mat circles = new Mat();
        Imgproc.HoughCircles(channels.get(0), circles, Imgproc.CV_HOUGH_GRADIENT, sensitivity, minDistance);
        results = 0;

        Circle bestCircle = null;
        double bestDifference = Double.MAX_VALUE;

        for (int i = 0; i < circles.width(); i++) {
            Circle circle = new Circle(circles.get(0,i)[0],circles.get(0,i)[1],circles.get(0,i)[2]);
            Mat mask = Mat.zeros(workingMat.size(), CvType.CV_8UC1);
            Imgproc.circle(mask, new Point((int) circle.x, (int) circle.y), (int) circle.radius, new Scalar(255), -1);
            Mat masked = new Mat((int) getAdjustedSize().height, (int) getAdjustedSize().width, CvType.CV_8UC3);
            workingMat.copyTo(masked, mask);
            double score = calculateScore(masked);

            mask.release();
            masked.release();
            results++;

            Imgproc.circle(displayMat, new Point(circle.x, circle.y), (int) circle.radius, new Scalar(0,0,255),2);

            if(score < bestDifference){
                bestDifference = score;
                bestCircle = circle;
            }
        }

        if(bestCircle != null){
            Imgproc.circle(displayMat, new Point(bestCircle.x, bestCircle.y), (int) bestCircle.radius, new Scalar(255,0,0),4);
            Imgproc.putText(displayMat, "Chosen", new Point(bestCircle.x, bestCircle.y),0,.8,new Scalar(255,255,255));
            foundCircle = bestCircle;
            isFound = true;
        }else{
            isFound = false;
            foundCircle = null;
        }

        if(showMask){
            return whiteMask;
        }
        Imgproc.cvtColor(displayMat, displayMat, Imgproc.COLOR_RGB2RGBA);
        return displayMat;
    }

    @Override
    public void useDefaults() {
        addScorer(stdDevScorer);
    }

    public boolean isFound() {
        return isFound;
    }

    public Circle getFoundCircle() {
        return foundCircle;
    }


}
