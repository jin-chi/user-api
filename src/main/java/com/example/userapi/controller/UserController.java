package com.example.userapi.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;

import jakarta.validation.Valid;


@RestController // 戻り値をJSONで返す
@RequestMapping("/users") // base path
@Validated
public class UserController {

  // インメモリ簡易ストア（スレッドセーフ）
  private final ConcurrentMap<Long, UserResponse> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong(1);

  @PostMapping // POST /users
  public UserResponse register(@RequestBody @Valid UserRequest req) {
    Long newId = sequence.getAndIncrement();
    UserResponse res = new UserResponse(newId, req.email(), req.name());
    store.put(newId, res); // 保存
    return res; // 200 OK + JSON
  }
}