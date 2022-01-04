package com.example.javaasync;

import android.util.Log;

public class BookDataRepository {
    private static final String TAG = BookDataRepository.class.getSimpleName();

    public void save(BookDetailModel book) {
        Log.d(TAG, "save() called with: book = [" + book + "]");
    }

    /**
     * 本来は、非同期でデータを更新
     */
    public void refreshBookDetail(Integer bookId) {
        Log.d(TAG, "refreshBookDetail() called with: bookId = [" + bookId + "]");
    }

    /**
     * 本来は、非同期でデータを取得
     */
    public BookDetailModel getBookDetail(Integer bookid) {
        Log.d(TAG, "getBookDetail() called with: bookid = [" + bookid + "]");
        //今は固定データを返す
        return new BookDetailModel(1, "hogeBook", 10);
    }
}
