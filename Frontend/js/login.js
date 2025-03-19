$('#submit-btn').on('click',function () {
    alert('ii')
    let loginUser = {
        'email':$('#email').val(),
        'password':$('#password').val(),
    }
    $.ajax({
        url:`http://localhost:8080/api/v1/auth/authenticate`,
        method:`POST`,
        headers:{
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Content-Type": "application/json"
        },
        data:JSON.stringify(loginUser),
        success:function (){
            alert('Login successfully');
        },
        error:function () {

        }

    });
});
