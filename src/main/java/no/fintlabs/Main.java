package no.fintlabs;

import no.fintlabs.service.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

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

//    @Override
//    public void run(String... args) throws Exception {
//        String apiUrl = "https://api.cast.ai/v1/pricing/nodes";
//        String response = apiService.callApi(apiUrl);
//
//        JSONObject jsonObj = new JSONObject(response);
//        JSONArray nodes = jsonObj.getJSONArray("nodes");
//
//        // Print header
//        System.out.println("+--------------------------------------+----------+---------------------+-------------+");
//        System.out.println("| Node ID                              | BasePrice| Total Regular Price | Total Price |");
//        System.out.println("+--------------------------------------+----------+---------------------+-------------+");
//
//        // Iterate through each node in the JSON array
//        for (int i = 0; i < nodes.length(); i++) {
//            JSONObject node = nodes.getJSONObject(i);
//            String id = node.getString("id");
//            String basePrice = node.getString("basePrice");
//            String totalRegularPrice = node.getString("totalRegularPrice");
//            String totalPrice = node.getString("totalPrice");
//
//            // Print each node's data
//            System.out.format("| %-36s | %-8s | %-19s | %-11s |\n", id, basePrice, totalRegularPrice, totalPrice);
//        }
//
//        // Print footer
//        System.out.println("+--------------------------------------+----------+---------------------+-------------+");
//    }

    //write a method that gets a single namespace cost report with a monthly breakdown.
    public void run(String... args) throws Exception {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMonths(1);
        String apiUrl = "https://api.cast.ai/v1/cost-reports/clusters/9c501dfb-8bac-4182-a798-d67552488065/namespaces/afk-no?startTime=2024-06-09T15%3A18%3A55.121331Z&endTime=2024-07-10T15%3A18%3A55.121331Z";
        String response = apiService.callApi(apiUrl);

        JSONObject jsonObj = new JSONObject(response);
        JSONArray dailyCostItems = jsonObj.getJSONArray("dailyCostItems");

        // Print header
        System.out.println("+----------------------------+----------------+--------------------+--------------------+");
        System.out.println("| Timestamp                  | Cost On Demand | Ram Cost On Demand | Cpu Cost On Demand |");
        System.out.println("+----------------------------+----------------+--------------------+--------------------+");

        // Iterate through each day in the JSON array
        for (int i = 0; i < dailyCostItems.length(); i++) {
            JSONObject day = dailyCostItems.getJSONObject(i);
            String date = day.getString("timestamp");
            String costOnDemand = day.getString("costOnDemand");
            String ramCostOnDemand = day.getString("ramCostOnDemand");
            String cpuCostOnDemand = day.getString("cpuCostOnDemand");

            // Print each day's data
            System.out.format("| %-26s | %-14s | %-18s | %-18s |\n", date, costOnDemand, ramCostOnDemand, cpuCostOnDemand);
        }

        // Print footer
        System.out.println("+----------------------------+----------------+--------------------+--------------------+");
    }
}
