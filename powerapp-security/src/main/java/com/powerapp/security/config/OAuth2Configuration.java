package com.powerapp.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @NotNull}))
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

	@Value("${check-user-scopes}")
	private Boolean checkUserScopes;
	private final DataSource dataSource;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;
	private final ClientDetailsService clientDetailsService;
	private final AuthenticationConfiguration authenticationConfiguration;

	private AuthenticationManager authenticationManager;

	@PostConstruct
	public void init() throws Exception {
		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public OAuth2RequestFactory requestFactory() {
		CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
		requestFactory.setCheckUserScopes(true);
		return requestFactory;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new CustomTokenEnhancer();
		converter.setKeyPair(new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "password".toCharArray()).getKeyPair("jwt"));
		return converter;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
	}

	@Bean
	public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
		return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
	}
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtAccessTokenConverter())
				.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
		if (checkUserScopes)
			endpoints.requestFactory(requestFactory());

	}
}