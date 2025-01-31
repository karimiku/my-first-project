document.addEventListener("DOMContentLoaded", () => {
    // **タスクの詳細表示切り替え**
    document.querySelectorAll(".menu-page li").forEach((item) => {
        const detailsContainer = item.querySelector(".task-details-container");

        item.addEventListener("click", () => {
            if (detailsContainer) {
                detailsContainer.classList.toggle("active");
            }
        });
    });

    // **タスクのチェックボックスの処理**
    document.querySelectorAll(".task-details-container input[type='checkbox']").forEach((checkbox) => {
        const taskItem = checkbox.closest(".menu-page li");
        if (taskItem && checkbox.checked) {
            taskItem.classList.add("completed");
        }

        checkbox.addEventListener("change", () => {
            const taskId = checkbox.dataset.taskId;
            const completed = checkbox.checked;

            fetch(`/task/update-completed`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ taskId, completed }),
            })
                .then((response) => {
                    if (response.ok && taskItem) {
                        taskItem.classList.toggle("completed", completed);
                    }
                })
                .catch((error) => console.error("サーバーエラー:", error));
        });
    });

    // **タスク編集機能**
    window.startEditingTask = function (button) {
        const taskContainer = button.closest(".task-details-container");
        const details = taskContainer.querySelector(".task-details");
        const editForm = taskContainer.querySelector(".edit-form");
        const saveButton = taskContainer.querySelector(".save-button");

        if (details && editForm) {
            details.style.display = "none"; // 詳細を非表示
            editForm.style.display = "block"; // フォームを表示
            button.style.display = "none"; // 編集ボタンを非表示
            saveButton.style.display = "inline-block"; // 保存ボタンを表示
        }
    };

    // **タスク保存機能**
    window.saveTask = function (button) {
        const taskContainer = button.closest(".task-details-container");
        const taskId = taskContainer.querySelector("input[type='checkbox']").dataset.taskId;
        const name = taskContainer.querySelector(".edit-name").value;
        const dueDateTime = taskContainer.querySelector(".edit-dueDateTime").value;
        const details = taskContainer.querySelector(".edit-details").value;

        fetch(`/task/update`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ taskId, name, dueDateTime, details }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    location.reload(); // 更新後ページをリロード
                } else {
                    console.error("タスク更新に失敗しました。");
                }
            })
            .catch((error) => console.error("サーバーエラー:", error));
    };

    // **リマインダーグループ処理**
    document.querySelectorAll(".group-item").forEach((groupItem) => {
        const reminderLists = groupItem.querySelectorAll(".reminder-list");

        if (reminderLists.length === 0) return;

        groupItem.addEventListener("click", (event) => {
            if (event.target.tagName.toLowerCase() === "input") return;

            reminderLists.forEach((reminderList) => {
                reminderList.classList.toggle("hidden");
                reminderList.classList.toggle("active");
            });
        });

        function updateGroupBackground() {
            const checkboxes = groupItem.querySelectorAll(".reminder-item input[type='checkbox']");
            if (checkboxes.length === 0) return;

            const allChecked = Array.from(checkboxes).every((checkbox) => checkbox.checked);
            groupItem.classList.toggle("completed", allChecked);
        }

        groupItem.querySelectorAll(".reminder-item input[type='checkbox']").forEach((checkbox) => {
            if (checkbox.checked) {
                updateGroupBackground();
            }

            checkbox.addEventListener("change", (event) => {
                const reminderId = checkbox.dataset.reminderId;
                const completed = checkbox.checked;

                fetch(`/reminder/update-completed`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ reminderId, completed }),
                })
                    .then((response) => {
                        if (response.ok) {
                            updateGroupBackground();
                        } else {
                            console.error("リマインダーの更新に失敗しました。");
                        }
                    })
                    .catch((error) => console.error("サーバーエラー:", error));

                event.stopPropagation();
            });
        });

        updateGroupBackground();
    });

    // **リマインダー編集機能**
    window.startEditingReminder = function (button) {
        const reminderContainer = button.closest(".reminder-item");
        const details = reminderContainer.querySelector("label");
        const editForm = reminderContainer.querySelector(".edit-form");
        const saveButton = reminderContainer.querySelector(".save-button");

        if (details && editForm) {
            details.style.display = "none"; // 詳細を非表示
            editForm.style.display = "block"; // フォームを表示
            button.style.display = "none"; // 編集ボタンを非表示
            saveButton.style.display = "inline-block"; // 保存ボタンを表示
        }
    };

    // **リマインダー保存機能**
    window.saveReminder = function (button) {
        const reminderContainer = button.closest(".reminder-item");
        const reminderId = reminderContainer.querySelector("input[type='checkbox']").dataset.reminderId;
        const name = reminderContainer.querySelector(".edit-name").value;

        fetch(`/reminder/update`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ reminderId, name }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    location.reload(); // 更新後ページをリロード
                } else {
                    console.error("リマインダー更新に失敗しました。");
                }
            })
            .catch((error) => console.error("サーバーエラー:", error));
    };
});