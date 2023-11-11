package artem;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YouTubeIntegration {

    // Вставьте свой ключ API сюда
    private static final String API_KEY = "AIzaSyDfu7E9dvs71M1JAV8MOOFZg6tUuJqpKhk";

    public static String searchVideo(String query) {
        try {
            YouTube youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("YourAppName")
                    .build();

            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey(API_KEY);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(1L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null && !searchResultList.isEmpty()) {
                return "https://www.youtube.com/watch?v=" + searchResultList.get(0).getId().getVideoId();
            } else {
                return "No results found.";
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return "An error occurred during the search.";
        }
    }
}
