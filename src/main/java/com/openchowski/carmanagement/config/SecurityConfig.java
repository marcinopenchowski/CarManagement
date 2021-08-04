package com.openchowski.carmanagement.config;

import com.openchowski.carmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource securityDataSource;

    @Autowired
    private UserService userService;

    @Value("${spring.queries.user-query}")
    private String userQuery;

    @Value("${spring.queries.authority-query}")
    private String authorityQuery;

    @Autowired
    public SecurityConfig(DataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(authorityQuery)
                .dataSource(securityDataSource);

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // matchers for all
                .antMatchers("/", "/users/showChangePasswordForm", "/users/changePassword", "/**/list", "/rentals/showReturnForm", "/rentals/showAddForm", "/rentals/remove", "/rentals/save", "/users/search", "/cars/search", "/employees/search").hasAnyRole("USER", "ADMIN", "MANAGER")
                // matchers for manager/admin
                .antMatchers("/cars/showAddForm", "/cars/delete", "/cars/save", "/cars/showUpdateForm", "/employees/delete", "/employees/save", "/employees/showAddForm", "/employees/showUpdateForm", "/rentals/delete", "/rentals/edit", "/rentals/showAddForm", "/rentals/showUpdateForm").hasAnyRole("MANAGER", "ADMIN")
                // matchers for only manager
                .antMatchers("/users/showAddForm", "/users/save", "/users/delete").hasRole("MANAGER")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .formLogin()
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}
