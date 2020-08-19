package com.pattern.singleton.test;

import com.pattern.singleton.lazy.LazyDoubleCheckSingleton;

/**
 * Created by Tom.
 */
public class ExectorThread implements Runnable{

    public void run() {

        LazyDoubleCheckSingleton singleton = LazyDoubleCheckSingleton.getInstance();
//        LazySimpleSingleton singleton = LazySimpleSingleton.getInstance();
//        ThreadLocalSingleton singleton = ThreadLocalSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
