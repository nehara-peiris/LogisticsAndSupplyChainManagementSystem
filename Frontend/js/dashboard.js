$('#user-dashboard').on('click',function () { 
    $.ajax({
        url:`http://localhost:8082/api/v1/admin/test1`,
        method:`GET`,
        headers:{
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Content-Type": "application/json"
        },
    
        success:function (resp){
            window.location.href = "adminDashboard.html";
        },
        error:function () {

        }

    });
})
$('#admin-dashboard').on('click',function () { 
    $.ajax({
        url:`http://localhost:8082/api/v1/admin/test2`,
        method:`GET`,
        headers:{
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Content-Type": "application/json"
        },
    
        success:function (resp){
            window.location.href = "userDashboard.html";
        }, 
        error:function (){

        }

    });
})