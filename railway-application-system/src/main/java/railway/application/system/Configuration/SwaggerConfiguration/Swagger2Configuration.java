package railway.application.system.Configuration.SwaggerConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableSwagger2
@Configuration
public class Swagger2Configuration {

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiDetails())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build();
    }

    private ApiInfo apiDetails() {
        return new ApiInfo("Railway Application System"
                ,"This Application is for railway reservation purpose. her you can do   `Ticket Reservation` , `Ticket Cancellation` , `Check Train Details` and many more thing can do..."
                ,"1.2.0"
                ,"Free To Use"
                ,new springfox.documentation.service.Contact("Railway Developer","http://localhost:8085/reservation/welcome","railway.developer@gmail.com")
                ,"API License"
                ,"http://localhost:8085/reservation/welcome"
                , Collections.emptyList());
    }

}