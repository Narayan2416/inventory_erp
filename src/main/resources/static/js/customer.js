let editingCustomerId = null;

async function addCustomer() {

    if (editingCustomerId != null) {
        await editCustomer(editingCustomerId);
        return;
    }

    const res = await fetch("/customers/addcustomer", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            customerName: document.getElementById("customerName").value,
            customerEmail: document.getElementById("customerEmail").value,
            customerPhone: document.getElementById("customerPhone").value,
            addressLine: document.getElementById("addressLine").value,
            city: document.getElementById("city").value,
            state: document.getElementById("state").value,
            pinCode: document.getElementById("pinCode").value
        })
    });

    const data = await res.json();

    if (data.valid) {
        closeAddCustomerModal();
        await getAllCustomers();
    } else {
        alert(data.message);
    }
}

async function getAllCustomers() {

    const res = await fetch("/customers/getcustomers");
    const data = await res.json();

    const tableBody = document.getElementById("customerTableBody");
    tableBody.innerHTML = "";

    const writeAccess = canWrite("customers");
    const addBtn = document.getElementById("addCustomerBtn");

    if (writeAccess) {
        addBtn.disabled = false;
        addBtn.onclick = showAddCustomerModal;
        addBtn.innerHTML = "+ Add Customer";
    } else {
        addBtn.disabled = true;
        addBtn.onclick = null;
        addBtn.innerHTML = "🔒 Add Customer";
    }

    data.forEach(customer => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${customer.customerId}</td>
            <td>${customer.customerName}</td>
            <td>${customer.customerEmail ?? ""}</td>
            <td>${customer.customerPhone}</td>
            <td>${customer.addressLine}</td>
            <td>${customer.city}</td>
            <td>${customer.state}</td>
            <td>${customer.pinCode}</td>

            <td>
                ${
                    writeAccess
                    ?
                    `
                    <button onclick="showEditCustomerModal(${customer.customerId})">
                        Edit
                    </button>

                    <button onclick="deleteCustomer(${customer.customerId})">
                        Delete
                    </button>
                    `
                    :
                    `
                    <button disabled>🔒 Edit</button>
                    <button disabled>🔒 Delete</button>
                    `
                }
            </td>
        `;

        tableBody.appendChild(row);
    });

}

async function editCustomer(customerId) {

    const res = await fetch(`/customers/editcustomer/${customerId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            customerName: document.getElementById("customerName").value,
            customerEmail: document.getElementById("customerEmail").value,
            customerPhone: document.getElementById("customerPhone").value,
            addressLine: document.getElementById("addressLine").value,
            city: document.getElementById("city").value,
            state: document.getElementById("state").value,
            pinCode: document.getElementById("pinCode").value
        })
    });

    const data = await res.json();

    if (data.valid) {
        closeAddCustomerModal();
        await getAllCustomers();
    } else {
        alert(data.message);
    }
}

async function deleteCustomer(customerId) {

    const res = await fetch(`/customers/deletecustomer/${customerId}`, {
        method: "DELETE"
    });

    const data = await res.json();

    if (data.valid) {
        await getAllCustomers();
    } else {
        alert(data.message);
    }
}

async function showAddCustomerModal() {

    editingCustomerId = null;

    document.getElementById("modalTitle").innerText = "Add Customer";
    document.getElementById("submitBtn").innerText = "Add Customer";

    document.getElementById("customerName").value = "";
    document.getElementById("customerEmail").value = "";
    document.getElementById("customerPhone").value = "";
    document.getElementById("addressLine").value = "";
    document.getElementById("city").value = "";
    document.getElementById("state").value = "";
    document.getElementById("pinCode").value = "";

    document.getElementById("addCustomerModal").style.display = "flex";
}

async function showEditCustomerModal(customerId) {

    const res = await fetch(`/customers/getcustomers/${customerId}`);
    const customer = await res.json();

    if (customer == null) {
        alert("Customer not found");
        return;
    }

    editingCustomerId = customerId;

    document.getElementById("modalTitle").innerText = "Edit Customer";
    document.getElementById("submitBtn").innerText = "Update Customer";

    document.getElementById("customerName").value = customer.customerName;
    document.getElementById("customerEmail").value = customer.customerEmail;
    document.getElementById("customerPhone").value = customer.customerPhone;
    document.getElementById("addressLine").value = customer.addressLine;
    document.getElementById("city").value = customer.city;
    document.getElementById("state").value = customer.state;
    document.getElementById("pinCode").value = customer.pinCode;

    document.getElementById("addCustomerModal").style.display = "flex";
}

function closeAddCustomerModal() {
    document.getElementById("addCustomerModal").style.display = "none";
}

window.onclick = function (event) {

    const modal = document.getElementById("addCustomerModal");

    if (event.target === modal) {
        modal.style.display = "none";
    }
};

getAllCustomers();