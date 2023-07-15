
        $(function() {
            $("input[name='numonly']").on('input', function(e) {
                $(this).val($(this).val().replace(/[^0-9]/g, ''));
            });
        });

        function onlynum() {
            var fm = document.getElementById("form2");
            var ip = document.getElementById("num");
            var tag = document.getElementById("value");
            var res = ip.value;
     
            if (res != '') {
                if (isNaN(res)) {
                     
                    // Set input value empty
                    ip.value = "";
                     
                    // Reset the form
                    fm.reset();
                    return false;
                } else {
                    return true;
                }
            }
        }