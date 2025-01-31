package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.SafetyReminderGroup;

/**
 * SafetyReminderGroupRepository インターフェース
 * 安全確認リマインダーグループのデータアクセスを提供する
 */
@Repository
public interface SafetyReminderGroupRepository extends JpaRepository<SafetyReminderGroup, Long> {

    /**
     * 指定したユーザーIDに紐づくリマインダーグループを取得する
     * @param userId ユーザーID
     * @return 該当するリマインダーグループのリスト
     */
    @Query("SELECT g FROM SafetyReminderGroup g WHERE g.userId = :userId")
    List<SafetyReminderGroup> findGroupByUserId(@Param("userId") Long userId);
}