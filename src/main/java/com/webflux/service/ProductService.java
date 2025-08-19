package com.webflux.service;

import com.webflux.document.Product;
import com.webflux.exception.CustomException;
import com.webflux.repository.ProductRepository;
import com.webflux.wrapper.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final static String NOT_FOUND_MESSAGE = "Product not found. API message.";
    private final static String ALREADY_IN_USE_MESSAGE = "Product name already in use. API message.";


    public Flux<ProductDTO> getAll(){
        return productRepository.findAll().map(this::toDTO);
    }

    public Mono<ProductDTO> getById(String id){
        return productRepository.findById(id)
                .map(this::toDTO)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)));
    }

    public Mono<Product> saveProduct(ProductDTO productDTO){
        Mono<Boolean> existsProduct = productRepository.findByName(productDTO.name()).hasElement();
        return existsProduct.flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, ALREADY_IN_USE_MESSAGE)) : productRepository.save(toDocument(productDTO)));
    }

    public Mono<Product> updateProduct(String id, ProductDTO productDTO){
        Mono<Boolean> existsProduct = productRepository.findById(id).hasElement();
        return existsProduct.flatMap(exists -> exists ? productRepository.save(new Product(id, productDTO.name(), productDTO.price())) : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)));
    }

    public Mono<Void> deleteProduct(String id){
        Mono<Boolean> existsProduct = productRepository.findById(id).hasElement();
        return existsProduct.flatMap(exists -> exists ? productRepository.deleteById(id) : Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE)));
    }

    private Product toDocument(ProductDTO productDTO){
        Product mappedProduct = new Product();
        mappedProduct.setName(productDTO.name());
        mappedProduct.setPrice(productDTO.price());
        return mappedProduct;
    }

    private ProductDTO toDTO(Product product){
        return new ProductDTO(product.getName(), product.getPrice());
    }

}
