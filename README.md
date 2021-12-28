# androidJavaAsync
Android 11でAsyncTaskがdeprecatedになったので、Javaで非同期処理をどう書くかの把握用

## 参考

1. JavaによるAndroid非同期処理の基本 (1/3)：CodeZine（コードジン） https://codezine.jp/article/detail/13199
> 　2020年9月8日にAndroid 11（APIレベル30）がリリースされました。このAPIレベル30で、AsyncTaskクラスが非推奨となりました。AsyncTaskは、Androidの非同期処理を行う際に非常に便利なクラスとして、様々な場面で利用されてきました。このクラスが非推奨になるということは、今後は代替の方法を採用していく必要があります。本稿では、全3回にわたって、非同期処理がよく登場する場面としてWeb API連携を題材に、AsyncTaskクラスを利用しないAndroidの非同期処理を紹介していきます。第1回である今回は、Javaによる非同期処理の記述方法を紹介します。

　　※Androidのバックグラウンドを使いこなす Thread, Looper, Handler https://academy.realm.io/jp/posts/android-thread-looper-handler/
  
  　※<a href="https://tips.priart.net/52/">Android 11(Android-R) で AsyncTask がDeprecatedに - Tips</a> によるとJavaなら以下の選択肢がある
   
      - Handlerを使う
      - LiveDataを使う
      - DataBinding化する
      - あとはRxJavaを使うもあるかな
   
2. GDD Blog: [Android]非推奨になったAsyncTaskの代わりに、ExecutorServiceとLiveDataを使う http://genz0.blogspot.com/2021/01/androidasynctaskexecutorservicelivedata.html

3. AndroidのRoomライブラリを用いたDB非同期処理をHandlerで書いてみた - OPTiM TECH BLOG https://tech-blog.optim.co.jp/entry/2020/12/16/100000
