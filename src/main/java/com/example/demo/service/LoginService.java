package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * LoginService クラス
 * ユーザーのログイン関連の処理を提供する
 */
@Service
@RequiredArgsConstructor // new したものを注入するコンストラクタを実装
public class LoginService {

    /** ユーザー情報リポジトリ */
    private final UserInfoRepository repository;

    /**
     * 指定したログインIDでユーザーを検索する
     * @param loginId ログインID
     * @return 該当するユーザー情報 (存在しない場合は Optional.empty())
     */
    public Optional<UserInfo> searchUserById(String loginId) {
        return repository.findByLoginId(loginId);
    }
}