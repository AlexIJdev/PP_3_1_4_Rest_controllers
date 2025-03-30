getInfoOfUser();

function getInfoOfUser() {
    // запрос информации о пользователе через REST API
    fetch("/user/info")
        .then(response => {
                if (!response.ok) {
                    console.error("Couldn\'t upload user data.");
                }
                response.json()
                    .then(user => {
                        // корректное представление ролей пользователя
                        let roles = user.roles.map(role => " " + role.name.replaceAll("ROLE_", ""));

                        // отображение данных о пользователе в HEADER
                        document.getElementById("user-email").textContent = user.email;
                        document.getElementById("user-roles").textContent = roles;

                        // отображение данных о пользователе в MAIN (в таблице)
                        document.getElementById("id").textContent = user.id;
                        document.getElementById("firstName").textContent = user.firstName;
                        document.getElementById("lastName").textContent = user.lastName;
                        document.getElementById("age").textContent = user.age;
                        document.getElementById("email").textContent = user.email;
                        document.getElementById("roles").textContent = roles;
                    })
                    .catch(error => console.error("Error when uploading user data: ", error));
            }
        )
}