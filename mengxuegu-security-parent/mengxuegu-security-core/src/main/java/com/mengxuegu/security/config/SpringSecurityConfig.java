package com.mengxuegu.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * alt+/ 导包
 * ctrl+o 覆盖
 */
@Configuration
@EnableWebSecurity//开启Security过滤器链
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    //设置默认的加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        //明文+随机盐值加密存储
        return new BCryptPasswordEncoder();
    }
    /**
     * 认证管理器
     * 1. 认证信息（用户名、密码）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //存储的密码必须是加密的，不然会报错"There is no PasswordEncoder mapped for the id "null""
        String password  = passwordEncoder().encode("12345678");
        logger.info("加密之后存储的密码" + password);
        auth.inMemoryAuthentication().withUser("mengxuegu")
                .password(password).authorities("ADMIN");
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//    }

    /**
     * 资源权限控制
     * 1. 被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic()//采用httpBasic认证方式
        http.formLogin()//采用表单登陆方式
                .loginPage("/login/page")
                .loginProcessingUrl("/login/form")//登陆表单提交处理url，默认是/login
                .usernameParameter("name")//默认是username
                .passwordParameter("pwd")//默认是password
                .and()
                .authorizeRequests()//认证请求
                .antMatchers("/login/page").permitAll()//放行请求
                .anyRequest().authenticated()//所有访问该应用的http请求都要通过身份认证才能实现访问
        ;
    }

    /**
     * 一般针对静态资源放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/dist/**","/modules/**","/plugins/**");
    }
}
