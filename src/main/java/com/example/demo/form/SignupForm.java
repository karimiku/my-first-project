package com.example.demo.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SignupForm クラス
 * ユーザーのサインアップフォームデータを保持する
 */
@Getter
@Setter
@ToString
public class SignupForm {

    /** ログインID */
    private String loginId;

    /** パスワード */
    private String password;
}
