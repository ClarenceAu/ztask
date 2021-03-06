function main() {
    var jArena = $(".msg_arena");
    msg_events_bind.apply(jArena);
    msg_do_reload(jArena, true);
}

function onMessageUpdate() {
    var jArena = $(".msg_arena");
    msg_do_reload(jArena, true);
}

function msg_events_bind(opt) {
    opt = opt || {};
    msg_opt(this, opt);

    this.delegate(".msg_reload", "click", msg_events_on_reload);
    this.delegate(".msg_more", "click", msg_events_on_more);
    this.delegate(".msg_kwd", "change", msg_events_on_reload);
    this.delegate(".msg_do_read", "click", msg_events_on_do_read);
    this.delegate(".msg_do_unread", "click", msg_events_on_do_unread);
    this.delegate(".msg_del", "click", msg_events_on_del);
    this.delegate(".msg_readall", "click", msg_events_on_do_readall);
    this.delegate(".msg_clearall", "click", msg_events_on_do_clearall);
}

function msg_events_on_del() {
    var ee = _msg_obj(this);
    ajax.get("/ajax/message/del", {
        mid: ee.msgId
    }, function(re) {
        z.removeIt(ee.jMsg);
    });
}

function msg_events_on_do_readall() {
    var ee = _msg_obj(this);
    ajax.get("/ajax/message/set/read", {
        read: true
    }, function(re) {
        msg_do_reload(ee.selection, true);
    });
}

function msg_events_on_do_clearall() {
    if(window.confirm(z.msg("msg.clear.confirm"))) {
        var ee = _msg_obj(this);
        ajax.get("/ajax/message/clear", function(re) {
            msg_do_reload(ee.selection, true);
            $("#msg_count").text(0);
        });
    }
}

function msg_events_on_do_read() {
    msg_do_set_read(this, true);
}

function msg_events_on_do_unread() {
    msg_do_set_read(this, false);
}

function msg_do_set_read(ele, read) {
    var ee = _msg_obj(ele);
    ajax.get("/ajax/message/set/read", {
        mid: ee.msgId,
        read: read
    }, function(re) {
        msg_html.apply(ee.jMsg, [re.data]);
        var num = $(".msg_st_unread",ee.selection).size();
        $("#msg_count").text(num);
    });
}

function msg_events_on_reload() {
    msg_do_reload(this, true);
}

function msg_events_on_more() {
    msg_do_reload(this, false);
}

function msg_do_reload(ele, clear) {
    var ee = _msg_obj(ele);
    if(clear)
        ee.lastId = "";
    ajax.get("/ajax/message/list", {
        kwd: ee.kwd,
        lstId: ee.lastId,
        limit: 100
    }, function(re) {
        if(clear) {
            $(".msg_list .msg",ee.selection).remove();
            $(".msg_more", ee.selection).removeAttr("msg-last-id");
        }
        msg_append.apply(ee.selection, [re.data]);
    });
}

function msg_append(msgs) {
    var selection = msg_selection(this);
    var jList = $(".msg_list", selection);
    var jMore = $(".msg_more", jList);
    for(var i = 0; i < msgs.length; i++) {
        msg_html.apply(jMore, [msgs[i]]);
    }
    jMore.attr("msg-last-id", jList.find(".msg").last().attr("msg-id"));
}

function msg_html(msg) {
    var cssRead = msg.read ? "msg_st_read" : " msg_st_unread";
    var cssNotify = msg.notified ? " msg_st_notify" : "";
    var cssFavo = msg.favorite ? " msg_st_favo" : "";
    var html = '<div class="msg ' + cssRead + cssNotify + cssFavo + '" msg-id="' + msg._id + '">';
    html += '    <span class="msg_text">' + task_format_text(msg.text) + '</span>';
    if(msg.read)
        html += '<a class="msg_do_unread">' + z.msg("msg.do.unread") + '</a>';
    else
        html += '<a class="msg_do_read">' + z.msg("msg.do.read") + '</a>';
    html += '    <a class="msg_del"></a>';
    html += '    <a class="msg_notify"></a>';
    html += '</div>';
    var jq;
    if(this.hasClass("msg_more")) {
        jq = $(html).insertBefore(this);
    } else if(this.hasClass("msg")) {
        jq = $(html).replaceAll(this);
    } else {
        throw "Unsupport selector '" + this.selector + "'";
    }
    z.blinkIt(jq);
}

/**
 * 从任何一个对象找到相关的 jMsg
 *
 * @return jMsg 对象
 */
function msg_jmsg(ele) {
    return $(ele).hasClass("msg") ? $(ele) : $(ele).parents(".msg");
}

/**
 * 从任何一个对象找到选区
 */
function msg_selection(ele) {
    return $(ele).hasClass("msg_events_opt") ? $(ele) : $(ele).parents(".msg_events_opt");
}

/**
 * 获取或者设置 opt 对象到选区
 *
 * @param selection : 选区对象
 * @param opt : 配置对象
 */
function msg_opt(selection, opt) {
    if(!opt)
        return selection.data("msg-events-opt");
    return selection.data("msg-events-opt", opt).addClass("msg_events_opt");
}

/**
 * 提供给事件处理函数使用的私有方法，它根据 ele 参数总结出一些必要的对象信息，并返回
 * @param ele 事件响应 DOM 对象
 * @return 常用的事件处理数据对象
 */
function _msg_obj(ele) {
    var me = $(ele);
    var jMsg = msg_jmsg(ele);
    var selection = msg_selection(ele);
    var kwd = $.trim($(".msg_kwd",selection).val());
    var lastId = $(".msg_more",selection).attr("msg-last-id");
    return {
        me: me,
        jMsg: jMsg,
        kwd: kwd,
        lastId: lastId,
        msgId: jMsg.attr("msg-id"),
        selection: selection,
        opt: msg_opt(selection)
    }
}

function adjustLayout() {
    var jArena = $(".msg_arena");
    jArena.css({
        width: this.width,
        height: this.height
    });
    var w = $(".msg_form", jArena).outerWidth();
    $(".msg_arena_wrapper", jArena).css({
        "padding-right": w
    });
}