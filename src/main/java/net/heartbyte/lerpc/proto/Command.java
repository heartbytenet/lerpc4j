package net.heartbyte.lerpc.proto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Command {
    @SerializedName("id") public String              ID;
    @SerializedName("tk") public String              token;
    @SerializedName("ns") public String              namespace;
    @SerializedName("mt") public String              method;
    @SerializedName("pm") public Map<String, Object> params;
}
