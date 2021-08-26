package com.telimay.spring.boot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	// Como en la clase SpringSecurityConfig se creo un Bean de BCryptPasswordEncoder,
	// entonces aca se puede reutilizar inyectando ese bean con autowired.
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// Por defecto AuthenticationManager no es un componente de spring
	// es decir, un bean por lo que no podríamos inyectarlo aca. Buscamos la forma de registrarlo
	// como componente en Spring. Para ello en la clase SpringSecurityConfig se sobrescribe el
	// metodo authenticationManager() y se agrega la decoracion @Bean para registrarlo como
	// componente Spring de nuestra aplicacion y poder reutilizarlo en varios lados del proyecto.
	
	// En caso que tengamos mas instancias de AutheticationManager en el proyecto utilizamos
	// la decoracion @Qualifier para inficar que queremos usar el Bean registrado en la clase
	// SpringSecurityConfig
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	// Metodos de configuration que se obtiene al sobreescribir los metodos de la superclase
	// AuthorizationServerConfigurerAdapter
	
	
	// Sec onfiguran los permisos a nuestros endpoints
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()"); // Para iniciar sesion todos pueden ingresar
	
	}

	// Clientes son los frontEnd que utilizaran el pairest
	// Lo que se configura aca es el permiso a los endpoints
	// Tenemos dos endpoint : uno para autenticar
	// y otro para 
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		clients.inMemory().withClient("angularapp")
		.secret(bCryptPasswordEncoder.encode("12345"))
		.scopes("read","write")              // El cliente puede escribir y leer ( la app que se conecta)
		.authorizedGrantTypes("password","refresh_token") // Requiere un username y contraseña
		.accessTokenValiditySeconds(3600)  			  // refresh_token nos permite tener un token renovado para acceder antes de que caduque
		.refreshTokenValiditySeconds(3600);
		
	}

	// Configuramos el EndPoints para el proceso de autentificacion y validacion de tokens
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain tokenEnhancerChain  = new TokenEnhancerChain();
		
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokeConverter() ));
		
		// Registramos el AutheticationManager
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		.accessTokenConverter(accessTokeConverter())
		.tokenEnhancer(tokenEnhancerChain);
		
		
	}

	@Bean
	public JwtTokenStore tokenStore() {
		
		return new JwtTokenStore(accessTokeConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokeConverter() {
		
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		
		//jwtAccessTokenConverter.setSigningKey("clave.para3jwt!token");
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVATE);
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLIC);
		
		return jwtAccessTokenConverter;
	}
	
	
	
	

}
