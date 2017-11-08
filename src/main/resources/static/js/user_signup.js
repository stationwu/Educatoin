$(document).ready(function(){
  $("#btnAdd").click(function(){
    console.log("#btnAdd clicked");

    var count = $('#children div.weui-panel__bd').length;
    count++;

    var nodeId = "child-" + count;
    $('#children div.weui-panel__bd:last').after(
        '<div id="'+nodeId+'" class="weui-panel__bd" >' +
           '<div class="weui-cells">' +
            '<div class="weui-cell">' +
                '<div class="weui-cell__bd">' +
                    '<input class="weui-input" placeholder="孩子姓名" type="text" />' +
                '</div>'+
            '</div>'+
        '</div>'+
        '<div class="weui-cell">'+
            '<div class="weui-cell__hd">'+
                '<label class="weui-label" for="">'+
                    '生日'+
                '</label>'+
            '</div>'+
            '<div class="weui-cell__bd">'+
                '<input class="weui-input" type="date" value=""/>'+
            '</div>'+
        '</div>'+
        '<div class="weui-cell">'+
            '<div class="weui-cell__hd">'+
                '<label class="weui-label" for="">'+
                    '剩余课时'+
                '</label>'+
            '</div>'+
            '<div class="weui-cell__bd">'+
                '<input class="weui-input" pattern="[0-9]*" type="number" value="24"/>'+
            '</div>'+
        '</div>'+
    '</div>'
    );
  });
});