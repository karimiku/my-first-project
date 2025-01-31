package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Task;

/**
 * TaskRepository インターフェース
 * タスクのデータアクセスを提供する
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * 指定したユーザーIDのタスクを取得する
     * @param userId ユーザーID
     * @return ユーザーのタスクリスト
     */
    List<Task> findByUserId(Long userId);

    /**
     * 指定したユーザーIDと日付のタスクを取得する
     * @param userId ユーザーID
     * @param dueDate 期限日
     * @return 指定した日付のタスクリスト
     */
    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND DATE(t.dueDateTime) = :dueDate")
    List<Task> findByUserIdAndDueDate(@Param("userId") Long userId, @Param("dueDate") LocalDate dueDate);

    /**
     * 指定した日時より前のタスクを削除する
     * @param now 現在時刻
     */
    @Modifying
    @Query("DELETE FROM Task t WHERE t.dueDateTime < :now")
    void deleteByDueDateTimeBefore(@Param("now") LocalDateTime now);
}