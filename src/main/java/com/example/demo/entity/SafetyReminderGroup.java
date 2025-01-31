package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SafetyReminderGroup エンティティクラス
 * ユーザーが作成するリマインダーグループを管理する
 */
@Entity
@Table(name = "safety_reminder_group")
@Getter
@Setter
@ToString
public class SafetyReminderGroup {

    /** 主キー (自動生成) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ユーザーID (グループの作成者) */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** グループ名 */
    @Column(nullable = false)
    private String name;

    /** グループに属するリマインダー一覧 */
    @OneToMany(mappedBy = "groupId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SafetyReminder> reminders;

    /** 作成日時 (自動設定) */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 (自動更新) */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
