package webserver.pathresolving;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueryFinder {

    public QueryFinder() {}

    public Map<String, String> getQueries(byte[] byteUrl) {
        String url = new String(byteUrl, StandardCharsets.UTF_8);
        if (!url.contains("?")) {
            return null;
        }
        Map<String, String> queries = new HashMap<>();
        String strippedQueries = url.split("\\?")[1];

        if (!strippedQueries.contains("&")) {
            String[] finalQuery = strippedQueries.split("=");
            queries.put(finalQuery[0], finalQuery[1]);
            return queries;
        }

        Arrays.stream(strippedQueries.split("&")).forEach(q -> {
            String[] currentQuery = q.split("=");
            System.out.println("Found query: " + currentQuery[0] + " : " + currentQuery[1]);
            queries.put(currentQuery[0], currentQuery[1]);
        });

        return queries;
    }


}
