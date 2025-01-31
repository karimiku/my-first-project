package com.example.demo.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.entity.UserInfo;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * ログイン機能を管理するコントローラー。
 * - ログイン画面の表示、認証処理、エラーメッセージの処理を担当。
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    /** ユーザー情報検索サービス */
    private final LoginService service;

    /** パスワードエンコーダー（Spring Security に登録されたものを使用） */
    private final PasswordEncoder passwordEncoder;

    /** メッセージソース（エラーメッセージ取得用） */
    private final MessageSource messageSource;

    /** セッション情報 */
    @Autowired
    private HttpSession session;

    /**
     * ログイン画面の表示
     * @param model Modelオブジェクト
     * @param form ログインフォーム情報
     * @return ログイン画面（login.html）
     */
    @GetMapping(UrlConst.LOGIN)
    public String showLoginPage(Model model, LoginForm form) {
        return "login";
    }

    /**
     * ログインエラー時の画面表示
     * - Spring Security の認証エラーを取得し、エラーメッセージを表示。
     *
     * @param model Modelオブジェクト
     * @param form ログインフォーム情報
     * @return ログイン画面（エラーメッセージ付き）
     */
    @GetMapping(value = UrlConst.LOGIN, params = "error")
    public String showLoginErrorPage(Model model, LoginForm form) {
        var errorInfo = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (errorInfo != null) {
            model.addAttribute("errorMsg", errorInfo.getMessage());
        }
        return "login";
    }

    /**
     * ログイン処理
     * - ユーザー情報を検索し、認証成功ならメニュー画面へリダイレクト。
     * - 認証失敗時はエラーメッセージを表示し、ログイン画面を再表示。
     *
     * @param model Modelオブジェクト
     * @param form ログインフォーム情報
     * @return 認証成功時：メニュー画面 / 失敗時：ログイン画面
     */
    @PostMapping(UrlConst.LOGIN)
    public String processLogin(Model model, LoginForm form) {
        Optional<UserInfo> userInfo = service.searchUserById(form.getLoginId());

        if (userInfo.isPresent() && passwordEncoder.matches(form.getPassword(), userInfo.get().getPassword())) {
            return "redirect:" + UrlConst.MENU;
        } else {
            model.addAttribute("errorMsg",
                    AppUtil.getMessage(messageSource, MessageConst.LOGIN_WRONG_INPUT));
            return "login";
        }
    }
}
