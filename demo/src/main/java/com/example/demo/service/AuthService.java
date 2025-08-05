package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.User;
import com.example.demo.UserRepository;
import com.example.demo.client.DummyAuthClient;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.DummyAuthResponse;

import lombok.RequiredArgsConstructor;
import feign.Response;

@Service
@RequiredArgsConstructor
public class AuthService {

	@Autowired
    private DummyAuthClient dummyAuthClient;
    private final UserRepository userRepository;

    public String login(String username, String password) {
        AuthRequest request = new AuthRequest(username, password);
        Response response = dummyAuthClient.login(request);
        String accessToken = response.headers().getOrDefault("Set-Cookie", Set.of()).stream()
                .filter(cookie -> cookie.startsWith("accessToken="))
                .map(cookie -> cookie.split(";")[0].split("=")[1])
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró la cookie de token"));

        String refreshToken = response.headers().getOrDefault("Set-Cookie", Set.of()).stream()
                .filter(cookie -> cookie.startsWith("refreshToken="))
                .map(cookie -> cookie.split(";")[0].split("=")[1])
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró la cookie de token"));

        
        // Guardar o actualizar el usuario en DB
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> new User(null, username, password, null, null));

        user.setLoginTime(LocalDateTime.now());
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return accessToken;
    }
}

