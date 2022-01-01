package com.example.javaasync;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * LiveData用モデル
 *
 * GDD Blog: [Android]非推奨になったAsyncTaskの代わりに、ExecutorServiceとLiveDataを使う
 * http://genz0.blogspot.com/2021/01/androidasynctaskexecutorservicelivedata.html
 */
public class MyViewModel extends ViewModel {
    private MutableLiveData<String> currentName = new MutableLiveData<String>();
    public MutableLiveData<String> getCurrentValue() {
        return currentName;
    }
}
