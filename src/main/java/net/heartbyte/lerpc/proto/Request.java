package net.heartbyte.lerpc.proto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Request {
    @SerializedName("t")
    private String token;

    @SerializedName("k")
    private String key;

    @SerializedName("n")
    private String namespace;

    @SerializedName("m")
    private String method;

    @SerializedName("p")
    private Map<String, Object> params;

    public static Request create(String namespace, String method) {
        return new Request()
                .withNamespace(namespace)
                .withMethod(method);
    }

    @Override
    public String toString() {
        return String.format(
                "Request{key=%s, namespace=%s, method=%s, params=%s}",
                this.getKey(), this.getNamespace(), this.getMethod(), this.getParams());
    }

    public Request withToken(String token) {
        this.token = token;

        return this;
    }

    public Request addToken(String token) {
        if (this.token == null) {
            this.token = token;
        }

        return this;
    }

    public Request withKey(String key) {
        this.key = key;

        return this;
    }

    public Request addKey(String key) {
        if (this.key == null) {
            this.key = key;
        }

        return this;
    }

    public Request withNamespace(String namespace) {
        this.namespace = namespace;

        return this;
    }

    public Request withMethod(String method) {
        this.method = method;

        return this;
    }

    public Request withParams(Map<String, Object> params) {
        this.params = params;

        return this;
    }

    public Request setParam(String key, Object value) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }

        this.params.put(key, value);

        return this;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Optional<Object> getParam(String key) {
        if (this.params == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(params.get(key));
    }
}
