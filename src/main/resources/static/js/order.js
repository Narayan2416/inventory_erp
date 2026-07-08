let editingOrderId = null;
let selectedOrderId = null;

let products = [];
let customers = [];
let orderItems = [];
let orders = [];

// =============================================
// Customers
// =============================================

async function loadCustomers() {

    const res = await fetch("/customers/getcustomers");
    customers = await res.json();

    const customer = document.getElementById("customer");

    customer.innerHTML = "";

    customers.forEach(c => {

        customer.innerHTML += `

            <option value="${c.customerId}">

                ${c.customerName}

            </option>

        `;

    });

}


async function loadProducts() {

    const res = await fetch("/products/getproducts");

    products = await res.json();

}

function addProductRow() {

    orderItems.push({

        productId: "",
        quantity: 1,
        price: 0,
        total: 0

    });

    renderItems();

}

// =============================================

function removeProductRow(index) {

    orderItems.splice(index,1);

    renderItems();

}

// =============================================

function renderItems() {

    const body = document.getElementById("orderItemBody");

    body.innerHTML = "";

    orderItems.forEach((item,index)=>{

        let options = `<option value="">Select Product</option>`;

        products.forEach(product=>{

            options += `

            <option

                value="${product.productId}"

                ${product.productId==item.productId ? "selected":""}

            >

                ${product.productName}

            </option>

            `;

        });

        body.innerHTML += `

        <tr>

        <td>

        <select

        onchange="changeProduct(${index},this.value)">

        ${options}

        </select>

        </td>

        <td>

        ₹${item.price}

        </td>

        <td>

        <input

        type="number"

        value="${item.quantity}"

        min="1"

        onchange="changeQuantity(${index},this.value)">

        </td>

        <td>

        ₹${item.total}

        </td>

        <td>

        <button

        onclick="removeProductRow(${index})">

        Delete

        </button>

        </td>

        </tr>

        `;

    });

    calculateGrandTotal();

}

// =============================================

function changeProduct(index,id){

    const product = products.find(

        p=>p.productId==id

    );

    if(product==null)
        return;

    orderItems[index].productId = product.productId;

    orderItems[index].price = product.productPrice;

    orderItems[index].total =

        product.productPrice *

        orderItems[index].quantity;

    renderItems();

}

// =============================================

function changeQuantity(index,qty){

    qty = Number(qty);

    if(qty<=0)
        qty=1;

    orderItems[index].quantity = qty;

    orderItems[index].total =

        qty *

        orderItems[index].price;

    renderItems();

}

// =============================================

function calculateGrandTotal(){

    let total = 0;

    orderItems.forEach(item=>{

        total += item.total;

    });

    document.getElementById("grandTotal").innerText = total;

}

// =============================================
// Add Order
// =============================================

async function addOrder(){

    if(editingOrderId!=null){

        editOrder(editingOrderId);

        return;

    }

    const res = await fetch("/orders/addorder",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({

            customerId:document.getElementById("customer").value,
            products:orderItems.map(item=>({
                productId:item.productId,
                quantity:item.quantity,
            }))
        })
    });

    const data = await res.json();
    if(data.valid){
        closeAddOrderModal();
        getAllOrders();
    }
    else{
        alert(data.message);
    }

}

// =============================================

async function showAddOrderModal(){

    editingOrderId = null;

    orderItems = [];

    document.getElementById("customer").selectedIndex = 0;


    await loadCustomers();

    await loadProducts();

    addProductRow();

    document.getElementById("addOrderModal").style.display = "flex";

}

// =============================================

function closeAddOrderModal(){

    document.getElementById("addOrderModal").style.display = "none";

}

window.onclick=function(event){

    const modal=document.getElementById("addOrderModal");

    if(event.target===modal){

        closeAddOrderModal();

    }

}

// =============================================
// Get All Orders
// =============================================

async function getAllOrders() {

    const res = await fetch("/orders/getorders");
    orders = await res.json();

    const tableBody = document.getElementById("ordersTableBody");
    tableBody.innerHTML = "";

    const writeAccess = canWrite("orders");
    const addBtn = document.getElementById("addOrderBtn");

    if (writeAccess) {

        addBtn.disabled = false;
        addBtn.onclick = showAddOrderModal;
        addBtn.innerHTML = "+ Add Order";

    } else {

        addBtn.disabled = true;
        addBtn.onclick = null;
        addBtn.innerHTML = "🔒 Add Order";

    }

    orders.forEach(order => {

        let total = 0;

        if (order.orderItems) {

            order.orderItems.forEach(item => {

                total += item.itemTotal;

            });

        }

        const row = document.createElement("tr");

        row.onclick = () => {

            selectedOrderId = order.orderId;

            showOrderItems(order);

        };

        row.innerHTML = `

            <td>${order.orderId}</td>

            <td>${order.customer.customerName}</td>

            <td>${order.orderDate}</td>

            <td>

                ${order.status ? "Processed" : "Not Yet Processed"}

            </td>

            <td>

                ₹${total}

            </td>

            <td>

                ${
                    writeAccess
                    ?

                    `

                    <button
                    onclick="event.stopPropagation();showEditOrderModal(${order.orderId})">

                    Edit

                    </button>

                    <button
                    onclick="event.stopPropagation();deleteOrder(${order.orderId})">

                    Delete

                    </button>

                    `

                    :

                    `

                    <button disabled>

                    🔒 Edit

                    </button>

                    <button disabled>

                    🔒 Delete

                    </button>

                    `

                }

            </td>

        `;

        tableBody.appendChild(row);

    });

}



