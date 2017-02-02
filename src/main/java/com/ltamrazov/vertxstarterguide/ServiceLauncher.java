package com.ltamrazov.vertxstarterguide;

import com.ltamrazov.vertxstarterguide.service.HelloWorker;
import com.ltamrazov.vertxstarterguide.web.ServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class ServiceLauncher extends AbstractVerticle{

    /**
     * Start method uses CompositeFuture to deploy all required verticles
     *
     * @param done
     */
    @Override
    public void start(Future<Void> done){
        int WORKER_POOL_SIZE = 200;
        // Deploy a single verticle with 500 workers.
        DeploymentOptions serverOpts = new DeploymentOptions()
                .setWorkerPoolSize(WORKER_POOL_SIZE);

        DeploymentOptions workerOpts = new DeploymentOptions()
                .setWorker(true)
                .setWorkerPoolSize(WORKER_POOL_SIZE);

        CompositeFuture.all(
                deploy(ServerVerticle.class.getName(), serverOpts),
                deploy(HelloWorker.class.getName(), workerOpts)
        ).setHandler(r -> {
            if(r.succeeded()){
                done.complete();
            }
            else {
                done.fail(r.cause());
            }
        });
    }

    private Future<Void> deploy(String name, DeploymentOptions opts){
        Future<Void> done = Future.future();

        vertx.deployVerticle(name, opts, res -> {
            if(res.failed()){
                System.out.println("Failed to deploy verticle " + name);
                done.fail(res.cause());
            }
            else {
                System.out.println("Deployed verticle " + name);
                done.complete();
            }
        });

        return done;
    }
}
