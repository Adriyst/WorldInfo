package webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.Controller;
import controllers.StandardController;
import utils.JsonUtils;
import weather.controllers.WeatherController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Webserver {

    static final int PORT = 8080;
    private static final JsonUtils utils = new JsonUtils();
    private final Controller weatherController = new WeatherController(utils);
    private final Controller standardController = new StandardController();

    public Webserver() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            this.contextCreator(server);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void contextCreator(HttpServer server) {
        // TODO
        // Endpoints are not displaying standard message.
        Controller[] controllers = {this.weatherController, this.standardController};
        Arrays.stream(controllers).forEach(cont -> {
            server.createContext(new String(cont.getEndpointPrefix(), StandardCharsets.UTF_8),
                    httpExchange -> this.generateExchangeFunction(httpExchange, cont.getEndpointPrefix()));
            Arrays.stream(cont.getAllowedCheckpoints())
                    .forEach(endpoint -> {
                        server.createContext(
                                new String(cont.getEndpointPrefix(), StandardCharsets.UTF_8) + endpoint,
                                httpExchange -> this.generateExchangeFunction(
                                        httpExchange, cont.checkEndpoint(endpoint)
                                ));
                    });
        });
    }

    private void generateExchangeFunction(HttpExchange httpExchange, byte[] response) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream out = httpExchange.getResponseBody();
        out.write(response);
        out.close();
    }
}
