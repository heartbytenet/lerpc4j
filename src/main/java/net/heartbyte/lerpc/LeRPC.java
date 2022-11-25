package net.heartbyte.lerpc;

import com.google.gson.Gson;
import net.heartbyte.lerpc.net.Http;
import net.heartbyte.lerpc.proto.Command;
import net.heartbyte.lerpc.proto.Result;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeRPC {
    public Gson   gson;
    public Http   clientHttp;
    public String token;
    public boolean secure;
    public String endpoint;

    public LeRPC(String endpoint, String token) {
        this.gson       = new Gson();
        this.token      = token;
        this.secure     = true;
        this.endpoint   = endpoint;
        this.clientHttp = new Http();
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Result execute(Command command) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Collections.singletonList("application/json"));

        command.token = this.token;

        return this.clientHttp.Execute(
                "POST",
                String.format("%s://%s/execute", (this.secure ? "https" : "http"), this.endpoint),
                this.gson.toJson(command).getBytes(StandardCharsets.UTF_8),
                headers,
                Result.class,
                false);
    }
}
