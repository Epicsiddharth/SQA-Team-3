package myproj;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WikipediaApiService {
    private static final String BASE_API_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=";

    private final CloseableHttpClient httpClient;

    public WikipediaApiService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public JsonNode search(String query) {
        HttpGet request = new HttpGet(BASE_API_URL + query);

        try {
            HttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
