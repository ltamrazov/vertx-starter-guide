package com.ltamrazov.vertxstarterguide.service;

import com.ltamrazov.vertxstarterguide.domain.Greeting;

/**
 * Created by levontamrazov on 2017-01-28.
 * Sample service class.
 */
public class HelloWorld {
    public HelloWorld(){
        // initiate your class
    }

    /**
     * Return a greeting for a particular name.
     * @param name - Name to greet
     * @return - Greeting object
     */
    public Greeting greet(String name){
        try{
            Thread.sleep(1500);
            return new Greeting(name);
        }catch (InterruptedException e){
            throw new RuntimeException("This is a safe message");
        }
    }
}
