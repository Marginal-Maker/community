function like(button, entityType, entityId, entityUserId, discussPostId){
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId":entityId, "entityUserId": entityUserId, "discussPostId": discussPostId},
        function (data){
            data = $.parseJSON(data);
            if(data.code == 0){
                $(button).children("i").text(data.likeCount);
                $(button).children("b").text(data.likeStatus == 1?'已赞':'赞');
            }else{
                alert(data.msg);
            }
        }
    );
}