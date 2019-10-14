package com.ss.employees.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@EnableResourceServer
@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception{
    http
        .requestMatchers()
        .antMatchers("/api/**")
      .and()
        .authorizeRequests()
          //.antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read') and hasRole('ROLE_CLIENT')")
          .anyRequest().authenticated()
      .and()
        .exceptionHandling()
          .accessDeniedHandler(new OAuth2AccessDeniedHandler());
  }

  @Bean
  @Autowired
  public RemoteTokenServices getRemoteTokenService(@Value("${oauth2.url.check-token:http://localhost:8888/oauth/check_token}") String url) {
    RemoteTokenServices rts = new RemoteTokenServices();
    rts.setCheckTokenEndpointUrl(url);
    rts.setClientId("employees");
    rts.setClientSecret("secret");
    return rts;
  }

  @Autowired
  private RemoteTokenServices remoteTokenServices;

  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) {
    resources
      .resourceId("employees")
      .tokenServices(remoteTokenServices);
  }

}
