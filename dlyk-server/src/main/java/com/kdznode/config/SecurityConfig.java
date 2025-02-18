package com.kdznode.config;

import com.kdznode.config.filter.JwtFilter;
import com.kdznode.config.handler.MyFailureHandler;
import com.kdznode.config.handler.MyLogoutSuccessHandler;
import com.kdznode.config.handler.MySucessfulHandler;
import com.kdznode.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author kdz
 * @create 2025-01-23-11:54
 */

@Configuration
public class SecurityConfig {

    @Resource
    private MySucessfulHandler mySucessfulHandler;

    @Resource
    private MyFailureHandler myFailureHandler;

    @Resource
    private JwtFilter JwtFilter;

    @Resource
    private MyLogoutSuccessHandler myLogoutHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,CorsConfigurationSource corsConfigurationSource) throws Exception {

        return http
                .formLogin((formLogin)->{
                    formLogin.loginProcessingUrl(Constants.LOGIN_URI)//登录url
                    .usernameParameter("loginAct")//账号
                    .passwordParameter("loginPwd")//密码
                    .successHandler(mySucessfulHandler)//登陆成功处理器
                    .failureHandler(myFailureHandler);//登录失败处理器
                })
                //请求拦截
                .authorizeHttpRequests((auth)->{
                    auth.requestMatchers(Constants.LOGIN_URI).permitAll()
                            .anyRequest().authenticated();//拦截任何请求
                })
                .csrf(csrf->{
                    csrf.disable();//禁用跨站请求伪造
                })
                //支持跨域请求
                .cors((cors)->{
                    cors.configurationSource(corsConfigurationSource);
                })
                .sessionManagement((session)->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                //添加自定义的filter
                .addFilterBefore(JwtFilter, LogoutFilter.class)
                //退出登录处理器
                .logout((logout)->{
                    logout.logoutUrl(Constants.LOGOUT_URI).logoutSuccessHandler(myLogoutHandler);
                })
                .build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * @description:
     * @author: kdz 跨区请求
     * @date: 2025/2/5 20:50
     * @param: []
     * @return: org.springframework.web.cors.CorsConfigurationSource
     **/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");//允许任何域名使用
        configuration.addAllowedMethod("*");//允许任何请求方法
        configuration.addAllowedHeader("*");//允许任何请求头

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
