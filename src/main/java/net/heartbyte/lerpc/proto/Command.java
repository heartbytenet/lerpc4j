package net.heartbyte.lerpc.proto;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Command {
    @SerializedName("id") public String              ID;
    @SerializedName("tk") public String              token;
    @SerializedName("ns") public String              namespace;
    @SerializedName("mt") public String              method;
    @SerializedName("pm") public Map<String, Object> params;

    public Command setNamespace(String value) {
        this.namespace = value;
        return this;
    }

    public Command setMethod(String value) {
        this.method = value;
        return this;
    }

    public Command setParam(String key, Object value) {
        if (this.params == null)
            this.params = new HashMap<>();
        this.params.put(key, value);
        return this;
    }
}