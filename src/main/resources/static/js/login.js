async function login() {

    const username =document.getElementById("username").value;
    const password =document.getElementById("password").value;

    const res = await fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userName: username,
            password: password
        })
    });

    const data = await res.json();

    if(data.valid) {
        window.location.href = "/dashboard";
    } else {
        alert(data.message);
    }
}