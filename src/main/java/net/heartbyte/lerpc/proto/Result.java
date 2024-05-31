package net.heartbyte.lerpc.proto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

public class Result {
    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    @SerializedName("k")
    private String key;

    @SerializedName("c")
    private ResultCode code;

    @SerializedName("d")
    private Map<String, Object> data;

    @SerializedName("m")
    private String message;

    public String getKey() {
        return key;
    }

    public ResultCode getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Optional<Object> getData(String key) {
        if (this.data == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(data.get(key));
    }

    @Override
    public String toString() {
        return String.format(
                "Result{key=%s, code=%s, data=%s message=%s}",
                getKey(), getCode(), getData(), getMessage());
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getDataValue(String key) {
        if (this.getCode() == ResultCode.ERROR) {
            return Optional.empty();
        }

        return this.getData(key)
                .flatMap(it -> Optional.of((T) it));
    }

    public <T> Optional<T> getDataConvert(String key, Type type) {
        return this.getDataValue(key)
                .map(gson::toJson)
                .map(it -> gson.fromJson(it, type));
    }

    public Optional<String> getDataString(String key) {
        return this.getDataValue(key);
    }

    public Optional<Integer> getDataInteger(String key) {
        return this.<Double>getDataValue(key)
                .map(Double::intValue);
    }

    public Optional<Double> getDataDouble(String key) {
        return this.getDataValue(key);
    }
}
