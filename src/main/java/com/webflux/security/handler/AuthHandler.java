package com.webflux.security.handler;

import com.webflux.security.service.UserService;
import com.webflux.security.wrapper.LoginDTO;
import com.webflux.security.wrapper.SignupDTO;
import com.webflux.security.wrapper.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {

    private final UserService userService;

    public Mono<ServerResponse> login(ServerRequest request){
        Mono<LoginDTO> dtoMono = request.bodyToMono(LoginDTO.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.login(dto), TokenDTO.class));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<SignupDTO> dtoMono = request.bodyToMono(SignupDTO.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.create(dto), SignupDTO.class));
    }

}
