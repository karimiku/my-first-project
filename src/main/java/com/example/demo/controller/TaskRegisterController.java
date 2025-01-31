package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.constant.UrlConst;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskRegisterService;

import lombok.RequiredArgsConstructor;

/**
 * タスク登録および管理を担当するコントローラー
 */
@Controller
@RequiredArgsConstructor
public class TaskRegisterController {

    /** タスク管理サービス */
    private final TaskRegisterService taskService;

    /**
     * タスク登録画面を表示する
     *
     * @param model Modelオブジェクト（タスク情報を追加）
     * @return タスク登録画面（task-register.html）
     */
    @GetMapping(UrlConst.TASK_REGISTER)
    public String showTaskRegisterForm(Model model) {
        model.addAttribute("TaskRegisterForm", new Task()); // 空のタスクを追加
        List<Task> tasks = taskService.getTaskByCurrentUser(); // 現在のユーザーのタスクリストを取得
        model.addAttribute("tasks", tasks); // タスクリストを追加
        return "task-register";
    }

    /**
     * 新しいタスクを登録する
     *
     * @param task 登録するタスク情報
     * @return タスク登録画面にリダイレクト
     */
    @PostMapping(UrlConst.TASK_REGISTER)
    public String registerTask(@ModelAttribute Task task) {
        taskService.saveTask(task); // タスクを保存
        return "redirect:" + UrlConst.TASK_REGISTER; // 登録後にタスクリスト画面へリダイレクト
    }

    /**
     * タスクを削除する
     *
     * @param id 削除するタスクのID
     * @return menu画面へリダイレクト
     */
    @PostMapping(UrlConst.TASK_DELETE)
    public String deleteReminder(@RequestParam("id") Long id) {
        if (taskService.deleteTaskById(id)) {
            return "redirect:/menu";
        } else {
        	return "menu";
        }
    }
    
    // タスク編集画面に遷移 (taskId を取得)
    @GetMapping(UrlConst.TASK_EDIT_ID)
    public String editTask(@PathVariable("id") Long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        return "task-edit";  // task-edit.html を表示
    }
    
    // タスクを更新
    @PostMapping(UrlConst.TASK_UPDATE)
    public String updateTask(@ModelAttribute Task task) {
    	taskService.saveTask(task); // タスクを保存
    	return "redirect:/menu";
    	
    }
}