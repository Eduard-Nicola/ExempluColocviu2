package ro.pub.cs.systems.eim.Colocviu1_245.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

import ro.pub.cs.systems.eim.Colocviu1_245.general.Constants;

public class ProcessingThread extends Thread {
    private Context context = null;
    private boolean isRunning = true;

    private int sum;

    public ProcessingThread(Context context, int sum) {
        this.context = context;
        this.sum = sum;
    }

    @Override
    public void run() {
        Log.d(Constants.PROCESSING_THREAD_TAG, "Thread has started!");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d(Constants.PROCESSING_THREAD_TAG, "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_BROADCAST);
        intent.putExtra(Constants.BROADCAST_RECEIVER_EXTRA,
                new Date(System.currentTimeMillis()) + " " + sum);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
