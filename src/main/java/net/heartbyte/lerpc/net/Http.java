package net.heartbyte.lerpc.net;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
                    con.getOutputStream().write(body);
                }

                BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String        inputLine;
                StringBuilder content   = new StringBuilder();
                while ((inputLine = input.readLine()) != null) {
                    content.append(inputLine);
                }
                input.close();

                con.disconnect();

                future.complete(gson.fromJson(content.toString(), type));
            } catch (IOException exception) {
                exception.printStackTrace(System.err);
                throw new RuntimeException(exception);
            } catch (Exception exception) {
                exception.printStackTrace(System.err);
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
