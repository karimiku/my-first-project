package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Task エンティティクラス
 * ユーザーのタスク情報を管理する
 */
@Entity
@Table(name = "task")
@Getter
@Setter
@ToString
public class Task {

    /** 主キー (自動生成) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ユーザーID (タスクの所有者) */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** タスク名 */
    @Column(nullable = false)
    private String name;

    /** 締め切り日時 */
    @Column(name = "due_date_time", nullable = false)
    private LocalDateTime dueDateTime;

    /** タスク詳細 (テキスト) */
    @Column(columnDefinition = "TEXT")
    private String details;

    /** 作成日時 (自動設定) */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時 (自動更新) */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /** タスク完了状態 (デフォルトは未完了) */
    @Column(name = "completed", nullable = false)
    private boolean completed = false;

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