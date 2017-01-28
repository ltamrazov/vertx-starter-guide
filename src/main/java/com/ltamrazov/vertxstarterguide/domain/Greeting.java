package com.ltamrazov.vertxstarterguide.domain;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class Greeting {
    private String greeting;

    public Greeting() {
    }

    public Greeting(String name){
        this.greeting = "Hello " + name;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
