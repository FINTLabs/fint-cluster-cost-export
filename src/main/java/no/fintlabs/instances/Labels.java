package no.fintlabs.instances;

import org.json.JSONArray;

public class Labels {
    public void printLabels(JSONArray labelValues) {
        System.out.println("+--------------------------------------+");
        System.out.println("| Label Values                         |");
        System.out.println("+--------------------------------------+");

        for (int i = 0; i < labelValues.length(); i++) {
            Object obj = labelValues.get(i);
            String label = obj.toString();

            System.out.format("| %-36s |%n", label);
        }
        System.out.println("+--------------------------------------+");
    }
}
