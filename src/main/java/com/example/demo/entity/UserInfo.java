package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * UserInfo エンティティクラス
 * ユーザー情報を管理する
 */
@Entity
@Table(name = "user_info")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    /** 主キー (自動生成) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** ログインID (ユニーク制約) */
    @Column(name = "login_id", unique = true, nullable = false)
    private String loginId;

    /** パスワード */
    @Column(nullable = false)
    private String password;

    /** ログイン失敗回数 */
    @Column(name = "login_failure_count")
    private int loginFailureCount = 0;

    /** アカウントロック日時 */
    @Column(name = "account_locked_time")
    private LocalDateTime accountLockedTime;

    /** 利用可能フラグ (true: 利用不可) */
    @Column(name = "is_disabled")
    private boolean isDisabled;

    /**
     * ログイン失敗回数をインクリメントする
     */
    public UserInfo incrementLoginFailureCount() {
        return new UserInfo(id, loginId, password, ++loginFailureCount, accountLockedTime, isDisabled);
    }

    /**
     * ログイン失敗情報をリセットする
     */
    public UserInfo resetLoginFailureInfo() {
        return new UserInfo(id, loginId, password, 0, null, isDisabled);
    }

    /**
     * アカウントロック情報を更新する
     */
    public UserInfo updateAccountLocked() {
        return new UserInfo(id, loginId, password, 0, LocalDateTime.now(), isDisabled);
    }
}

