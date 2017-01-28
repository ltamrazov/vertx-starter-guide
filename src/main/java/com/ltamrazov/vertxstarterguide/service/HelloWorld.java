package com.ltamrazov.vertxstarterguide.service;

import com.ltamrazov.vertxstarterguide.domain.Greeting;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class HelloWorld {
    public HelloWorld(){
        // initiate your class
    }

    public Greeting greet(String name){
        try{
            Thread.sleep(1500);
            return new Greeting(name);
        }catch (InterruptedException e){
            throw new RuntimeException("This is a safe message");
        }
    }
}
