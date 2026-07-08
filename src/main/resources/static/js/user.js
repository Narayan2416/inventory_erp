async function addUser() {
    if(editingUserId!=null){
        await editUser(editingUserId);
        return ;
    }

    const username =document.getElementById("username").value;
    const password =document.getElementById("password").value;
    const email =document.getElementById("email").value;
    const role =document.getElementById("role").value;

    const res = await fetch("/users/adduser", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userName: username,
            password: password,
            userEmail: email,
            roleId: role
        })
    });

    const data = await res.json();
    if(data.valid){
        closeAddUserModal();
        await getAllUsers();
    }
}

async function getAllUsers() {

    const res = await fetch("/users/getusers");
    const data = await res.json();

    const tableBody = document.getElementById("userTableBody");

    tableBody.innerHTML = "";

    const writeAccess = canWrite("users");
    const addBtn = document.getElementById("addUserBtn");

    if (writeAccess) {
        addBtn.disabled = false;
        addBtn.onclick = showAddUserModal;
        addBtn.innerHTML = "+ Add User";
    } else {
        addBtn.disabled = true;
        addBtn.onclick = null;
        addBtn.innerHTML = "🔒 Add User";
    }

    data.forEach(user => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${user.userId}</td>
            <td>${user.userName}</td>
            <td>${user.userEmail}</td>
            <td>${user.role.roleName}</td>

            <td>
                ${
                    writeAccess
                    ?
                    `
                    <button onclick="showEditUserModal(${user.userId})">
                        Edit
                    </button>

                    <button onclick="deleteUser(${user.userId})">
                        Delete
                    </button>
                    `
                    :
                    `
                    <button disabled>🔒Edit</button>
                    <button disabled>🔒Delete</button>
                    `
                }
            </td>
        `;

        tableBody.appendChild(row);
    });
}

async function editUser(userId) {

    const res = await fetch(`/users/edituser/${userId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userName: document.getElementById("username").value,
            password: document.getElementById("password").value,
            userEmail: document.getElementById("email").value,
            roleId: document.getElementById("role").value
        })
    });

    const data = await res.json();
    if(data.valid){
        closeAddUserModal();
        await getAllUsers();
    }
    else alert(data.message);
}

async function deleteUser(userId) {

    const res = await fetch(`/users/deleteuser/${userId}`, {
        method: "DELETE"
    });

    const data = await res.json();
    if(data.valid){
        await getAllUsers();
    }
    else alert(data.message);
}

let editingUserId = null;

function showAddUserModal() {

    editingUserId = null;

    document.getElementById("modalTitle").innerText = "Add User";
    document.getElementById("submitBtn").innerText = "Add User";

    document.getElementById("username").required = true;
    document.getElementById("password").required = true;
    document.getElementById("email").required = true;

    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
    document.getElementById("email").value = "";
    document.getElementById("role").selectedIndex = 0;

    document.getElementById("addUserModal").style.display = "flex";
}

async function showEditUserModal(userId) {

    const res = await fetch(`/users/getusers/${userId}`);
    const user = await res.json();

    editingUserId = userId;
    if(user==null){
        alert("User not found");
        return ;
    }

    document.getElementById("modalTitle").innerText = "Edit User";
    document.getElementById("submitBtn").innerText = "Update User";

    document.getElementById("username").required = false;
    document.getElementById("password").required = false;
    document.getElementById("email").required = false;

    document.getElementById("username").value = user.userName;
    document.getElementById("password").value = "";
    document.getElementById("email").value = user.userEmail;
    document.getElementById("role").value = user.role.roleId;

    document.getElementById("addUserModal").style.display = "flex";
}

function closeAddUserModal() {
    document.getElementById("addUserModal").style.display = "none";
}

window.onclick = function(event) {

    const modal = document.getElementById("addUserModal");

    if(event.target === modal){
        modal.style.display = "none";
    }
};

getAllUsers();
