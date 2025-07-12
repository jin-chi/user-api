package com.example.userapi.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.userapi.dto.LoginRequest;
import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;

@Service
public class UserService {
  
  private final ConcurrentMap<Long, UserResponse> store = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Long> emailIndex = new ConcurrentHashMap<>();
  private final AtomicLong seq = new AtomicLong(1);

  /* 登録 */
  public UserResponse register(UserRequest req) {
    if (emailIndex.containsKey(req.email())) {
      throw new IllegalArgumentException("メールアドレスは既に登録済みです");
    }
    long id = seq.getAndIncrement();
    UserResponse res = new UserResponse(id, req.email(), req.name());
    store.put(id, res);
    emailIndex.put(req.email(), id);
    // passwordは今は文で保持しない
    return res;
  }

  /* ログイン認証（パスワード省略・メール存在チェックのみ） */
  public UserResponse login(LoginRequest req) {
    Long id = emailIndex.get(req.email());
    if (id == null) {
      throw new SecurityException("メールアドレスまたはパスワードが不正です");
    }
    return store.get(id);
  }

}
