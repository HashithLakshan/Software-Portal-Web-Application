package Software.Portal.web.configuretions;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class swagger {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Software Portal Web API")
                        .version("1.0")
                        .description("This is a sample Spring Boot application with Swagger."));
    }
}
