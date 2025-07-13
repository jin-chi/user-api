package com.example.userapi.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userapi.dto.LoginRequest;
import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final ConcurrentMap<Long, UserCredential> byId = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Long> byMail = new ConcurrentHashMap<>();
  private final AtomicLong seq = new AtomicLong(1);

  private record UserCredential(
    Long id,
    String email,
    String name,
    String pwHash
  ) {}

  /* 登録 */
  public UserResponse register(UserRequest req) {
    if (byMail.containsKey(req.email())) {
      throw new IllegalArgumentException("メールアドレスは既に登録済みです");
    }
    long id = seq.getAndIncrement();
    String hash = passwordEncoder.encode(req.password());
    byId.put(id, new UserCredential(id, req.email(), req.name(), hash));
    byMail.put(req.email(), id);
    return new UserResponse(id, req.email(), req.name());
  }

  /* ログイン認証 */
  public UserResponse login(LoginRequest req) {
    Long id = byMail.get(req.email());
    if (id == null) {
      throw new SecurityException("メールアドレスまたはパスワードが不正です");
    }

    UserCredential userCredential = byId.get(id);
    if (!passwordEncoder.matches(req.password(), userCredential.pwHash())) {
      throw new SecurityException("メールまたはパスワードが不正です");
    }
    return new UserResponse(userCredential.id(), userCredential.email(), userCredential.name());
  }

}
