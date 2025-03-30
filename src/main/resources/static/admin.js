getInfoOfAdmin();
getInfoOfUsers();
getInfoOfRoles();
addNewUser();

// получение данных о текущем пользователе
function getInfoOfAdmin() {
    // запрос информации о пользователе через REST API
    fetch("/admin/info")
        .then(response => {
            if (response.ok) {
                response.json()
                    .then(user => {
                        // корректное представление ролей пользователя
                        let roles = user.roles.map(role => " " + role.name.replaceAll("ROLE_", ""));

                        // отображение данных о пользователе в HEADER
                        document.getElementById("current_user-email").textContent = user.email;
                        document.getElementById("current_user-roles").textContent = roles;

                        // отображение данных о пользователе в USER PANEL (в таблице)
                        document.getElementById("current_id").textContent = user.id;
                        document.getElementById("current_firstName").textContent = user.firstName;
                        document.getElementById("current_lastName").textContent = user.lastName;
                        document.getElementById("current_age").textContent = user.age;
                        document.getElementById("current_email").textContent = user.email;
                        document.getElementById("current_roles").textContent = roles;
                    }).catch(error => console.error("Error when uploading user data: ", error));
            } else {
                console.error("Couldn\'t upload data of user.");
            }
        })
}

// получение данных обо всех пользователях
function getInfoOfUsers() {
    // запрос информации о пользователях через REST API
    fetch("/admin/allUsers")
        .then(response => {
            if (response.ok) {
                response.json()
                    .then(users => {
                        // переменные для данных о текущем пользователе и списке его ролей
                        let current_user = "";
                        let roles_of_current_user = "";
                        const tableOfUsers = document.getElementById("tableOfUsers");

                        // получение данных о текущем пользователе и списке его ролей
                        for (let user of users) {
                            roles_of_current_user = user.roles.map(role => " " + role.name.replaceAll("ROLE_", ""));
                            current_user +=
                                `<tr>
                                    <td>${user.id}</td>
                                    <td>${user.firstName}</td>
                                    <td>${user.lastName}</td>
                                    <td>${user.age}</td>
                                    <td>${user.email}</td>
                                    <td>${roles_of_current_user}</td>
                                    <td>
                                        <!-- открытие модального окна обновления пользователя -->                         
                                        <button data-bs-target="#updateModal" data-bs-toggle="modal" type="button" 
                                        class="btn btn-info text-white" onclick="updateUser(${user.id})">Edit</button>                                
                                    </td>
                                    <td>
                                        <!-- открытие модального окна удаления пользователя -->
                                        <button data-bs-target="#deleteModal" data-bs-toggle="modal" type="button" 
                                        class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                                    </td>
                                </tr>`;
                        }
                        tableOfUsers.innerHTML = current_user;
                    })
            } else {
                console.error("Couldn\'t upload data of user\'s.");
            }
        })
}

// получение данных обо всех ролях пользователей
function getInfoOfRoles() {
    // запрос информации о ролях пользователей через REST API
    fetch("/admin/allRoles")
        .then(response => {
            if (response.ok) {
                response.json()
                    .then(roles => {
                        // переменные для данных о ролях пользователей для форм создания / обновления
                        const forNewUserRoles = document.getElementById("forNewUserRoles");
                        const forUpdateUserRoles = document.getElementById("forUpdateUserRoles");

                        // получение данных о ролях пользователей
                        forNewUserRoles.innerHTML = "";
                        forUpdateUserRoles.innerHTML = "";

                        roles.forEach(role => {
                            const newOption = document.createElement("option");
                            newOption.value = role.id;
                            newOption.text = role.name.replace("ROLE_", "");
                            forNewUserRoles.appendChild(newOption);

                            const updateOption = document.createElement("option");
                            updateOption.value = role.id;
                            updateOption.text = role.name.replace("ROLE_", "");
                            forUpdateUserRoles.appendChild(updateOption);
                        })
                    })
            } else {
                console.error("Couldn\'t upload data of role\'s.");
            }
        })
}

// добавление нового пользователя
function addNewUser() {
    document.getElementById("new-user-form").addEventListener("submit", (e) => {
        e.preventDefault();
        // получение выбранной / выбранных роли / ролей из формы создания пользователя
        let forNewUserRoles = document.getElementById("forNewUserRoles");

        // преобразование полученных ролей в формат, пригодный для JSON
        let roles = [];
        for (let i = 0; i < forNewUserRoles.options.length; i++) {
            if (forNewUserRoles.options[i].selected) {
                roles.push({
                    id: forNewUserRoles.options[i].value,
                    name: "ROLE_" + forNewUserRoles.options[i].innerHTML
                })
            }
        }

        // передача информации о новом пользователе через REST API
        fetch("/admin/new", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                firstName: document.getElementById("firstName").value,
                lastName: document.getElementById("lastName").value,
                age: document.getElementById("age").value,
                email: document.getElementById("email").value,
                password: document.getElementById("password").value,
                roles: roles
            })
        }).then((response) => {
            if (!response.ok) {
                // возвращение json из GlobalExceptionHandler с обработкой ошибки
                return response.json().then(errorData => {
                    throw new Error(errorData.message);
                });
            }
            // обновление таблицы всех пользователей
            getInfoOfUsers();

            // переход на вкладку с таблицей всех пользователей
            document.getElementById("users-table-tab").click();
        }).catch(error => {
            if (error.message === "A user with the same username already exists!") {
                document.getElementById("error-email-message-for-new").style.display = "block";
            }
            console.log("There was a problem in the process of creating a new user: " + error.message);
        })
    })
}

