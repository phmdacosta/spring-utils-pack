import com.pedrocosta.utils.AppProperties;
import org.junit.jupiter.api.Test;

public class AppPropertiesTest {

    @Test
    public void testGetProperty() {
        String val = AppProperties.get("test.property");
        assert "Property Test.".equals(val);
    }
}
