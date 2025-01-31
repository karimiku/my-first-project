package com.example.demo.constant;

/**
 * アプリケーションで使用するURLパスの定数クラス。
 * 各画面のエンドポイントを定義し、URLの管理を統一。
 */
public class UrlConst {

    // ======= 認証関連 =======
    
    /** ログイン画面 */
    public static final String LOGIN = "/login";

    /** ユーザー登録画面 */
    public static final String SIGNUP = "/signup";


    // ======= メニュー関連 =======
    
    /** メニュー画面 */
    public static final String MENU = "/menu";
    
    /** リマインダー削除API */
    public static final String REMINDER_DELETE = "/delete-reminder";


    // ======= タスク関連 =======
    
    /** タスク登録画面 */
    public static final String TASK_REGISTER = "/task-register";
    
    /** タスクの完了状態を更新するAPIエンドポイント */
    public static final String TASK_UPDATE_COMPLETED = "/task/update-completed";
  
    /** リマインダーの完了状態を更新するAPIエンドポイント */
    public static final String REMINDER_UPDATE_COMPLETED = "/reminder/update-completed";
    
    /** タスクの日付別取得API（カレンダー用） */
    public static final String TASKS_BY_DATE = "/tasks-by-date";
    
    /** タスク削除API */
    public static final String TASK_DELETE = "/delete-task";
    
    /** タスク編集*/
    public static final String TASK_EDIT_ID = "/task/edit/{id}";
    
    /** タスク更新 */
    public static final String TASK_UPDATE = "/task/update";


    // ======= 安全確認リマインダー関連 =======
    
    /** リマインダー登録画面 */
    public static final String SAFETY_REMINDER_REGISTER = "/safety-reminder-register";

    /** リマインダーグループ登録画面 */
    public static final String SAFETY_GROUP_REGISTER = "/safety-group-register";
    
    /** リマインダー編集*/
    public static final String REMINDER_EDIT_ID = "/reminder/edit/{id}";
    
    /** リマインダー更新 */
    public static final String REMINDER_UPDATE = "/reminder/update";

}