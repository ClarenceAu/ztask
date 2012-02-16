<%@include file="/WEB-INF/jsp/include/_setup.jsp" %>
<html>
<head>
<%@include file="/WEB-INF/jsp/include/_page_metas.jsp" %>
<%@include file="/WEB-INF/jsp/include/_common_rs.jsp" %>

<script language="JavaScript" src="${rs}/js/ztask_task_events.js"></script>

<link rel="stylesheet" type="text/css" href="${rs}/css/page_message.css"/>

<script language="JavaScript" src="${rs}/js/page.js"></script>
<script language="JavaScript" src="${rs}/js/page_message.js"></script>

</head>
<body>
    <% /*==========================================顶部固定条=*/ %>
    <%@include file="/WEB-INF/jsp/include/_sky.jsp" %>
    
    <div class="msg_arena"><div class="msg_arena_wrapper">
        <% /*===========================================搜索=*/ %>
        <div class="msg_form">
            <input class="msg_kwd" placeholder="${msg['msg.input.tip']}">
            <a class="msg_reload">${msg['msg.reload']}</a>
            <pre class="msg_tip">${msg['msg.form.tip']}</pre>
        </div>
        <% /*===========================================列表=*/ %>
        <div class="msg_list scro">
            <div class="msg_more">${msg['msg.more']}</div>
            <div class="msg_btns">
                <a class="msg_readall">${msg['msg.readall']}</a>
                <a class="msg_clearall">${msg['msg.clear']}</a>
            </div>
        </div>
    </div></div>
    
    <% /*==========================================本地化字符串支持=*/ %>   
    <%@include file="/WEB-INF/jsp/include/_msgs.jsp" %>
</body>
</html>