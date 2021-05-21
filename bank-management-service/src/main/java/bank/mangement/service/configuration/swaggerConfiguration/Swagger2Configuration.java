package bank.mangement.service.configuration.swaggerConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class Swagger2Configuration {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo("Bank Management System"
                , "Bank Management Service are here "
                , "1.0.0"
                , "Free To Use"
                , new springfox.documentation.service.Contact("Railway Developer", "http://bank-management-system.com", "bank.railway.service@gmail.com")
                , "API License"
                , "http://bank-management-system.com"
                , Collections.emptyList());
    }

}