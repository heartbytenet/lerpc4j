package net.heartbyte.lerpc.net;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface NetClient {
    <T> T Execute(
            String                    method,
            String                    url,
            byte[]                    body,
            Map<String, List<String>> headers,
            Type                      type,
            boolean                   async
    );
}
