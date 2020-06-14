package com.example.demo.config;

import labs.psychogen.row.filter.RowFilterChain;
import labs.psychogen.row.filter.RowInvokerFiler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebMvc
public class Config {

    //temp
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setDaemon(true);

        return scheduler;
    }


    @Configuration
    public static class RowSecurityConfiguration {
        private final SecurityBasedRowFilter securityBasedRowFilter;
        private final RowFilterChain rowFilterChain;

        public RowSecurityConfiguration(SecurityBasedRowFilter securityBasedRowFilter, RowFilterChain rowFilterChain) {
            this.securityBasedRowFilter = securityBasedRowFilter;
            this.rowFilterChain = rowFilterChain;
        }

        @PostConstruct
        public void registerFiler(){
            System.out.println("Registering filter");
            rowFilterChain.addFilterBefore(securityBasedRowFilter, RowInvokerFiler.class);
        }
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration
    @EnableAspectJAutoProxy
    @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 5 * 24 * 60 * 60, redisFlushMode = RedisFlushMode.ON_SAVE)
    static class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .httpBasic().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .requestCache()
                .requestCache(new NullRequestCache())
                .and()
                .cors().disable()
                .csrf().disable()
                .rememberMe().alwaysRemember(true);
        }
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setHostName("redis.youtopin.com");
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }


    @Bean
    public static AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
