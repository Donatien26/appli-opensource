package com.example.appliopensource.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfiguration {

    public String issuerAuthorizationURL = "https://auth.insee.test/auth/realms/agents-insee-interne/protocol/openid-connect/auth";

    public String issuerRefreshURL = "https://auth.insee.test/auth/realms/agents-insee-interne/protocol/openid-connect/token";

    public String issuerTokenURL = "https://auth.insee.test/auth/realms/agents-insee-interne/protocol/openid-connect/token";

    public String issuerDescription = "Le super système d'authentification de l'insee";

    public String contactName = "Donatien ENEMAN";

    public String contactEmail = "monEmailPasDuToutPrivé@example.com";

    public String title;

    @Autowired
    BuildProperties buildProperties;

    public final String OAUTHSCHEME = "keycloak";

    @Bean
    public OpenAPI customOpenAPIBasicAndOIDC() {
        final OpenAPI openapi = createOpenAPI();
        openapi.components(new Components().addSecuritySchemes(OAUTHSCHEME,
                new SecurityScheme().type(SecurityScheme.Type.OAUTH2).in(SecurityScheme.In.HEADER)
                        .description(issuerDescription)
                        .flows(new OAuthFlows()
                                .authorizationCode(new OAuthFlow().authorizationUrl(issuerAuthorizationURL)
                                        .tokenUrl(issuerTokenURL).refreshUrl(issuerRefreshURL)))));
        return openapi;
    }

    private OpenAPI createOpenAPI() {
        Contact contact = new Contact().url("https://gitlab.com/Donatien26/appli-opensource");
        if (contactEmail != null) {
            contact = contact.email(contactEmail).name(contactEmail);
        }
        final OpenAPI openapi = new OpenAPI().info(new Info().title("Swagger application (bientot) opensource")
                .description("Cette application ne fait rien du tout").version(buildProperties.getVersion())
                .contact(contact));

        return openapi;
    }

    @Bean
    public OperationCustomizer addAuth() {
        return (operation, handlerMethod) -> {
            return operation.addSecurityItem(new SecurityRequirement().addList(OAUTHSCHEME));
        };
    }
}
