import org.junit.Assert;
import org.junit.Test;
import utils.JsonUtils;
import weather.models.WeatherPeriod;
import weather.requests.WeatherJsonParser;

import java.util.Arrays;
import java.util.Objects;

public class WeatherTest {

    @Test
    public void testJson() {
        JsonUtils utils = new JsonUtils();
        WeatherJsonParser parser = new WeatherJsonParser(utils);
        WeatherPeriod period = parser.nextWeek();

        Assert.assertFalse(
                Arrays.stream(period.getWeathers()).anyMatch(Objects::isNull)
        );
    }
}
