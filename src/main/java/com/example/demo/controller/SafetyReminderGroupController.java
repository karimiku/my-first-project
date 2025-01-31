package com.example.demo.controller;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.UrlConst;
import com.example.demo.entity.SafetyReminderGroup;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.service.SafetyReminderGroupService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * 安全確認リマインダーグループの登録・管理を行うコントローラー。
 */
@Controller
@RequiredArgsConstructor
public class SafetyReminderGroupController {
    
    /** グループ管理のサービス */
    private final SafetyReminderGroupService groupService;
    
    /** ユーザー情報取得用のリポジトリ */
    private final UserInfoRepository userInfoRepository;
    
    /** メッセージソース（エラーメッセージ取得用） */
    private final MessageSource messageSource;

    /**
     * グループ登録ページの表示
     *
     * - ユーザーが新しいリマインダーグループを作成するページを表示する。
     *
     * @param model Modelオブジェクト（画面にデータを渡す）
     * @return グループ登録ページ（safety-group-register.html）
     */
    @GetMapping(UrlConst.SAFETY_GROUP_REGISTER)
    public String showGroupRegisterPage(Model model) {
        model.addAttribute("SafetyReminderGroupForm", new SafetyReminderGroup());
        return "safety-group-register";
    }

    /**
     * グループ登録の処理
     *
     * - ログインユーザーのIDを取得し、新しいグループを登録する。
     * - すでに同じグループ名が存在する場合はエラーメッセージを表示。
     *
     * @param groupForm 登録するグループ情報
     * @param user ログインユーザー情報
     * @param model Modelオブジェクト（エラーメッセージを渡す）
     * @return 成功時：リマインダー登録ページにリダイレクト
     *         失敗時：グループ登録ページを再表示（エラーメッセージ付き）
     */
    @PostMapping(UrlConst.SAFETY_GROUP_REGISTER)
    public String registerGroup(
        @ModelAttribute("SafetyReminderGroupForm") SafetyReminderGroup groupForm,
        @AuthenticationPrincipal User user, // ログインユーザーを取得
        Model model
    ) {
        // ログインユーザーのIDを取得
        Long userId = getUserIdFromLoginId(user.getUsername());
        if (userId == null) {
            throw new IllegalStateException("ログインユーザーが見つかりません");
        }
        groupForm.setUserId(userId); // user_id をセット

        // 既存のグループと重複していないかチェック
        List<SafetyReminderGroup> groups = groupService.getGroupForCurrentUser();
        boolean isDuplicate = groups.stream()
                                    .anyMatch(group -> group.getName().equals(groupForm.getName()));
        if (isDuplicate) {
            model.addAttribute("errorGroup", AppUtil.getMessage(messageSource, MessageConst.REMINDER_GROUP_EXISTED));
            return "safety-group-register";
        }

        // グループを保存
        groupService.addGroup(groupForm);
        
        return "redirect:" + UrlConst.SAFETY_REMINDER_REGISTER;
    }

    /**
     * ログインID（username）からユーザーIDを取得するメソッド
     *
     * @param loginId ユーザーのログインID
     * @return ユーザーID（見つからない場合は null）
     */
    private Long getUserIdFromLoginId(String loginId) {
        return userInfoRepository.findByLoginId(loginId)
                .map(userInfo -> userInfo.getId()) // Optional から ID を取得
                .orElse(null);
    }
}


