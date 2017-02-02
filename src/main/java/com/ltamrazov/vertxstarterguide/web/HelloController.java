package com.ltamrazov.vertxstarterguide.web;

import com.ltamrazov.vertxstarterguide.config.API;
import com.ltamrazov.vertxstarterguide.config.Events;
import com.ltamrazov.vertxstarterguide.service.HelloWorld;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class HelloController {
    private Vertx vertx;
    private HelloWorld service;
    private Router router;

    public HelloController(Vertx vertx, HelloWorld service){
        this.vertx = vertx;
        this.service = service;
    }

    public Router getRouter(){
        if(router == null){
            router = Router.router(vertx);
            router.get(API.GREETING).handler(this::getGreeting);
            router.get(API.GREETING_W).handler(this::getGreetingW);
        }

        return router;
    }

    private void getGreeting(RoutingContext ctx){
        String name = ctx.request().getParam("name");
        vertx.executeBlocking(
                fut -> { fut.complete(service.greet(name)); },
                false, // IMPORTANT TO MAKE THIS FALSE
                res -> {
                    handleAsyncResponse(res, ctx); }
        );
    }

    private void getGreetingW(RoutingContext ctx){
        String data = new JsonObject()
                .put("name", ctx.request().getParam("name"))
                .encode();

        vertx.eventBus().send(Events.GREET, data, res -> { handleEventBusResponse(res, ctx); });
    }

    private void handleAsyncResponse(AsyncResult<Object> res, RoutingContext ctx){
        // Handler for the future. If successful, encode result and send
        if(res.succeeded()){
            try {
                ctx.response().end(Json.encode(res.result()));
            }
            catch(EncodeException e){
                ctx.fail(new RuntimeException("Failed to encode results."));
            }

        }
        else {
            ctx.fail(res.cause());
        }
    }

    private void handleEventBusResponse(AsyncResult<Message<Object>> res, RoutingContext ctx){
        if(res.succeeded()){
            ctx.response().end(res.result().body().toString());
        }
        else {
            ctx.fail(res.cause());
        }
    }
}