// обновление пользователя
function updateUser(id) {
    // запрос текущей информации от сервера о пользователе, у которого происходит обновление данных
    fetch("/admin/allUsers/" + id)
        .then(response => {
            if (response.ok) {
                response.json()
                    .then(async user => {
                        // получение данных о пользователе, у которого происходит обновление данных
                        document.getElementById("update-user-id").value = user.id;
                        document.getElementById("update-user-firstName").value = user.firstName;
                        document.getElementById("update-user-lastName").value = user.lastName;
                        document.getElementById("update-user-age").value = user.age;
                        document.getElementById("update-user-email").value = user.email;
                        document.getElementById("update-user-password").value = user.password;
                    })
            } else {
                console.error("Couldn\'t upload user\'s data.");
            }
        })

    // работа с формой обновления пользователя
    document.getElementById("update-user-form").addEventListener("submit", (e) => {
        e.preventDefault();

        // получение выбранной / выбранных роли / ролей из формы обновления пользователя
        let forUpdateUserRoles = document.getElementById("forUpdateUserRoles");

        // преобразование полученных ролей в формат, пригодный для JSON
        let roles = [];
        for (let i = 0; i < forUpdateUserRoles.options.length; i++) {
            if (forUpdateUserRoles.options[i].selected) {
                roles.push({
                    id: forUpdateUserRoles.options[i].value,
                    name: "ROLE_" + forUpdateUserRoles.options[i].innerHTML
                })
            }
        }

        // отправка на сервер обновленной информации о пользователе
        fetch("/admin/update", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: document.getElementById("update-user-id").value,
                firstName: document.getElementById("update-user-firstName").value,
                lastName: document.getElementById("update-user-lastName").value,
                age: document.getElementById("update-user-age").value,
                email: document.getElementById("update-user-email").value,
                password: document.getElementById("update-user-password").value,
                roles: roles
            })
        }).then((response) => {
            if (!response.ok) {
                // возвращение json из GlobalExceptionHandler с обработкой ошибки
                return response.json().then(errorData => {
                    throw new Error(errorData.message);
                });
            }
            // обновление таблицы всех пользователей
            getInfoOfUsers();
        }).catch(error => {
            if (error.message === "A user with the same username already exists!") {
                document.getElementById("error-email-message-for-update").style.display = "block";
            }
            console.log("There was a problem in the process of updating this user: " + error.message);
        })
    })
}

// удаление пользователя
function deleteUser(id) {
    // запрос текущей информации от сервера о пользователе, которого необходимо удалить
    fetch("/admin/allUsers/" + id)
        .then(response => {
            if (response.ok) {
                response.json()
                    .then(async user => {
                        // получение данных о пользователе, которого необходимо удалить
                        document.getElementById("delete-user-id").value = user.id;
                        document.getElementById("delete-user-firstName").value = user.firstName;
                        document.getElementById("delete-user-lastName").value = user.lastName;
                        document.getElementById("delete-user-age").value = user.age;
                        document.getElementById("delete-user-email").value = user.email;
                        document.getElementById("delete-user-password").value = user.password;
                        const forDeleteUserRoles = document.getElementById("forDeleteUserRoles");
                        forDeleteUserRoles.innerHTML = "";
                        user.roles.forEach(role => {
                            const deleteOption = document.createElement("option");
                            deleteOption.value = role.id;
                            deleteOption.text = role.name.replace("ROLE_", "");
                            forDeleteUserRoles.appendChild(deleteOption);
                        })
                    })
            } else {
                console.error("Couldn\'t delete user.");
            }
        })

    // работа с формой удаления пользователя
    document.getElementById("delete-user-form").addEventListener("submit", (e) => {
        e.preventDefault();

        // получение выбранной / выбранных роли / ролей из формы удаления пользователя
        let forDeleteUserRoles = document.getElementById("forDeleteUserRoles");

        // преобразование полученных ролей в формат, пригодный для JSON
        let roles = [];
        for (let i = 0; i < forDeleteUserRoles.options.length; i++) {
            if (forDeleteUserRoles.options[i].selected) {
                roles.push({
                    id: forDeleteUserRoles.options[i].value,
                    name: "ROLE_" + forDeleteUserRoles.options[i].innerHTML
                })
            }
        }

        // отправка на сервер обновленной информации о пользователе
        fetch("/admin/delete", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: document.getElementById("delete-user-id").value,
                firstName: document.getElementById("delete-user-firstName").value,
                lastName: document.getElementById("delete-user-lastName").value,
                age: document.getElementById("delete-user-age").value,
                email: document.getElementById("delete-user-email").value,
                password: document.getElementById("delete-user-password").value,
                roles: roles
            })
        }).then((response) => {
            if (!response.ok) {
                // возвращение json из GlobalExceptionHandler с обработкой ошибки
                return response.json().then(errorData => {
                    throw new Error(errorData.message);
                });
            }
            // обновление таблицы всех пользователей
            getInfoOfUsers();
        }).catch(error => {
            console.log("There was a problem in the process of deleting this user: " + error.message);
        })
    })
}

// очистка полей для формы создания нового пользователя перед использованием в рамках текущей сессии
document.getElementById("new-user-tab").addEventListener("click", async function (event) {
    event.preventDefault();
    document.getElementById("new-user-form").reset();
})