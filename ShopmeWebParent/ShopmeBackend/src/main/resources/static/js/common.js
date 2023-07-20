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

    customizeDropdownMenu();
    }
)

function showModalDialog(title, body) {
    $("#modalTitle").text(title);
    $("#modalBody").text(body);
    $("#modalDialog").modal("show")
}

function customizeDropdownMenu() {
    $(" .navbar .dropdown").hover(
        function () {
            $(this).find(".dropdown-menu").first().stop(true, true).delay(250).slideDown();
        },
        function () {
            $(this).find(".dropdown-menu").first().stop(true, true).delay(100).slideUp()
        }
    )

    $(".dropdown > a").on("click", function (e) {
        location.href = this.href;
    })
}

