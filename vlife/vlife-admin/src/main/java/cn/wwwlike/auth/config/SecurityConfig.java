/*
 *  vlife http://github.com/wwwlike/vlife
 *
 *  Copyright (C)  2018-2022 vlife
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package cn.wwwlike.auth.config;

import cn.wwwlike.auth.service.SysUserService;
import cn.wwwlike.auth.service.SysGroupService;
import cn.wwwlike.sys.service.SysResourcesService;
import cn.wwwlike.web.security.core.*;
import cn.wwwlike.web.security.filter.MyUsernamePasswordAuthenticationFilter;
import cn.wwwlike.web.security.filter.VerifyTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.Map;
@Configuration
@ConditionalOnExpression("${security.basic.enable:true}")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysGroupService groupService;
    @Autowired
    SysResourcesService resourcesService;
    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("MD5");
    }


    @Autowired()
    EDefaultWebSecurityExpressionHandler ewebSecurityExpressionHandler;

    @Bean
    EDefaultWebSecurityExpressionHandler ewebSecurityExpressionHandler() {
        EDefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new EDefaultWebSecurityExpressionHandler();
        return webSecurityExpressionHandler;
    }

    //配置权限校验
    @Bean
    PermissionEvaluator permissionEvaluator() {
        EPermissionEvaluator permissionEvaluator = new EPermissionEvaluator();
        return permissionEvaluator;
    }

    @Bean
    EAuthenticationFailureHandler eauthenticationFailureHandler() {
        return new EAuthenticationFailureHandler();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    // 该方法是登录的时候会进入
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.inMemoryAuthentication();
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());//设置密码处理方式//设置用户校验方式
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new VerifyTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new MyUsernamePasswordAuthenticationFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        //表达式拦截器 注册表
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = httpSecurity
                .authorizeRequests();
        authorizeRequests.expressionHandler(ewebSecurityExpressionHandler());//验证人员是否有权限的过滤
        // 403  未登录请求资源  EAccessDeniedHandler 没有权限的处理
        httpSecurity.exceptionHandling().authenticationEntryPoint(new Jwt403AuthenticationEntryPoint())
                .accessDeniedHandler(new EAccessDeniedHandler());


        //接口资源同步，做更新，不做初始化
        if(resourcesService.findAll().size()!=0){
            resourcesService.sync();
        }

        Map<String, String> map = groupService.resourceGroupMap();
        for (String url : map.keySet()) {
            // 资源路径与角色组绑定，以此资源为父资源角色所在的角色组
            authorizeRequests.antMatchers("/**/" + url)
                    .access("hasPermission('','" + map.get(url) + "')");
        }
        authorizeRequests.antMatchers( "/dist/**","/open/api/getToken","/tsCode/code/*",
                   "/sysUser/sendEmail","/git/*","/git/token/*", "/ts/test/file", "/ts/upload", "/sysFile/upload", "/sysFile/image/*", "/sysFile/uploadImg", "/ts/download", "/static/index.html").permitAll().anyRequest().authenticated()
                .and().formLogin()
                .failureHandler(eauthenticationFailureHandler())
                .and()
                .logout()
                .permitAll();
    }

    /**
     * 得到当前用户
     *
     * @return
     */
    public static SecurityUser getCurrUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        } else {
            try {
                return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        //{F4T9t2BE3HCvD9khLCxL/nyib/AdM1WqR/tMx5eJJ2k=}f0afa783ba7607063606fdb43c2e55fb
        System.out.println(new MessageDigestPasswordEncoder("MD5").encode("123456"));
    }
}
