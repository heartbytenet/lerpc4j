package net.heartbyte.lerpc.proto;

import com.google.gson.annotations.SerializedName;

public enum ResultCode {
    @SerializedName("0")
    NONE(0),

    @SerializedName("1")
    SUCCESS(1),

    @SerializedName("2")
    WARNING(2),

    @SerializedName("3")
    ERROR(3),
    ;

    private final int value;

    ResultCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
