package no.fintlabs.instances;

import java.util.List;

public class UrlGeneratorSingleWorkload {
    public void generateUrls(List<WorkloadDetail> workloadDetails) {

        for (WorkloadDetail workloadDetail : workloadDetails) {
            String url = String.format("https://api.cast.ai/v1/cost-reports/clusters/%s/namespaces/%s/%s/%s/datatransfer-costs",
                    workloadDetail.getClusterId(),
                    workloadDetail.getNamespace(),
                    workloadDetail.getWorkloadType(),
                    workloadDetail.getWorkloadName());
        }
    }
}
