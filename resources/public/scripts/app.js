var username = null;
var password = null;
var accessLevel = null;
var accType = null;
var referringToCustID = null;

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
    var custnm = $("[name='custnm']")[0].value;
    var crcuspass = $("[name='crcuspass']")[0].value;
    var re = new RegExp('^[a-zA-Z0-9]+$');
    var nmre = new RegExp('^[a-zA-Z]+$');
    if(!re.test(custname)){
        $('#admin-dynamic-2').html("<h4></h4><h6>Customer ID can only contain alphanumeric characters</h6>");
    }
    else if(accessLevel == null){
        $('#admin-dynamic-2').html("<h4></h4><h6>Customer access level not specified</h6>");
    }
    else if(!re.test(crcuspass)){
        $('#admin-dynamic-2').html("<h4></h4><h6>Password can only contain alphanumeric characters</h6>");
    }
    else if(!nmre.test(custnm)){
        $('#admin-dynamic-2').html("<h4></h4><h6>Name is not valid, first name only</h6>");
    }
    else{
        var form = {
            		'custname' : custname,
            		'crcuspass' : crcuspass,
            		'custnm' : custnm,
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

function loadCustomerDetails(custname){
    $("[name='searchcust']").prop('disabled',true);
    var form = {
        'custname': custname,
        'privileged': "1"
    };
    $.ajax({
                		type: 'POST',
                		url: '/loadcustomerdetails',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-2').html(response);
                		}
    });
}

function changeName(custname){
    $('.custmanbtns').prop('disabled', true);
    $('#admin-dynamic-3').html("<input type=\"text\" name=\"chcusname\" placeholder=\"New Name\"><button onclick=\"chcusnamesub(\'"+custname+"\')\">Change</button>");
}

function chcusnamesub(custname){
    var namechange = $("[name='chcusname']")[0].value;
    var re = new RegExp('^[a-zA-Z]+$');
    if(!re.test(namechange)){
        $('#admin-dynamic-4').html("<h4></h4><h6>Name is not valid, first name only</h6>");
        return;
    }
    var form = {
        'custname': custname,
        'namechange': namechange
    };
    $.ajax({
                		type: 'POST',
                		url: '/changename',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-4').html("<h4></h4><h6>Successfully Changed Name</h6>");
                		}
    });
}

function changeCustStatus(custname){
    $('.custmanbtns').prop('disabled', true);
    var form = {
        'custname': custname
    };
    $.ajax({
                		type: 'POST',
                		url: '/changecuststatus',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-3').html(response);
                		}
    });
}

function changeCustPassword(custname){
    $('.custmanbtns').prop('disabled', true);
    $('#admin-dynamic-3').html("<input type=\"password\" name=\"chcuspass\" placeholder=\"New Password\"><button onclick=\"chcuspasssub(\'"+custname+"\')\">Change</button>");
}

function chcuspasssub(custname){
    var passchange = $("[name='chcuspass']")[0].value;
    var re = new RegExp('^[a-zA-Z0-9]+$');
    if(!re.test(passchange)){
        $('#admin-dynamic-4').html("<h4></h4><h6>Password can only contain alphanumeric characters</h6>");
        return;
    }
    var form = {
        'custname': custname,
        'passchange': passchange
    };
    $.ajax({
                		type: 'POST',
                		url: '/changepass',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-4').html("<h4></h4><h6>Successfully Changed Password</h6>");
                		}
    });
}

function getListOfAllAccounts(custname){
    referringToCustID = custname;
    $('.custmanbtns').prop('disabled', true);
    var form = {
        'custname': custname
    };
    $.ajax({
                		type: 'POST',
                		url: '/getlistofallaccounts',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-3').html(response);
                		}
    });
}

function createNewAccount(custname){
    $(".accbtns").prop('disabled',true);
    var form = {
            'custname': custname
        };
    $.ajax({
                    		type: 'POST',
                    		url: '/getacccreationpage',
                    		data: form,
                    		success: function(response){
                                $('#admin-dynamic-4').html(response);
                    		}
        });
}

function setAccType(acctype){
    accType = acctype;
    $('.acctypebtn').prop('disabled', true);
}

