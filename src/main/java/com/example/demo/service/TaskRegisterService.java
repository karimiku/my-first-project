package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserInfoRepository;

/**
 * TaskRegisterService クラス
 * タスクの登録・管理に関するビジネスロジックを提供する
 */
@Service
public class TaskRegisterService {

    private final TaskRepository taskRepository;
    private final UserInfoRepository userInfoRepository;

    /**
     * コンストラクタ
     * @param taskRepository タスクリポジトリ
     * @param userInfoRepository ユーザー情報リポジトリ
     */
    public TaskRegisterService(TaskRepository taskRepository, UserInfoRepository userInfoRepository) {
        this.taskRepository = taskRepository;
        this.userInfoRepository = userInfoRepository;
    }

    /**
     * タスクを登録する
     * @param task 登録するタスク
     */
    public void saveTask(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        var user = userInfoRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        task.setUserId(user.getId());
        taskRepository.save(task);
    }
    
    /**
     * 現在ログインしているユーザーのタスクを取得する
     * @return ユーザーのタスクリスト
     */
    public List<Task> getTaskByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        var user = userInfoRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        return taskRepository.findByUserId(user.getId());
    }
    
    /**
     * タスクの完了状態を更新する
     * @param taskId タスクID
     * @param completed 完了状態 (true: 完了, false: 未完了)
     */
    public void updateTaskCompleted(Long taskId, boolean completed) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("タスクが見つかりません"));
        task.setCompleted(completed);
        taskRepository.save(task);
    }
    
    /**
     * ユーザーの日付ごとのタスクをグループ化して取得する
     * @return 日付ごとにグループ化されたタスクのマップ
     */
    public Map<String, List<Task>> getTaskGroupedByDate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        var user = userInfoRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findByUserId(user.getId()).stream()
                .filter(task -> !task.getDueDateTime().toLocalDate().isBefore(now.toLocalDate()))
                .collect(Collectors.toList());

        return tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getDueDateTime().toLocalDate().toString()));
    }
    
    /**
     * 毎日深夜0時に過去のタスクを自動削除する
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredTasks() {
        LocalDateTime now = LocalDateTime.now();
        taskRepository.deleteByDueDateTimeBefore(now);
    }
    
    /**
     * 指定したタスクを削除する
     * @param id タスクID
     * @return 削除成功なら true、存在しなければ false
     */
    public boolean deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    // ID でタスクを取得
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定されたタスクが見つかりません: ID = " + id));
    }
}
