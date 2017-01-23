function Notify(text, temps = 500) {
    var snackbarContainer = document.querySelector('#Notification');
    var data = {
        message: text,
        timeout: temps
    };
    snackbarContainer.MaterialSnackbar.showSnackbar(data);
}

function createbubble(name, type) {
    var data = "";

    data += '<div class="section__circle-container mdl-cell mdl-cell--2-col mdl-cell--1-col-phone">'
    data += '<div class="section__circle-container__circle color' + type + '"></div>'
    data += '</div>'
    data += '<div class="section__text mdl-cell mdl-cell--10-col-desktop mdl-cell--6-col-tablet mdl-cell--3-col-phone">'
    data += '<h4>' + name + '</h4>';
    data += '</div>';
    return data;
}

function createbubbleArea(modulename, info, type) {
    var data = "";

    data += '<div class="section__circle-container mdl-cell mdl-cell--2-col mdl-cell--1-col-phone">'
    data += '<div class="section__circle-container__circle color' + type + '"></div>'
    data += '</div>'
    data += '<div class="section__text mdl-cell mdl-cell--10-col-desktop mdl-cell--6-col-tablet mdl-cell--3-col-phone">'
    data += '<h4>' + modulename + '</h4>';
    data += '<h3>' + info + '</h3>';
    data += '</div>';
    return data;
}

function generateModuleCard(Trigger, Action, ModuleName, Link) {
    var card = '<section class="section--center mdl-grid mdl-grid--no-spacing mdl-shadow--2dp">\n';
    card += '<div class="mdl-card__supporting-text mdl-grid mdl-grid--no-spacing">\n';
    card += '<h4 class="mdl-cell mdl-cell--12-col">' + ModuleName + '</h4>';
    Trigger.forEach((value, index) => {
        card += createbubble(value, 'Trigger');
    })
    Action.forEach((value, index) => {
        card += createbubble(value, 'Action');
    })
    card += '<div class="mdl-card__actions">'
    if (Link != "") {
        card += '<a href="' + Link + '" class="mdl-button">Authentification</a>'
    }
    card += '</div></section>';
    return card;
}

function generateAreaCard(Trigger, Action, id) {
    var card = '<section class="section--center mdl-grid mdl-grid--no-spacing mdl-shadow--2dp">\n';
    card += '<div class="mdl-card__supporting-text mdl-grid mdl-grid--no-spacing">\n';
    card += '<h4 class="mdl-cell mdl-cell--12-col">' + "Area :" + id + '</h4>';
    card += createbubbleArea(Trigger.module, Trigger.info, 'Trigger');
    card += createbubbleArea(Action.module, Action.info, 'Action');
    card += '<div class="mdl-card__actions">'
    card += '<a onclick="deleteArea(\'' + id + '\');return false;" href="#" class="mdl-button">Supprimer</a>'
    card += '</div></section>';
    return card;
}

function ListArea() {
    $("#AreaContainer").empty();
    Area.List((ret) => {
        if (ret.success) {
            ret.Areas = JSON.parse(ret.Areas);
            ret.Areas.forEach((area, index) => {
                Module.getActionName(area.actionModuleName, area.actionID, (actionName) => {
                    Module.getTriggerName(area.triggerModuleName, area.triggerID, (
                        triggerName) => {
                        var Trigger = {
                            module: area.triggerModuleName,
                            info: triggerName
                        };
                        var Action = {
                            module: area.actionModuleName,
                            info: actionName
                        };
                        $("#AreaContainer").append(generateAreaCard(Trigger,
                            Action, area.ID));
                    });
                });
            });
        }
    })
}