package webserver.pathresolving;

import controllers.Controller;
import webserver.RapidoWebserver;

import java.nio.charset.StandardCharsets;

public class UrlPathHandler {

    private static final String STANDARD_DOMAIN = "/";

    public UrlPathHandler(){}

    public Controller resolveController(byte[] byteUrl, RapidoWebserver server) {
        String url = new String(byteUrl, StandardCharsets.UTF_8);
        String[] allDomains = url.split("/");
        String domain = allDomains.length > 1 ? allDomains[allDomains.length-2] : STANDARD_DOMAIN;
        return this.getControllerFromDomain(domain, server);
    }

    public String getEndpoint(byte[] byteUrl) {
        String url = new String(byteUrl, StandardCharsets.UTF_8);
        String[] allDomains = url.split("/");
        return allDomains[allDomains.length-1];
    }

    private Controller getControllerFromDomain(String domain, RapidoWebserver server) {
        switch (domain) {
            case "weather": {
                return server.getWeatherController();
            }
            default: {
                return server.getStandardController();
            }
        }
    }

}
