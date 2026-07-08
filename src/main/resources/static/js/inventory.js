async function loadProducts() {

    const res = await fetch("/products/getproducts");
    const products = await res.json();

    const select = document.getElementById("productId");

    select.innerHTML = `
        <option value="">Select Product</option>
    `;

    products.forEach(product => {

        const option = document.createElement("option");

        option.value = product.productId;

        option.textContent =
            `${product.productName} (${product.productCompany})`;

        select.appendChild(option);
    });
}

let editingInventoryId = null;

async function addInventory() {

    if (editingInventoryId != null) {
        await editInventory(editingInventoryId);
        return;
    }

    const res = await fetch("/inventory/addinventory", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({

            productId: document.getElementById("productId").value,

            quantity: document.getElementById("quantity").value,

            location: document.getElementById("location").value

        })
    });

    const data = await res.json();

    if (data.valid) {

        closeAddInventoryModal();
        await getAllInventory();

    } else {

        alert(data.message);

    }
}

async function getAllInventory() {

    const res = await fetch("/inventory/getinventory");
    const data = await res.json();

    const tableBody = document.getElementById("inventoryTableBody");
    tableBody.innerHTML = "";

    const writeAccess = canWrite("inventory");
    const addBtn = document.getElementById("addInventoryBtn");

    if(writeAccess){
        addBtn.disabled=false;
        addBtn.onclick=showAddInventoryModal;
        addBtn.innerHTML="+ Add Inventory";
    }
    else{
        addBtn.disabled=true;
        addBtn.onclick=null;
        addBtn.innerHTML="🔒 Add Inventory";
    }

    data.forEach(inv=>{
        const row=document.createElement("tr");

        row.innerHTML=`
            <td>${inv.inventoryId}</td>
            <td>${inv.product.productName}</td>
            <td>${inv.product.productCompany}</td>
            <td>${inv.location}</td>
            <td>${inv.quantity}</td>
            <td>${inv.lastUpdated.split("T")[0]}</td>
            <td>${inv.lastUpdated.split("T")[1]}</td>

            <td>

            ${
                writeAccess ?

                `
                <button onclick="showEditInventoryModal(${inv.inventoryId})">
                    Edit
                </button>

                <button onclick="deleteInventory(${inv.inventoryId})">
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

async function editInventory() {

    const res = await fetch(`/inventory/editinventory/${editingInventoryId}`,{

        method:"PUT",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({

            inventoryId:editingInventoryId,

            productId:selectedProductId,

            quantity:document.getElementById("quantity").value,

            location:document.getElementById("location").value

        })

    });

    const data=await res.json();

    if(data.valid){

        closeAddInventoryModal();
        await getAllInventory();

    }

    else{

        alert(data.message);

    }

}

async function deleteInventory(id){

    const res=await fetch(`/inventory/deleteinventory/${id}`,{

        method:"DELETE"

    });

    const data=await res.json();

    if(data.valid){

        await getAllInventory();

    }

    else{

        alert(data.message);

    }

}

async function showAddInventoryModal(){

    editingInventoryId=null;

    document.getElementById("modalTitle").innerText="Add Inventory";
    document.getElementById("submitBtn").innerText="Add Inventory";

    document.getElementById("productId").value="";
    document.getElementById("location").value="";
    document.getElementById("quantity").value="";

    await loadProducts();

    document.getElementById("addInventoryModal").style.display="flex";

}

async function showEditInventoryModal(id){

    const res=await fetch(`/inventory/getinventory/${id}`);

    const inv=await res.json();

    editingInventoryId=id;

    document.getElementById("modalTitle").innerText="Edit Inventory";
    document.getElementById("submitBtn").innerText="Update Inventory";

    selectedProductId=inv.product.productId;

    document.getElementById("location").value=inv.location;

    document.getElementById("quantity").value=inv.quantity;

    await loadProducts();

    document.getElementById("productId").value = inv.product.productId;

    document.getElementById("addInventoryModal").style.display="flex";

}

function closeAddInventoryModal() {
    document.getElementById("addInventoryModal").style.display = "none";
}

getAllInventory();
