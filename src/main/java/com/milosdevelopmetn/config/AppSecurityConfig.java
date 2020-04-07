package com.milosdevelopmetn.config;

import com.milosdevelopmetn.FlashMessage;
import com.milosdevelopmetn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private UserService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**", "/style/**",  "/js/app.js",  "/vendor/**", "/images/**");
	}

	@Configuration
	@Order(1)
	public static class AppConfigurationAdapter3 extends WebSecurityConfigurerAdapter{
		public AppConfigurationAdapter3(){
			super();
		}
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.antMatcher("/manager/**")
					.authorizeRequests()
					.antMatchers("/manager").permitAll()
					.antMatchers("/manager/**").access("hasRole('MANAGER')")
					.and()
					.formLogin()
					.loginPage("/manager")
					.permitAll()
					.successHandler(loginSuccessHandler())
					.failureHandler(loginFailureHandler())
					.and()
					.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/manager")
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.and()
					.csrf().disable();
		}

		private AuthenticationSuccessHandler loginSuccessHandler(){
			return ((request, response, authentication) -> response.sendRedirect("manager/index"));
		}

		public AuthenticationFailureHandler loginFailureHandler() {
			return (request, response, exception) -> {
				request.getSession().setAttribute("flash1", new FlashMessage("Netacno manager ime ili sifra. Pokusajte ponovo.", FlashMessage.Status.FAILURE));
				response.sendRedirect("/manager");
			};
		}

	}

	@Configuration
	@Order(2)
	public static class AppConfigurationAdapter extends WebSecurityConfigurerAdapter{
		public AppConfigurationAdapter(){
			super();
		}
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.antMatcher("/admin/**")
					.authorizeRequests()
					.antMatchers("/admin").permitAll()
					.antMatchers("/admin/**").access("hasRole('ADMIN')")
					.and()
					.formLogin()
					.loginPage("/admin")
					.permitAll()
					.successHandler(loginSuccessHandler())
					.failureHandler(loginFailureHandler())
					.and()
					.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/admin")
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.and()
					.csrf().disable();
		}

		private AuthenticationSuccessHandler loginSuccessHandler(){
			return ((request, response, authentication) -> response.sendRedirect("admin/hotels"));
		}

		public AuthenticationFailureHandler loginFailureHandler() {
			return (request, response, exception) -> {
				request.getSession().setAttribute("flash2", new FlashMessage("Netacno admin ime ili sifra. Pokusajte ponovo.", FlashMessage.Status.FAILURE));
				response.sendRedirect("/admin");
			};
		}

	}

	@Configuration
	@Order(3)
	public static class AppConfigurationAdapter2 extends WebSecurityConfigurerAdapter{
		public AppConfigurationAdapter2(){
			super();
		}
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http

					.authorizeRequests()
					.antMatchers( "/login", "/register").permitAll()
					.antMatchers("/", "/home").permitAll()
					.antMatchers("/account/**").access("hasRole('USER')")
					.and()
					.formLogin()
					.loginPage("/login")
					.permitAll()
					.successHandler(loginSuccessHandler())
					.failureHandler(loginFailureHandler())
					.and()
					.logout().invalidateHttpSession(true)
					.clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/home").permitAll()
					.and()
					.csrf().disable();

		}

		private AuthenticationSuccessHandler loginSuccessHandler(){
			return (request, response, authentication) -> response.sendRedirect("/home");
		}

		public AuthenticationFailureHandler loginFailureHandler() {
			return (request, response, exception) -> {
				request.getSession().setAttribute("flash1", new FlashMessage("Netacno korisnicko ime ili sifra. Pokusajte ponovo.", FlashMessage.Status.FAILURE));
				response.sendRedirect("/login");
			};
		}


	}

	@Bean
	public EvaluationContextExtension securityExtension(){
		return new EvaluationContextExtension() {
			@Override
			public String getExtensionId() {
				return "security";
			}

			@Override
			public Object getRootObject() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				return new SecurityExpressionRoot(authentication) {};
			}
		};
	}

}
