package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.UrlConst;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskRegisterService;

import lombok.RequiredArgsConstructor;

/**
 * タスク管理のAPIコントローラー
 * - カレンダー表示用のタスク取得API
 * - タスクの完了状態を更新するAPI
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {
    
    /** タスク管理サービス */
    private final TaskRegisterService taskRegisterService;

    /**
     * 📅 日付ごとのタスクリストを取得するAPI
     * JavaScript（カレンダー表示）から `/api/tasks-by-date` を `fetch()` するために使用。
     *
     * @return 日付ごとのタスクリストをマッピングした `Map<String, List<Task>>`
     */
    @GetMapping(UrlConst.TASKS_BY_DATE)
    public Map<String, List<Task>> getTaskByDate() {
        return taskRegisterService.getTaskGroupedByDate();
    }

    /**
     *  タスクの完了状態を更新するAPI
     * フロントエンド（JavaScript）から `fetch('/api/task/update-completed')` で呼び出される。
     *
     * @param payload リクエストボディ（`taskId`, `completed` のマップ）
     * @return 更新成功時：HTTP 200（タスクの状態を更新しました）
     */
    @PostMapping(UrlConst.TASK_UPDATE_COMPLETED)
    public ResponseEntity<String> updateTaskCompleted(@RequestBody Map<String, Object> payload) {
        // リクエストから taskId と completed を取得
        Long taskId = ((Number) payload.get("taskId")).longValue();
        Boolean completed = (Boolean) payload.get("completed");

        // サービスでタスクの完了状態を更新
        taskRegisterService.updateTaskCompleted(taskId, completed);

        return ResponseEntity.ok("タスクの状態を更新しました");
    }
}