function submitCreateAccount(custname){
    var accname = $("[name='accname']")[0].value;
    var re = new RegExp('^[a-zA-Z0-9]+$');
    if(!re.test(accname)){
        $('#admin-dynamic-5').html("<h4></h4><h6>Account Name Can Only Contain Alphanumeric Characters</h6>");
    } else if(accType == null){
        $('#admin-dynamic-5').html("<h4></h4><h6>Account Type Not Specified</h6>");
    }
    else{
        var form = {
            		'custname' : custname,
            		'acctype' : accType,
            		'accname' : accname
        };
        $.ajax({
                		type: 'POST',
                		url: '/newaccount',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-5').html(response);
                            $('.acctypebtn').prop('disabled', false);
                            accType = null;
                		}
        });
    }
}

function loadAccountDetails(accno){
    $('.accbtns').prop('disabled', true);
    var form = {
        'accno' : accno
        'privileged' : '1'
    }
    $.ajax({
                    		type: 'POST',
                    		url: '/getaccountpage',
                    		data: form,
                    		success: function(response){
                                $('#admin-dynamic-4').html(response);
                    		}
    });
}

function alterAccountBalance(accno){
    $('.accmanbtns').prop('disabled', true);
    $('#admin-dynamic-5').html("<h4></h4>"+
    "<input type=\"number\" step=\"any\" name=\"alterbalance\" placeholder=\"New Balance\"</input>"+
    +"<button onclick=\"alterBalance('" + accno + "')\">Change</button>");
}

function alterBalance(accno){
    var newbalance = $("[name='alterbalance']")[0].value;
    var form = {
        'accno' : accno,
        'balance' : newbalance
    };
    $.ajax({
                    		type: 'POST',
                    		url: '/alterbalance',
                    		data: form,
                    		success: function(response){
                                $('#admin-dynamic-6').html("<h4></h4><h6>Successfully Changed Balance for Account Number: "+accno+" to: ₹ "+newbalance+"</h6>");
                    		}
    });
}

function reopenAccount(accno){
    $('.accmanbtns').prop('disabled', true);
    $('#admin-dynamic-5').html("<h4></h4><h6>Successfuly reopened Account with Number: "+accno"</h6>");
    var form = {
        'accno' : accno,
        'custname' : referringToCustID
    };
    $.ajax({
                    		type: 'POST',
                    		url: '/reopenaccount',
                    		data: form,
                    		success: function(response){
                                return;
                    		}
    });
}

function changeAccountName(accno){
    $('.accmanbtns').prop('disabled', true);
    $('#admin-dynamic-5').html("<h4></h4>"+
        "<input type=\"text\" name=\"changeaccname\" placeholder=\"New Name\"</input>"+
        +"<button onclick=\"changeAccName('" + accno + "')\">Change</button>");
}

function changeAccName(accno){
    var newname = $("[name='changeaccname']")[0].value;
    var re = new RegExp('^[a-zA-Z0-9]+$');
        if(!re.test(accname)){
            $('#admin-dynamic-6').html("<h4></h4><h6>Account Name Can Only Contain Alphanumeric Characters</h6>");
        }
        else{
            var form = {
                'accno' : accno,
                'name' : newname
            };
            $.ajax({
                                		type: 'POST',
                                		url: '/changeaccname',
                                		data: form,
                                		success: function(response){
                                            $('#admin-dynamic-6').html("<h4></h4><h6>Successfully Changed Name for Account Number: "+accno+"</h6>");
                                		}
            });
        }
}

function closeAccount(accno){
    $('.accmanbtns').prop('disabled', true);
    $('#admin-dynamic-5').html("<h4></h4><h6>Successfuly closed Account with Number: "+accno"</h6>");
    var form = {
        'accno' : accno,
        'custname' : referringToCustID
    };
    $.ajax({
                    		type: 'POST',
                    		url: '/closeaccount',
                    		data: form,
                    		success: function(response){
                                return;
                    		}
    });
}

function getAccLogs(accno){
    $('.accmanbtns').prop('disabled', true);
    var form = {
        'accno' : accno
    };
    $.ajax({
                    		type: 'POST',
                    		url: '/getacclogs',
                    		data: form,
                    		success: function(response){
                                $('#admin-dynamic-5').html(response);
                    		}
    });
}

function getTransferPage(custname){
    $('.custmanbtns').prop('disabled', true);
    $("[name='searchcust']").prop('disabled',true);
    var form = {
        'custname': custname,
        'privileged': "1"
    };
    $.ajax({
                		type: 'POST',
                		url: '/loadcustomerdetails',
                		data: form,
                		success: function(response){
                            $('#admin-dynamic-2').html(response);
                		}
    });
}