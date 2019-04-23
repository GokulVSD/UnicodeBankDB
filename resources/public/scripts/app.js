var username = null;
var password = null;

function login(){
    if(username === null){
    		username = $("[name='username']")[0].value;
    		password = $("[name='password']")[0].value;
    	}
    	var form = {
    		'username' : username,
    		'password' : password
    	};
    	$.ajax({
    		type: 'POST',
    		url: '/login',
    		data: form,
    		success: function(response){
    			$('.module').html(response);
    			$('.module').addClass('module-after-login');
    			$('.login-header').addClass('after-login');
    		}
    	});
}