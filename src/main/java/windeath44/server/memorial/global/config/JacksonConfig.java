package windeath44.server.memorial.global.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import windeath44.server.memorial.global.deserializer.XssStringJsonDeserializer;

@Configuration
public class JacksonConfig {

    @Bean
    public Module xssSanitizingModule(XssStringJsonDeserializer xssDeserializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, xssDeserializer);
        return module;
    }
}
