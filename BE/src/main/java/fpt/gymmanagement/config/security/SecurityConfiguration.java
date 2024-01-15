package fpt.gymmanagement.config.security;

import fpt.gymmanagement.config.security.filter.JwtAuthenticationFilter;
import fpt.gymmanagement.config.security.oauth2.CustomOAuth2DetailService;
import fpt.gymmanagement.config.security.oauth2.handler.CustomOAuth2FailureHandler;
import fpt.gymmanagement.config.security.oauth2.handler.CustomOAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String MANAGER = "MANAGER";
    private static final String STAFF = "STAFF";
    private static final String MENTOR = "MENTOR";

    private final JwtAuthenticationFilter filter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    @Autowired
    private CustomOAuth2DetailService customOAuth2DetailService;
    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;
    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable().cors(Customizer.withDefaults());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/manage/**").hasAnyAuthority(ADMIN, MANAGER, STAFF)
                .antMatchers("/manage/orders/assign").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers("/roles").hasAnyAuthority(ADMIN, MANAGER, STAFF, MENTOR)
                //Cấu hình api unlock feedback
                .antMatchers(HttpMethod.PUT, "/feedbacks/action").hasAnyAuthority(ADMIN, MANAGER)
                //Cấu hình api branch
                .antMatchers(HttpMethod.GET, "/branches/**").permitAll()
                .antMatchers("/branches/**").hasAnyAuthority(ADMIN)
                //Cấu hình api course
                .antMatchers(HttpMethod.GET, "/courses/**").permitAll()
                .antMatchers("/courses/**").hasAnyAuthority(ADMIN, MANAGER)
                //Cấu hình api course-type
                .antMatchers(HttpMethod.GET, "/course-types/**").permitAll()
                .antMatchers("/course-types/**").hasAnyAuthority(ADMIN)
                //Cấu hình device
                .antMatchers("/devices/**").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers(HttpMethod.GET, "/device-types/**").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers("/device-types/**").hasAnyAuthority(ADMIN)
                .antMatchers("/device-update-histories/**").hasAnyAuthority(ADMIN, MANAGER)
                //Cấu hình PT Course
                .antMatchers(HttpMethod.GET, "/pt-courses/course").permitAll()
                .antMatchers(HttpMethod.GET, "/pt-courses/mentor").permitAll()
                .antMatchers(HttpMethod.GET, "/pt-courses/info").permitAll()
                //Cấu hình PT Course - cấu hình lấy thông tin gym-member, và khóa học đang hướng dẫn cho mentor
                .antMatchers("/pt-courses/gym-members").hasAnyAuthority(ADMIN, MANAGER, MENTOR)
                .antMatchers("/pt-courses/my-courses").hasAnyAuthority(MENTOR)
                .antMatchers("/pt-courses/**").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers("/orders/create-for-user").hasAnyAuthority(ADMIN, MANAGER, STAFF)
                //Cấu hình blog
                .antMatchers(HttpMethod.GET, "/blogs/**").permitAll()
                .antMatchers("/blogs/**").hasAnyAuthority(ADMIN, MANAGER, MENTOR)
                .antMatchers(HttpMethod.GET, "/blog-types/**").permitAll()
                .antMatchers("/blog-types/**").hasAnyAuthority(ADMIN)
                //Cấu hình feedback
                .antMatchers(HttpMethod.GET, "/feedbacks/**").permitAll()
                .antMatchers("/users/get-otp", "/users/validate-otp").hasAnyAuthority(ADMIN, USER, MANAGER, STAFF, MENTOR)
                .antMatchers("/orders/transaction").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/notification/**").hasAnyAuthority(ADMIN, MANAGER, STAFF, USER, MENTOR)
                .antMatchers("/guest-consultant/**").permitAll()
                .antMatchers("/gym-information/**").hasAnyAuthority(ADMIN, MANAGER, STAFF, MENTOR)
                .antMatchers("/gym-user-information/**").hasAnyAuthority(USER)
                .antMatchers("/pt-courses-user/**").permitAll()
                .anyRequest().authenticated()
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .and().httpBasic()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2DetailService)
                .and()
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler);
        return http.build();
    }
}
