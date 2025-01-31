package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserInfo;

/**
 * UserInfoRepository インターフェース
 * ユーザー情報のデータアクセスを提供する
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    /**
     * 指定したログインIDでユーザーを検索する
     * @param loginId ログインID
     * @return 該当するユーザー情報 (存在しない場合は Optional.empty())
     */
    Optional<UserInfo> findByLoginId(String loginId);
}