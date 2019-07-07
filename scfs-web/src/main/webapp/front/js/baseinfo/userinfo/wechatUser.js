window.operateEvents = {
    'click .divide': function(e, value, row, index){
        var id = row.id;
        layer.confirm('确定要绑定吗？', {
            btn: ['确定', '取消'] //按钮
        }, function() {
            GLOBAL.ajax("/user/wechat/update", {
                userId: id,
                openid: options.param.id
            }, function(){
                layer.msg("绑定成功", {  icon: 1 }) ;
                GLOBAL.go("html/baseinfo/userinfo/newBuildWechat.html");
            })
        }, function() {});
    }
};
function operateFormatter(value, row, index) {
    return [
        '<a class="divide" href="javascript:void(0)">',
        '<span class="btn btn-primary btn-sm"  >绑定</span>',
        '</a>  '
    ].join('');
}/*数据表格*/
/*数据表格*/
var tabCols = [
    {
        title: '序号',
        field: 'columnsNumber',
        width: 10
    },{
        title: '工号',
        field: 'employeeNumber',
        width: 80,
        align: 'center'
    }, {
        title: '用户名',
        field: 'userName',
        width: 75,
        align: 'center'
    }, {
        title: '中文名',
        field: 'chineseName',
        width: 80,
        align: 'center'
    }, {
        title: '英文名',
        field: 'englishName',
        width: 100,
        align: 'center'
    },{
        title: '角色',
        field: 'roleName',
        width: 100,
        align: 'center'
    }, {
        title: '手机',
        field: 'mobilePhone',
        width: 100,
        align: 'center'
    }, {
        title: '邮箱',
        field: 'email',
        width: 80,
        align: 'center'
    },  {
        title: '操作',
        field: 'operate',
        width: 80,
        align: 'center',
        events: operateEvents,
        formatter: operateFormatter
    }
];

options.initPage = function() {

    GLOBAL.initTable($('#js_dataTable'), tabCols, null);
    //还原查询条件及过滤后的数据
    GLOBAL.restoreQuery($('#js_dataTable'));
}

$("#btnSearch").click(function() {
    var data = $("#searchForm").serializeObject();
    var url = $('#js_dataTable').data("url");
    GLOBAL.local(url, data);

    GLOBAL.tableRefresh($('#js_dataTable'), data);
})

$("#btnNew").click(function() {
    var url = $(this).data("url");
    GLOBAL.go(url, options.param);
})
$("#back").click(function(){
    var url = $(this).data("url");
    GLOBAL.go(url);
})