package net.heartbyte.lerpc.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    @Override
    public CompletableFuture<Result> execute(Request request) {
        return this.getNetClient().Execute(
                "POST",
                this.getRemote(),
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
                this.getRemote(),
                this.getGson()
                        .toJson(request.addToken(this.getToken()))
                        .getBytes(StandardCharsets.UTF_8),
                this.getHeaders(),
                Result.class,
                false));
    }
}
