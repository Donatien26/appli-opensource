package com.example.appliopensource.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@ConfigurationProperties("com.example.myapp.security")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /** Enable basic authentication */
    private boolean basicAuthenticationEnabled = false;

    /**
     * Enable bearer authentication
     *
     * <p>
     * A Spring Security oAuth configuration is mandatory if enabled
     *
     * <p>
     * For instance you should add the
     * spring.security.oauth2.resourceserver.jwt.jwk-set-uri property by the public
     * key location of your oAuth server
     */
    private boolean bearerAuthenticationEnabled = false;

    private boolean basicFakeAuthenticationEnabled = false;

    /** Enable basic authentication over ldap connection */
    private boolean ldapAccountManagementEnabled = false;

    /** Ldap url where are stored accounts for managment */
    private String ldapAccountManagmentUrl;
    /** Base DN where are stored ldap accounts for managment */
    private String ldapAccountManagmentUserBase;
    /** Group DN where are stored permissions for ldap accounts for managment */
    private String ldapAccountManagmentGroupeBase;
    /** Search in subtree * */
    private boolean ldapAccountManagmentGroupSubtree;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // api, so csrf is disabled
        http.csrf().disable();

        // allow basic authentication
        if (basicAuthenticationEnabled) {
            http.httpBasic();
        }
        // allow jwt bearer authentication
        if (bearerAuthenticationEnabled) {
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> {
                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter());
            }));
        }

        http.authorizeRequests(authz -> authz.antMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers("/**").authenticated().anyRequest().denyAll());
    }

    // Customization to get Keycloak Role and get preffered_username as principal

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
        return jwtAuthenticationConverter;
    }

    Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new Converter<Jwt, Collection<GrantedAuthority>>() {
            @Override
            @SuppressWarnings({ "unchecked", "serial" })
            public Collection<GrantedAuthority> convert(Jwt source) {
                return ((Map<String, List<String>>) source.getClaim("realm_access")).get("roles").stream()
                        .map(s -> new GrantedAuthority() {
                            @Override
                            public String getAuthority() {
                                return "ROLE_" + s;
                            }

                            @Override
                            public String toString() {
                                return getAuthority();
                            }
                        }).collect(Collectors.toList());
            }
        };
    }

    // User de test en basic pour voir un accès concurrent basic/bearer

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (ldapAccountManagementEnabled) {
            if (basicFakeAuthenticationEnabled) {
                auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("admin");
            } else {
                auth.ldapAuthentication().userSearchBase(ldapAccountManagmentUserBase).userSearchFilter("(uid={0})")
                        .groupSearchBase(ldapAccountManagmentGroupeBase)
                        .groupSearchSubtree(ldapAccountManagmentGroupSubtree).contextSource()
                        .url(ldapAccountManagmentUrl);
            }
        }

    }

    public boolean isBasicAuthenticationEnabled() {
        return basicAuthenticationEnabled;
    }

    public void setBasicAuthenticationEnabled(boolean basicAuthenticationEnabled) {
        this.basicAuthenticationEnabled = basicAuthenticationEnabled;
    }

    public boolean isBearerAuthenticationEnabled() {
        return bearerAuthenticationEnabled;
    }

    public void setBearerAuthenticationEnabled(boolean bearerAuthenticationEnabled) {
        this.bearerAuthenticationEnabled = bearerAuthenticationEnabled;
    }

    public boolean isLdapAccountManagmentEnabled() {
        return ldapAccountManagementEnabled;
    }

    public void setLdapAccountManagementEnabled(boolean ldapAccountManagementEnabled) {
        this.ldapAccountManagementEnabled = ldapAccountManagementEnabled;
    }

    public String getLdapAccountManagmentUrl() {
        return ldapAccountManagmentUrl;
    }

    public void setLdapAccountManagmentUrl(String ldapAccountManagmentUrl) {
        this.ldapAccountManagmentUrl = ldapAccountManagmentUrl;
    }

    public String getLdapAccountManagmentUserBase() {
        return ldapAccountManagmentUserBase;
    }

    public void setLdapAccountManagmentUserBase(String ldapAccountManagmentUserBase) {
        this.ldapAccountManagmentUserBase = ldapAccountManagmentUserBase;
    }

    public String getLdapAccountManagmentGroupeBase() {
        return ldapAccountManagmentGroupeBase;
    }

    public void setLdapAccountManagmentGroupeBase(String ldapAccountManagmentGroupeBase) {
        this.ldapAccountManagmentGroupeBase = ldapAccountManagmentGroupeBase;
    }

    public boolean getLdapAccountManagmentGroupSubtree() {
        return ldapAccountManagmentGroupSubtree;
    }

    public void setLdapAccountManagmentGroupSubtree(boolean ldapAccountManagmentGroupSubtree) {
        this.ldapAccountManagmentGroupSubtree = ldapAccountManagmentGroupSubtree;
    }

    public boolean isBasicFakeAuthenticationEnabled() {
        return this.basicFakeAuthenticationEnabled;
    }

    public void setBasicFakeAuthenticationEnabled(boolean basicFakeAuthenticationEnabled) {
        this.basicFakeAuthenticationEnabled = basicFakeAuthenticationEnabled;
    }
}