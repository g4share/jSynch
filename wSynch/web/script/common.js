$(function(){
    var errorClass = 'error';

    $('form').submit(function(){

        var valid = true;
        var reqMsg = 'This field is required!';

        $('.' + errorClass, this).remove();

        $(':input[req]', this).each(function(){
            var parent = $(this).parent();
            if( $(this).val() == '' ){
                var msg = $(this).attr('req');
                msg = (msg != '') ? msg : reqMsg;
                AddToParent(parent, errorClass, msg);
                valid = false;
            };
        });

        return valid;
    });
});

function AddToParent(parentObject, cssClass, message) {
    $('<span class="'+ cssClass +'">'+ message +'</span>')
        .appendTo(parentObject)
        .fadeIn('fast')
        .click(function(){ $(this).remove(); });
}
