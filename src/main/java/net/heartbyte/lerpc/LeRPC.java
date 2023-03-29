package net.heartbyte.lerpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.heartbyte.lerpc.net.Client;
import net.heartbyte.lerpc.net.Http;
import net.heartbyte.lerpc.proto.Command;
import net.heartbyte.lerpc.proto.Result;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class LeRPC {
    public final Gson gson;

    public Client  clientHttp;
    public String  token;
    public boolean secure;
    public String  endpoint;

    public Gson getGson() {
        return this.gson;
    }

    public LeRPC(Client client, String endpoint, String token) {
        this.gson       = new GsonBuilder().disableHtmlEscaping().create();
        this.token      = token;
        this.secure     = true;
        this.endpoint   = endpoint;
        this.clientHttp = client;
    }

    public LeRPC(String endpoint, String token) {
        this.gson       = new GsonBuilder().disableHtmlEscaping().create();
        this.token      = token;
        this.secure     = true;
        this.endpoint   = endpoint;
        this.clientHttp = new Http();
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Optional<Result> executeAttempt(Command command) {
        command.token = this.token;

        Map<String, List<String>> headers = new HashMap<>();

        headers.put("Content-Type", Collections.singletonList("application/json"));

        return Optional.ofNullable(this.clientHttp.Execute(
                "POST",
                String.format("%s://%s/execute", (this.secure ? "https" : "http"), this.endpoint),
                this.getGson().toJson(command).getBytes(StandardCharsets.UTF_8),
                headers,
                Result.class,
                false));
    }

    public Optional<Result> execute(Command command) {
        for (int attempt = 0; attempt < 10; attempt++) {
            Optional<Result> resultOptional = this.executeAttempt(command);
            if (resultOptional.isPresent())
                return resultOptional;
        }

        return Optional.empty();
    }
}
