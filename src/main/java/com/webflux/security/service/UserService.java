package com.webflux.security.service;

import com.webflux.exception.CustomException;
import com.webflux.security.entity.UserEntity;
import com.webflux.security.entity.UserPrincipal;
import com.webflux.security.enums.Role;
import com.webflux.security.repository.UserRepository;
import com.webflux.security.token.JwtProvider;
import com.webflux.security.wrapper.LoginDTO;
import com.webflux.security.wrapper.SignupDTO;
import com.webflux.security.wrapper.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDTO> login(LoginDTO loginDTO){
        return userRepository.findByUsernameOrEmail(loginDTO.username(), loginDTO.username())
                .filter(user -> passwordEncoder.matches(loginDTO.password(), user.getPassword()))
                .map(user ->
                        new TokenDTO(
                                jwtProvider.createToken(new UsernamePasswordAuthenticationToken(
                                        new UserPrincipal(user.getUsername(), null, user.getRoles().stream().map(SimpleGrantedAuthority::new).toList(), true, true, true, true), null))))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Bad credentials.")));
    }

    public Mono<UserEntity> create(SignupDTO signupDTO){
        UserEntity user = new UserEntity();
        user.setUsername(signupDTO.username());
        user.setEmail(signupDTO.email());
        user.setPassword(passwordEncoder.encode(signupDTO.password()));
        user.setRoles(List.of(Role.ROLE_USER.name()));
        Mono<Boolean> userExists = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).hasElement();
        return userExists
                .flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Username or email allready in use.")) : userRepository.save(user));
    }

}
