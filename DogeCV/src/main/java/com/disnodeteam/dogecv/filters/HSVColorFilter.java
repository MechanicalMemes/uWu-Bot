package com.disnodeteam.dogecv.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Victo on 1/1/2018.
 */

public class HSVColorFilter extends DogeCVColorFilter{

    private Scalar perfect = new Scalar(255,255,255);
    private Scalar range = new Scalar(0,0,0);

    Mat workingMat = new Mat();

    public HSVColorFilter(Scalar color, Scalar range){
        updateSettings(color, range);
    }

    public void updateSettings(Scalar color, Scalar range){
        this.perfect = color;
        this.range = range;
    }

    @Override
    public void process(Mat input, Mat mask) {
        workingMat = input.clone();
        Imgproc.cvtColor(workingMat,workingMat,Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.GaussianBlur(workingMat,workingMat,new Size(5,5),0);

        Scalar lower = new Scalar(perfect.val[0] - (range.val[0]/2), perfect.val[1] - (range.val[1]/2),perfect.val[2] - (range.val[2]/2));
        Scalar upper = new Scalar(perfect.val[0] + (range.val[0]/2), perfect.val[1] + (range.val[1]/2),perfect.val[2] + (range.val[2]/2));
        Core.inRange(workingMat,lower,upper,mask);
    }
}
