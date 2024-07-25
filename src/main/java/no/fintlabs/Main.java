package no.fintlabs;

import no.fintlabs.instances.*;
import no.fintlabs.service.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class Main implements CommandLineRunner {

    private final ApiService apiService;

    @Autowired
    public Main(ApiService apiService) {
        this.apiService = apiService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime aMonthAgo = now.minusMonths(1);

        String startTime = aMonthAgo.toString().replace(":", "%3A") + "Z";
        String endTime = now.toString().replace(":", "%3A") + "Z";

        // Create url for labels
        String labelUrl = "https://api.cast.ai/v1/cost-reports/workload-labels/values?label=fintlabs.no%2Forg-id&startTime=%s&endTime=%s".replace("%2F", "/");
        String formattedLabelUrl = String.format(labelUrl, startTime, endTime);
        String response = apiService.callApi(formattedLabelUrl);

        // Create url for workload metadata
        String workloadUrl = "https://api.cast.ai/v1/cost-reports/workloads/metadata?startTime=%s&endTime=%s";
        String formattedWorkloadUrl = String.format(workloadUrl, startTime, endTime);
        String workloadResponse = apiService.callApi(formattedWorkloadUrl);

        JSONObject labelJsonObj = new JSONObject(response);
        JSONArray labelValues = labelJsonObj.getJSONArray("labelValues");

        JSONObject workloadJsonObj = new JSONObject(workloadResponse);
        JSONArray workloadMetadata = workloadJsonObj.getJSONArray("workloads");

        Labels labels = new Labels();
        List<String> namespaces = labels.extractNamespaces(labelValues);
        labels.printLabels(namespaces);

        Workload workload = new Workload();
        List<WorkloadDetail> workloadDetails = workload.getWorkloads(workloadMetadata);

        UrlGeneratorSingleWorkload urlGeneratorSingleWorkload = new UrlGeneratorSingleWorkload();
        urlGeneratorSingleWorkload.generateUrls(workloadDetails);

        NamespaceDataFetcher namespaceDataFetcher = new NamespaceDataFetcher(apiService);
        namespaceDataFetcher.fetchAndProcessData(workloadDetails, startTime, endTime, namespaces);
    }
}
