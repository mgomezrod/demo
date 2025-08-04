package com.example.demo.client;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.DummyAuthResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import feign.Response;


@FeignClient(name = "dummyAuthClient", url = "https://dummyjson.com")
public interface DummyAuthClient {

    @PostMapping(value = "/auth/login", consumes = "application/json")
    Response login(@RequestBody AuthRequest request);
}
