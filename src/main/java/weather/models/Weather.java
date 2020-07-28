package weather.models;

public class Weather {

    private boolean isRain;
    private double mmOfDownfall;
    private double temperature;

    public Weather(boolean isRain, double mmOfDownfall, double temperature) {
        this.isRain = isRain;
        this.mmOfDownfall = mmOfDownfall;
        this.temperature = temperature;
    }

    public String toJson() {
        return "{" +
                " \"isRain\": " + this.isRain + "," +
                " \"mmOfDownFall\": " + this.mmOfDownfall + "," +
                " \"temperature\": " + this.temperature +
                "}";
    }

    public boolean isRain() {
        return isRain;
    }

    public void setRain(boolean rain) {
        isRain = rain;
    }

    public double getMmOfDownfall() {
        return mmOfDownfall;
    }

    public void setMmOfDownfall(float mmOfDownfall) {
        this.mmOfDownfall = mmOfDownfall;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
