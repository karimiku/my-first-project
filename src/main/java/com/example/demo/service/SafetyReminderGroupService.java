package com.example.demo.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.SafetyReminderGroup;
import com.example.demo.repository.SafetyReminderGroupRepository;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * SafetyReminderGroupService クラス
 * 安全確認リマインダーグループに関するビジネスロジックを提供する
 */
@Service
@RequiredArgsConstructor
public class SafetyReminderGroupService {
    
    private final SafetyReminderGroupRepository groupRepository;
    private final UserInfoRepository userInfoRepository;

    /**
     * 指定したユーザーIDのリマインダーグループを取得する
     * @param userId ユーザーID
     * @return ユーザーに紐づくリマインダーグループのリスト
     */
    public List<SafetyReminderGroup> getGroupByUserId(Long userId) {
        return groupRepository.findGroupByUserId(userId);
    }

    /**
     * ログイン中のユーザーのリマインダーグループを取得する
     * @return ログイン中のユーザーに紐づくリマインダーグループのリスト
     */
    public List<SafetyReminderGroup> getGroupForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        var user = userInfoRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        return getGroupByUserId(user.getId());
    }

    /**
     * 新しいリマインダーグループを追加する
     * @param group 追加するリマインダーグループ
     */
    public void addGroup(SafetyReminderGroup group) {
        groupRepository.save(group);
    }

    /**
     * 指定したリマインダーグループを削除する
     * @param groupId 削除するグループのID
     */
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}

