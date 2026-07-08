async function loadCompanies() {

    const res = await fetch("/products/companies");
    const companies = await res.json();

    const datalist = document.getElementById("productCompanies");
    datalist.innerHTML = "";

    companies.forEach(company => {

        const option = document.createElement("option");
        option.value = company;

        datalist.appendChild(option);
    });
}

async function loadProductTypes() {

    const res = await fetch("/products/types");
    const companies = await res.json();

    const datalist = document.getElementById("productTypes");
    datalist.innerHTML = "";

    companies.forEach(company => {

        const option = document.createElement("option");
        option.value = company;

        datalist.appendChild(option);
    });
}

let editingProductId = null;

async function addProduct() {

    if (editingProductId != null) {
        await editProduct(editingProductId);
        return;
    }

    const res = await fetch("/products/addproduct", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            productName: document.getElementById("productName").value,
            productType: document.getElementById("productType").value,
            productCompany: document.getElementById("productCompany").value,
            productPrice: document.getElementById("productPrice").value
        })
    });

    const data = await res.json();

    if (data.valid) {
        closeAddProductModal();
        await getAllProducts();
    } else {
        alert(data.message);
    }
}

async function getAllProducts() {

    const res = await fetch("/products/getproducts");
    const data = await res.json();

    const tableBody = document.getElementById("productTableBody");
    tableBody.innerHTML = "";

    const writeAccess = canWrite("products");
    const addBtn = document.getElementById("addProductBtn");

    if (writeAccess) {
        addBtn.disabled = false;
        addBtn.onclick = showAddProductModal;
        addBtn.innerHTML = "+ Add Product";
    } else {
        addBtn.disabled = true;
        addBtn.onclick = null;
        addBtn.innerHTML = "🔒 Add Product";
    }

    data.forEach(product => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${product.productId}</td>
            <td>${product.productName}</td>
            <td>${product.productType}</td>
            <td>${product.productCompany}</td>
            <td>${product.productPrice}</td>

            <td>
                ${
                    writeAccess
                    ?
                    `
                    <button onclick="showEditProductModal(${product.productId})">
                        Edit
                    </button>

                    <button onclick="deleteProduct(${product.productId})">
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

async function editProduct(productId) {

    const res = await fetch(`/products/editproduct/${productId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            productName: document.getElementById("productName").value,
            productType: document.getElementById("productType").value,
            productCompany: document.getElementById("productCompany").value,
            productPrice: document.getElementById("productPrice").value
        })
    });

    const data = await res.json();

    if (data.valid) {
        closeAddProductModal();
        await getAllProducts();
    } else {
        alert(data.message);
    }
}

async function deleteProduct(productId) {

    const res = await fetch(`/products/deleteproduct/${productId}`, {
        method: "DELETE"
    });

    const data = await res.json();

    if (data.valid) {
        await getAllProducts();
    } else {
        alert(data.message);
    }
}

async function showAddProductModal() {

    editingProductId = null;

    document.getElementById("modalTitle").innerText = "Add Product";
    document.getElementById("submitBtn").innerText = "Add Product";

    document.getElementById("productName").value = "";
    document.getElementById("productType").value = "";
    await loadProductTypes();
    document.getElementById("productCompany").value = "";
    await loadCompanies();
    document.getElementById("productPrice").value = "";

    document.getElementById("addProductModal").style.display = "flex";
}

async function showEditProductModal(productId) {

    const res = await fetch(`/products/getproducts/${productId}`);
    const product = await res.json();

    if (product == null) {
        alert("Product not found");
        return;
    }

    editingProductId = productId;

    document.getElementById("modalTitle").innerText = "Edit Product";
    document.getElementById("submitBtn").innerText = "Update Product";

    document.getElementById("productName").value = product.productName;
    document.getElementById("productType").value = product.productType;
    await loadProductTypes();
    document.getElementById("productCompany").value = product.productCompany;
    await loadCompanies();
    document.getElementById("productPrice").value = product.productPrice;

    document.getElementById("addProductModal").style.display = "flex";
}

function closeAddProductModal() {
    document.getElementById("addProductModal").style.display = "none";
}

window.onclick = function (event) {

    const modal = document.getElementById("addProductModal");

    if (event.target === modal) {
        modal.style.display = "none";
    }
};

getAllProducts();