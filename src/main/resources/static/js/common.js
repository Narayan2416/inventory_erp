async function loadMenu() {

    const response = await fetch("/dashboard/menu");
    const permissions = await response.json();

    sessionStorage.setItem(
        "permissions",
        JSON.stringify(permissions)
    );

    const menu = document.getElementById("menu");

    menu.innerHTML = "";

    Object.entries(permissions).forEach(([module, access]) => {

        if (access === null) return;

        const li = document.createElement("li");
        const a = document.createElement("a");

        a.href = "/dashboard/" + module;
        a.innerText =
            module.charAt(0).toUpperCase() +
            module.slice(1);

        li.appendChild(a);
        menu.appendChild(li);
    });
}

function canRead(module) {
    const permissions =
        JSON.parse(sessionStorage.getItem("permissions"));

    return permissions[module] != null;
}

function canWrite(module) {
    const permissions =
        JSON.parse(sessionStorage.getItem("permissions"));

    return permissions[module] === "write";
}

function searchTable(inputId, tableBodyId) {

    const searchText = document
        .getElementById(inputId)
        .value
        .toLowerCase()
        .trim();

    const rows = document.querySelectorAll(`#${tableBodyId} tr`);

    rows.forEach(row => {

        let found = false;

        for (const cell of row.cells) {

            if (cell.innerText.toLowerCase().includes(searchText)) {
                found = true;
                break;
            }
        }

        row.style.display = found ? "" : "none";
    });
}

loadMenu();