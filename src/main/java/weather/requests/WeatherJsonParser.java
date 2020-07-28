package weather.requests;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.JsonUtils;
import weather.models.Weather;
import weather.models.WeatherPeriod;

import java.util.*;

import static utils.SecretConsts.LATITUDE;
import static utils.SecretConsts.LONGITUDE;

public class WeatherJsonParser {

    private final WeatherRequest request;
    private final DateVerifier verifier = new DateVerifier();


    public WeatherJsonParser(JsonUtils utils) {
        this.request = new WeatherRequest(utils);

    }

    public WeatherPeriod nextWeek() {
        return this.nextWeek(LATITUDE, LONGITUDE);
    }

    // TODO:
    // Allow request with given date
    // OR set time to mid day or something
    public WeatherPeriod nextWeek(double latitude, double longitude) {
        JSONObject json = Objects.requireNonNull(this.request.getJson(latitude, longitude));
        JSONArray timeseries = json.getJSONObject("properties").getJSONArray("timeseries");

        Weather[] weathers = new Weather[7];
        int inputIndex = 0;
        for (int i = 0; i < timeseries.length(); i++) {
            if (Arrays.stream(weathers).noneMatch(Objects::isNull)) {
                break;
            }

            JSONObject current = timeseries.getJSONObject(i);
            String currentTime = current.getString("time");

            if (!this.verifier.verifyEntry(currentTime)) {
                continue;
            }

            JSONObject currentDetails = current.getJSONObject("data")
                    .getJSONObject("instant")
                    .getJSONObject("details");

            double downfall = current.getJSONObject("data")
                    .getJSONObject("next_6_hours")
                    .getJSONObject("details")
                    .getDouble("precipitation_amount");

            Weather weather = new Weather(
                    downfall > 0, downfall, currentDetails.getDouble("air_temperature"));

            weathers[inputIndex++] = weather;
        }
        return new WeatherPeriod(weathers);
    }

    private static class DateVerifier {

        private Date checkTime;
        private final List<Date> usedDates = new LinkedList<>();

        public DateVerifier() {}

        private boolean verifyEntry(String currentTime) {

            String[] splitTime = currentTime.split("T");
            int[] dateArray = Arrays.stream(splitTime[0].split("-"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            Date toCheck = new Calendar.Builder().setDate(dateArray[0], dateArray[1], dateArray[2]).build().getTime();

            if (this.usedDates.isEmpty()) {
                this.checkTime = toCheck;
                this.usedDates.add(toCheck);
                return true;
            } else {
                if (this.usedDates.contains(toCheck)) {
                    return false;
                }
                if (toCheck.after(this.checkTime)) {
                    this.usedDates.add(toCheck);
                    this.checkTime = toCheck;
                    return true;
                }
            }
            return false;
        }
    }
}
