$('.tab a,.links a').on('click', function(e) {
    e.preventDefault();
    $(this).parent().addClass('active');
    $(this).parent().siblings().removeClass('active');
    var target = $(this).attr('href');
    $('.tab-content > div').not(target).hide();

    $(target).fadeIn(600);

});

$(document).ready(function() {
    $(".sign-in").click(function () {
        var account = $(".account").val();
        var password = $(".password").val();
        if (account === "") {
            $(".account").focus();
            formMessage('登录账户不能为空', 'warning');
            return false;
        } else if (password === "") {
            $(".password").focus();
            formMessage('登录密码不能为空', 'warning');
            return false;
        } else {
            formMessage('正在登录...', 'succeed');
            window.setTimeout(function () {
                var postData = {
                    account: account,
                    password: password
                };
                $.ajax({
                    type: "POST",
                    contentType: "application/json;charset=utf-8",
                    url: "user/checkLogin",
                    data: JSON.stringify(postData),
                    dataType: 'json',
                    success: function (result) {
                        if (result.code === 1001) {
                            $(".account").focus();
                            formMessage(result.message, 'error');
                        } else if (result.code === 1002) {
                            $(".account").focus();
                            formMessage(result.message, 'error');
                        } else if (result.code === 1003) {
                            $(".password").focus();
                            $(".password").val("");
                            formMessage(result.message, 'error');
                        } else if (result.code === 1) {
                            formMessage('登录验证成功,正在跳转首页', 'succeed');
                            window.location.href = '/provider-project/page/index.html';
                        } else {
                            window.location.href = '/provider-project/login.html';
                        }
                    }
                });
            }, 500);
        }
    });

    $(".register").click(function() {
        var username = $(".register-name").val();
        var phone = $(".register-phone").val();
        var email = $(".register-email").val();
        var registerPwd = $(".register-password").val();
        var confirmPassword = $(".confirm-password").val();
        var accountType = 1;
        if(username === "") {
            $(".register-name").focus();
            formMsg('请填写用户名称', 'warning');
            return false;
        } else if(!(/^1[34578]\d{9}$/.test(phone))){
            $(".register-phone").focus();
            formMsg('电话号码不正确,请重新填写', 'warning');
            return false;
        } else if(!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email))) {
            $(".register-email").focus();
            formMsg('邮箱地址不正确,请重新填写', 'warning');
            return false;
        }else if(registerPwd === "") {
            $(".register-password").focus();
            formMsg('密码不能为空', 'warning');
            return false;
        }else if(confirmPassword === "") {
            $(".confirm-password").focus();
            formMsg('验证密码不能为空', 'warning');
            return false;
        }else if(confirmPassword !== registerPwd) {
            $(".confirm-password").focus();
            formMsg('前后密码不一致', 'warning');
            return false;
        }else {
            formMsg('正在注册...', 'succeed');
            window.setTimeout(function() {
                var postdata = {
                    username:username,
                    account:phone,
                    email:email,
                    registerPwd:registerPwd,
                    confirmPassword:confirmPassword,
                    accountType:accountType
                };
                $.ajax({
                    type: "POST",
                    contentType:"application/json;charset=utf-8",
                    url: "user/register",
                    data: JSON.stringify(postdata),
                    dataType: 'json',
                    success: function(result) {
                        if(result.code === 1000) {
                            $(".register-name").focus();
                            formMsg(result.message, 'error');
                        }  else if(result.code === 1004) {
                            $(".confirm-password").focus();
                            formMsg(result.message, 'error');
                        } else if(result.code === 1) {
                            formMsg('注册成功,请联系管理员登录', 'succeed');
                            window.location.href = '/provider-project/login.html';
                        } else {
                            window.location.href = '/provider-project/login.html';
                        }
                    }
                });
            }, 500);
        }
    })
});

function formMessage(msg, type) {
    $('.form-message').html(''); //先清空数据
    $('.form-message').append('<div class="form-' + type + '-text">' + msg + '</div>');
}

function formMsg(msg, type) {
    $('.form-msg').html(''); //先清空数据
    $('.form-msg').append('<div class="form-' + type + '-text">' + msg + '</div>');
}