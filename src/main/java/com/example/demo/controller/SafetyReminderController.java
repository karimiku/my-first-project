package com.example.demo.controller;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.entity.SafetyReminder;
import com.example.demo.entity.SafetyReminderGroup;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.service.SafetyReminderGroupService;
import com.example.demo.service.SafetyReminderService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * 安全確認リマインダーの管理を行うコントローラー。
 * - リマインダーの登録・表示・編集・削除を担当。
 */
@Controller
@RequiredArgsConstructor
public class SafetyReminderController {

    /** 安全確認リマインダーのサービス */
    private final SafetyReminderService reminderService;
    
    /** リマインダーグループのサービス */
    private final SafetyReminderGroupService groupService;
    
    /** ユーザー情報リポジトリ */
    private final UserInfoRepository userInfoRepository;
    
    /** メッセージソース（エラーメッセージ取得用） */
    private final MessageSource messageSource;

    /**
     * リマインダー登録画面の表示
     * - ユーザーごとのグループを取得して表示する。
     */
    @GetMapping(UrlConst.SAFETY_REMINDER_REGISTER)
    public String showReminderRegisterPage(@AuthenticationPrincipal User user, Model model) {
        Long userId = getUserIdFromLoginId(user.getUsername());
        model.addAttribute("userGroups", groupService.getGroupByUserId(userId)); // ユーザーのグループを取得
        model.addAttribute("SafetyReminderForm", new SafetyReminder()); // 新規リマインダーオブジェクトを設定
        return "safety-reminder-register";
    }

    /**
     * リマインダー登録処理
     * - 既存のリマインダー名と重複がある場合はエラーを表示。
     */
    @PostMapping(UrlConst.SAFETY_REMINDER_REGISTER)
    public String registerReminder(
            @ModelAttribute("SafetyReminderForm") SafetyReminder reminderForm,
            @AuthenticationPrincipal User user,
            Model model) {
        Long userId = getUserIdFromLoginId(user.getUsername());
        if (userId == null) {
            throw new IllegalStateException("ログインユーザーが見つかりません");
        }
        
        // 既に同じリマインダーがあるかチェック
        boolean exists = reminderService.existsReminderByNameAndUserId(reminderForm.getReminderName(), userId);
        if (exists) {
            model.addAttribute("errorReminder", AppUtil.getMessage(messageSource, MessageConst.REMINDER_EXISTED));
            return "safety-reminder-register";
        }
        
        // リマインダーを登録
        reminderForm.setUserId(userId);
        reminderService.addReminder(reminderForm);

        return "redirect:/menu";
    }

    /**
     * リマインダー編集画面の表示
     * - 既存のリマインダー情報をフォームに入力して表示する
     */
    @GetMapping(UrlConst.REMINDER_EDIT_ID)
    public String editReminder(@PathVariable("id") Long reminderId, Model model, @AuthenticationPrincipal User user) {
        SafetyReminder reminder = reminderService.getReminderById(reminderId);
        if (reminder == null) {
            return "redirect:/menu";
        }
        Long userId = getUserIdFromLoginId(user.getUsername());
        List<SafetyReminderGroup> userGroups = groupService.getGroupByUserId(userId);
        
        model.addAttribute("reminder", reminder);
        model.addAttribute("userGroups", userGroups);
        
        return "reminder-edit";
    }

    /**
     * リマインダー編集処理
     * - 既存のリマインダーを削除し、新しいリマインダーを登録する。
     */
    @PostMapping(UrlConst.REMINDER_UPDATE)
    public String updateReminder(
            @ModelAttribute("SafetyReminderForm") SafetyReminder reminderForm,
            @RequestParam("id") Long oldReminderId,
            @AuthenticationPrincipal User user) {
        Long userId = getUserIdFromLoginId(user.getUsername());
        if (userId == null) {
            throw new IllegalStateException("ログインユーザーが見つかりません");
        }

        SafetyReminder oldReminder = reminderService.getReminderById(oldReminderId);
        if (oldReminder == null) {
            return "redirect:/menu";
        }

        // 新しいリマインダーにグループ情報をセット
        reminderForm.setUserId(userId);
        reminderForm.setGroup(oldReminder.getGroup());
        reminderForm.setId(null);

        // 先に新しいリマインダーを登録し、その後古いリマインダーを削除
        reminderService.addReminder(reminderForm);
        reminderService.deleteReminderByIdEdit(oldReminderId);

        return "redirect:/menu";
    }

    /**
     * ユーザーのログインID（loginId）から user_id を取得するメソッド。
     */
    private Long getUserIdFromLoginId(String loginId) {
        return userInfoRepository.findByLoginId(loginId)
                .map(userInfo -> userInfo.getId())
                .orElse(null);
    }
    /**
     * menu画面からリマインダーを削除
     */
    @PostMapping(UrlConst.REMINDER_DELETE)
    public String deleteReminderFromMenu(@RequestParam("id") Long id) {
        if (reminderService.deleteReminderByIdMenu(id)) {
            return "redirect:/menu"; // メニュー画面へ戻る
        } else {
            return "redirect:/menu?error"; // エラーハンドリング（オプション）
        }
    }
}




