package fpt.gymmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {
    public static final String USER_TAG = "User API";
    public static final String PREPARE_ORDER_TAG = "Prepare Order API";
    public static final String MANAGE_USER_TAG = "Manage User API";
    public static final String AUTHENTICATION_TAG = "Authentication API";
    public static final String ORDER_USER_TAG = "Order(User) API";
    public static final String ORDER_DETAIL_USER_TAG = "Order Detail(User) API";
    public static final String MANAGE_ORDER_TAG = "Manage Order API";
    public static final String COURSE_TAG = "Course API";
    public static final String COURSE_TYPE_TAG = "Course Type API";
    public static final String PT_COURSE_TAG = "PT Course API";
    public static final String USER_COURSE_TAG = "User Course API";
    public static final String FEEDBACK_TAG = "Feedback API";


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer Token", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(defaultAuth()))
                .build();
    }

    private SecurityReference defaultAuth() {
        return new SecurityReference("Bearer Token", new AuthorizationScope[0]);
    }
}
