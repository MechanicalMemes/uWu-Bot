package com.disnodeteam.dogecv.dnn;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoldClassifier extends ImageClassifier {
    /**
     * Initializes an {@code ImageClassifier}.
     *
     * @param activity
     */
    private float[][] labelProbArray = null;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    public  Map<Integer, Object> outputMap;

    public GoldClassifier(Activity activity) throws IOException {
        super(activity);
        labelProbArray = new float[1][getNumLabels()];
    }


    @Override
    protected String getModelPath() {
        return "gold.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "goldlables.txt";
    }

    @Override
    protected int getImageSizeX() {
        return 300;
    }

    @Override
    protected int getImageSizeY() {
        return 300;
    }

    @Override
    protected int getNumBytesPerChannel() {
        // the quantized model uses a single byte only
        return 1;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
        imgData.put((byte) ((pixelValue >> 16) & 0xFF));
        imgData.put((byte) ((pixelValue >> 8) & 0xFF));
        imgData.put((byte) (pixelValue & 0xFF));
    }

    @Override
    protected float getProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    @Override
    protected void setProbability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.floatValue();
    }

    @Override
    protected float getNormalizedProbability(int labelIndex) {
        return getProbability(labelIndex);

    }

    @Override
    protected void runInference() {
        Object[] inputArray = {imgData};
        outputMap = new HashMap<>();

        float[][][] out1 = new float[1][10][4];
        float[][] out2 = new float[1][10];
        float[][] out3 = new float[1][10];

        outputMap.put(0, out1);
        outputMap.put(1, out2);
        outputMap.put(2, out3);
        tflite.runForMultipleInputsOutputs(inputArray, outputMap);


    }
}
