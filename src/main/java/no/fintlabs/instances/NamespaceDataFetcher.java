package no.fintlabs.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fintlabs.service.ApiService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NamespaceDataFetcher {
    private ApiService apiService;

    public NamespaceDataFetcher(ApiService apiService) {
        this.apiService = apiService;
    }

    public void fetchAndProcessData(List<WorkloadDetail> workloadDetails, String startTime, String endTime, List<String> namespaces) throws IOException {
        System.out.println("Total namespaces to filter: " + namespaces.size());
        System.out.println("Total workload details: " + workloadDetails.size());

        Set<String> normalizedNamespaces = namespaces.stream()
                .map(ns -> ns.toLowerCase().trim().replace("-", "."))
                .collect(Collectors.toSet());

        Map<String, List<WorkloadDetail>> workloadsByCluster = workloadDetails.stream()
                        .filter(detail -> {
                            String normalizedNamespace = detail.getNamespace().toLowerCase().trim().replace("-", ".");
                            return normalizedNamespaces.contains(normalizedNamespace);
                        })
                        .collect(Collectors.groupingBy(WorkloadDetail::getClusterId));

        for (Map.Entry<String, List<WorkloadDetail>> entry : workloadsByCluster.entrySet()) {
            String clusterId = entry.getKey();
            List<WorkloadDetail> clusterWorkloads = entry.getValue();

            System.out.println("Cluster ID: " + clusterId);

            Set<String> processedNamespaces = new HashSet<>();

            for (WorkloadDetail detail : clusterWorkloads) {
                String normalizedNamespace = detail.getNamespace().toLowerCase().trim().replace("-", ".");

                if (!processedNamespaces.contains(normalizedNamespace)) {
                    processedNamespaces.add(normalizedNamespace);

                String url = String.format(
                        "https://api.cast.ai/v1/cost-reports/clusters/%s/namespace-totalcost?startTime=%s&endTime=%s",
                        detail.getClusterId(),
                        startTime,
                        endTime
                );

                try {
                    String response = apiService.callApi(url);
                    String namespace = detail.getNamespace();
                    processApiResponse(namespace, response);
                } catch (IOException e) {
                    System.err.println("Error fetching data for namespace: " + detail.getNamespace());
                    e.printStackTrace();
                }
            }
            }
        }
    }

    private void processApiResponse(String namespace,String response) {
        System.out.println("Response for namespace " + namespace + ": "+ response);
    }
}
