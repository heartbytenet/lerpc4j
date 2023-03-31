package net.heartbyte.lerpc.proto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Optional;

public class Result {
    @SerializedName("id") public String              ID;
    @SerializedName("ok") public boolean             success;
    @SerializedName("pl") public Map<String, Object> payload;
    @SerializedName("er") public String              error;

    public <T> Optional<T> getPayloadObject(String key) {
        if (!this.success) {
            return Optional.empty();
        }

        if (this.payload == null) {
            return Optional.empty();
        }

        Object obj = this.payload.getOrDefault(key, null);
        if (obj == null) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable((T) this.payload.getOrDefault(key, null));
        } catch (ClassCastException ignored) {
            return Optional.empty();
        }
    }

    public Optional<String> getPayloadString(String key) {
        return this.getPayloadObject(key);
    }

    public Optional<Integer> getPayloadInteger(String key) {
        return this.<Double>getPayloadObject(key)
                .map(Double::intValue);
    }

    public Optional<Double> getPayloadDouble(String key) {
        return this.getPayloadObject(key);
    }
}
