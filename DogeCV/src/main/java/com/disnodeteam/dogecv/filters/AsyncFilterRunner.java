package com.disnodeteam.dogecv.filters;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class AsyncFilterRunner {
    private List<DogeCVColorFilter> filters = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();
    public void addFilter(final DogeCVColorFilter filter, final Mat input, final Mat output){
        filters.add(filter);
        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                filter.process(input, output);
            }
        });
        workerThread.setName("Filter Thread: " + filter.getClass().getSimpleName());
        threads.add(workerThread);
    }

    public void runAll(){
        for (Thread thread: threads) {
            thread.run();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
