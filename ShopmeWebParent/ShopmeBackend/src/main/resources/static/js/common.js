// day la lenh xu ly khi nhan vao nut button loggout, mot popup hien ra de
// xac nhan nguoi dung co muon thuc su loggout
$(document).ready( function () {
    $("#logoutBtn").on("click", function (e) {
        e.preventDefault()
        body = "Do you really want to loggout?";
        showModalDialog("Logout Confirm", body);
        $("#continueModalBtn").on("click", function (e) {
            $("#logoutForm").submit()
        })
    })
    }
)

