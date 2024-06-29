package net.heartbyte.lerpc.client;

import net.heartbyte.lerpc.net.Http;
import net.heartbyte.lerpc.net.NetClient;

public class HttpClientImpl extends ClientBase {
    public HttpClientImpl(NetClient client, String remote, String token) {
        super(client, remote, token);
    }

    public HttpClientImpl(String remote, String token) {
        super(new Http(), remote, token);
    }
}
