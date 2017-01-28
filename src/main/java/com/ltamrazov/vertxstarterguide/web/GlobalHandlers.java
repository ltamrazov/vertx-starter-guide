package com.ltamrazov.vertxstarterguide.web;

import com.ltamrazov.vertxstarterguide.exceptions.CustomException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class GlobalHandlers {
    private GlobalHandlers(){}

    public static void error(RoutingContext ctx){
        int status;
        String msg;

        // Get thrown exception from context
        Throwable failure = ctx.failure();

        if(CustomException.class.isAssignableFrom(failure.getClass())){
            msg = failure.getMessage();
            status = HttpResponseStatus.BAD_REQUEST.code();
        }
        else {
            System.out.println(failure);
            msg = "Sorry, something went wrong";
            status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
        }

        // Log the error, and send a json encoded response.
        JsonObject res = new JsonObject().put("status", status).put("message", msg);
        ctx.response().setStatusCode(status).end(res.encode());
    }
}
