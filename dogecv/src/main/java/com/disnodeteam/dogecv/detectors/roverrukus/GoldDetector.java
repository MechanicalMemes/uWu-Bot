package com.disnodeteam.dogecv.detectors.roverrukus;

import android.util.Log;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;
import com.disnodeteam.dogecv.scoring.RatioScorer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 9/10/2018.
 */

public class GoldDetector extends DogeCVDetector {

    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;

    public RatioScorer ratioScorer = new RatioScorer(1.0,1);
    public MaxAreaScorer maxAreaScorer = new MaxAreaScorer(100000,5);
    public DogeCVColorFilter yellowFilter = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW);

    private Mat yellowMask = new Mat();
    private Mat workingMat = new Mat();
    private Mat hiarchy    = new Mat();
    private int results;
    @Override
    public Mat process(Mat input) {
        if(input.channels() < 0 || input.cols() <= 0){
            Log.e("DogeCV", "Bad INPUT MAT!");
        }
        input.copyTo(workingMat);
        yellowFilter.process(input,yellowMask);
        List<MatOfPoint> contoursYellow = new ArrayList<>();

        Imgproc.findContours(yellowMask, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursYellow,-1,new Scalar(230,70,70),2);
        results = 0;

        Rect bestRect = null;
        double bestScore = Double.MAX_VALUE;
        for(MatOfPoint cont : contoursYellow){
            double score = calculateScore(cont);
            results++;

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);
           if(score < bestScore){
               bestScore = score;
               bestRect = rect;
           }
        }

        if(bestRect != null){
            Imgproc.rectangle(workingMat, bestRect.tl(), bestRect.br(), new Scalar(0,255,255),2);
        }

        return workingMat;
    }

    @Override
    public void useDefaults() {
        addScorer(ratioScorer);
        addScorer(maxAreaScorer);
    }

    public int getResults(){
        return results;
    }
}
