package com.webflux.handler;

import com.webflux.document.Product;
import com.webflux.service.ProductService;
import com.webflux.validation.ObjectValidator;
import com.webflux.wrapper.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAll(ServerRequest request){
        Flux<ProductDTO> products = productService.getAll().delayElements(Duration.ofMillis(500));
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(products, ProductDTO.class);
    }

    public Mono<ServerResponse> getOne(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<ProductDTO> product = productService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(product, Product.class);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> saveProduct(ServerRequest request){
        Mono<ProductDTO> product = request.bodyToMono(ProductDTO.class).doOnNext(objectValidator::validate);
        return product.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.saveProduct(p), Product.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> updateProduct(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<ProductDTO> product = request.bodyToMono(ProductDTO.class).doOnNext(objectValidator::validate);
        return product.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.updateProduct(id, p), Product.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> deleteProduct(ServerRequest request){
        String id = request.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.deleteProduct(id), Product.class);
    }

}
