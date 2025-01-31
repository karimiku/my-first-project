package com.example.demo.service;

import java.util.Optional;

import org.dozer.Mapper; // 正しい Mapper のインポート
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * SignupService クラス
 * ユーザーの登録処理を提供する
 */
@Service
@RequiredArgsConstructor // new したものを注入するコンストラクタを実装
public class SignupService {
    
    private final UserInfoRepository repository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * ユーザー情報を登録する
     * @param form 登録フォームのデータ
     * @return 登録されたユーザー情報 (すでに存在する場合は空の Optional)
     */
    public Optional<UserInfo> registUserInfo(SignupForm form) {
        var userInfoExisted = repository.findByLoginId(form.getLoginId());
      
        // すでに存在するログインIDの場合は登録しない
        if (userInfoExisted.isPresent()) {
            return Optional.empty();
        }

        // フォームデータを UserInfo エンティティにマッピング
        var userInfo = mapper.map(form, UserInfo.class);
    
        // パスワードをエンコードしてセット
        var encodedPassword = passwordEncoder.encode(form.getPassword());
        userInfo.setPassword(encodedPassword);
        
        // UserInfo を保存して返す
        return Optional.of(repository.save(userInfo));
    }
}



