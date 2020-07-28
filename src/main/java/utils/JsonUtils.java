package utils;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JsonUtils {

    public JSONObject readFromJson(String filename) {
        try {
            File file = new File(filename);
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            JSONParser parser = new JSONParser();
            return new JSONObject((org.json.simple.JSONObject) parser.parse(reader));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean writeToJson(String data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(data);
            writer.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
