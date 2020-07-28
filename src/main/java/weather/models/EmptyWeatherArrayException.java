package weather.models;

public class EmptyWeatherArrayException extends RuntimeException {

    public EmptyWeatherArrayException(String message) {
        super(message);
    }
}
