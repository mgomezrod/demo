package com.example.demo.dto;

import lombok.Data;

@Data
public class DummyAuthResponse {
    private int id;
    private String username;
    private String email;
    private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
