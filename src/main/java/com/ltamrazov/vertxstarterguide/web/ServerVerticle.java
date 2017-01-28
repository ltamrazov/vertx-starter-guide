package com.ltamrazov.vertxstarterguide.web;

import com.ltamrazov.vertxstarterguide.config.API;
import com.ltamrazov.vertxstarterguide.service.HelloWorld;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class ServerVerticle extends AbstractVerticle{
    @Override
    public void start(Future<Void> future) throws Exception{
        int PORT = 8181;
        HelloWorld service = new HelloWorld();
        HelloController controller = new HelloController(vertx, service);
        Router helloRouter = controller.getRouter();

        // Mount the RouterFactory on /api/v1 end point
        // Initiate the main router and RouterFactory
        Router mainRouter = Router.router(vertx);
        mainRouter.route().consumes("application/json");
        mainRouter.route().produces("application/json");

        Set<String> allowHeaders = getAllowedHeaders();
        Set<HttpMethod> allowMethods = getAllowedMethods();
        mainRouter.route().handler(BodyHandler.create());
        mainRouter.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));

        mainRouter.mountSubRouter(API.HELLO_API, helloRouter);
        mainRouter.route().failureHandler(GlobalHandlers::error);

        // Create the http server and pass it the router
        vertx.createHttpServer()
            .requestHandler(mainRouter::accept)
            .listen(PORT, res -> {
                if(res.succeeded()){
                    System.out.println("Server listening on port " + PORT);
                    future.complete();
                }
                else{
                    System.out.println("Failed to launch server");
                    future.fail(res.cause());
                }
            });
    }

    private Set<String> getAllowedHeaders(){
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        return allowHeaders;
    }

    private Set<HttpMethod> getAllowedMethods(){
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        return allowMethods;
    }
}
