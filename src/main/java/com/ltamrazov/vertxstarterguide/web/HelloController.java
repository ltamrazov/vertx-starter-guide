package com.ltamrazov.vertxstarterguide.web;

import com.ltamrazov.vertxstarterguide.config.API;
import com.ltamrazov.vertxstarterguide.service.HelloWorld;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
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
            router.get(API.LB_CHECK).handler(this::lbCheck);
            router.get(API.GREETING).handler(this::getGreeting);
        }

        return router;
    }

    private void lbCheck(RoutingContext ctx){
        ctx.response().end("ok");
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
            // Cast the exception and return error.
            // The service insures there are no other possibilities for exception.
            ctx.fail(res.cause());
        }
    }
}
