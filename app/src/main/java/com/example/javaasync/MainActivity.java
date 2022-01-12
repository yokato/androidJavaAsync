package com.example.javaasync;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.IntEvaluator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaasync.databinding.ActivityMainBinding;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //2.LiveData用モデルの作成(変数定義)
    private MyViewModel model;
    BroadcastReceiver br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //region Data Binding 使用時
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // LiveData使用時は必須
        // 参考：- https://developer.android.com/topic/libraries/data-binding/architecture?hl=ja#livedata
        //      - ViewDataBinding.setLifecycleOwner() しないと LiveData が更新されなかった
        //        Qiita https://qiita.com/mmorita/items/6c95d859b0f2467c2788
        binding.setLifecycleOwner(this);
        //endregion Data Binding 使用時

        //2.LiveData用モデルの作成(インスタンス生成)
        model = new ViewModelProvider(this).get(MyViewModel.class);
        //region Data Binding 使用時
        // ViewModelをデータバインディングにセットする
        binding.setViewModel(model);
        model.fetchBookDetail();
        //endregion Data Binding 使用時

        //region4.MVVMアーキテクチャ(Data Binding 不使用時)
//        model.title.observe(this, title -> {
//            TextView tv = (TextView)findViewById(R.id.book_title);
//            tv.setText(getString(R.string.book_title, title));
//        });
//
//        model.amount.observe(this, amount -> {
//            TextView tv = (TextView)findViewById(R.id.book_amount);
//            tv.setText(getString(R.string.book_amount, amount));
//        });
//
//        if (savedInstanceState == null) {
//            model.fetchBookDetail();
//        }
        //endregion 4.MVVMアーキテクチャ(Data Binding 不使用時)

        br = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(br, filter);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
        unregisterReceiver(br);
    }
    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean state = intent.getBooleanExtra("state", false);
            Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = ["
                    + intent + "], state = " + state);
        }
    }
}
