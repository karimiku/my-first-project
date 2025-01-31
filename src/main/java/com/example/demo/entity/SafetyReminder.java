package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SafetyReminder エンティティクラス
 * 安全確認リマインダーのデータを管理する
 */
@Entity
@Table(name = "safety_reminder")
@Getter
@Setter
@ToString
public class SafetyReminder {

    /** 主キー (自動生成) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ユーザーID (リマインダーの作成者) */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** グループ (リマインダーが属するグループ) */
    @ManyToOne // ここを追加！
    @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
    private SafetyReminderGroup group; // SafetyReminderGroup への関連

    /** グループID (外部キーとして保持) */
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    /** リマインダーの名前 */
    @Column(name = "reminder_name", nullable = false)
    private String reminderName;

    /** 作成日時 (自動設定) */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 (自動更新) */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** 完了状態 (デフォルトは未完了) */
    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    /**
     * エンティティの新規作成時に `createdAt` と `updatedAt` を現在時刻に設定
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * エンティティの更新時に `updatedAt` を現在時刻に更新
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}




