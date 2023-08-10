$("document").ready(function () {

    $("#mainImageInput").change(function () {
        if(this.files[0].size > 1024 * 1024) {
            this.setCustomValidity("This image must small than 1MB!")
            this.reportValidity();
        } else {
            this.setCustomValidity("")
            showImageThumbnail(this, $("#mainImageThumbnail"));
        }
    })

    $("input[name=extraImage]").each(function (index) {
        let extraImageInput = $(this)
        extraImageInput.on("change", function () {
            checkFile(extraImageInput[0], index)
        })
    })

    $("a[name=btnRemoveExtra]").each(function (index) {
        btnRemove = $(this)
        btnRemove.on("click", function () {
            removeExtra(index)
        })
    })
})

function checkFile(imageInput, index) {
    console.log(imageInput)
    console.log(index)
    if(imageInput.files[0].size > 1024 * 1024) {
        imageInput.setCustomValidity("This image must small than 1MB!")
        imageInput.reportValidity();
    } else {
        imageInput.setCustomValidity("")
        showImageThumbnail(imageInput, $("#extraImageThumbnail" + index));
        $("#extraImageName" + index).attr("value",imageInput.files[0].name)
        if(index >= numberExtraImages) {
            addExtraImage(index + 1)
        }
    }
}


function showImageThumbnail(fileInput, imageThumbnail) {
    console.log(fileInput)
    file = fileInput.files[0]
    reader = new FileReader()

    reader.onload = function (e) {
        imageThumbnail.attr("src", e.target.result)
    }

    reader.readAsDataURL(file)
}
function addExtraImage(index) {

    html = `<div id="extraImageDiv${index}">
      <div class="m-2 row">
        <div id="headerExtra${index}">
          <label class="col-form-label">Extra Image${index + 1}:</label>
        </div>
        <img id="extraImageThumbnail${index}" src="${image_default}" class="img-fluid img-thumbnail" style="width: 350px; height: 200px; object-fit: contain">
      </div>
      <input name="extraImage" id="extraImage${index}" type="file" class="form-control" accept="image/jpeg, image/png" onchange="checkFile(this, ${index})">
    </div>`

    removeTag = `<a href="javascript:removeExtra(${index - 1})" class="fa-solid fa-remove text-dark float-end text-decoration-none"></a>`

    $("#imagesDiv").append(html)
    $(`#headerExtra${index - 1}`).append(removeTag)
}

function removeExtra(index) {
    $("#extraImageDiv" + index).remove()
}