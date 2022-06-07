import com.pedrocosta.utils.AppProperties;
import org.junit.jupiter.api.Test;

public class AppPropertiesTest {

    private final String PROP_NAME = "test.property";

    @Test
    public void testGetProperty() {
        String val = AppProperties.get(PROP_NAME);
        assert "Property Test.".equals(val);
    }

    @Test
    public void testGetProperty_propertiesFileNotExist() {
        AppProperties appProperties = AppProperties.instance("abc.properties");
        String val = appProperties.getProperty(PROP_NAME);
        assert val == null;
    }
}
