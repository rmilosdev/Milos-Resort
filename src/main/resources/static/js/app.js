$(function () {

    $message = $('.flash');
    $message1 =  $('.flash1');

    $message.delay(4000).fadeOut();
    $message1.delay(4000).fadeOut();

    $passInput = $('#password-input');
    $passInput.val('');

    //Brisanje korisnika iz baze

    $dugmeObrisi = $('.obrisi');

    $dugmeObrisi.on('click', function (e) {
        $("#dialog").dialog({
            autoOpen: false,
            modal: true
        });

        e.preventDefault();
        var targetUrl = $(this).attr("href");

        $("#dialog").dialog({
            buttons : {
                "Potvrdi" : function() {
                    window.location.href = targetUrl;
                },
                "Ponisti" : function() {
                    $(this).dialog("close");
                }
            }
        });
        $("#dialog").dialog("open");
    });

    //Brisanje hotela iz baze

    $dugmeObrisi1 = $('.obrisiHotel');

    $dugmeObrisi1.on('click', function (e) {
        $("#dialog").dialog({
            autoOpen: false,
            modal: true
        });

        e.preventDefault();
        var targetUrl = $(this).attr("href");

        $("#dialog").dialog({
            buttons : {
                "Potvrdi" : function() {
                    window.location.href = targetUrl;
                },
                "Ponisti" : function() {
                    $(this).dialog("close");
                }
            }
        });
        $("#dialog").dialog("open");
    });

    //Brisanje sobe iz baze

    $dugmeObrisi2 = $('.obrisiSobu');

    $dugmeObrisi2.on('click', function (e) {
        $(".dialogRoom").dialog({
            autoOpen: false,
            modal: true
        });

        e.preventDefault();
        $('#brisiSobuModal').modal('toggle');

        $(".dialogRoom").dialog({
            buttons : {
                "Potvrdi" : function() {
                    $('#formDeleteRoom').submit();
                },
                "Ponisti" : function() {
                    $(this).dialog("close");
                }
            }
        });
        $(".dialogRoom").dialog("open");
    });

    //Brisi tip sobe iz baze

    $dugmeObrisi2 = $('.obrisiTipSobe');

    $dugmeObrisi2.on('click', function (e) {
        $(".dialogRoomType").dialog({
            autoOpen: false,
            modal: true
        });

        e.preventDefault();
        $('#brisiTipModal').modal('toggle');

        $(".dialogRoomType").dialog({
            buttons : {
                "Potvrdi" : function() {
                    $('#formRoomTypeDelete').submit();
                },
                "Ponisti" : function() {
                    $(this).dialog("close");
                }
            }
        });

        $(".dialogRoomType").dialog("open");
    });

    $('#choosenType').change(function () {
        //Room Type update dropdown
        $("#choosenType option").each(function () {
            if($(this).attr('id') == "firstNull"){
                document.getElementById("updateTypeName").disabled = true;
                document.getElementById("updateTypePrice").disabled = true;
                document.getElementById("updateNumberBeds").disabled = true;
            }else{
                document.getElementById("updateTypeName").disabled = false;
                document.getElementById("updateTypePrice").disabled = false;
                document.getElementById("updateNumberBeds").disabled = false;
            }

        });

        $('#firstForm').submit();

    });

    $('#firstForm').on('submit', function (e) {
        e.preventDefault();

        var typeID = $('#choosenType').val();
        $.ajax({
            url: $(this).attr('action'),
            type: "POST",
            data: {id: typeID},
            success: function (data) {
                var obj = data;
                $('#updateTypeId').val(obj.id);
                $('#updateTypeName').val(obj.name);
                $('#updateTypePrice').val(obj.price)

                if(obj.numBed === "one"){
                    $('#updateNumberBeds').val("one");
                }else if(obj.numBed ==="two"){
                    $('#updateNumberBeds').val("two");
                }else if(obj.numBed === "three"){
                    $('#updateNumberBeds').val("three");
                }else{
                    $('#updateNumberBeds').val("four");
                }
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert("Morate izabrati postojeci tip!" + errorThrown);
            },
            dataType: "json"
        });
    });




//    Manager add room form
    $('.manager-link').on('click', function () {

        $('.manager-list-items').css('background-color', '#343a40');

        var page = $(this).attr('href');

        $(this).parent('li').css('background-color', '#5e6163');

        $('.manager-content').load(page);
        return false;
    });







//    Trazenje sobe sa ID-jem
    $('#findRoom').on('click', function () {
        var IdValue = $('#roomId').val();
        var newTable = null;

        $.ajax({
            url: '/admin/room/findRoom',
            type: "POST",
            data: {roomId: IdValue},
            success: function (data) {
                var room = data;


                $('#find-success').css('visibility', 'visible');
                $('#find-failed').css('visibility', 'hidden');

                $('#updateroomType').attr('disabled', false);
                $('#updateroomFree').attr('disabled', false);

                $('#colRoomId').text(room.id);
                $('#colRoomType').text(room.roomType.name);
                if(room.free === false){
                    $('#colRoomStatus').html('<span style="color: green;">Slobodna</span>');
                }else{
                    $('#colRoomStatus').html('<span style="color: red;">Zauzeta</span>');
                }


                $('#findRoomTable').css('visibility', 'visible');
                $('#updateRoomSubmit').attr('disabled', false);

            },
            error: function (jXHR, textStatus, errorThrown) {
                $('#findRoomTable').css('visibility', 'hidden');
                $('#find-success').css('visibility', 'hidden');
                $('#find-failed').css('visibility', 'visible');
                $('#updateroomType').attr('disabled', true);
                $('#updateroomFree').attr('disabled', true);
                $('#updateRoomSubmit').attr('disabled', true);
            },
            dataType: "json"
        });
    });

    $('#formFindRoom').on('submit', function (e) {
        //Saljemo id sobe na drugi kontroler kako bi izvrsili azuriranje te sobe
        $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: $(this).attr('action'),
                data: JSON.stringify(IdValue),
        });
    });


    //Rezervisanje sobe, izracinavanje iznosa

    //Odredimo modalnu formu koja ima klasu modal fade show
    //nakon toga uzimamo njene child elemente i radimo sa njima

    $('.reserve-open-form-button').each(function () {
        $(this).on('click', function () {

            var modalName =  $(this).attr('data-target');

            var priceForOneDay = $(modalName).find('input[id^="singlePrice"]').val();
            var numberOfDays = $(modalName).find('input[id^="numberOfDays"]');

            $(numberOfDays).change(function () {
                $(modalName).find('input[id^="price"]').val(numberOfDays.val() * priceForOneDay);
            });
        });
    });

    //Otkazivanje rezervacije na stranici user account

    // ('.btn-outline-danger').on('click', function (e) {
    //     $("#dialog1").dialog({
    //         autoOpen: false,
    //         modal: true
    //     });
    //
    //     e.preventDefault();
    //     var targetUrl = $(this).attr("href");
    //
    //     $("#dialog1").dialog({
    //         buttons : {
    //             "Da" : function() {
    //                 window.location.href = targetUrl;
    //             },
    //             "Ne" : function() {
    //                 $(this).dialog("close");
    //             }
    //         }
    //     });
    //     $("#dialog1").dialog("open");
    // });


});



