package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.SafetyReminder;

/**
 * SafetyReminderRepository インターフェース
 * 安全確認リマインダーのデータアクセスを提供する
 */
@Repository
public interface SafetyReminderRepository extends JpaRepository<SafetyReminder, Long> {

    /**
     * 指定したグループIDに紐づくリマインダーを取得する
     * @param groupId グループID
     * @return 該当するリマインダーのリスト
     */
    List<SafetyReminder> findByGroupId(Long groupId);

    /**
     * グループにまだリマインダーが残っているか確認する
     * @param groupId グループID
     * @return 残っていれば true、1つもなければ false
     */
    boolean existsByGroupId(Long groupId); // ← ここを追加！

    /**
     * 同じ名前のリマインダーが既に存在するかチェックする
     * @param reminderName リマインダー名
     * @param userId ユーザーID
     * @return 存在する場合は true、存在しない場合は false
     */
    boolean existsByReminderNameAndUserId(String reminderName, Long userId);
}