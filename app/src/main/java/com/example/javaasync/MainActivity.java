package com.example.javaasync;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.IntEvaluator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //2.LiveData用モデルの作成(変数定義)
    private MyViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2.LiveData用モデルの作成(インスタンス生成)
        model = new ViewModelProvider(this).get(MyViewModel.class);
        //3.LiveDataの更新コールバックを受け取るリスナーの設定
        final Observer<String> nameObserver = new Observer<String>() {
            /**
             * ViewModelの更新通知
             * @param newValue
             */
            @Override
            public void onChanged(String newValue) {
                Log.d(TAG, "onChanged() called with: newValue = [" + newValue + "]");
                //画面の更新
                TextView textView = findViewById(R.id.textView);
                textView.setText(newValue);
            }
        };
        model.getCurrentValue().observe(this, nameObserver);

//        asyncExecute(1);
//        asyncExecute(2);
        asyncExecute(3);

        //region4.MVVMアーキテクチャ
        model.title.observe(this, title -> {
            TextView tv = (TextView)findViewById(R.id.book_title);
            tv.setText(getString(R.string.book_title, title));
        });

        model.amount.observe(this, amount -> {
            TextView tv = (TextView)findViewById(R.id.book_amount);
            tv.setText(getString(R.string.book_amount, amount));
        });

        if (savedInstanceState == null) {
            model.fetchBookDetail();
        }
        //endregion 4.MVVMアーキテクチャ
    }

    @UiThread
    public void asyncExecute(int i) {
        // Looper:MessageQueueへのMessage, Runnableの追加をキャッチする。
        // 投げられるオブジェクトをキャッチするために、監視し続けている。
        // このgetMainLooper()で取得したLooperはオブジェクトの追加をキャッチすると、
        // UIスレッドに紐づくHandlerにそのオブジェクトを渡す。
        Looper mainLooper = Looper.getMainLooper();
        //Handler:Looperから渡されたMessageを用いて処理を行う
        Handler handler = HandlerCompat.createAsync(mainLooper);
        //WorkerスレッドにHandlerオブジェクトを渡す（目的：Workerスレッドでの処理結果をUIスレッドのMessageQueueに渡すために使う）
        BackgroundTask backgroundTask = new BackgroundTask(handler, i);

        //バックグラウンドタスクをワーカースレッドで実行
        ExecutorService executorService = Executors.newSingleThreadExecutor();//ワーカースレッド用実行サービス生成
        executorService.submit(backgroundTask); //実行サービスにバックグラウンドタスクオブジェクトを提出
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
            try {
                Thread.sleep(_i*1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "run: e", e);
            }
            // UIの更新方法1: Handler で
            PostExecutor postExecutor = new PostExecutor(_i);
            _handler.post(postExecutor);

            // UIの更新方法2: LiveData で中央の文字列を更新
            String anotherName = _i + "秒経過" + new Date();
            model.getCurrentValue().postValue(anotherName);
        }
    }

    /**
     * バックグラウンドタスクの実行後に行う後処理（主にUI更新処理）
     * - トースト表示
     */
    private class PostExecutor implements Runnable {
        private final int _i;
        public PostExecutor(int i) {
            _i = i;
        }
        @UiThread
        @Override
        public void run() {
            Log.d("Async-PostExecutor:" + _i, "run() called");
            Toast toast = Toast.makeText(MainActivity.this, "PostExecutorからトースト:" + _i, Toast.LENGTH_SHORT);
            toast.show();//UiThreadなので表示される
        }
    }
}
