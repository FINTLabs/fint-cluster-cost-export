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

        String url = "https://api.cast.ai/v1/cost-reports/workload-labels/values?label=fintlabs.no%2Forg-id&startTime=%s&endTime=%s".replace("%2F", "/");

        String formattedUrl = String.format(url, startTime, endTime);

        String response = apiService.callApi(formattedUrl);

        JSONObject jsonObj = new JSONObject(response);
        JSONArray labelValues = jsonObj.getJSONArray("labelValues");

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
        System.out.println("+--------------------------------------+");
    }
}