// =============================================
// Delete Order
// =============================================

async function deleteOrder(orderId) {

    const res = await fetch(`/orders/deleteorder/${orderId}`, {
            method: "DELETE"
        }
    );

    const data = await res.json();

    if (data.valid) {

        document.getElementById("orderItemsTableBody").innerHTML = "";

        await getAllOrders();

    }

    else {

        alert(data.message);

    }

}



// =============================================
// Edit Order
// =============================================

async function editOrder(orderId) {
    const res = await fetch(`/orders/editorder/${orderId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            customerId: document.getElementById("editCustomer").value,
            status: document.getElementById("editStatus").value
        })
    });

    const data = await res.json();
    if (data.valid) {
        closeEditOrderModal();
        await getAllOrders();
    }
    else {
        alert(data.message);
    }
}



// =============================================
// Show Edit Order
// =============================================

async function showEditOrderModal(orderId) {

    const res = await fetch(

        `/orders/getorders/${orderId}`

    );

    const order = await res.json();

    if (order == null) {

        alert("Order not found");

        return;

    }

    editingOrderId = orderId;

    await loadCustomers();

    document.getElementById("editCustomer").value =

        order.customer.customerId;

    document.getElementById("editStatus").value =

        order.status;

    document.getElementById("editOrderModal").style.display = "flex";

}

function showOrderItems(order) {

    const tableBody = document.getElementById("orderItemsTableBody");

    tableBody.innerHTML = "";

    const writeAccess = canWrite("orders");

    order.orderItems.forEach(item => {

        const row = document.createElement("tr");

        row.innerHTML = `

            <td>${item.orderItemId}</td>

            <td>${item.product.productName}</td>

            <td>${item.quantity}</td>

            <td>₹ ${item.product.productPrice}</td>

            <td>₹ ${item.itemTotal}</td>

            <td>

                ${
                    writeAccess
                    ?
                    `
                        <button onclick="showEditOrderItemModal(${item.orderItemId})">
                            Edit
                        </button>

                        <button onclick="deleteOrderItem(${item.orderItemId})">
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



// =============================================
// Close Edit Modal
// =============================================

function closeEditOrderModal() {

    document.getElementById("editOrderModal").style.display = "none";

}

async function loadEditProducts(selectedId = "") {

    await loadProducts();

    const select = document.getElementById("editProduct");

    select.innerHTML = "";

    products.forEach(product => {

        select.innerHTML += `

            <option
                value="${product.productId}"
                ${product.productId == selectedId ? "selected" : ""}
            >

                ${product.productName}

            </option>

        `;

    });

}

async function loadCustomers() {

    const res = await fetch("/customers/getcustomers");

    customers = await res.json();

    const customerSelect = document.getElementById("customer");
    const editCustomerSelect = document.getElementById("editCustomer");

    customerSelect.innerHTML = "";
    editCustomerSelect.innerHTML = "";

    customers.forEach(c => {

        const option = `

            <option value="${c.customerId}">

                ${c.customerName}

            </option>

        `;

        customerSelect.innerHTML += option;
        editCustomerSelect.innerHTML += option;

    });

}

async function updateOrder(){

    await editOrder(editingOrderId);

}

let editingOrderItemId = null;

async function showEditOrderItemModal(orderItemId){

    const res = await fetch(

        `/orderitems/getorderitem/${orderItemId}`

    );

    const item = await res.json();

    editingOrderItemId = orderItemId;

    await loadEditProducts(item.product.productId);

    document.getElementById("editQuantity").value = item.quantity;

    document.getElementById("editItemModal").style.display = "flex";

}

function closeEditItemModal(){

    document.getElementById("editItemModal").style.display = "none";

}

async function updateItem(){

    const res = await fetch(

        `/orderitems/editorderitem/${editingOrderItemId}`,

        {

            method:"PUT",

            headers:{

                "Content-Type":"application/json"

            },

            body:JSON.stringify({

                productId:document.getElementById("editProduct").value,

                quantity:document.getElementById("editQuantity").value

            })

        }

    );

    const data = await res.json();

    if(data.valid){

        closeEditItemModal();

        await getAllOrders();

        const order = orders.find(

            o=>o.orderId==selectedOrderId

        );

        if(order){

            showOrderItems(order);

        }

    }

    else{

        alert(data.message);

    }

}

async function deleteOrderItem(orderItemId){

    if(!confirm("Delete this item?"))
        return;

    const res = await fetch(

        `/orderitems/deleteorderitem/${orderItemId}`,

        {

            method:"DELETE"

        }

    );

    const data = await res.json();

    if(data.valid){

        await getAllOrders();

        const order = orders.find(

            o=>o.orderId==selectedOrderId

        );

        if(order){

            showOrderItems(order);

        }

    }

    else{

        alert(data.message);

    }

}

window.addEventListener("click",function(event){

    const editItemModal = document.getElementById("editItemModal");

    if(event.target===editItemModal){

        closeEditItemModal();

    }

});

getAllOrders();