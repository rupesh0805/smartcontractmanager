package com.project.scm.config;

import com.project.scm.services.impl.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private SecurityCustomUserDetailService userDetailService;
    private OAuthAuthenicationSuccessHandler authenicationSuccessHandler;

    @Autowired
    public SecurityConfig(SecurityCustomUserDetailService userDetailService,OAuthAuthenicationSuccessHandler authenicationSuccessHandler){
        this.userDetailService = userDetailService;
        this.authenicationSuccessHandler  =authenicationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //configure request path
        httpSecurity.authorizeHttpRequests(authorize-> {
//            authorize.requestMatchers("/home","/register","/login").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //form default login
//        httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.formLogin(formlogin-> {
            formlogin.loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .successForwardUrl("/user/profile")
//                    .failureForwardUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password");

            // formLogin.failureHandler(new AuthenticationFailureHandler() {
            // @Override
            // public void onAuthenticationFailure(HttpServletRequest request,
            // HttpServletResponse response,
            // AuthenticationException exception) throws IOException, ServletException {
            // // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method
            // 'onAuthenticationFailure'");
            // }
            // });

            // formLogin.successHandler(new AuthenticationSuccessHandler() {
            // @Override
            // public void onAuthenticationSuccess(HttpServletRequest request,
            // HttpServletResponse response,
            // Authentication authentication) throws IOException, ServletException {
            // // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method
            // 'onAuthenticationSuccess'");
            // }
            // });
        });

        //logout requires post , if csrf is enabled, Spring enables it by default
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout")
                    .logoutSuccessUrl("/login?logout=true");
        });

        //oauth configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(authenicationSuccessHandler);
        });
        return httpSecurity.build();
    }


    //Configuration of Authentication Provider for Spring Security
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //Userdetail Service obj
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        //Password encoder obj
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("rupesh")
//                .password("rupesh123")
//                .roles("ADMIN","USER")
//                .build();
//
//        UserDetails user2 = User.withUsername("max")
//                .password("max123")
//                .build();
//        return new InMemoryUserDetailsManager();
//    }
}
