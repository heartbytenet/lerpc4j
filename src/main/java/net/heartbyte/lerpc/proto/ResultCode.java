package net.heartbyte.lerpc.proto;

public enum ResultCode {
    NONE(0),
    SUCCESS(1),
    WARNING(2),
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
