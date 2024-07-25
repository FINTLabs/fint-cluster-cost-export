package no.fintlabs.instances;

import no.fintlabs.service.ApiService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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

        List<WorkloadDetail> sortedWorkloadDetailsByNamespace = workloadDetails.stream()
                .filter(detail -> {
                            String normalizedNamespace = detail.getNamespace().toLowerCase().trim().replace("-", ".");
                    return normalizedNamespaces.contains(normalizedNamespace);
                })
                .toList();

        System.out.println("Filtered workload details count: " + sortedWorkloadDetailsByNamespace.size());

        for (WorkloadDetail detail : sortedWorkloadDetailsByNamespace) {
            String url = String.format(
                    "https://api.cast.ai/v1/cost-reports/clusters/%s/namespace-totalcost?startTime=%s&endTime=%s",
                    detail.getClusterId(),
                    startTime,
                    endTime
            );

            try {
                String response = apiService.callApi(url);
                processApiResponse(response, detail.getNamespace());
            } catch (IOException e) {
                System.err.println("Error fetching data for namespace: " + detail.getNamespace());
                e.printStackTrace();            }
        }
    }

    private void processApiResponse(String response, String namespace) {
        System.out.println("Response for namespace " + namespace + ": "+ response);
    }
}
