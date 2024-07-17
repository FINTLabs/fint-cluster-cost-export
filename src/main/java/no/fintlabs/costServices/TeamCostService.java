package no.fintlabs.costServices;

import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Collections;
import java.util.List;

@Service
public class TeamCostService {
    String baseUrl = "https://api.fintlabs.no/v1/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

     public List<Labels> fetchApiStatus(){
         TeamCostServiceImpl teamCostServiceImpl = retrofit.create(TeamCostServiceImpl.class);
         Call<List<Labels>> labels = teamCostServiceImpl.getLabels();
         System.out.println(labels);
         return Collections.emptyList();
     }
}
