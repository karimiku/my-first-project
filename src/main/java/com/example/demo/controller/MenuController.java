package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.constant.UrlConst;
import com.example.demo.entity.SafetyReminderGroup;
import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.MenuService;
import com.example.demo.service.SafetyReminderGroupService;
import com.example.demo.service.SafetyReminderService;

import lombok.RequiredArgsConstructor;

/**
 * メニュー画面の表示およびタスク・リマインダーの更新を管理するコントローラー。
 * 
 * - メニュー画面の表示
 * - タスクの完了状態の更新
 * - リマインダーの完了状態の更新
 */
@Controller
@RequiredArgsConstructor
public class MenuController {
    
    /** メニュー画面のサービス（今日のタスク取得） */
    private final MenuService menuService;
    
    /** 安全確認リマインダーグループのサービス */
    private final SafetyReminderGroupService groupService;
    
    /** 安全確認リマインダーのサービス */
    private final SafetyReminderService reminderService;
    
    /** タスクリポジトリ（DBアクセス用） */
    private final TaskRepository taskRepository;

    /**
     * メニュー画面の表示処理
     * 
     * - 今日のタスクを取得し、画面に渡す。
     * - 安全確認リマインダーグループを取得し、画面に渡す。
     *
     * @param model Modelオブジェクト（画面にデータを渡す）
     * @return メニュー画面（menu.html）
     */
    @GetMapping(UrlConst.MENU)
    public String view(Model model) {
        // サービス層から今日のタスクを取得
        List<Task> task = menuService.getTodayTaskByCurrentUser();
        model.addAttribute("task", task); // タスクをモデルに追加

        // サービス層から安全確認リマインダーグループを取得
        List<SafetyReminderGroup> group = groupService.getGroupForCurrentUser();
        model.addAttribute("reminderGroup", group);
        
        return "menu";
    }

    /**
     * タスクの完了ステータスを更新するAPI。
     *
     * - タスクIDを受け取り、完了状態を更新する。
     * - タスクが見つからない場合はHTTP 404を返す。
     *
     * @param payload リクエストボディ（タスクID・完了ステータス）
     * @return 更新成功時：HTTP 200、失敗時：HTTP 404（タスクが見つからない場合）
     */
    @PostMapping(UrlConst.TASK_UPDATE_COMPLETED)
    @Transactional
    public ResponseEntity<String> updateTaskCompleted(@RequestBody Map<String, Object> payload) {
        // パラメータチェック
        if (!payload.containsKey("taskId") || !payload.containsKey("completed")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameters");
        }

        Long taskId;
        Boolean completed;
        try {
            taskId = Long.valueOf(payload.get("taskId").toString());
            completed = Boolean.valueOf(payload.get("completed").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameter format");
        }

        // タスクを取得して更新
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setCompleted(completed);
            taskRepository.save(task);
            return ResponseEntity.ok("Task updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }

    /**
     * リマインダーの完了ステータスを更新するAPI。
     *
     * - リマインダーIDを受け取り、完了状態を更新する。
     *
     * @param payload リクエストボディ（リマインダーID・完了ステータス）
     * @return 更新成功時：HTTP 200
     */
    @PostMapping(UrlConst.REMINDER_UPDATE_COMPLETED)
    @ResponseBody
    @Transactional
    public ResponseEntity<String> updateReminderCompleted(@RequestBody Map<String, Object> payload) {
        // パラメータチェック
        if (!payload.containsKey("reminderId") || !payload.containsKey("completed")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameters");
        }

        Long reminderId;
        Boolean completed;
        try {
            reminderId = Long.valueOf(payload.get("reminderId").toString());
            completed = Boolean.valueOf(payload.get("completed").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameter format");
        }

        // リマインダーの完了ステータスを更新
        reminderService.updateCompletedStatus(reminderId, completed);

        return ResponseEntity.ok("Reminder updated successfully");
    }
}