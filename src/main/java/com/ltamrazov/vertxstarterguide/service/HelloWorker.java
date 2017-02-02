package com.ltamrazov.vertxstarterguide.service;

import com.google.inject.Inject;
import com.ltamrazov.vertxstarterguide.config.Events;
import com.ltamrazov.vertxstarterguide.domain.Greeting;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * Created by levontamrazov on 2017-02-02.
 * Worker verticle that will process requests through the event
 * bus
 */
public class HelloWorker extends AbstractVerticle{
    private HelloWorld service;

    /**
     * Constructor takes a service this verticle should be using.
     * @param service - HelloWorld service instance
     */
    @Inject
    public HelloWorker(HelloWorld service) {
        this.service = service;
    }

    /**
     * Start method gets called when the verticle is deployed.
     *
     * @param done
     */
    @Override
    public void start(Future<Void> done){
        // Create a message consumer of type String
        // and set it to listen ont he GREET event.
        MessageConsumer<String> consumer= vertx.eventBus().consumer(Events.GREET);

        // Handler for the event
        consumer.handler(m -> {
            // Parse the body into a json object, and call the service with
            // the required parameters
            JsonObject data = new JsonObject(m.body());
            Greeting retval = service.greet(data.getString("name"));

            // reply to the sender or fail the message.
            try{
                m.reply(Json.encode(retval));
            }catch (EncodeException e){
                m.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "Failed to encode data.");
            }
        });

        done.complete();
    }
}
