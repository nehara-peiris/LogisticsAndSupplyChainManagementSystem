$(document).ready(function () {
    $('#save-btn').on('click', function (event) {
        event.preventDefault(); // Prevent default behavior

        // Collect user data
        const user = {
            'email': $('#email').val().trim(),
            'name': $('#name').val().trim(),
            'password': $('#password').val().trim(),
            'role': $('#role').val().trim()
        };

        // Basic input validation
        if (!user.email || !user.name || !user.password || user.role === "Role") {
            alert("Please fill all fields correctly!");
            return;
        }

        // Send AJAX request
        $.ajax({
            url: `http://localhost:8082/api/v1/user/register`,
            method: `POST`,
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify(user),
            success: function (response) {
                alert("User Registered Successfully!");

                if (response && response.data) {
                    let token = response.data.token;
                    console.log("Token:", token);

                    // Store token securely
                    localStorage.setItem("authToken", token);
                    sessionStorage.setItem("authToken", token);

                    // Decode the JWT token to extract user role
                    const decodedToken = jwt_decode(token); // Decode token
                    const userRole = decodedToken.role; // Extract role from token (assuming it's named 'role')

                    // Redirect based on the user's role
                    if (userRole === 'ADMIN') {
                        window.location.href = "adminDashboard.html"; // Admin dashboard
                    } else if (userRole === 'USER') {
                        window.location.href = "userDashboard.html"; // User dashboard
                    } else {
                        alert("Unknown role detected.");
                    }
                } else {
                    console.log("No token received.");
                }
            },
            error: function (err) {
                console.error("Error:", err);
                alert("Error: " + (err.responseJSON?.message || "Something went wrong!"));
            }
        });
    });
});
