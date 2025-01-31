document.addEventListener("DOMContentLoaded", () => {
    const calendarHeader = document.querySelector("#current-month");
    const prevMonthButton = document.querySelector("#prev-month");
    const nextMonthButton = document.querySelector("#next-month");
    const calendarGrid = document.querySelector(".calendar-grid");

    let currentDate = new Date();

    function renderWeekdays() {
        const weekdays = ["日", "月", "火", "水", "木", "金", "土"];
        weekdays.forEach(day => {
            const dayName = document.createElement("div");
            dayName.classList.add("day-name");
            dayName.textContent = day;
            calendarGrid.appendChild(dayName);
        });
    }

    function renderCalendar(date, tasksByDate = {}) {
        const year = date.getFullYear();
        const month = date.getMonth();
        calendarHeader.textContent = `${year}年${month + 1}月`;

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);

        calendarGrid.innerHTML = "";
        renderWeekdays();

        for (let i = 0; i < firstDay.getDay(); i++) {
            const emptyCell = document.createElement("div");
            emptyCell.classList.add("calendar-day", "empty");
            calendarGrid.appendChild(emptyCell);
        }

        for (let day = 1; day <= lastDay.getDate(); day++) {
            const dayCell = document.createElement("div");
            dayCell.classList.add("calendar-day");

            const dateLabel = document.createElement("div");
            dateLabel.classList.add("date");
            dateLabel.textContent = day;
            dayCell.appendChild(dateLabel);

            const taskList = document.createElement("ul");
            taskList.classList.add("task-list");
            const dateKey = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
            const tasks = tasksByDate[dateKey] || [];

            tasks.forEach(task => {
                const taskItem = document.createElement("li");
                taskItem.classList.add("task-item");
                taskItem.textContent = task.name;

                const taskDetailsContainer = document.createElement("div");
                taskDetailsContainer.classList.add("task-details-container");
                taskDetailsContainer.style.display = "none";

                const checkboxLabel = document.createElement("label");
                const taskCheckbox = document.createElement("input");
                taskCheckbox.type = "checkbox";
                taskCheckbox.checked = task.completed;
                taskCheckbox.classList.add("task-checkbox");

                checkboxLabel.appendChild(taskCheckbox);
                checkboxLabel.appendChild(document.createTextNode(" 完了"));

                // 初期表示時に背景色を設定
                if (taskCheckbox.checked) {
                    taskItem.style.backgroundColor = "#d4edda";
                }

                // チェックボックス変更時のイベントリスナー
                taskCheckbox.addEventListener("change", () => {
                    const completed = taskCheckbox.checked;

                    // 背景色の変更
                    if (completed) {
                        taskItem.style.backgroundColor = "#d4edda";
                    } else {
                        taskItem.style.backgroundColor = "";
                    }

                    // 完了状態をサーバーに送信
                    fetch(`/task/update-completed`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({ taskId: task.id, completed: completed }),
                    })
                        .then(response => {
                            if (!response.ok) {
                                console.error("タスクの更新に失敗しました。");
                                alert("タスクの更新に失敗しました。");
                            }
                        })
                        .catch(error => {
                            console.error("サーバーエラー:", error);
                            alert("サーバーエラーが発生しました。");
                        });
                });

                const taskDueDate = document.createElement("p");
                taskDueDate.textContent = `期限: ${new Date(task.dueDateTime).toLocaleString()}`;
                taskDueDate.classList.add("task-due-date");

                const taskDetails = document.createElement("p");
                taskDetails.textContent = `詳細: ${task.details}`;
                taskDetails.classList.add("task-details");

                // 削除ボタンの追加
                const deleteButton = document.createElement("button");
                deleteButton.textContent = "削除";
                deleteButton.classList.add("delete-button");
                deleteButton.addEventListener("click", async () => {
                    try {
                        const response = await fetch(`/delete-task`, {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/x-www-form-urlencoded",
                            },
                            body: new URLSearchParams({ id: task.id }),
                        });

                        if (response.ok) {
                            taskItem.remove();
                            taskDetailsContainer.remove();
                        } else {
                            console.error("削除に失敗しました");
                            alert("削除に失敗しました。");
                        }
                    } catch (error) {
                        console.error("エラーが発生しました:", error);
                        alert("エラーが発生しました。");
                    }
                });

                taskDetailsContainer.appendChild(checkboxLabel);
                taskDetailsContainer.appendChild(taskDueDate);
                taskDetailsContainer.appendChild(taskDetails);
                taskDetailsContainer.appendChild(deleteButton);

                taskItem.addEventListener("click", () => {
                    const isVisible = taskDetailsContainer.style.display === "block";
                    taskDetailsContainer.style.display = isVisible ? "none" : "block";
                });

                taskList.appendChild(taskItem);
                taskList.appendChild(taskDetailsContainer);
            });

            dayCell.appendChild(taskList);
            calendarGrid.appendChild(dayCell);
        }
    }

    async function fetchTasksByDate() {
        try {
            const response = await fetch("/api/tasks-by-date");
            if (!response.ok) {
                throw new Error("タスクの取得に失敗しました");
            }
            const tasksByDate = await response.json();
            renderCalendar(currentDate, tasksByDate);
        } catch (error) {
            console.error("タスク取得エラー:", error);
        }
    }

    prevMonthButton.addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        fetchTasksByDate();
    });

    nextMonthButton.addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        fetchTasksByDate();
    });

    fetchTasksByDate();
});