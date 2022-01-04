package com.example.javaasync;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.NumberFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LiveData用モデル
 * <p>
 * GDD Blog: [Android]非推奨になったAsyncTaskの代わりに、ExecutorServiceとLiveDataを使う
 * http://genz0.blogspot.com/2021/01/androidasynctaskexecutorservicelivedata.html
 */
public class MyViewModel extends ViewModel {
    private static final String TAG = MyViewModel.class.getSimpleName();
    private MutableLiveData<String> currentName = new MutableLiveData<String>();

    public MutableLiveData<String> getCurrentValue() {
        return currentName;
    }

    //region 4.MVVMアーキテクチャ
    // 「プロに追いつくAndroid開発入門」のp.26 2.3.3 ViewModelを作成する
    private MutableLiveData<String> _title = new MutableLiveData<>();
    public LiveData<String> title = _title;
    private MutableLiveData<String> _amaount = new MutableLiveData<>();
    public LiveData<String> amount = _amaount;

    BookDataRepository bookDataRepository = new BookDataRepository();

    void fetchBookDetail() {
        Log.d(TAG, "fetchBookDetail() called");
        BgTaskFetchBookDetailModel bgTaskFetchBookDetailModel = new BgTaskFetchBookDetailModel();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(bgTaskFetchBookDetailModel);
    }

    private class BgTaskFetchBookDetailModel implements Runnable {

        @Override
        public void run() {
            Log.d(TAG, "run() called");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "run: e", e);
            }
            //本の詳細を取得
            BookDetailModel bookDetailModel = bookDataRepository.getBookDetail(1);

            //タイトルのデータを流す
            _title.postValue(bookDetailModel.title);

            //金額を表示用に数値をカンマ区切りにしてデータを流す
            _amaount.postValue(NumberFormat.getNumberInstance().format(bookDetailModel.amount));
        }
    }
    //endregion 4.MVVMアーキテクチャ
}
