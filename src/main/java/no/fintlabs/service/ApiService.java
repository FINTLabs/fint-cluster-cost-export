package no.fintlabs.service;

import java.io.IOException;

public interface ApiService {
    String callApi(String url) throws IOException;

    String callGetApi(String url) throws IOException;
}
