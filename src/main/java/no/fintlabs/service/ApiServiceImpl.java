package no.fintlabs.service;

import org.springframework.beans.factory.annotation.Value;
import okhttp3.*;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${fint.cluster.cost.export.apiKey}")
    private String apiKey;

    private final OkHttpClient client;
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");// private final String apiKey = "17a479e1d6d7dca1a262a9f10cf1a7c7196940a9b80abbe6f4a7ab077ef06099";  // Replace with your actual API key

    public ApiServiceImpl() {
        this.client = new OkHttpClient();
    }

    @Override
    public String callApi(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("X-API-Key", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            } else {
                assert response.body() != null;
                throw new IOException("Failed to get response: " + response.body().string());
            }
        }
    }
}