//Prikazivanje soba odredjenog hotela
$('#choosenHotel').change(function () {
    $('#table-body').html('');
    $('#form-show-rooms').submit();
})
$('#form-show-rooms').on('submit', function (e) {
    e.preventDefault();

     var name = $('#choosenHotel').val();
    $.ajax({
        url: $(this).attr('action'),
        type: "POST",
        data: {name: name},
        success: function (data) {

            var array = data;
            var tableBody = $('#table-body');
            var i = 0;

            array.forEach(function (item) {
                console.log(item);
                i = i+1;

                var rowNumber = "rowNumber" + i;
                var updateNumber = "operations"+i;
                var newRow = $('<tr></tr>');
                newRow.attr("id", rowNumber);
                var roomId = $('<th scope="row"></th>').text(item.id);
                var hotelName = $('<td></td>').text(name);
                var roomType = $('<td></td>').text(item.roomType.name);
                var roomStatus = null;

                if(item.free !== true){
                    roomStatus = $('<td style="color: green;"></td>').text('Slobodna');
                }else{
                    roomStatus = $('<td style="color: red;"></td>').text('Zauzeta');
                }

                newRow.append(roomId);
                roomId.after(hotelName);
                hotelName.after(roomType);
                roomType.after(roomStatus);
                tableBody.prepend(newRow);
            });
        },
        error: function (jXHR, textStatus, errorThrown) {
            $('#table-body').html('');
            $('#table-body').append('<h4 class="text-center">Nije selektovan hotel!</h4>')
        },
        dataType: "json"
    });
});


//Administrator rooms operations page

$hiddenDiv1 = $('.hidden-div-1');
$link1 = $('#operation-link-1');
$hiddenDiv2 = $('.hidden-div-2');
$link2 = $('#operation-link-2');

$link1.on('click', function () {
    if($hiddenDiv2.is(':visible')){
        $hiddenDiv2.slideUp();
    }
    if($hiddenDiv1.is(':visible')){
        $hiddenDiv1.slideUp();
    }else{
        $hiddenDiv1.slideDown();
    }

});

$link2.on('click', function () {
    if($hiddenDiv1.is(':visible')){
        $hiddenDiv1.slideUp();
    }
    if($hiddenDiv2.is(':visible')){
        $hiddenDiv2.slideUp();
    }else{
        $hiddenDiv2.slideDown();
    }
});



