package com.example.userapi.dto;

public record UserRequest (
  String email,
  String password,
  String name
) {}