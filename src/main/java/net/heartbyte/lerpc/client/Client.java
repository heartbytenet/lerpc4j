package net.heartbyte.lerpc.client;

import net.heartbyte.lerpc.proto.Request;
import net.heartbyte.lerpc.proto.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Client {
    int getRetries();
    void setRetries(int value);
    int getRetryDelay();
    void setRetryDelay(int value);

    CompletableFuture<Result> execute(Request request);
    Optional<Result> executeSync(Request request);
    CompletableFuture<Result> executeWithRetry(Request request, boolean sync);
}
