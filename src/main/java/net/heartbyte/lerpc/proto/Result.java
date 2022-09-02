package net.heartbyte.lerpc.proto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Result {
    @SerializedName("id") public String              ID;
    @SerializedName("ok") public boolean             success;
    @SerializedName("pl") public Map<String, Object> payload;
    @SerializedName("er") public String              error;
}
