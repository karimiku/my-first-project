package com.example.demo.form;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TaskRegisterForm クラス
 * タスク登録フォームのデータを保持する
 */
@Getter
@Setter
@ToString
public class TaskRegisterForm {

    /** タスク名 */
    private String name;

    /** 締め切り日時 */
    private LocalDateTime dueDateTime;

    /** タスク詳細 */
    private String details;
}
