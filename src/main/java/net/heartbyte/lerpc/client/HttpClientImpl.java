package net.heartbyte.lerpc.client;

import net.heartbyte.lerpc.net.Http;

public class HttpClientImpl extends ClientBase {
    public HttpClientImpl(String remote, String token) {
        super(new Http(), remote, token);
    }
}
