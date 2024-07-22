package no.fintlabs.instances;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workload {
    public void printWorkloads(JSONArray workloadMetadata) {

        for (int i = 0; i < workloadMetadata.length(); i++) {
            Object obj = workloadMetadata.get(i);

            System.out.println("+-------------------------------------------------------------------+");
            System.out.format("| Namespace:     %-50s |%n", ((JSONObject) obj).get("namespace"));
            System.out.format("| Workload Type: %-50s |%n", ((JSONObject) obj).get("workloadType"));
            System.out.format("| Workload Name: %-50s |%n", ((JSONObject) obj).get("workloadName"));
            System.out.format("| Cluster Id:    %-50s |%n", ((JSONObject) obj).get("namespace"));

            Map<String, Object> map = new HashMap<>();
            map.put("labels", ((JSONObject) obj).get("labels"));

            JSONArray labels = (JSONArray) map.get("labels");
            for (int j = 0; j < labels.length(); j++) {
                JSONObject label = labels.getJSONObject(j);
                String name = label.getString("name");
                if ("fintlabs.no/team".equals(name)) {
                    String value = label.getString("value");
                    System.out.format("| Team:          %-50s |%n", value);
                }
            }
            System.out.println("+-------------------------------------------------------------------+");
        }
    }

    public List<WorkloadDetail> getWorkloads(JSONArray workloadMetadata) {
        List<WorkloadDetail> workloadDetails = new ArrayList<>();

        for (int i = 0; i < workloadMetadata.length(); i++) {
            JSONObject obj = workloadMetadata.getJSONObject(i);

            String namespace = obj.getString("namespace");
            String workloadType = obj.getString("workloadType");
            String workloadName = obj.getString("workloadName");
            String clusterId = obj.getString("clusterId");

            workloadDetails.add(new WorkloadDetail(clusterId, namespace, workloadType, workloadName));
        }
        return workloadDetails;
    }
}
