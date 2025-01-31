package com.example.demo.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * アプリケーション共通ユーティリティクラス
 * メッセージの取得を簡素化するヘルパーメソッドを提供する
 */
public class AppUtil {
    
    /**
     * メッセージキーをもとにメッセージを取得する
     * @param messageSource メッセージソース
     * @param key メッセージキー
     * @param params パラメータ (可変長引数)
     * @return 日本語ロケールのメッセージ文字列
     */
    public static String getMessage(MessageSource messageSource, String key, Object... params) {
        return messageSource.getMessage(key, params, Locale.JAPAN);
    }
}
