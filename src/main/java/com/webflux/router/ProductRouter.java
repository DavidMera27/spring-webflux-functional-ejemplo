package com.webflux.router;

import com.webflux.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProductRouter {

    public static final String PATH = "product";

    @Bean
    RouterFunction<ServerResponse> router(ProductHandler handler){
        return RouterFunctions.route()
                .GET(PATH, handler::getAll)
                .GET(PATH + "/{id}", handler::getOne)
                .POST(PATH, handler::saveProduct)
                .PUT(PATH + "/{id}", handler::updateProduct)
                .DELETE(PATH + "/{id}", handler::deleteProduct)
                .build();
    }

}
