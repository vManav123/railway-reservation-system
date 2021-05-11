package railway.reservation.system.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class Swagger2Configuration {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("railway.reservation.system.Controller"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo("Railway Management System"
                ,"Railway Management Service are here "
                ,"1.1.0"
                ,"Free To Use"
                ,new springfox.documentation.service.Contact("Railway Developer","http://railway-management-system.com","railway.developer@gmail.com")
                ,"API Lisence"
                ,"http://railway-management-system.com"
                , Collections.emptyList());
    }

}