package com.example.demo.constant;

/**
 * アプリケーションで使用するメッセージ定数クラス。
 * 各画面で表示するエラーメッセージや通知メッセージのキーを管理。
 */
public class MessageConst {

    // ======= ログイン画面関連 =======
    
    /** 入力内容が間違っている場合 */
    public static final String LOGIN_WRONG_INPUT = "login.wrongInput";

    /** ログインIDが未入力 */
    public static final String LOGIN_EMPTY_LOGIN_ID = "login.emptyLoginId";

    /** パスワードが未入力 */
    public static final String LOGIN_EMPTY_PASSWORD = "login.emptyPassword";


    // ======= ユーザー登録画面関連 =======

    /** 既に登録されているログインID */
    public static final String SIGNUP_EXISTED_LOGIN_ID = "signup.existedLoginId";

    /** ユーザー登録が完了 */
    public static final String SIGNUP_REGIST_SUCCEED = "signup.registSucceed";

    /** ログインIDが未入力 */
    public static final String SIGNUP_EMPTY_LOGINID = "signup.emptyLoginId";

    /** パスワードが未入力 */
    public static final String SIGNUP_EMPTY_PASSWORD = "signup.emptyPassword";


    // ======= 安全確認リマインダー関連 =======

    /** 既に登録されているリマインダーグループ */
    public static final String REMINDER_GROUP_EXISTED = "remindergroup.existed";

    /** 既に登録されているリマインダー */
    public static final String REMINDER_EXISTED = "reminder.existed";

}