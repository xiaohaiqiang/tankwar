import java.io.IOException;
import java.util.Properties;

public class TestConfig {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String num = properties.getProperty("initTankCount");
        System.out.println(num);
    }
}
