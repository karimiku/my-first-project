package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.SafetyReminderGroup;
import com.example.demo.entity.Task;
import com.example.demo.repository.SafetyReminderGroupRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserInfoRepository;

/**
 * MenuService クラス
 * ログイン中のユーザーに関連するタスクやリマインダーを取得する
 */
@Service
public class MenuService {
    
    private final TaskRepository taskRepository;
    private final UserInfoRepository userInfoRepository;
    private final SafetyReminderGroupRepository reminderGroupRepository;

    /**
     * コンストラクタ
     * @param taskRepository タスクリポジトリ
     * @param userInfoRepository ユーザー情報リポジトリ
     * @param reminderGroupRepository 安全確認リマインダーグループリポジトリ
     */
    public MenuService(TaskRepository taskRepository, 
                       UserInfoRepository userInfoRepository, 
                       SafetyReminderGroupRepository reminderGroupRepository) {
        this.taskRepository = taskRepository;
        this.userInfoRepository = userInfoRepository;
        this.reminderGroupRepository = reminderGroupRepository;
    }

    /**
     * ログイン中のユーザーの今日のタスクを取得
     * @return 今日のタスクリスト
     */
    public List<Task> getTodayTaskByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        // ログイン中のユーザーを取得
        var user = userInfoRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        // 今日の日付を取得
        LocalDate today = LocalDate.now();

        // 今日のタスクを取得
        return taskRepository.findByUserIdAndDueDate(user.getId(), today);
    }

    /**
     * ログイン中のユーザーに紐づく安全確認リマインダーグループを取得
     * @return ユーザーのリマインダーグループリスト
     */
    public List<SafetyReminderGroup> getGroupForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        // ログイン中のユーザーを取得
        var user = userInfoRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        // ユーザーに紐づくグループを取得
        return reminderGroupRepository.findGroupByUserId(user.getId());
    }
    
    
}