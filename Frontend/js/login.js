$('#submit-btn').on('click', function () {
    // Alert to ensure the event is triggered
    alert('Logging in...');

    let loginUser = {
        'email': $('#email').val(),
        'password': $('#password').val(),
    }

    // Send AJAX request for authentication
    $.ajax({
        url: `http://localhost:8082/api/v1/auth/authenticate`,
        method: `POST`,
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(loginUser),
        success: function (response) {
            // Assuming the response contains a JWT token
            let token = response.token;
            if (token) {
                // Store token securely
                localStorage.setItem("authToken", token);
                sessionStorage.setItem("authToken", token);

                // Decode the token to check user role
                const decodedToken = jwt_decode(token); // Decode token
                const userRole = decodedToken.role; // Assuming role is in the payload

                // Redirect based on the user's role
                if (userRole === 'ADMIN') {
                    window.location.href = "adminDashboard.html"; // Redirect to admin dashboard
                } else if (userRole === 'USER') {
                    window.location.href = "userDashboard.html"; // Redirect to user dashboard
                } else {
                    alert("Unknown role detected.");
                }
            } else {
                alert("Token not received.");
            }
        },
        error: function (err) {
            console.error("Login Error:", err);
            alert("Login failed. Please try again.");
        }
    });
});
