package myproj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

@ExtendWith(MockitoExtension.class)
public class JUnitAllTests {	
	
	//JUNIT TESTS
	
	//First Test: testing main page gives 200 response code
	@Test
    public void testWikipediaHomePage() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("https://en.wikipedia.org/wiki/Main_Page");

        HttpResponse response = httpClient.execute(request);

        assertEquals(200, response.getStatusLine().getStatusCode());

        httpClient.close();
	}
	//Second Test: testing a fake page for a 404 response code
	@Test
    public void testFakeWikipediaHomePage() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet("https://en.wikipedia.org/wiki/Fake_Main_Page");

        HttpResponse response = httpClient.execute(request);

        assertEquals(404, response.getStatusLine().getStatusCode());

        httpClient.close();
	}
	//Third Test: testing to see if certain content is on the website
	@Test
	public void testWikipediaMainPageContent() throws IOException {
	    CloseableHttpClient httpClient = HttpClients.createDefault();

	    HttpGet request = new HttpGet("https://en.wikipedia.org/wiki/Main_Page");

	    HttpResponse response = httpClient.execute(request);

	    String content = EntityUtils.toString(response.getEntity());

	    assertTrue(content.contains("From today's featured article"));

	    httpClient.close();
	}
	//Fourth Test: testing response time to see if it's a reasonable amount of time
	@Test
	public void testWikipediaResponseTime() throws IOException {
	    CloseableHttpClient httpClient = HttpClients.createDefault();

	    HttpGet request = new HttpGet("https://en.wikipedia.org/wiki/Main_Page");

	    long startTime = System.currentTimeMillis();

	    HttpResponse response = httpClient.execute(request);

	    long endTime = System.currentTimeMillis();

	    long responseTime = endTime - startTime;

	    assertTrue(responseTime < 2000);

	    httpClient.close();
	}
	//Fifth Test: testing the website's security
	@Test
	public void testWikipediaHttpsAndSecurity() throws IOException {
	    CloseableHttpClient httpClient = HttpClients.createDefault();

	    HttpGet request = new HttpGet("https://en.wikipedia.org/wiki/Main_Page");

	    HttpResponse response = httpClient.execute(request);

	    assertEquals("https", request.getURI().getScheme());

	    Header[] headers = response.getAllHeaders();
	    for (Header header : headers) {
	        System.out.println(header.getName() + ": " + header.getValue());
	    }

	    boolean hasStrictTransportSecurity = Arrays.stream(headers).anyMatch(h -> h.getName().equalsIgnoreCase("Strict-Transport-Security"));
	    boolean hasXContentTypeOptions = Arrays.stream(headers).anyMatch(h -> h.getName().equalsIgnoreCase("X-Content-Type-Options"));

	    System.out.println("Has Strict-Transport-Security: " + hasStrictTransportSecurity);
	    System.out.println("Has X-Content-Type-Options: " + hasXContentTypeOptions);

	    assertTrue("Strict-Transport-Security header is missing", hasStrictTransportSecurity);
	    assertTrue("X-Content-Type-Options header is missing", hasXContentTypeOptions);

	    httpClient.close();
	}
	//Sixth Test: testing internal links
	@Test
    public void testInternalLink() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Main_Page").get();
        
        Elements links = doc.select("a[href^='/wiki/']");
        if (!links.isEmpty()) {
            String link = links.first().attr("href");

            HttpGet linkRequest = new HttpGet("https://en.wikipedia.org" + link);
            HttpResponse linkResponse = httpClient.execute(linkRequest);
            int statusCode = linkResponse.getStatusLine().getStatusCode();
            
            assertTrue("Internal link failed: " + link, statusCode == 200 || statusCode == 301 || statusCode == 302);
            System.out.println("Checked link: " + link + " - Status code: " + statusCode);
        } else {
            System.out.println("No internal links found on the page.");
        }

        httpClient.close();
    }
	//Seventh Test: testing external links
	@Test
    public void testExternalLink() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Main_Page").get();
        
        Elements externalLinks = doc.select("a[href^='http']");
        if (!externalLinks.isEmpty()) {
            String externalLink = externalLinks.first().attr("href");

            HttpGet linkRequest = new HttpGet(externalLink);
            HttpResponse linkResponse = httpClient.execute(linkRequest);
            int statusCode = linkResponse.getStatusLine().getStatusCode();
            
            assertTrue("External link failed: " + externalLink, statusCode == 200 || statusCode == 301 || statusCode == 302);
            System.out.println("Checked link: " + externalLink + " - Status code: " + statusCode);
        } else {
            System.out.println("No external links found on the page.");
        }
        httpClient.close();
    }
	//Eight Test: testing if wikipedia supports other languages
	@Test
    public void testWikipediaLanguageSupport() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            Map<String, String> languages = new HashMap<>();
            languages.put("https://en.wikipedia.org/wiki/Main_Page", "Welcome to Wikipedia");
            languages.put("https://es.wikipedia.org/wiki/Wikipedia:Portada", "Bienvenidos a Wikipedia");
            languages.put("https://fr.wikipedia.org/wiki/Wikip%C3%A9dia:Accueil_principal", "Bienvenue sur Wikipédia");

            for (Map.Entry<String, String> entry : languages.entrySet()) {
                String url = entry.getKey();
                String expectedText = entry.getValue();

                HttpGet request = new HttpGet(url);

                HttpResponse response = httpClient.execute(request);

                assertEquals(200, response.getStatusLine().getStatusCode());

                String content = EntityUtils.toString(response.getEntity());
                Document doc = Jsoup.parse(content);

                assertTrue("Expected text not found in " + url, doc.text().contains(expectedText));
                System.out.println("Verified language support for: " + url);
            }
        } finally {
            httpClient.close();
        }
    }
	//Ninth Test: testing wikipedia's success API response
	private static final String BASE_API_URL = "https://en.wikipedia.org/w/api.php";
	@Test
    public void testWikipediaApiSuccessResponse() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            String url = BASE_API_URL + "?action=query&format=json&list=search&srsearch=Java";

            HttpGet request = new HttpGet(url);

            HttpResponse response = httpClient.execute(request);

            assertEquals(200, response.getStatusLine().getStatusCode());

            String content = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(content);

            assertNotNull("Query node is missing", jsonResponse.get("query"));
            assertNotNull("Search node is missing", jsonResponse.get("query").get("search"));

            JsonNode searchResults = jsonResponse.get("query").get("search");
            assertTrue("No search results found", searchResults.size() > 0);
            System.out.println("Verified API response structure and content");
        } finally {
            httpClient.close();
        }
    }
	//Tenth Test: testing wikipedia's warning API response
	@Test
	public void testWikipediaApiWarningResponse() throws IOException {
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    try {
	        String url = BASE_API_URL + "?action=query&format=json&list=search&srsearch=Java&invalidparam=value";

	        HttpGet request = new HttpGet(url);

	        HttpResponse response = httpClient.execute(request);

	        assertEquals(200, response.getStatusLine().getStatusCode());

	        String content = EntityUtils.toString(response.getEntity());
	        System.out.println("Response content: " + content);
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsonResponse = mapper.readTree(content);

	        assertNotNull("Warnings node is missing", jsonResponse.get("warnings"));
	        assertNotNull("Main warning node is missing", jsonResponse.get("warnings").get("main"));
	        assertNotNull("Warning message is missing", jsonResponse.get("warnings").get("main").get("*"));

	        System.out.println("Warning message: " + jsonResponse.get("warnings").get("main").get("*").asText());
	    } finally {
	        httpClient.close();
	    }
	}
	
	//MOCKITO TESTS
	
	@Mock
    private CloseableHttpClient httpClient;
    @InjectMocks
    private WikipediaApiService apiService;
    
	//Eleventh Test: testing if API returns search results correctly
    @Test
    public void testWikipediaApiReturnsSearchResults() throws IOException {
        String jsonResponse = "{ \"query\": { \"search\": [ { \"title\": \"Java\" } ] } }";
        InputStream stream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));

        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = mock(HttpEntity.class);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(stream);

        JsonNode response = apiService.search("Java");

        assertNotNull(response.get("query").get("search"));
    }
	//Twelfth Test: testing if API can handle an empty search result
    @Test
    public void testWikipediaApiEmptySearchResult() throws IOException {
        String jsonResponse = "{ \"query\": { \"search\": [] } }";
        InputStream stream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));

        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = mock(HttpEntity.class);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(stream);

        JsonNode response = apiService.search("NonExistent");

        assertTrue(response.get("query").get("search").isEmpty());
    }
	//Thirteenth Test: testing if API can handle a network error
	@Test
	public void testWikipediaApiNetworkError() throws IOException {
	    when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException("Network error"));

	    JsonNode response = apiService.search("Java");

	    assertNull(response);
	}
	//Fourteenth Test: testing if API can handle a malformed JSON response
	@Test
    public void testWikipediaApiMalformedJsonResponse() throws IOException {
        String malformedJson = "{ \"query\": { \"search\": [ { \"title\": \"Java\" } ";

        InputStream stream = new ByteArrayInputStream(malformedJson.getBytes(StandardCharsets.UTF_8));

        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = mock(HttpEntity.class);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(stream);

        JsonNode response = apiService.search("Java");

        assertNull(response);
    }
	//Fifteenth Test: testing if API can handle an error response
	@Test
    public void testWikipediaApiErrorResponse() throws IOException {
        String errorJson = "{ \"error\": { \"code\": \"invalid_param\", \"info\": \"Unrecognized parameter: invalidparam.\" } }";

        InputStream stream = new ByteArrayInputStream(errorJson.getBytes(StandardCharsets.UTF_8));

        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity httpEntity = mock(HttpEntity.class);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(stream);

        JsonNode response = apiService.search("Java&invalidparam=value");

        assertNotNull(response.get("error"));
    }
	//Sixteenth Test: testing if API can handle a redirect response
	@Test
	public void testWikipediaApiRedirectResponse() throws IOException {
	    String redirectJson = "{ \"query\": { \"search\": [ { \"title\": \"Java (programming language)\", \"snippet\": \"Java is a programming language.\" } ] } }";

	    InputStream stream = new ByteArrayInputStream(redirectJson.getBytes(StandardCharsets.UTF_8));

	    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
	    HttpEntity httpEntity = mock(HttpEntity.class);

	    when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
	    when(httpResponse.getEntity()).thenReturn(httpEntity);
	    when(httpEntity.getContent()).thenReturn(stream);

	    JsonNode response = apiService.search("Java");

	    assertNotNull(response.get("query"));
	    assertTrue(response.get("query").get("search").size() > 0);
	    assertEquals("Java (programming language)", response.get("query").get("search").get(0).get("title").asText());
	}
	//Seventeenth Test: testing if API can handle the rate limit exceeded
	@Test
	public void testWikipediaApiRateLimitExceeded() throws IOException {
	    String rateLimitJson = "{ \"error\": { \"code\": \"ratelimit\", \"info\": \"You have exceeded your rate limit.\" } }";

	    InputStream stream = new ByteArrayInputStream(rateLimitJson.getBytes(StandardCharsets.UTF_8));

	    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
	    HttpEntity httpEntity = mock(HttpEntity.class);

	    when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
	    when(httpResponse.getEntity()).thenReturn(httpEntity);
	    when(httpEntity.getContent()).thenReturn(stream);

	    JsonNode response = apiService.search("Java");

	    assertNotNull(response.get("error"));
	    assertEquals("ratelimit", response.get("error").get("code").asText());
	    assertEquals("You have exceeded your rate limit.", response.get("error").get("info").asText());
	}
	//Eighteenth Test: testing if API can handle unauthorized access
	@Test
	public void testWikipediaApiUnauthorizedAccess() throws IOException {
	    String unauthorizedJson = "{ \"error\": { \"code\": \"unauthorized\", \"info\": \"You are not authorized to access this resource.\" } }";

	    InputStream stream = new ByteArrayInputStream(unauthorizedJson.getBytes(StandardCharsets.UTF_8));

	    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
	    HttpEntity httpEntity = mock(HttpEntity.class);

	    when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
	    when(httpResponse.getEntity()).thenReturn(httpEntity);
	    when(httpEntity.getContent()).thenReturn(stream);

	    JsonNode response = apiService.search("Java");

	    assertNotNull(response.get("error"));
	    assertEquals("unauthorized", response.get("error").get("code").asText());
	    assertEquals("You are not authorized to access this resource.", response.get("error").get("info").asText());
	}
	//Nineteenth Test: testing if API can handle an internal server error
	@Test
	public void testWikipediaApiInternalServerError() throws IOException {
	    String internalServerErrorJson = "{ \"error\": { \"code\": \"internal_server_error\", \"info\": \"An internal server error occurred.\" } }";

	    InputStream stream = new ByteArrayInputStream(internalServerErrorJson.getBytes(StandardCharsets.UTF_8));

	    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
	    HttpEntity httpEntity = mock(HttpEntity.class);

	    when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
	    when(httpResponse.getEntity()).thenReturn(httpEntity);
	    when(httpEntity.getContent()).thenReturn(stream);

	    JsonNode response = apiService.search("Java");

	    assertNotNull(response.get("error"));
	    assertEquals("internal_server_error", response.get("error").get("code").asText());
	    assertEquals("An internal server error occurred.", response.get("error").get("info").asText());
	}
	//Twentieth Test: testing if API can handle an empty JSON response
	@Test
	public void testWikipediaApiEmptyJsonResponse() throws IOException {
	    String emptyJson = "{}";

	    InputStream stream = new ByteArrayInputStream(emptyJson.getBytes(StandardCharsets.UTF_8));

	    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
	    HttpEntity httpEntity = mock(HttpEntity.class);

	    when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
	    when(httpResponse.getEntity()).thenReturn(httpEntity);
	    when(httpEntity.getContent()).thenReturn(stream);

	    JsonNode response = apiService.search("Java");

	    assertTrue(response.isEmpty());
	}
}

