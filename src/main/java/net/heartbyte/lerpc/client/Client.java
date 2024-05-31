package net.heartbyte.lerpc.client;

import net.heartbyte.lerpc.proto.Request;
import net.heartbyte.lerpc.proto.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Client {
    CompletableFuture<Result> execute(Request request);
    Optional<Result> executeSync(Request request);
}
