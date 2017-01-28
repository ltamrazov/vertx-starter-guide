package com.ltamrazov.vertxstarterguide;

import com.ltamrazov.vertxstarterguide.web.ServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class ServiceLauncher extends AbstractVerticle{
    @Override
    public void start(Future<Void> done){
        int WORKER_POOL_SIZE = 500;
        // Deploy a single verticle with 500 workers.
        DeploymentOptions opts = new DeploymentOptions().setWorkerPoolSize(500);
        String verticle = ServerVerticle.class.getName();
        vertx.deployVerticle(verticle, opts, res -> {
            if(res.failed()){
                System.out.println("Failed to deploy verticle");
                done.fail(res.cause());
            }
            else {
                System.out.println("Successfully deployed verticle");
                done.complete();
            }
        });
    }
}
