package io.reactivex.rxjava3;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyClass {
    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                System.out.println("===create: " + Thread.currentThread().getName());
                emitter.onNext("1");
            }
        }).map(new Function<String, Integer>() {

            @Override
            public Integer apply(String s) {
                System.out.println("===String -> Integer: " + Thread.currentThread().getName());
                return Integer.valueOf(s);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Throwable {
                System.out.println(" ===Integer-> Observable: " + Thread.currentThread().getName());
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        System.out.println("===Observable<String> call: " + Thread.currentThread().getName());
                        for (int i = 0; i < integer; i++) {
                            emitter.onNext(i + "");
                        }
                        emitter.onComplete();
                    }
                });
            }
        }).map(new Function<String, Long>() {
            @Override
            public Long apply(String s) throws Throwable {
                System.out.println("===String -> Long: " + Thread.currentThread().getName());
                return Long.parseLong(s);
            }
        }).subscribeOn(Schedulers.io())
        .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Throwable {
                System.out.println("===onNext: " + Thread.currentThread().getName());
            }
        });
    }
}
