package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.User;
import com.example.demo.UserRepository;
import com.example.demo.client.DummyAuthClient;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.DummyAuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DummyAuthClient dummyAuthClient;
    private final UserRepository userRepository;

    public String login(String username, String password) {
        AuthRequest request = new AuthRequest(username, password);
        DummyAuthResponse response = dummyAuthClient.login(request);

        String accessToken = response.getToken();
        String refreshToken = "not_provided"; // dummyjson no lo devuelve

        // Guardar o actualizar el usuario en DB
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> new User(null, username, password, null, null));

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return "Login exitoso. Token guardado.";
    }
}

