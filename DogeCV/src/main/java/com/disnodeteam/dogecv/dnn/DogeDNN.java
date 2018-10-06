package com.disnodeteam.dogecv.dnn;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Debug;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class DogeDNN {


    protected Interpreter tflite;
    private String modelPath;
    private Activity activity;
    private ByteBuffer imgData;
    private int[] intValues = new int[getImageSizeX() * getImageSizeY()];
    public DogeDNN(String modelPath){
        this.modelPath = modelPath;

    }

    public void init(Activity activity) {
        this.activity = activity;

        try {
            tflite = new Interpreter(loadModelFile(activity));
        } catch (IOException e) {
            Log.e("DogeCV", "TFLite Init Error: " + e.getMessage());
        }

        imgData = ByteBuffer.allocateDirect(1 * getImageSizeX() * getImageSizeY() * 3 * 1);
        imgData.order(ByteOrder.nativeOrder());
    }

    public byte[][] runMat(Bitmap input){
        byte[][] labelProbArray = null;
        convertBitmapToByteBuffer(input);
        if(tflite != null && imgData != null){
            tflite.run(imgData, labelProbArray);
        }else{
            Log.e("DogeCV", "TFLite or ImgData Null");
        }
        return labelProbArray;
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        Log.d("DogeCV", "Converting Bitmap: " + bitmap.getWidth() +"/" + bitmap.getHeight() + " - " + bitmap.getByteCount());
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;

        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }

    }

    private void addPixelValue(int val) {
        imgData.put((byte) ((val >> 16) & 0xFF));
        imgData.put((byte) ((val >> 8) & 0xFF));
        imgData.put((byte) (val & 0xFF));
    }

    private int getImageSizeY() {
        return 300;
    }

    private int getImageSizeX() {
        return 300;
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        Log.d("DogeCV", "Assets: " + activity.getAssets());
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    public String getModelPath() {
        return modelPath;
    }
}
