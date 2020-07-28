package weather.controllers;

import controllers.Controller;
import utils.JsonUtils;
import weather.models.WeatherPeriod;
import weather.requests.WeatherJsonParser;

public class WeatherController implements Controller {

    private WeatherPeriod period;
    private final WeatherJsonParser parser;
    private final static String WEATHER_ENDPOINT_PREFIX = "/weather/";
    private final static String[] WEATHER_ALLOWED_ENDPOINTS = {
            "today", "nextweek", "avgtemp", "avgdownfall", "/"
    };

    public WeatherController(JsonUtils utils) {
        this.parser = new WeatherJsonParser(utils);
        this.period = parser.nextWeek();
    }

    public String getTodaysWeather() {
        return this.period.getWeathers()[0].toJson();
    }

    public String getNextWeeksWeather() {
        return this.period.toJson();
    }

    public double getAverageTemperature() {
        return this.period.getAverageTemperature();
    }

    public double getAverageDownfall() {
        return this.period.getAverageDownfall();
    }

    public void setCoords(double latitude, double longitude) {
        this.period = this.parser.nextWeek(latitude, longitude);
    }

    @Override
    public String standardMessage() {
        return "{ \"error\": \"You are looking for weather, but your request is malformed.\" }";
    }

    @Override
    public byte[] checkEndpoint(String endPoint) {
        switch (endPoint) {
            case ("today"): {
                return this.getTodaysWeather().getBytes();
            }
            case ("nextweek"): {
                return this.getNextWeeksWeather().getBytes();
            }
            case("avgtemp"): {
                String jsonString = "{ \"averageTemperature\": " + this.getAverageTemperature() + " }";
                return jsonString.getBytes();
            }
            case ("avgdownfall"): {
                String jsonString = "{ \"averageDownfall\": " + this.getAverageDownfall() + " }";
                return jsonString.getBytes();
            } default: {
                return this.standardMessage().getBytes();
            }
        }
    }

    @Override
    public String[] getAllowedCheckpoints() {
        return WEATHER_ALLOWED_ENDPOINTS;
    }

    @Override
    public byte[] getEndpointPrefix() {
        return WEATHER_ENDPOINT_PREFIX.getBytes();
    }
}
