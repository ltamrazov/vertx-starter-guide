package com.ltamrazov.vertxstarterguide.web;

import com.google.inject.Inject;
import com.ltamrazov.vertxstarterguide.config.API;
import com.ltamrazov.vertxstarterguide.config.Events;
import com.ltamrazov.vertxstarterguide.service.HelloWorld;
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
 * Hello controller is responsible fort he Hello service api.
 * It contains all the relevant handlers and creates a router
 * that maps the end points to the handlers.
 */
public class HelloController {
    private Vertx vertx;
    private HelloWorld service;
    private Router router;

    /**
     * Takes an instance of Vertx for blocking calls and
     * the service it should be using.
     * @param vertx - Vertx instance
     * @param service - HelloWorld service
     */
    @Inject
    public HelloController(Vertx vertx, HelloWorld service){
        this.vertx = vertx;
        this.service = service;
    }

    /**
     * Return a configured Router instance
     * @return - vertx Router
     */
    public Router getRouter(){
        if(router == null){
            router = Router.router(vertx);
            router.get(API.GREETING).handler(this::getGreeting);
            router.get(API.GREETING_W).handler(this::getGreetingW);
        }

        return router;
    }

    /**
     * Handler that uses executeBlocking to process request
     * async
     * @param ctx - RoutingContext for the request
     */
    private void getGreeting(RoutingContext ctx){
        String name = ctx.request().getParam("name");
        vertx.executeBlocking(
                fut -> { fut.complete(service.greet(name)); },
                false, // IMPORTANT TO MAKE THIS FALSE
                res -> {
                    handleAsyncResponse(res, ctx); }
        );
    }

    /**
     * Handler that uses the event bus and a worker verticle
     * to process the request async
     * @param ctx - RoutingContext for the request
     */
    private void getGreetingW(RoutingContext ctx){
        String data = new JsonObject()
                .put("name", ctx.request().getParam("name"))
                .encode();

        vertx.eventBus().send(Events.GREET, data, res -> { handleEventBusResponse(res, ctx); });
    }

    /**
     * Helper method for handling executeBlocking responses
     * @param res - AsynResult from executeBlocking
     * @param ctx - RoutingContext for the request
     */
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

    /**
     * Helper method for handling event bus responses
     * @param res - Event bus response
     * @param ctx - RoutingContext for the request
     */
    private void handleEventBusResponse(AsyncResult<Message<Object>> res, RoutingContext ctx){
        if(res.succeeded()){
            ctx.response().end(res.result().body().toString());
        }
        else {
            ctx.fail(res.cause());
        }
    }
}
