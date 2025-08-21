package com.webflux.security.router;

import com.webflux.handler.ProductHandler;
import com.webflux.security.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthRouter {

    public static final String PATH = "auth";

    @Bean
    RouterFunction<ServerResponse> routerAuth(AuthHandler handler){
        return RouterFunctions.route()
                .POST(PATH + "/login", handler::login)
                .POST(PATH + "/signup", handler::create)
                .build();
    }

}
