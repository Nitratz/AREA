var Module = {
    Load: function (name, path, callback) {
        $.get('/Module/Loader?name=' + encodeURIComponent(name) + "&path=" + encodeURIComponent(path), function (ret) {
            ret = JSON.parse(ret);
            callback(ret.success);
        })
    },
    List: function (callback) {
        $.get('/Module/List', function (ret) {
            ret = JSON.parse(ret);
            callback(ret);
        });
    },
    getTriggers: function (moduleName, callback) {
        $.get('/Module/' + moduleName + '/Triggers', function (ret) {
            ret = JSON.parse(ret);
            if (ret.success) {
                callback(ret.triggers);
            }
        });
    },
    getAction: function (moduleName, callback) {
        $.get('/Module/' + moduleName + '/Actions', function (ret) {
            ret = JSON.parse(ret);
            if (ret.success) {
                callback(ret.Actions);
            }
        });
    },
    getActionName: function (moduleName, id, callback) {
        $.get('/Module/' + moduleName + '/Action?id=' + id, function (ret) {
            callback(ret);
        });
    },
    getTriggerName: function (moduleName, id, callback) {
        $.get('/Module/' + moduleName + '/Trigger?id=' + id, function (ret) {
            callback(ret);
        });
    }
}

var Area = {
    Create: function (actionID, triggerID, actionModuleName, triggerModuleName, callback) {
        $.get('/User/AddArea?token=' + getCookie("token") +
            "&actionID=" + actionID +
            "&triggerID=" + triggerID +
            "&actionModuleName=" + actionModuleName +
            "&triggerModuleName=" + triggerModuleName,
            function (ret) {
                ret = JSON.parse(ret);
                callback(ret.success);
            })
    },
    List: function (callback) {
        $.get('/User/ListArea?token=' + getCookie("token"), function (ret) {
            ret = JSON.parse(ret);
            callback(ret);
        });
    },
    Delete: function (id, callback) {
        $.get('/User/DeleteArea?token=' + getCookie("token") + "&AreaID=" + id, function (ret) {
            ret = JSON.parse(ret);
            callback(ret.success);
        });
    }
}