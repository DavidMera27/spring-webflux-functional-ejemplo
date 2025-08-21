package com.webflux.security.token;

import com.webflux.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> jwtProvider.validateToken(auth.getCredentials().toString()))
                .log()
                .onErrorResume(e -> Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "Bad token")))
                .map(decodedToken -> new UsernamePasswordAuthenticationToken(
                        jwtProvider.extractUsername(decodedToken),
                        null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(jwtProvider.getSpecificClaim(decodedToken, "Authorities").asString())));
    }
}
