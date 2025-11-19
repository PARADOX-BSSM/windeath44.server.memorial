package windeath44.server.memorial;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class Application {
  static {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
