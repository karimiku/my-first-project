package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.SafetyReminder;
import com.example.demo.repository.SafetyReminderGroupRepository;
import com.example.demo.repository.SafetyReminderRepository;

import lombok.RequiredArgsConstructor;

/**
 * SafetyReminderService クラス
 * 安全確認リマインダーに関するビジネスロジックを提供する
 */
@Service
@RequiredArgsConstructor
public class SafetyReminderService {
    
    private final SafetyReminderRepository reminderRepository;
    private final SafetyReminderGroupRepository groupRepository; // ← 追加

    /**
     * 指定したグループIDに紐づくリマインダーを取得する
     * @param groupId グループID
     * @return グループに紐づくリマインダーのリスト
     */
    public List<SafetyReminder> getReminderByGroupId(Long groupId) {
        return reminderRepository.findByGroupId(groupId);
    }

    /**
     * 新しいリマインダーを追加する
     * @param reminder 追加するリマインダー
     */
    @Transactional
    public void addReminder(SafetyReminder reminder) {
        reminderRepository.save(reminder);
    }

    /**
     * 指定したIDのリマインダーを削除する
     * @param reminderId 削除するリマインダーのID
     */
    @Transactional
    public void deleteReminder(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    /**
     * 指定したIDのリマインダーの完了ステータスを更新する
     * @param reminderId リマインダーID
     * @param completed 完了ステータス (true: 完了, false: 未完了)
     */
    @Transactional
    public void updateCompletedStatus(Long reminderId, Boolean completed) {
        SafetyReminder reminder = reminderRepository.findById(reminderId)
            .orElseThrow(() -> new IllegalArgumentException("リマインダーが見つかりません: ID = " + reminderId));
        
        reminder.setCompleted(completed);
        reminderRepository.save(reminder);
    }

    /**
     * 指定したIDのリマインダーを取得する
     * @param id リマインダーID
     * @return 該当するリマインダー (存在しない場合は null)
     */
    public SafetyReminder getReminderById(Long id) {
        return reminderRepository.findById(id).orElse(null);
    }

    /**
     * リマインダーの情報を更新する
     * @param reminder 更新するリマインダー
     */
    @Transactional
    public void updateReminder(SafetyReminder reminder) {
        reminderRepository.save(reminder);
    }

    /**
     * 指定したユーザーが同じ名前のリマインダーを既に持っているか確認する
     * @param reminderName リマインダー名
     * @param userId ユーザーID
     * @return 存在する場合は true、存在しない場合は false
     */
    public boolean existsReminderByNameAndUserId(String reminderName, Long userId) {
        return reminderRepository.existsByReminderNameAndUserId(reminderName, userId);
    }
    
    /**
     * 指定したリマインダーを削除する
     * @param id リマインダーID
     * @return 削除成功なら true、存在しなければ false
     */
    @Transactional
    public boolean deleteReminderByIdEdit(Long id) {
        if (reminderRepository.existsById(id)) {
            // 1. リマインダーを取得
            SafetyReminder reminder = reminderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("リマインダーが見つかりません: ID = " + id));

            // 2. リマインダーを削除
            reminderRepository.deleteById(id);

            // 3. グループにまだリマインダーがあるか確認
            Long groupId = reminder.getGroup().getId();
            boolean hasReminders = reminderRepository.existsByGroupId(groupId);

            // 4. もしリマインダーがなくなったらグループを削除
            if (!hasReminders) {
                groupRepository.deleteById(groupId);
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    @Transactional
    public boolean deleteReminderByIdMenu(Long id) {
        if (reminderRepository.existsById(id)) {
            // 1. リマインダーを取得
            SafetyReminder reminder = reminderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("リマインダーが見つかりません: ID = " + id));

            // 2. リマインダーを削除
            reminderRepository.deleteById(id);

            // 3. グループにまだリマインダーがあるか確認
            Long groupId = reminder.getGroup().getId();
            boolean hasReminders = reminderRepository.existsByGroupId(groupId);

            // 4. もしリマインダーがなくなったらグループを削除
            if (!hasReminders) {
                groupRepository.deleteById(groupId);
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    
}

