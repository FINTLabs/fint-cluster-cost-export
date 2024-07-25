package no.fintlabs.instances;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Labels {
    public List<String> extractNamespaces(JSONArray labelValues) {
        List<String> namespaces = new ArrayList<>();

        for (int i = 0; i < labelValues.length(); i++) {
            String namespace = labelValues.getString(i);
            namespaces.add(namespace);
        }
        return namespaces;
    }
    public void printLabels(List<String> namespaces) {
        System.out.println("+--------------------------------------+");
        System.out.println("| Label Values                         |");
        System.out.println("+--------------------------------------+");

        for (String namespace : namespaces) {
            System.out.format("| %-36s |%n", namespace);
        }
        System.out.println("+--------------------------------------+");
    }
}
