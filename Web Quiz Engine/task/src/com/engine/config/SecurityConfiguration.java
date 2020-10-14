package com.engine.config;
import com.engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
            response.setHeader("WWW-Authenticate", "FormBased");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        };
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/quizzes/**").hasAuthority("USER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin().loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password")
                .permitAll()
                .successForwardUrl("/postLogin").and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint).and().headers().frameOptions().disable();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
