package webserver;


import controllers.Controller;
import controllers.StandardController;
import org.rapidoid.buffer.Buf;
import org.rapidoid.http.AbstractHttpServer;
import org.rapidoid.http.HttpStatus;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.net.impl.RapidoidHelper;
import utils.JsonUtils;
import weather.controllers.WeatherController;
import webserver.pathresolving.QueryFinder;
import webserver.pathresolving.UrlPathHandler;

import java.util.Map;


public class RapidoWebserver extends AbstractHttpServer {

    private static final int PORT = 8080;
    private static final JsonUtils utils = new JsonUtils();
    private final UrlPathHandler pathHandler = new UrlPathHandler();
    private final QueryFinder queryFinder = new QueryFinder();
    private final WeatherController weatherController = new WeatherController(utils);
    private final Controller standardController = new StandardController();

    protected HttpStatus handle(Channel ctx, Buf buf, RapidoidHelper data) {
        if (data.isGet.value) {
            Controller controller = this.pathHandler.resolveController(data.path.bytes(buf), this);
            Map<String, String> queries = this.queryFinder.getQueries(data.path.bytes(buf));
            String endPoint = this.pathHandler.getEndpoint(data.path.bytes(buf));

            if (queries != null) {
                System.out.println("Looking for query...");
                System.out.println("found " + queries.size() + " queries.");

                if (endPoint == "weather" && queries.containsKey("lat") && queries.containsKey("long")) {
                    System.out.println("found query. Setting coords...");
                    weatherController.setCoords(
                            Double.parseDouble(queries.get("lat")), Double.parseDouble(queries.get("long"))
                    );
                }
            }

            return json(ctx, data.isKeepAlive.value, controller.checkEndpoint(endPoint));
        }

        return HttpStatus.NOT_FOUND;
    }

    public static void main(String[] args) {
        new RapidoWebserver().listen(PORT);
    }

    public Controller getWeatherController() {
        return this.weatherController;
    }

    public Controller getStandardController() {
        return this.standardController;
    }
}
