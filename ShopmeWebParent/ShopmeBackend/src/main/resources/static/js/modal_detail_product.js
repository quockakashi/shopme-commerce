const path = "/ShopmeAdmin";
$('document').ready(function () {
    $(".btn-details").on('click', function (e) {
        e.preventDefault();
        let url = $(this).attr('href');
        $.get(url, {}, function (response) {
            //     overview section
            $('#nameDetail').attr('value', response.name);
            $('#aliasDetail').attr('value', response.alias);
            $('#brandDetail').attr('value', response.brand.name);
            $('#categoryDetail').attr('value', response.category.name)
            if(response.enabled == true)
                $('#enabledDetail').attr('checked', 'checked')
            if(response.inStock == true)
                $('#inStockDetail').attr('checked', 'checked')
            $('#priceDetail').attr('value', response.price);
            $('#costDetail').attr('value', response.cost);
            $('#discountDetail').attr('value', response.discountPercent);
            let updatedTime = moment(response.updatedTime).format('DD-MM-YYYY HH:mm:ss');
            $('#updatedDetail').text(updatedTime);

            //***** DESCRIPTION SECTION
            const shortDescription = $('#shortDesDetail');
            let fullDescription = $('#longDesDetail');
            shortDescription.empty();
            fullDescription.empty();
            shortDescription.append(response.shortDescription);
            fullDescription.append(response.fullDescription);

            //***** IMAGE SECTION
            const imagesDetail = $('#imagesDetail');
            imagesDetail.empty();
            imagesDetail.append(
                `<div class="col-3 border p-2"
                         style="display: flex; flex-direction: column; justify-content: center; text-align: center">
                        <p>Main image</p>
                        <img id="mainImageDetail" alt="main-image" src="${path + response.mainImagePath}"
                             style="max-width: 150px; max-height: 200px; object-fit: contain">
                    </div>`
            );
            let i = 1;

            response.images.forEach(img => {
                imagesDetail.append(
                    `<div class="col-3 border p-2"
                             style="display: flex; flex-direction: column; justify-content: center; text-align: center">
                            <p>Extra Image ${i}</p>
                            <img alt="extra image + ${i}" src="${path + img.imagePath}" style="max-width: 150px; max-height: 200px; object-fit: contain">
                        </div>`
                );
                i++;
            })

            // DETAILS
            $('#bodyDetails').empty();
            if(response.details.length > 0) {
                response.details.forEach(detail => {
                    $('#bodyDetails').append(
                        `<tr>
                            <td>${detail.name}</td>
                            <td>${detail.value}</td>
                        </tr>`
                    )
                });
            }

            //******* SHIPPING
            $('#lengthDetail').attr('value', response.length);
            $('#widthDetail').attr('value', response.width);
            $('#heightDetail').attr('value', response.height);
            $('#weightDetail').attr('value', response.weight);

            // ****** SHOW MODAL
            $("#modalDetail").modal("show")

        })
    })
})