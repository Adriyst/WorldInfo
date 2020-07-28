package weather.models;

import java.util.Arrays;

public class WeatherPeriod {

    private final Weather[] weathers;
    private final double averageTemperature;
    private final double averageDownfall;
    private final boolean didRain;

    public WeatherPeriod(Weather[] weathers) {
        if (weathers.length == 0) {
            throw new EmptyWeatherArrayException("The list of weathers provided was empty");
        }
        this.weathers = weathers;
        this.averageTemperature = this.calculateAverageTemperature();
        this.averageDownfall = this.calculateAverageDownfall();
        this.didRain = Arrays.stream(weathers).anyMatch(w -> !w.isRain());
    }

    private double calculateAverageTemperature() {
        return Arrays.stream(weathers)
                .mapToDouble(Weather::getTemperature)
                .sum()/this.weathers.length;
    }

    private double calculateAverageDownfall() {
        return Arrays.stream(weathers)
                .mapToDouble(Weather::getMmOfDownfall)
                .sum()/this.weathers.length;
    }

    public Weather[] getWeathers() {
        return weathers;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public boolean isDidRain() {
        return didRain;
    }

    public double getAverageDownfall() {
        return averageDownfall;
    }

    public String toJson() {
        String[] weatherJson = Arrays.stream(this.weathers).map(Weather::toJson).toArray(String[]::new);
        String AllWeathers = String.join(
                ",",
                weatherJson
        );
        return "[" +
                AllWeathers +
                "]";
    }
}
