var username = null;
var password = null;
var accessLevel = null;

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

function createCustomer(){
    $('#options button').prop('disabled', true);
    $.ajax({
        		type: 'GET',
        		url: '/createcustomer',
        		success: function(response){
                    $('#admin-dynamic-1').html(response);
        		}
    });
}

function custAccessLevel(level){
    accessLevel = level;
    $('.alevelbtn').prop('disabled', true);
}

function submitCustCreate(){
    var custname = $("[name='custname']")[0].value;
    var crcuspass = $("[name='crcuspass']")[0].value;
    var re = new RegExp('^[a-zA-Z0-9]+$');
    if(!re.test(custname)){
        $('#admin-dynamic-2').html("<h6>Customer ID can only contain alphanumeric characters</h6>");
    }
    else if(accessLevel == null){
        $('#admin-dynamic-2').html("<h6>Customer access level not specified</h6>");
    }
    else if(!re.test(crcuspass)){
        $('#admin-dynamic-2').html("<h6>Password can only contain alphanumeric characters</h6>");
    }
    else{
        var form = {
            		'custname' : custname,
            		'crcuspass' : crcuspass,
            		'accesslevel' : accessLevel
        };
        $.ajax({
                		type: 'POST',
                		url: '/newcustomer',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-2').html(response);
                            $('.alevelbtn').prop('disabled', false);
                            accessLevel = null;
                		}
        });
    }
}

function getSystemLogs(){
    $('#options button').prop('disabled', true);
    $.ajax({
        		type: 'GET',
        		url: '/getsystemlogs',
        		success: function(response){
                    $('#admin-dynamic-1').html(response);
        		}
    });
}

function manageCustomer(){
    $('#options button').prop('disabled', true);
    $('#admin-dynamic-1').html("<input type=\"text\" name=\"searchcust\" placeholder=\"Search by Customer ID\">");
    $("[name='searchcust']").bind("enterKey",function(e){
       var searchcust = $("[name='searchcust']")[0].value;
       var re = new RegExp('^[a-zA-Z0-9]+$');
       if(!re.test(searchcust)){
           $('#admin-dynamic-2').html("<h4>Customer ID can only contain alphanumeric characters</h4>");
           return;
       }
       var form = {
                   	'searchcust' : searchcust
       };
       $.ajax({
                   	type: 'POST',
                   	url: '/getonecustomer',
                   	data: form,
                   	success: function(response){
                          $('#admin-dynamic-2').html(response);
                   	}
       });
    });
    $("[name='searchcust']").keyup(function(e){
        if(e.keyCode == 13)
        {
            $(this).trigger("enterKey");
        }
    });
    $.ajax({
            		type: 'GET',
            		url: '/getallcustomers',
            		success: function(response){
                        $('#admin-dynamic-2').html(response);
            		}
    });
}