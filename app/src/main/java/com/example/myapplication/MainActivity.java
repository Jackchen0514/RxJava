package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button tv = findViewById(R.id.sample_btn);
        tv.setText(stringFromJNI());

        CompositeDisposable mRxEvent = new CompositeDisposable();

       //Flowable flowable = Flowable.range(0,10);
        /*Flowable flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Throwable {
                emitter.onNext(1);
                Log.d("xinsi", "emitter onNext ----> 1");
                emitter.onNext(2);
                Log.d("xinsi", "emitter onNext ----> 2");
                emitter.onNext(3);
                Log.d("xinsi", "emitter onNext ----> 3");
                emitter.onNext(4);
                Log.d("xinsi", "emitter onNext ----> 4");
                emitter.onComplete();
                Log.d("xinsi", "emitter onNext ----> complete");
            }
        }, BackpressureStrategy.BUFFER);

        Disposable subscribe = flowable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer i) throws Throwable {
                //对应onNext()
                Log.d("xinsi", "Integer i " + i);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                //对应onError()
                Log.d("xinsi", "Throwable onError");
            }
        }, new Action() {
            @Override
            public void run() throws Throwable {
                //对应onComplete()
                Log.d("xinsi", "Action run onComplete");
            }
        });

        mRxEvent.add(subscribe);
        mRxEvent.clear();*/
        /*flowable.subscribe(new Subscriber<Integer>() {
                    Subscription sub;
                    //当订阅后，会首先调用这个方法，其实就至关于onStart()，
                    //传入的Subscription s参数能够用于请求数据或者取消订阅
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.w("xinsi","onsubscribe start");
                        sub=s;
                        sub.request(1);
                    }

                    @Override
                    public void onNext(Integer o) {
                        Log.w("xinsi","onNext--->" + o);
                        sub.request(2);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.w("xinsi","onComplete");
                    }
                });*/

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*if (mSubscription != null) {
                   mSubscription.request(64);
               }*/
                Observable mObservable = Observable.create(new ObservableOnSubscribe<Object>() {

                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                        Log.d("xinsi", "Observable subscribe " + Thread.currentThread().getName());
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onComplete();
                    }
                });

                Observer mObserver = new Observer<Integer>() {
                    //在订阅后，发送数据前
                    //Disposable可用于取消订阅
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("xinsi", "Observer onSubscribe d " + d);
                    }

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.d("xinsi", "Observer onNext: " + value + ", " + Thread.currentThread().getName());
                        Toast.makeText(MainActivity.this, "value: " + value + ", Thread: " + Thread.currentThread(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("xinsi", "Observer onError e: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("xinsi", "Observer onComplete");
                    }
                };

                mObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(mObserver);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.d("xinsi", "Activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.d("xinsi", "Activity onTouchEvent");
        return super.onTouchEvent(event);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
