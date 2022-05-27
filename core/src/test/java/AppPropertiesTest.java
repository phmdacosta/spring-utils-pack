import com.pedrocosta.utils.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AppPropertiesTest {

    private final String PROP_NAME = "test.property";

    @Test
    public void testGetProperty() {
        String val = AppProperties.get(PROP_NAME);
        assert "Property Test.".equals(val);
    }

    @Test
    public void testGetProperty_propertiesFileNotExist() throws IOException {
        AppProperties appProperties = AppProperties.instance("abc.properties");
        assert appProperties != null;

        String val = appProperties.getProperty(PROP_NAME);
        assert val == null;
    }
}
