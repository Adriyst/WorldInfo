package webserver.pathresolving;

import java.util.Map;

public class QueryResolver {

    private final QueryFinder finder = new QueryFinder();
    private Map<String, String> queries;

    public QueryResolver() {}

    public void fetchQueries(byte[] pathString) {

    }

}
