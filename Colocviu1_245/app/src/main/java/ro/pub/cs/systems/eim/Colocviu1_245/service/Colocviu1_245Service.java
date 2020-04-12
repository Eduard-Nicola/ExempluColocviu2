package ro.pub.cs.systems.eim.Colocviu1_245.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import ro.pub.cs.systems.eim.Colocviu1_245.general.Constants;

public class Colocviu1_245Service extends Service {
    ProcessingThread processingThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int sum = intent.getIntExtra(Constants.SUM, -1);
        processingThread = new ProcessingThread(this, sum);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }
}
