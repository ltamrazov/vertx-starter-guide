package com.ltamrazov.vertxstarterguide;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ltamrazov.vertxstarterguide.config.AppConfig;
import com.ltamrazov.vertxstarterguide.service.HelloWorld;
import com.ltamrazov.vertxstarterguide.web.HelloController;
import io.vertx.core.Vertx;

/**
 * Created by levontamrazov on 2017-02-02.
 */
public class ServiceBinder extends AbstractModule{

    @Provides @Singleton
    public HelloController provideController(Vertx vertx, HelloWorld service){
        return new HelloController(vertx, service);
    }

    @Provides @Singleton
    public HelloWorld provideService(){
        return new HelloWorld();
    }

    @Provides @Singleton
    public AppConfig provideAppConfig(){
        return new AppConfig(30, 30);
    }

    @Override
    protected void configure() {

    }
}
