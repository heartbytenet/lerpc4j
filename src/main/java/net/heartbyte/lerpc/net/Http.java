package net.heartbyte.lerpc.net;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Http {
    public static String          UserAgent = "leRPC/1.0";
    public static Gson            gson      = new Gson();
    public static ExecutorService service   = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    @SuppressWarnings("unchecked")
    public <T> T Execute(String method, String url, byte[] body, Map<String, List<String>> headers, Type type, boolean async) {
        CompletableFuture<T> future = new CompletableFuture<>();

        Runnable runnable = () -> {
            try {
                HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();

                con.setRequestMethod(method);

                headers.forEach((key, val) -> {
                    con.setRequestProperty(key, val.get(0));
                });

                con.addRequestProperty("User-Agent", UserAgent);

                if (body != null) {
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, StandardCharsets.UTF_8));
                    writer.write(new String(body).toCharArray());
                    writer.close();
                    wr.close();
                }

                BufferedReader input = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

                String        inputLine;
                StringBuilder content   = new StringBuilder();
                while ((inputLine = input.readLine()) != null) {
                    content.append(inputLine);
                }
                input.close();

                con.disconnect();

                future.complete(gson.fromJson(content.toString(), type));
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
                future.completeExceptionally(exception);
            }
        };

        if (async) {
            service.submit(runnable);
        } else {
            runnable.run();
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace(System.err);
                return null;
            }
        }

        return (T) future;
    }

}
