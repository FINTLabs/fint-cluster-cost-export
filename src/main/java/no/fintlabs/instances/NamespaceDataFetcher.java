package no.fintlabs.instances;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.fintlabs.service.ApiService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NamespaceDataFetcher {
    private ApiService apiService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Map<String, Double>> clusterNamespaceCosts = new HashMap<>();


    public NamespaceDataFetcher(ApiService apiService) {
        this.apiService = apiService;
    }

    public void fetchAndProcessData(List<WorkloadDetail> workloadDetails, String startTime, String endTime, List<String> namespaces) throws IOException {
        System.out.println("Total namespaces to filter: " + namespaces.size());
        System.out.println("Total workload details: " + workloadDetails.size());
        System.out.println("Please wait while the data is being fetched and processed...\n");

        Set<String> normalizedNamespaces = namespaces.stream()
                .map(this::normalizeNamespace)
                .collect(Collectors.toSet());

        Map<String, List<WorkloadDetail>> workloadsByCluster = workloadDetails.stream()
                        .filter(detail -> normalizedNamespaces.contains(normalizeNamespace(detail.getNamespace())))
                        .collect(Collectors.groupingBy(WorkloadDetail::getClusterId));

        for (Map.Entry<String, List<WorkloadDetail>> entry : workloadsByCluster.entrySet()) {
            String clusterId = entry.getKey();
            List<WorkloadDetail> clusterWorkloads = entry.getValue();

            Set<String> processedNamespaces = new HashSet<>();

            for (WorkloadDetail detail : clusterWorkloads) {
                String normalizedNamespace = normalizeNamespace(detail.getNamespace());

                if (processedNamespaces.add(normalizedNamespace)) {
                String url = String.format(
                        "https://api.cast.ai/v1/cost-reports/clusters/%s/namespace-totalcost?startTime=%s&endTime=%s",
                        detail.getClusterId(),
                        startTime,
                        endTime
                );

                try {
                    String response = apiService.callApi(url);
                    String namespace = detail.getNamespace();
                    processApiResponse(clusterId, detail.getNamespace(), response);
                } catch (IOException e) {
                    System.err.println("Error fetching data for namespace: " + detail.getNamespace());
                    e.printStackTrace();
                }
            }
            }
        }
        printAggregatedTotalCostsByCluster();
    }

    private String normalizeNamespace(String namespace) {
        return namespace.toLowerCase().trim().replace("-", ".");
    }

    private void processApiResponse(String clusterId, String namespace,String response) {
        try {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray itemsArray = jsonResponse.getJSONArray("items");

        double totalNamespaceCost = 0.0;

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject item = itemsArray.getJSONObject(i);
            String itemNamespace = item.getString("namespace");

            if (itemNamespace.equals(namespace)) {
                JSONArray costMetrics = item.getJSONArray("costMetrics");
                for (int j = 0; j < costMetrics.length(); j++) {
                    JSONObject costMetric = costMetrics.getJSONObject(j);
                    double totalCost = costMetric.optDouble("totalCost");
                    totalNamespaceCost += totalCost;
                }
            }
        }

        Map<String, Double> namespaceCosts = clusterNamespaceCosts.computeIfAbsent(clusterId, k -> new HashMap<>());
        namespaceCosts.put(namespace, totalNamespaceCost);

        } catch (Exception e) {
            System.err.println("Error processing API response for cluster " + clusterId + " and namespace " + namespace);
            e.printStackTrace();
        }
    }

    private void printAggregatedTotalCostsByCluster() {
        System.out.println("Total costs by cluster and namespace: ");
        clusterNamespaceCosts.forEach((clusterId,namespaceCosts) -> {
            System.out.println("\nCluster ID: " + clusterId);
            namespaceCosts.forEach((namespace, totalCost) ->
                    System.out.format("Namespace: %-20s | Total Cost: %-25s\n", namespace, totalCost));
        });
    }
}
