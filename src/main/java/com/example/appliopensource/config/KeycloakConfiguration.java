package com.example.appliopensource.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class KeycloakConfiguration extends KeycloakWebSecurityConfigurerAdapter {
    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Required to handle spring boot configurations
     *
     * @return
     */
    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Autowired
    public void configureProvider(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // Configurations personnalisées
        http // disable csrf because of API mode
                .csrf().disable().sessionManagement().and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint()).and()
                // access h2-console with keycloak
                .headers().frameOptions().disable().and()

                // delegate logout endpoint to spring security
                .logout().addLogoutHandler(keycloakLogoutHandler()).logoutUrl("/logout").logoutSuccessUrl("/").and()
                // espace public obligatoire pour consulter swagger, fonctionne si swagger
                // deployer a la racine
                .authorizeRequests().antMatchers("/", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                .permitAll().antMatchers("/**").authenticated();
    }

}
