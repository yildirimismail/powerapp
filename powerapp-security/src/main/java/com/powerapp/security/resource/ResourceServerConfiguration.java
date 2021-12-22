package com.powerapp.security.resource;

import com.powerapp.security.AccessDeniedHandler;
import com.powerapp.security.AuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.validation.constraints.NotNull;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @NotNull}))
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String[] WHITE_LIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/webjars/**"
    };

    public final TokenStore tokenStore;
	private final AccessDeniedHandler accessDeniedHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors();
        http
                .csrf().disable();
        http
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(WHITE_LIST)
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
				.authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
				.resourceId("USER_ADMIN_RESOURCE")
				.tokenStore(tokenStore);
    }

}