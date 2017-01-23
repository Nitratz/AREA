function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function deleteCookie(cname) {
    setCookie(cname, "", -1);
}

var User = {
    connentUser: function (email, password, callback) {
        $.get('/User/Login?email=' + encodeURIComponent(email) + '&password=' + encodeURIComponent(password), function (ret) {
            ret = JSON.parse(ret);
            if (ret.success == true) {
                setCookie('token', ret.Token, 10);
            }
            callback(ret.success);
        });
    },
    createUser: function (email, password, fullname, callback) {
        $.get('/User/CreateUser?email=' + encodeURIComponent(email) + '&password=' + encodeURIComponent(password) + '&fullname=' + encodeURIComponent(fullname), function (ret) {
            ret = JSON.parse(ret);
            callback(ret.success);
        });
    },
    checkToken: function (callback) {
        $.get('/User/Token?token=' + getCookie("token"), function (ret) {
            ret = JSON.parse(ret);
            if (!ret.success)
                deleteCookie('token');
            callback(ret.success);
        });
    },
    GetAuthLink: function (moduleName, callback) {
        $.get('/User/GetAuthLink?token=' + getCookie("token") + '&moduleName=' + moduleName, function (ret) {
            callback(ret);
        });
    },
    ListService: function (callback) {
        $.get('/User/ListService?token=' + getCookie("token"), function (ret) {
            ret = JSON.parse(ret);
            callback(ret);
        });
    },
}