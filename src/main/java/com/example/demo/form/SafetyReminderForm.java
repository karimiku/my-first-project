package com.example.demo.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SafetyReminderForm クラス
 * 安全確認リマインダーのフォームデータを保持する
 */
@Getter
@Setter
@ToString
public class SafetyReminderForm {

    /** リマインダー名 */
    private String reminderName;
}
