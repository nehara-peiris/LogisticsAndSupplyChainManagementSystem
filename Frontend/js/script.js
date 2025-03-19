$('#save-btn').on('click',function () {
    const user = {
        'email':$('#email').val(),
        'name' : $('#name').val(),
        'password':$('#password').val(),
        'role':$('#role').val()
    } 
    $.ajax({
        url:`http://localhost:8080/api/v1/user/register`,
        method:`POST`,
        headers:{
            "content-type":`application/json`
        },
        data:JSON.stringify(user),
        success:function(response){
            alert("User Saved");
            if (response && response.data) { 
                let token = response.data.token; 
                console.log("Token:", token); 

               
                localStorage.setItem("authToken", token);

               
                sessionStorage.setItem("authToken", token);
            } else {
                console.log("No token received.");
            }
            
        },
        erro:function(err){
            console.log(err);
        }
    });
 });




 