package no.fintlabs;

import no.fintlabs.service.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        String apiUrl = "https://api.cast.ai/v1/pricing/clusters/9c501dfb-8bac-4182-a798-d67552488065/nodes?nodeIds=98ce2a8f-0d27-4204-ada0-99db43f9b94d&nodeIds=1a0b158a-0fcc-4b6f-8be2-407b99aa28ec";
        String response = apiService.callApi(apiUrl);

        JSONObject jsonObj = new JSONObject(response);
        JSONArray nodes = jsonObj.getJSONArray("nodes");

        // Print header
        System.out.println("+--------------------------------------+----------+-------------------+-----------+");
        System.out.println("| Node ID                              | BasePrice| Total Regular Price | Total Price |");
        System.out.println("+--------------------------------------+----------+-------------------+-----------+");

        // Iterate through each node in the JSON array
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            String id = node.getString("id");
            String basePrice = node.getString("basePrice");
            String totalRegularPrice = node.getString("totalRegularPrice");
            String totalPrice = node.getString("totalPrice");

            // Print each node's data
            System.out.format("| %-36s | %-8s | %-18s | %-10s |\n", id, basePrice, totalRegularPrice, totalPrice);
        }

        // Print footer
        System.out.println("+--------------------------------------+----------+-------------------+-----------+");
    }
}