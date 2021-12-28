package com.example.javaasync;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asyncExecute(1);
        asyncExecute(2);
        asyncExecute(3);

    }

    @UiThread
    public void asyncExecute(int i) {
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundTask backgroundTask = new BackgroundTask(handler, i);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundTask);
    }

    private class BackgroundTask implements Runnable {
        private final Handler _handler;
        private final int _i;
        public BackgroundTask(Handler handler, int i) {
            _handler = handler;
            _i = i;
        }

        @WorkerThread
        @Override
        public void run() {
            Log.d("Async-BackgroundTask:" + _i, "run() called");
            PostExecutor postExecutor = new PostExecutor(_i);
            _handler.post(postExecutor);
            Toast.makeText(MainActivity.this, "BackgroundTaskからトースト:" + _i, Toast.LENGTH_SHORT).show();//UiThreadでないので表示されない
        }
    }

    private class PostExecutor implements Runnable {
        private final int _i;
        public PostExecutor(int i) {
            _i = i;
        }
        @UiThread
        @Override
        public void run() {
            Log.d("Async-PostExecutor:" + _i, "run() called");
            Toast.makeText(MainActivity.this, "PostExecutorからトースト:" + _i, Toast.LENGTH_SHORT).show();//UiThreadなので表示される
        }
    }
}
