package com.example.demo.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * LoginForm クラス
 * ユーザーのログインフォームデータを保持する
 */
@Getter
@Setter
@ToString
public class LoginForm {

    /** ログインID */
    private String loginId;

    /** パスワード */
    private String password;
}
