package com.zxcPro.config;

import com.zxcPro.interceptor.CheckTokenInterceptor;
import com.zxcPro.interceptor.SetTimeForAllInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private CheckTokenInterceptor checkTokenInterceptor;

    @Autowired
    private SetTimeForAllInterceptor setTimeForAllInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkTokenInterceptor)
                .addPathPatterns("/shopcart/**")
                .addPathPatterns("/orders/**");

        registry.addInterceptor(setTimeForAllInterceptor).addPathPatterns("/**");
    }
}
