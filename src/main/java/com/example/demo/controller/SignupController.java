package com.example.demo.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.form.SignupForm;
import com.example.demo.service.SignupService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録を管理するコントローラー。
 * - ユーザー登録画面の表示
 * - 入力チェック
 * - 登録処理
 */
@Controller
@RequiredArgsConstructor
public class SignupController {
    
    /** ユーザー登録サービス */
    private final SignupService service;
    
    /** メッセージソース（エラーメッセージ取得用） */
    private final MessageSource messageSource;

    /**
     * ユーザー登録画面の表示
     *
     * @param model Modelオブジェクト（画面にデータを渡す）
     * @param form ユーザー登録フォーム情報
     * @return ユーザー登録画面（signup.html）
     */
    @GetMapping(UrlConst.SIGNUP)
    public String view(Model model, SignupForm form) {
        return "signup";
    }

    /**
     * ユーザー登録処理
     *
     * - 入力チェック（ログインID・パスワードが空でないか）
     * - ログインIDの重複チェック
     * - 登録成功時には成功メッセージを表示
     *
     * @param model Modelオブジェクト（エラーメッセージを渡す）
     * @param form ユーザー登録フォーム情報
     * @param redirectAttributes リダイレクト時のメッセージ送信用
     * @return 登録成功時：ログイン画面にリダイレクト
     *         失敗時：再度ユーザー登録画面を表示（エラーメッセージ付き）
     */
    @PostMapping(UrlConst.SIGNUP)
    public String signup(Model model, SignupForm form, RedirectAttributes redirectAttributes) {
        // 入力チェック（ログインIDが空かどうか）
        if (form.getLoginId() == null || form.getLoginId().isEmpty()) {
            model.addAttribute("errorMsg", AppUtil.getMessage(messageSource, MessageConst.SIGNUP_EMPTY_LOGINID));
            return "signup";
        }

        // 入力チェック（パスワードが空かどうか）
        if (form.getPassword() == null || form.getPassword().isEmpty()) {
            model.addAttribute("errorMsg", AppUtil.getMessage(messageSource, MessageConst.SIGNUP_EMPTY_PASSWORD));
            return "signup";
        }

        // ログインIDの重複チェック
        var userInfoOpt = service.registUserInfo(form);
        if (userInfoOpt.isEmpty()) {
            model.addAttribute("errorMsg", AppUtil.getMessage(messageSource, MessageConst.SIGNUP_EXISTED_LOGIN_ID));
            return "signup";
        }

        // 登録成功メッセージをリダイレクト時に渡す
        redirectAttributes.addFlashAttribute("message", AppUtil.getMessage(messageSource, MessageConst.SIGNUP_REGIST_SUCCEED));
        
        // ログイン画面にリダイレクト
        return "redirect:" + UrlConst.LOGIN;
    }
}


