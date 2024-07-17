package no.fintlabs.costServices;

import org.springframework.beans.factory.annotation.Value;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.List;

public interface TeamCostServiceImpl{
    @Value("${fint.cluster.cost.export.apiKey}")
    final String apiKey = "";

    @Headers({
            "accept: application/json",
            "X-API-Key: " + apiKey
    })
    @GET("cost-reports/workload-labels/values?label=fintlabs.no%2Fteam&startTime=2024-06-16T00%3A00%3A00.123456Z&endTime=2024-07-16T00%3A00%3A00.123456Z")
    public Call<List<Labels>> getLabels();
}
