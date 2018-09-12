package com.disnodeteam.dogecv.detectors.roverrukus;

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

    public RatioScorer ratioScorer = new RatioScorer(1.0,15);
    public DogeCVColorFilter yellowFilter = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW);

    private Mat yellowMask = new Mat();
    private Mat workingMat = new Mat();
    private Mat hiarchy    = new Mat();

    @Override
    public Mat process(Mat input) {
        input.copyTo(workingMat);
        yellowFilter.process(input,yellowMask);
        List<MatOfPoint> contoursYellow = new ArrayList<>();

        Imgproc.findContours(yellowMask, contoursYellow, hiarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(workingMat,contoursYellow,-1,new Scalar(230,70,70),2);

        for(MatOfPoint cont : contoursYellow){
            double ratioScore = ratioScorer.calculateDifference(cont);


            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);
            Imgproc.putText(workingMat,"Score: " + ratioScore, new Point(rect.x, rect.y),0,1.0, new Scalar(255,255,255));
        }

        return workingMat;
    }

    @Override
    public void useDefaults() {
        addScorer(ratioScorer);
    }


}
