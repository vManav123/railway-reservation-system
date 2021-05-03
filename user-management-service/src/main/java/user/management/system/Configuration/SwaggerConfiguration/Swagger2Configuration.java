package user.management.system.Configuration.SwaggerConfiguration;

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
                .apis(RequestHandlerSelectors.basePackage("user.management.system"))
                .paths(regex("/user.*"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo("User Management System"
                ,"user Management Service are here "
                ,"1.1.0"
                ,"Free To Use"
                ,new springfox.documentation.service.Contact("Railway Developer","http://user-management-system.com","railway.developer@gmail.com")
                ,"API Lisence"
                ,"http://user-management-system.com"
                , Collections.emptyList());
    }

}