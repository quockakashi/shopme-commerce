
$(document).ready(function () {
    $("a[name=btnRemoveDetail]").each(function (index) {
        removeBtn = $(this)
        removeBtn.on("click", function () {
            $("#detailControl" + index).remove()
        })
    })
})
function addDetailInput() {
    ereserBtn = `<a class="fa-solid fa-eraser text-secondary float-end col-2 my-auto" href="javascript:removeDetail(${numberDetails - 1})"></a>`
    html = `<div class="row" id="detailControl${numberDetails}">
      <div class="col-5">
        <label class=col-form-label>Name</label>
        <input name="nameInput" class="form-control" type="text">
      </div>
      <div class="col-5">
        <label class=col-form-label>Value</label>
        <input name="valueInput" class="form-control" type="text">
      </div>

    </div>
  </div>`
    $(`#detailControl${numberDetails - 1}`).append(ereserBtn)
    $("#detailDiv").append(html)
    numberDetails++
}

function removeDetail(index){
    $("#detailControl" + index).remove()
}