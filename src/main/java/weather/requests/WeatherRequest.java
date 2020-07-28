package weather.requests;

import cleaning.JsonCleaner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.JsonUtils;


import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static utils.SecretConsts.STORED_DATA;


public class WeatherRequest {

    private static final String BASE_REQUEST_URL = "https://api.met.no/weatherapi/locationforecast/2.0/";
    private static final String FILENAME_BASE = STORED_DATA;
    private static final String FILENAME_SUFFIX = "_weather.json";
    private final JsonCleaner cleaner = new WeatherJsonCleaner();
    private final JsonUtils utils;

    public WeatherRequest(JsonUtils utils) { this.utils = utils; }

    protected org.json.JSONObject getJson(double latitude, double longitude) {
        String filename = this.formatFileName(latitude);
        File fileCheck = new File(filename);

        if (fileCheck.exists()) {
            System.out.println("Fetching from saved file...");
            this.cleaner.cleanOld(filename);
            return this.utils.readFromJson(filename);
        }

        System.out.println("Starting to fetch weather.");
        String forecastUrl = BASE_REQUEST_URL + "compact?lat=" + latitude + "&lon=" + longitude;
        try {
            URL url = new URL(forecastUrl);
            System.out.println("URL OK.");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            System.out.println("CONNECTION OK.");
            con.setRequestMethod("GET");

            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "WorldInfo/0.1");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            System.out.println("Response code: " + status);

            if (status != 200) {
                return null;
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            con.getInputStream()
                    )
            );
            StringBuilder content = new StringBuilder();
            in.lines().forEach(content::append);
            in.close();
            con.disconnect();

            String result = content.toString();
            if (!this.utils.writeToJson(result, filename)) {
                System.out.println("Something went wrong while trying to dump JSON to file.");
            }
            if (!this.cleaner.cleanOld(filename)) {
                System.out.println("WARNING: cleaner could not clean some weather JSON files.");
            };
            JSONParser parser = new JSONParser();
            return new org.json.JSONObject((JSONObject) parser.parse(result));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatFileName(double latitude) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy-hh");
        Date date = new Date();
        String fileNameAddition = format.format(date);
        return FILENAME_BASE + fileNameAddition + (int) latitude + FILENAME_SUFFIX;
    }

    private static class WeatherJsonCleaner implements JsonCleaner {

        @Override
        public boolean cleanOld(String filename) {
            Date dateToCheck = this.resolveDate(filename);
            File[] files = new File(FILENAME_BASE).listFiles(f -> f.getName() != filename);
            if (files == null || files.length == 0) {
                return false;
            }
            File[] toDelete = Arrays.stream(files).filter(f -> {
                Date comparison = this.resolveDate(f.getAbsolutePath());
                Date weekCheck = new Date(dateToCheck.getTime() - (1000 * 60 * 60 * 24 * 7));
                return comparison.before(weekCheck);
            }).toArray(File[]::new);

            if (toDelete.length == 0) {
                return false;
            }

            boolean anyDeleted = false;
            for (File file : toDelete) {
                anyDeleted = file.delete();
            }
            return anyDeleted;
        }

        private Date resolveDate(String filename) {
            String[] dateCheck = filename.replace(FILENAME_BASE, "").split("-");
            int[] times = new int[3];
            for (int i = 0; i < 3; i++) {
                times[i] = (Integer.parseInt(dateCheck[i]));
            }
            return new Calendar.Builder().setDate(times[2], times[1], times[0]).build().getTime();
        }
    }

}
