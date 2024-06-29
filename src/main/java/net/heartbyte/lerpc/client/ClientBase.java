package net.heartbyte.lerpc.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.heartbyte.lerpc.net.Http;
import net.heartbyte.lerpc.net.NetClient;
import net.heartbyte.lerpc.proto.Request;
import net.heartbyte.lerpc.proto.Result;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class ClientBase implements Client {
    private Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    private NetClient netClient;

    private String remote;
    private String token;

    private int retries    = 1;
    private int retryDelay = 100;


    public Gson getGson() {
        return this.gson;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public String getRemote() {
        return remote;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int getRetries() {
        return retries;
    }

    @Override
    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public int getRetryDelay() {
        return retryDelay;
    }

    @Override
    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }


    public ClientBase(NetClient netClient, String remote, String token) {
        this.netClient = netClient;
        this.remote = remote;
        this.token = token;
    }

    public Map<String, List<String>> getHeaders() {
        Map<String, List<String>> headers = new HashMap<>();

        headers.put("Content-Type", Collections.singletonList("application/json"));

        return headers;
    }

    public String getUrl() {
        switch (this.getNetClient().getKind()) {
            case HTTP:
                return String.format("http://%s/execute", this.getRemote());

            case HTTPS:
                return String.format("https://%s/execute", this.getRemote());

            default:
                throw new RuntimeException("unimplemented client kind: " + this.getNetClient().getKind());
        }
    }

    @Override
    public CompletableFuture<Result> execute(Request request) {
        return this.getNetClient().Execute(
                "POST",
                this.getUrl(),
                this.getGson()
                        .toJson(request.addToken(this.getToken()))
                        .getBytes(StandardCharsets.UTF_8),
                this.getHeaders(),
                Result.class,
                true);
    }

    @Override
    public Optional<Result> executeSync(Request request) {
        return Optional.ofNullable(this.getNetClient().Execute(
                "POST",
                this.getUrl(),
                this.getGson()
                        .toJson(request.addToken(this.getToken()))
                        .getBytes(StandardCharsets.UTF_8),
                this.getHeaders(),
                Result.class,
                false));
    }

    @Override
    public CompletableFuture<Result> executeWithRetry(Request request, boolean sync) {
        CompletableFuture<Result> future = new CompletableFuture<>();

        Runnable runnable = () -> {
            for (int x = 0; x < this.getRetries(); x++) {
                Result result = this.executeSync(request).orElse(null);
                if (result == null) {
                    try {
                        Thread.sleep(this.getRetryDelay());
                    } catch (InterruptedException exception) {
                        future.completeExceptionally(exception);
                        return;
                    }

                    continue;
                }

                future.complete(result);
                break;
            }

            future.completeExceptionally(new RuntimeException("maximum retries reached"));
        };

        if (sync) {
            runnable.run();
        } else {
            Http.service.submit(runnable);
        }

        return future;
    }
}
