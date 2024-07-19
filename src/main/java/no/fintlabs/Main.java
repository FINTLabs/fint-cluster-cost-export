package no.fintlabs;

import no.fintlabs.service.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

        // Print header
        System.out.println("+--------------------------------------+");
        System.out.println("| Label Values                         |");
        System.out.println("+--------------------------------------+");

        // Iterate through each label in the JSON array
        for (int i = 0; i < labelValues.length(); i++) {
            Object obj = labelValues.get(i);
            String label = obj.toString();

            // Print each label's name
            System.out.format("| %-36s |\n", label);
        }

        // Print footer
        System.out.println("+--------------------------------------+\n\n");

        // Print header
        System.out.println("+--------------------------------------+");
        System.out.println("| Teams                                |");
        System.out.println("+--------------------------------------+");

        // Iterate trough the workloadMetadata
        for (int i = 0; i < workloadMetadata.length(); i++) {
            Object obj = workloadMetadata.get(i);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("labels", ((JSONObject) obj).get("labels"));

            JSONArray labels = (JSONArray) map.get("labels");
            for (int j = 0; j < labels.length(); j++) {
                JSONObject label = labels.getJSONObject(j);
                String name = label.getString("name");
                if ("fintlabs.no/team".equals(name)) {
                    String value = label.getString("value");
                    System.out.format("| %-36s |\n", value);
                }
            }
        }


        // Print footer
        System.out.println("+--------------------------------------+");
    }
}
