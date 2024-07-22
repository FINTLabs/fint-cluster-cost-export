package no.fintlabs.instances;

import lombok.Getter;

public class WorkloadDetail {
    @Getter
    private String clusterId;
    @Getter
    private String namespace;
    @Getter
    private String workloadType;
    @Getter
    private String workloadName;

    public WorkloadDetail(String clusterId, String namespace, String workloadType, String workloadName) {
        this.clusterId = clusterId;
        this.namespace = namespace;
        this.workloadType = workloadType;
        this.workloadName = workloadName;
    }
}
