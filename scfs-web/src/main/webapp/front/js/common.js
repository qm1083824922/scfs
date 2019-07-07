//全局对象
window.GLOBAL = {};

GLOBAL.host = "http://127.0.0.1/scfs/"; //开发环境
// GLOBAL.host = "https://www.scferp.com/scfs/";//生产环境or测试环境

GLOBAL.selectUrl = 'common/selected/query?key=';
GLOBAL.selectGsaUrl = 'common/gsa/selected/query?key=';
GLOBAL.permUrl = 'common/perm/query';

GLOBAL.body = $("#js_mainWrap");

var options = {
    param: {
        id: 0,
        cid: 0
    }
};

GLOBAL.initOptionsParam = function(param) {
    var prarmArr = param.split(",");
    options.param = {};
    for (var i = 0; i < prarmArr.length; i++) {
        options.param[prarmArr[i]]
    }
}
	
//页面跳转
GLOBAL.go = function(url) {
    GLOBAL.body.empty().load(url, function(e) {
        localStorage.currentUrl = url;

        var localParam = GLOBAL.local("param");
        if(options.param && (options.param.id !== 0 || options.param.cid !== 0)){
            GLOBAL.local("param", options.param);
        }else if(localParam){
            options.param = localParam;
        }
        
        // 格式化时间插件
        if (typeof($.fn.datetimepicker) != 'undefined') {
            $(".js_datePicker").datetimepicker({
                defaultTime: '00:00:00',
                format: 'Y-m-d H:i:s'
            });

            $.datetimepicker.setLocale("ch");
        }

        //标签页切换
        $(".tab-item").unbind().click(function(e) {
            e.stopPropagation();
            var url = $(this).data("url");
            GLOBAL.go(url, options.param);
        })

        GLOBAL.initSelect();
        GLOBAL.initTree();
        GLOBAL.collapse();
        
        //控制页面上的按钮按权限显示
        GLOBAL.permissionBtn($("[data-permissionurl]"));
        options.initPage && options.initPage();
        $('select:visible').select2({language: "zh-CN"});
        $('#searchForm').find("button[type='reset']").click(function(){
        	GLOBAL.clearSearch();
        });
        $(':input').attr('autocomplete', 'off');
    });
}

/**
 * 获取客户端当前日期前后n天的日期,
 * 如GLOBAL.getDefaultDate(-7)，得到 ‘2016-02-16’
 * @param {integer} disDays 天数的差值
 */
GLOBAL.getDefaultDate = function(disDays){
    var myDate = new Date(); //获取今天日期
    myDate.setDate(myDate.getDate() + disDays);
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    month = month < 10 ? 0 + month : month;
    day = day < 10 ? 0 + day : day;
    return year + "-" + month + "-" + day;
}

/**
 * 如果不需要请求参数，则第二个参数设置为null
 * 如果要提交请求时，需要将按钮禁用，请将第5个参数设置为要禁用的按钮（jq对象）
 * 如果需要指定contentType, 请将第四个参数设置为true
 */
GLOBAL.ajax = function(url, data, callBack, contentType, $btn, isAsync) {
    var isAsync = isAsync && isAsync == true ? true : false;
    var options = {
        url: GLOBAL.host + url,
        type: "post",
        data: data,
        dataType: "json",
        async: isAsync,
        //contentType: "application/json",
        success: function(e) {
            if (e.login) {
                location.href = location.protocol + "//" + location.host + "/login.html";
                return;
            }
            
            layer.closeAll('loading');
            if (e && e.success != undefined) {
                if (!e.success && e.msg) {
                    layer.msg(e.msg, { icon: 2 });
                } else {
                    if(e.items){
                        for(var i in e.items){
                            if(e.items[i] == null){
                                e.items[i] = "";
                            }
                        }
                    }
                    callBack && callBack(e);
                }
                //拉取下拉框选项时走这里
            } else {
                callBack && callBack(e);
            }
            
            if ($btn) {
                setTimeout(function() {
                    $btn.removeClass("processing");
                }, 2500)
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            var status = XMLHttpRequest.status;
            switch (status) {
                case 400:
                    layer.msg("请求参数有误", { "icon": 2 });
                    break;
                case 403:
                    layer.msg("请求被禁止", { "icon": 2 });
                    break;
                case 404:
                    layer.msg("请求的资源不存在", { "icon": 2 });
                    break;
                case 500:
                    var response_text = XMLHttpRequest.responseText;
                    response_text = response_text.replace(/\n/g, "\\n"); //回车
                    response_text = response_text.replace(/\r/g, "\\r"); //换行
                    response_text = response_text.replace(/\t/g, "\\t"); //水平制表符

                    try {
                        var response = $.parseJSON(response_text);
                        if (response.code == "-400") { //超时
                            //设置超时标志，超时之后不再请求后台，防止多次弹出超时提示
                            layer.msg("请求超时，请重试", { "icon": 2 });
                        }
                    } catch (e) {
                        layer.msg(response_text, { "icon": 2 });
                    }
                    break;
                default:
                    layer.msg("服务器不可用", { "icon": 2 });
            }
            if ($btn) {
                setTimeout(function() {
                    $btn.removeClass("processing");
                }, 2500)
            }
        }
    }
    if (contentType) {
        options.contentType = "application/json";
    }

    //后台成功响应后，按钮再保持2s的时间禁用状态，避免快速重复提交数据到后台
    if ($btn && $btn.addClass) {
        $btn.addClass("processing");
    }

    $.ajax(options);
}

//下拉框初始化
GLOBAL.initSelect = function() {
    $(".js-select").each(function() {
        var $this = $(this);
        var gsa = $this.data("gsa");
        if(gsa){
        	GLOBAL.selectUrl=GLOBAL.selectGsaUrl;
        }
        var key = $this.data("url");
        var needEmptyOption = $this.data("empty-option");
        var options = needEmptyOption ? "" : "<option value=''>请选择</option>";
        if (!key) {
            $this.append(options);
            return;
        }
        if($(this).attr("pid") && ($("#"+$(this).attr("pid")).val() == "" || $("#"+$(this).attr("pid")).val() == null || $("#"+$(this).attr("pid")).val().toString == 'undefined')){
            $this.append(options);
        	return;
        }

        GLOBAL.ajax(GLOBAL.selectUrl + key, null, function(data) {
            var items = data.items;
            if (items) {
                for (var k = 0; k < items.length; k++) {
                    options += '<option value="' + items[k].code + '">' + items[k].value + '</option>';
                }
            }
            $this.append(options);
        });

        $this.change(function() {
            var thisValue = $(this).val();
            var thisId = $(this).attr("id");
            $("[pid='" + thisId + "']").each(function() {
                var $this = $(this),
                    pid = $(this).attr("pid"),
                    key = $this.data("url");
                var needEmptyOption = $this.data("empty-option");
                if (!key) {
                    return;
                }
                $this.empty();

                if(pid && ($("#"+pid).val() == "" || $("#"+pid).val() == null || $("#"+pid).val().toString == 'undefined')){
                    return;
                }
                
                var options = needEmptyOption ? "" : "<option value=''>请选择</option>";
                GLOBAL.ajax(GLOBAL.selectUrl + key + "&pId=" + thisValue, null, function(data) {
                    var items = data.items;
                    if (items) {
                        for (var k = 0; k < items.length; k++) {
                            options += '<option value="' + items[k].code + '">' + items[k].value + '</option>';
                        }
                    }
                    $this.append(options).change();
                    $('select:visible').select2({language: "zh-CN"});
                })
            })
        })
        if(gsa){
        	GLOBAL.selectUrl = 'common/selected/query?key=';
        }
    })

    $("[pid]").change(function() {
        var $this = $(this);
        var thisId = $this.attr("id");
        var thisValue = $this.val();
        $("[pid='" + thisId + "']").each(function() {
            var $this = $(this),
            	pid = $this.attr("pid"),
                url = $this.data("url");
            var needEmptyOption = $this.data("empty-option");
            $this.empty();
            if (!url) {
                return;
            }
            if(pid && ($("#"+pid).val() == "" || $("#"+pid).val() == null || $("#"+pid).val().toString == 'undefined')){
                return;
            }
            var options = needEmptyOption ? "" : "<option value=''>请选择</option>";
            GLOBAL.ajax(GLOBAL.selectUrl + url + "&pId=" + thisValue, null, function(data) {
                var items = data.items;
                if (items) {
                    for (var k = 0; k < items.length; k++) {
                        options += '<option value="' + items[k].code + '">' + items[k].value + '</option>';
                    }
                }
                $this.append(options).change();
                $('select:visible').select2({language: "zh-CN"});
            })
        })
    })
}

GLOBAL.initCheckbox = function(){
    $(".js-checkbox").each(function(){
        var $this = $(this);
        var key = $this.data("url");
        if (!key) {
            return;
        }
        var options = "";
        GLOBAL.ajax(GLOBAL.selectUrl + key, null, function(data) {
            var items = data.items;
            if (items) {
                for (var k = 0; k < items.length; k++) {
                    options += '<option value="' + items[k].code + '">' + items[k].value + '</option>';
                }
                $this.append(options);
            }
        });
    })
}

GLOBAL.initTree = function(){

    $(".js-tree").each(function(){
        var $this = $(this);
        var url = $this.data("url");
        var dataId = $this.data("id");
        var domId = $this.attr("id");
        var needAllNodes = $(this).hasClass("need-all-nodes");
        $this.after('<input type="hidden" class="js-initform" id="' + dataId + '" name="' + dataId + '"/>');

        //获取当前节点及所有子节点的id
        function getChildren(ids,treeNode){
            treeNode.id &&　ids.push(treeNode.id);
            if (treeNode.isParent){
                for(var obj in treeNode.children){
                    getChildren(ids,treeNode.children[obj]);
                }
            }
            return ids;
        }

        var setting = {
                view: {
                    dblClickExpand: false
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onClick: function(e, treeId, treeNode) {
                        var zTree = $.fn.zTree.getZTreeObj("dataTree"),
                            nodes = zTree.getSelectedNodes(),
                            v = "", ids="";
                        var idsArr = [];
                        var allNodesIds = getChildren(idsArr,treeNode).join(",");

                        nodes.sort(function compare(a, b) {
                            return a.id - b.id;
                        });
                        for (var i = 0, l = nodes.length; i < l; i++) {
                            v += nodes[i].name + ",";
                            ids += nodes[i].id + ",";
                        }
                        if (v.length > 0){
                           v = v.substring(0, v.length - 1);
                           ids = ids.substring(0, ids.length - 1);
                        }
                        $("#"+dataId).val(needAllNodes ? allNodesIds : ids);
                        if(window["onTreeSelect"]){
                        	onTreeSelect(needAllNodes ? allNodesIds : ids);
                        }
                        $this.val(v).change();
                        
                    }
                }
            };

        $.ajax({
            type: "get",
            url: GLOBAL.host + url,//"http://10.32.4.220/scf/common/department/tree"
            dataType: "json",
            success: function(e){
                if (e.login) {
                    location.href = location.protocol + "//" + location.host + "/login.html";
                    return;
                }
                var data = e.items;
                if(data.id){
                    $this.attr("placeholder", "单击选择数据").focus(function() {
                        var offset = $this.offset();
                        $("#treeBox").css({
                            left: offset.left + "px",
                            top: offset.top + $this.outerHeight() - 1 + "px"
                        }).slideDown("fast");
                    });
                    $.fn.zTree.init($("#dataTree"), setting, data);
                }else{
                    $this.attr("placeholder","暂无数据...");
                }
            },
            error: function(e){
                $this.attr("placeholder","暂无数据...");
            }
        })
        

        $("body").mousedown(function(event) {
            if ($(event.target).closest("#treeBox").length <= 0 && $(event.target).attr('id') !== domId) {
                $("#treeBox").fadeOut("fast");
            }
        })

    })
}

//初始化表单数据
GLOBAL.getEditData = function($from, dataId) {
    //编辑页面表单控件值初始化
    var formUrl = $from.data("url");
    var editData = null;
    GLOBAL.ajax(formUrl, { id: dataId, ids: dataId }, function(e) {
        var item = e.items;
        if (!item) {
            return;
        }
        if (item.length == 1) {
            editData = item[0];
        } else if (item.length > 1) {
            layer.msg('返回数据异常！', { icon: 2 });
            return;
        } else {
            editData = item;
        }
        if (editData) {
            //下拉框在文档中出现的顺序很重要，后面的下拉框取值，依赖于前面下拉框的值
            $(".js-initform", $from).each(function() {
                var $this = $(this);
                var id = $this.attr("id");
                var toThound = $this.hasClass("toThound");
                var needEmptyOption = $this.data("empty-option");
                editData[id] = editData[id] == null ? "" : editData[id];
                if (this.nodeName.toLowerCase() == "select") {
                    var options = "";
                    var url = "";

                    if (!$(this).attr("pid")) {
                        url = $this.data("url");
                    } else {
                        var pid = $(this).attr("pid");
                        var id2 = editData[pid];

                        if (id2 != null) {
                            url = $this.data("url") + "&pId=" + id2;
                        } else {
                            url = $this.data("url");
                        }
                    }

                    GLOBAL.ajax("common/selected/query?key=" + url, null, function(data) {
                            if (data) {
                                options = needEmptyOption ? "" : "<option value=''>请选择</option>";
                                var items = data.items;
                                if (items) {
                                    for (var k = 0; k < items.length; k++) {
                                        options += '<option value="' + items[k].code + '">' + items[k].value + '</option>';
                                    }
                                    $this.append(options);
                                    $this.val(editData[id]);
                                }
                            }
                        })
                } else if (this.nodeName.toLowerCase() == "label") {
                    if (editData[id] || editData[id] == 0) {
                        var showValue = toThound && !isNaN(parseFloat(editData[id])) ? parseFloat(editData[id]).toThounds() : editData[id];
                        $this.text(showValue);
                    }
                } else {
                    var showValue = toThound && !isNaN(parseFloat(editData[id])) ? parseFloat(editData[id]).toThounds() : editData[id];
                    $this.val(showValue);
                }
            })
        }
    });
    return editData;
}

//将表单数据序列化为josn对象
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            //if(this.value != "" || this.value == null)  {
            o[this.name] = this.value;
            //}
        }
    });
    return o;
};

//任意数字添加千分位分隔符
Number.prototype.toThounds = function() {
    var number = (this.toFixed(2) || 0).toString();
    var pointIdx = number.indexOf(".");
    var reg = /(\d)(?=(?:\d{3})+$)/g;
    if (pointIdx == -1) {
        return number.replace(reg, '$1,')
    } else {
        var des = number.substr(pointIdx + 1);
        var int = number.substring(0, pointIdx);
        var num = pointIdx == -1 ? number : int;
        return num.replace(reg, '$1,') + "." + des;
    }
}

GLOBAL.local = function(key){
    //存储
    if(typeof arguments[1] != "undefined"){
        objStr = JSON.stringify(arguments[1]);
        localStorage.setItem(key, objStr);
    //获取
    }else{
        var objStr = localStorage.getItem(key);
        if(objStr){
            return JSON.parse(objStr);
        }
    }
}

GLOBAL.restoreQuery = function($table){
    var tableUrl = $table.data("url");
    var searchObj = GLOBAL.local(tableUrl);
    for(var i in searchObj){
        $("#searchForm [name='"+ i +"']").val(searchObj[i]);
    }
    GLOBAL.tableRefresh($table, searchObj);
}

/** ****************************bootstrap table***************************** */
/**
 * [initTable description]
 * @param  {[type]} $table    表格对象（jquery)
 * @param  {[type]} tabCols   表格列
 * @param  {[type]} condition 表格搜索条件，如果需要在页面切换时，保持查询条件，则可将查询条件置于此参数（待扩展）
 * @param  {[type]} jsonData  表格数据来源为json数据，此时，将不再根据表格的data-url指定的参数去后台取数据
 * @return {[type]}           [description]
 */
GLOBAL.initTable = function($table, tabCols, condition, jsonData, moreOpt) {
    for (var i = 0; i < tabCols.length; i++) {

        if(moreOpt && moreOpt.heightLight){
            tabCols[i].formatter = moreOpt.heightLight;
        }
        if (tabCols[i].field == 'opertaList') {
        	//tabCols[i].class = "th-width-180";
            tabCols[i].formatter = function(value, row, index) {
                var optStr = "";
                if(value && value.length){
                     for (var i = 0; i < value.length; i++) {
                        var classStr = CONFIG.opt[value[i].value];
                        optStr += '<span class="btn btn-primary btn-xs mr5 ' + classStr + '" data-url="' + value[i].code + '">' + value[i].value + '</span>'
                    }
                }
                if (optStr != "") {
                	optStr = '<span style="width: 180px;">' + optStr + '</span>';
                }
               return optStr;
            }
        }
        if (tabCols[i].field == 'columnsNumber') {
            tabCols[i].formatter = function(value, row, index) {
                return index + 1;
            }
        }
    }

    var url = $table.data("url");

    var option = {
        method: 'post', // 请求方式（*）
        toolbar: '#toolbar', // 工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: "asc", //排序方式
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 25, //每页的记录行数（*）
        pageList: [25, 45, 75, 100], //可供选择的每页的行数（*）
        strictSearch: true,
        search: true,
        clickToSelect: false, //是否启用点击选中行
        //height: 460,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id", //每一行的唯一标识，一般为主键列
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: tabCols,
        paginationPreText: "上一页",
        paginationNextText: "下一页",
        mobileResponsive: true,
        rowStyle: function(row, index){
            var bgFlag = row.backcolor,
                fontFlag = row.fontcolor;
            var obj = {};
            if(bgFlag || fontFlag){
                var backcolor = ["", "#d0daf2"],
                    fontColor = ["", "red", "blue"];
                obj = {"background-color": backcolor[bgFlag], "color": fontColor[fontFlag]}
                
            }
            return {
                css: obj
            };
        },
        queryParams: function(params) {
            var tmp = {};
            if (condition) {
                condition.per_page = params.limit;
                condition.page = params.offset;
                tmp = condition;
            } else {
                tmp = {
                    per_page: params.limit,
                    page: params.offset
                }
            }
            return tmp;
        }, //传递参数（*）
        responseHandler: function(res) {
            if (res.success) {
                return {
                    "rows": res.items,
                    "total": res.total,
                    "options": res
                };
            } else {
                layer.msg(res.msg, { icon: 2 });
            }
        },
        // onPageChange: function(number, size) {
        //     var opt = $table.bootstrapTable('getOptions');
        //     opt.queryParams = function(params) {
        //         var tmp = {};
        //         if (condition) {
        //             condition.per_page = params.limit;
        //             condition.page = params.offset;
        //             tmp = condition;
        //         } else {
        //             tmp = {
        //                 per_page: params.limit,
        //                 page: params.offset
        //             }
        //         }
        //         return tmp;
        //     }
        // },
        onRefresh: function(){
            var opt = $table.bootstrapTable('getOptions');
            opt.queryParams = function(params) {
                var tmp = {};
                if (condition) {
                    condition.per_page = params.limit;
                    condition.page = params.offset;
                    tmp = condition;
                } else {
                    tmp = {
                        per_page: params.limit,
                        page: params.offset
                    }
                }
                return tmp;
            }
        },
        onLoadSuccess: function(e){
            var e = e || e.options;
            var currentPage = e.options.current_page;
            var perPage = e.options.per_page;
            var page = currentPage / perPage;
            var opt = $table.bootstrapTable('getOptions');
            opt.queryParams = function(params) {
                var tmp = {};
                if (condition) {
                    condition.per_page = params.limit;
                    condition.page = params.offset;
                    tmp = condition;
                } else {
                    tmp = {
                        per_page: params.limit,
                        page: params.offset
                    }
                }
                return tmp;
            }

            moreOpt && moreOpt.onLoadSuccess && moreOpt.onLoadSuccess();

            var $pagination = $table.closest(".fixed-table-body").siblings(".fixed-table-pagination").find("ul.pagination");
            $pagination.find(".page-number.active").removeClass("active");
            $pagination.find("li:eq("+(page + 1)+")").addClass("active");

        }
    };

    moreOpt && $.extend(option, moreOpt);
    if (jsonData) {
        option.data = jsonData;
    } else {
        option.url = GLOBAL.host + url;
    }

    $table.bootstrapTable(option);
}

/**
 * [reportTable description]
 * @param  {object} $table      报表table jquery对象
 * @param  {object} tabCols     columns对象
 * @param  {int} fixedNumber    冻结的列数量，如fixedNumber=3, 则冻结前三列
 * @return {[type]}             [description]
 */
GLOBAL.reportTable = function($table, tabCols, fixedNumber, moreOpt) {

    for (var i = 0; i < tabCols.length; i++) {
        if (tabCols[i].field == 'columnsNumber') {
            tabCols[i].formatter = function(value, row, index) {
                return "<span style='display:block;width:25px'>"+(index + 1)+"</span>";
            }
            break;
        }
    }
    var url = $table.data("url");
    var option = {
        method: 'post', // 请求方式（*）
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        uniqueId: "id", //每一行的唯一标识，一般为主键列
        columns: tabCols,
        fixedColumns: true,
        fixedNumber: fixedNumber,
        mobileResponsive: true,
        queryParams: function(params) {
            return {
                per_page: params.limit,
                page: params.offset
            };
        }, //传递参数（*）
        responseHandler: function(res) {
            if (res.success) {
                return {
                    "rows": res.items,
                    "total": res.total,
                    "options": res
                };
            } else {
                layer.msg(res.msg, { icon: 2 });
            }
        }
    };

    moreOpt && $.extend(option, moreOpt);

    if (!option.data) {
        option.url = GLOBAL.host + url;
    }
    
    $table.bootstrapTable(option);
}

//表格刷新时，添加查询参数，用于分页时带查询参数（常用于点击查询按钮时）
GLOBAL.tableRefresh = function($table, query) {
    var opt = $table.bootstrapTable('getOptions');
    opt.queryParams = function(params) {
        var tmp = {};
        if (query) {
            query.per_page = params.limit;
            query.page = 0;
            tmp = query;
        } else {
            tmp = {
                per_page: params.limit,
                page: params.offset
            }
        }
        return tmp;
    }
    $table.bootstrapTable('refresh', { query: query });
}

//限制数据表格只选择一行，并返回数据行id
GLOBAL.selectId = function($table) {
    var selectedRows = $table.bootstrapTable('getSelections');
    var idsLen = selectedRows.length;
    if (idsLen == 0) {
        layer.msg("请只选择一个！", { icon: 2 });
        return false;
    }
    if (idsLen > 1) {
        layer.msg("请只选择一个！", { icon: 2 });
        return false;
    }
    return selectedRows[0].id;
}

GLOBAL.selectIds = function($table) {
    var selectedRows = $table.bootstrapTable('getSelections');
    var idsLen = selectedRows.length;
    if($table.find("tr").length == 0){
        layer.msg("无数据");
        return false;
    }
    if (idsLen == 0) {
        layer.msg("请选择！", { icon: 2 });
        return false;
    }
    var ids = [];
    for (var i = 0; i < selectedRows.length; i++) {
        ids[i] = selectedRows[i].id;
    }
    return ids.unique().join(",");
}


//重置页面内部的页签链接
GLOBAL.goBack = function(fn) {
    $(".js-back").click(function() {
        fn && fn();
        var url = $(this).data("url");
        GLOBAL.go(url);
    })
}


GLOBAL.permissionBtn = function($btns){
	$btns.each(function() {
		var $this = $(this);
		var permissionUrl = $(this).data("permissionurl");
		if(!permissionUrl){
			return;
		}
        GLOBAL.ajax(GLOBAL.permUrl, {url: permissionUrl}, function(e){
            var isShow = e.success ? "show" : "hide";
            $this[isShow]();
        });
	});
}

GLOBAL.preview = function(option){
    var url = option.url,
        type = option.type;
    var times = 1, hasScale = false;
    var scaleX = 0, scaleY = 0;
    var angle = -1;

    if($(".preview-box").length){
        $(".preview-box").remove();
    }
    var type = type.toLowerCase();
    if(type == "jpg" || type == "png" || type == "gif" || type == "jpeg" || type == "bmp"){
        var $box = $('<div class="preview-box"><div class="toolbox"><span class="original-scale"></span><span class="percent">100%</span><span class="rotate-btn"></span></div><div class="preview-close"><span>+</span></div></div>');
        $box.appendTo($("body"));
        var $img = $('<img src="'+url+'" class="preview-img"/>');
        var timer = null;
        $img.appendTo($box).css({
            left: $(window).width() / 2 - 300
        }).load(function(){
            $box.fadeIn("fast");
            $(this).css("transform","scale(1.1)");
        }).mousedown(function(e){
            e.preventDefault();
            e.stopPropagation();
            var  T = 0, m = 0;
            var ix = e.clientX,
                iy = e.clientY;
            var delx = 0;
                dely = 0;
            var imgX = this.offsetLeft,
                imgY = this.offsetTop;
            
            if(times > 1){
                delx = (times - 1 ) * $(this).width() / 2;//arr[times];
                dely = (times - 1 ) * $(this).height() / 2;
                imgX = this.offsetLeft + delx,
                imgY = this.offsetTop + dely;
            }
            
            $(this).css({
                transition:"none"
            })
            //长按图片时，图片放大
            timer = setInterval(function(){
                T++;
                if(T > 100){
                    clearInterval(timer);
                    $img.css({
                        transition:"all .3s linear",
                        transform: "scale(2)",
                        "transform-origin": (ix - $img.offset().left) +"px " + (iy - $img.offset().top) + "px"
                    })
                    document.onmousemove = null;
                }
            },1);

            document.onmousemove = function(e){
                m++;
                var nx = e.clientX,
                    ny = e.clientY;
                var dx = nx - ix,
                    dy = ny - iy;
                if(Math.abs(dx) > 5 || Math.abs(dy) > 5){
                    clearInterval(timer);
                    T = 0;
                }

                $img.css({
                    left: imgX + dx,
                    top: imgY + dy
                })
            }
            document.onmouseup = function(e){
                document.onmousemove = null;
                document.onmouseup = null;
                clearInterval(timer);
                T = 0;
            }
        })
        
        $img.mousewheel(function(e, delta, deltaX, deltaY) {
            e.preventDefault();
            var wx = e.clientX,
                wy = e.clientY;
            var imgX = $img.offset().left,
                imgY = $img.offset().top;
            var dx = wx - imgX,
                dy = wy - imgY;
            var oldTransform = "";
            angle = 0;
            if(times == 1){
                scaleX = dx,
                scaleY = dy;
            }
            times += .5 * delta;
            hasScale = true;
            times = times <= 1 ? 1 : times;
            $box.find(".percent").text(times * 100 + "%");
            
            $img.css({
                transition:"all 0.1s linear",
                transform: "scale(" + times + ")",
                "transform-origin": scaleX +"px " + scaleY + "px"
            })
        })

        $box.mousedown(function(e){
            e.stopPropagation();
            $(this).fadeOut("fast");
            $img.css({
                transform: "scale(1)",
                transition:"all .1s linear"
            });
        })

        $box.find(".preview-close").click(function(){
            $(this).parent().remove();
        })

        $box.find(".original-scale").mousedown(function(e){
            e.stopPropagation();
            times = 1;
            $img.css({
                transform: "scale(1)",
                transition: "all .1s linear",
                "transform-origin": "center center"
            })
        })

        $box.find(".rotate-btn").mousedown(function(e){
            e.stopPropagation();
            times = 1;
            angle += 90;
            $img.css({
                transform: "rotate("+angle+"deg)",
                transition: "all .1s linear",
                "transform-origin": "center center"
            })
        })

    }else{
        window.open(url);
    }
}


GLOBAL.collapse = function(){
    $(".collapse-title").each(function(){
        var $this = $(this);
        $this.find(".collapse-btn").remove();
        $this.append('<span class="collapse-btn"></span>');
        var $collapseBtn = $this.find(".collapse-btn");
        var $collapseContent = $this.parent().find(".collapse-content");
        if($collapseContent.closest(".collapse-box").length > 0){
            $collapseContent.unwrap(".collapse-box");
        }
        var $box = $collapseContent.wrap('<div class="collapse-box"></div>');
        $collapseBtn.click(function(){
            $(this).toggleClass("expanded");
            $box.toggleClass("hidden");
        })
    })
}

//删除单个
GLOBAL.detItem = function(obj, text) {
    var $this = $(obj);
    var delteSucc = function($this) {
        layer.msg('加载中', { icon: 16, shade: [0.8, '#000'], time: 0 });

        $.get($this.attr("href"), function(data) {
            /*optional stuff to do after success */
            layer.closeAll();
            window.location.reload();
        });
    }

    layer.confirm(text, function() {
        delteSucc($this);
    });

    return false;
}

/**
 * 获取URL参数(类似PHP的$_GET)
 *
 * @param {string} name 参数
 * @param {string} str  待获取字符串
 *
 * @return {string} 参数值
 */
GLOBAL.getParam = function(name, str) {
    var pattern = new RegExp('[\?&]' + name + '=([^&]+)', 'g');
    str = str || location.search;
    var arr, match = '';

    while ((arr = pattern.exec(str)) !== null) {
        match = arr[1];
    }

    return match;
};

GLOBAL.getUrlParams = function(url){
	var p_url = decodeURIComponent(url || location.search);
	var url_search = p_url.substring(p_url.indexOf("?") + 1);
	var nParams = {};
	var params = url_search.split("&");
	for(var i = 0; i < params.length; i++) {
		var key = params[i].split("=")[0];
		var value = params[i].split("=")[1];
		nParams[key] = value;
	}
	return nParams;
},

GLOBAL.floatTool = function() {
    
    /*
     * 判断obj是否为一个整数
     */
    function isInteger(obj) {
        return Math.floor(obj) === obj
    }
    
    /*
     * 将一个浮点数转成整数，返回整数和倍数。如 3.14 >> 314，倍数是 100
     * @param floatNum {number} 小数
     * @return {object}
     *   {times:100, num: 314}
     */
    function toInteger(floatNum) {
        var ret = {times: 1, num: 0}
        if (isInteger(floatNum)) {
            ret.num = floatNum
            return ret
        }
        var strfi  = floatNum + ''
        var dotPos = strfi.indexOf('.')
        var len    = strfi.substr(dotPos+1).length
        var times  = Math.pow(10, len)
        var intNum = parseInt(floatNum * times + 0.5, 10)
        ret.times  = times
        ret.num    = intNum
        return ret
    }
    
    /*
     * 核心方法，实现加减乘除运算，确保不丢失精度
     * 思路：把小数放大为整数（乘），进行算术运算，再缩小为小数（除）
     *
     * @param a {number} 运算数1
     * @param b {number} 运算数2
     * @param digits {number} 精度，保留的小数点数，比如 2, 即保留为两位小数
     * @param op {string} 运算类型，有加减乘除（add/subtract/multiply/divide）
     *
     */
    function operation(a, b, op) {
        var o1 = toInteger(a)
        var o2 = toInteger(b)
        var n1 = o1.num
        var n2 = o2.num
        var t1 = o1.times
        var t2 = o2.times
        var max = t1 > t2 ? t1 : t2
        var result = null
        switch (op) {
            case 'add':
                if (t1 === t2) { // 两个小数位数相同
                    result = n1 + n2
                } else if (t1 > t2) { // o1 小数位 大于 o2
                    result = n1 + n2 * (t1 / t2)
                } else { // o1 小数位 小于 o2
                    result = n1 * (t2 / t1) + n2
                }
                return result / max
            case 'subtract':
                if (t1 === t2) {
                    result = n1 - n2
                } else if (t1 > t2) {
                    result = n1 - n2 * (t1 / t2)
                } else {
                    result = n1 * (t2 / t1) - n2
                }
                return result / max
            case 'multiply':
                result = (n1 * n2) / (t1 * t2)
                return result
            case 'divide':
                return result = function() {
                    var r1 = n1 / n2
                    var r2 = t2 / t1
                    return operation(r1, r2, 'multiply')
                }()
        }
    }
    
    // 加减乘除的四个接口
    function add(a, b) {
        return operation(a, b, 'add')
    }
    function subtract(a, b) {
        return operation(a, b, 'subtract')
    }
    function multiply(a, b) {
        return operation(a, b, 'multiply')
    }
    function divide(a, b) {
        return operation(a, b, 'divide')
    }
    
    // exports
    return {
        add: add,
        subtract: subtract,
        multiply: multiply,
        divide: divide
    }
}();

Number.prototype.add = function(num){
    if(Math.floor(num) === num){
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.add(this, num);
}
Number.prototype.subtract = function(num){
    if(Math.floor(num) === num){
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.subtract(this, num);
}
Number.prototype.multiply = function(num){
    if(Math.floor(num) === num){
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.multiply(this, num);
}
Number.prototype.divide = function(num){
    if(Math.floor(num) === num){
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.divide(this, num);
}


//数组去重
Array.prototype.unique = function() {
    this.sort(); //先排序
    var res = [this[0]];
    for (var i = 1; i < this.length; i++) {
        if (this[i] !== res[res.length - 1]) {
            res.push(this[i]);
        }
    }
    return res;
}

Array.prototype.max = function() {
  var max = this[0];
  var len = this.length;
  for (var i = 1; i < len; i++){
    if (this[i] > max) {
      max = this[i];
    }
  } 
  return max;
}

Array.prototype.min = function() {
  var min = this[0];
  var len = this.length;
  for (var i = 1; i < len; i++){
    if (this[i] < min){
      min = this[i];
    } 
  } 
  return min;
}

//数组查找
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};

//删除数组
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

//扩展browser方法
jQuery.extend({
    browser: function() {
        var rwebkit = /(webkit)\/([\w.]+)/,
            ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/,
            rmsie = /(msie) ([\w.]+)/,
            rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/,
            browser = {},
            ua = window.navigator.userAgent,
            browserMatch = uaMatch(ua);
        if (browserMatch.browser) {
            browser[browserMatch.browser] = true;
            browser.version = browserMatch.version;
        }
        return { browser: browser };
    }
});


if (jQuery.validator) {
    jQuery.extend(jQuery.validator.messages, {
        required: "不能为空",
        remote: "请修正该字段",
        email: "请输入正确格式的电子邮件",
        url: "请输入合法的网址",
        date: "请输入合法的日期",
        dateISO: "请输入合法的日期 (ISO).",
        number: "请输入合法的数字",
        digits: "只能输入整数",
        creditcard: "请输入合法的信用卡号",
        equalTo: "请再次输入相同的值",
        accept: "请输入拥有合法后缀名的字符串",
        maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
        minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
        rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
        range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
        max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
        min: jQuery.validator.format("请输入一个最小为 {0} 的值")
    });

    // 手机号码验证  
    jQuery.validator.addMethod("mobile", function(value, element) {
        var length = value.length;
        var mobile = /^((1[3,5,8][0-9]{1})+\d{8})$/
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请输入合法的手机号码");
    
    // 英文字母和数字
    jQuery.validator.addMethod("alnum", function(value, element){
    	var alnum = /^[a-zA-Z0-9]+$/;
    	return this.optional(element) || alnum.test(value);
    }, "只能包括英文字母和数字");
    
    //身份证验证
    jQuery.validator.addMethod("isIdCardNo", function (value, element){
        return this.optional(element) || isIdCardNo(value);
    }, "请正确输入您的身份证号码");
};


/*
 * @desc 修改url中某个指定的参数的值  url 目标url arg 需要替换的参数名称 arg_val 替换后的参数的值
 * @return url 参数替换后的url
 * @author linchenghao 2015-11-10
 */
function changeURLArg(url, arg, arg_val) {
    var pattern = arg + '=([^&]*)';
    var replaceText = arg + '=' + arg_val;
    if (url.match(pattern)) {
        var tmp = '/(' + arg + '=)([^&]*)/gi';
        tmp = url.replace(eval(tmp), replaceText);
        return tmp;
    } else {
        if (url.match('[\?]')) {
            return url + '&' + replaceText;
        } else {
            return url + '?' + replaceText;
        }
    }
    return url + '\n' + arg + '\n' + arg_val;
}

/*
 * input只能输入四位正小数 
 * 例如：onkeyup="numberFix4Decimal(this)"
 * @param obj
 */
function numberFix4Decimal(obj){
    obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
    obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
    obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个,清除多余的
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/,'$1$2.$3'); //只能输入四位小数
}

/*
 * input只能输入两位正小数 
 * 例如：onkeyup="numberFix2Decimal(this)"
 * @param obj
 */
function numberFix2Decimal(obj){
    obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
    obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
    obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个,清除多余的
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两位小数
}

//增加身份证验证
function isIdCardNo(num) {
    var factorArr = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1);
    var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
    var varArray = new Array();
    var intValue;
    var lngProduct = 0;
    var intCheckDigit;
    var intStrLen = num.length;
    var idNumber = num;
    // initialize
    if ((intStrLen != 15) && (intStrLen != 18)) {
        return false;
    }
    // check and set value
    for (i = 0; i < intStrLen; i++) {
        varArray[i] = idNumber.charAt(i);
        if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
            return false;
        } else if (i < 17) {
            varArray[i] = varArray[i] * factorArr[i];
        }
    }

    if (intStrLen == 18) {
        //check date
        var date8 = idNumber.substring(6, 14);
        if (isDate8(date8) == false) {
            return false;
        }
        // calculate the sum of the products
        for (i = 0; i < 17; i++) {
            lngProduct = lngProduct + varArray[i];
        }
        // calculate the check digit
        intCheckDigit = parityBit[lngProduct % 11];
        // check last digit
        if (varArray[17] != intCheckDigit) {
            return false;
        }
    }
    else {        //length is 15
        //check date
        var date6 = idNumber.substring(6, 12);
        if (isDate6(date6) == false) {
            return false;
        }
    }
    return true;
}
function isDate6(sDate) {
    if (!/^[0-9]{6}$/.test(sDate)) {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    if (year < 1700 || year > 2500) return false
    if (month < 1 || month > 12) return false
    return true
}

function isDate8(sDate) {
    if (!/^[0-9]{8}$/.test(sDate)) {
        return false;
    }
    var year, month, day;
    year = sDate.substring(0, 4);
    month = sDate.substring(4, 6);
    day = sDate.substring(6, 8);
    var iaMonthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    if (year < 1700 || year > 2500) return false
    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
    if (month < 1 || month > 12) return false
    if (day < 1 || day > iaMonthDays[month - 1]) return false
    return true
}

GLOBAL.setDefaultDate = function(diffDate){
    var myDate = new Date(); //获取今天日期
    myDate.setDate(myDate.getDate()+ diffDate);
    return getFullDate(myDate);
}

function getFullDate(targetDate) {
    var D, y, m, d;
    if (targetDate) {
        D = new Date(targetDate);
        y = D.getFullYear();
        m = D.getMonth() + 1;
        d = D.getDate();
    } 
    m = m > 9 ? m : '0' + m;
    d = d > 9 ? d : '0' + d;

    return y + '-' + m + '-' + d;
}

function getTimeByDateStr(dateStr){  
    var year = parseInt(dateStr.substring(0,4));  
    var month = parseInt(dateStr.substring(5,7),10)-1;  
    var day = parseInt(dateStr.substring(8,10),10);  
    return new Date(year, month, day).getTime();  
}  

GLOBAL.setDefaultDate = function(diffDate){
    var myDate = new Date(); //获取今天日期
    myDate.setDate(myDate.getDate()+ diffDate);
    return getFullDate(myDate);
}

GLOBAL.setBeginOfMonth = function(){
    var myDate = new Date(); //获取今天日期
    myDate.setDate(1);
    return getFullDate(myDate);
}

GLOBAL.setEndOfMonth = function(){
	var myDate = new Date(); //获取今天日期
	var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
	var endOfMonth = new Date(year, month, 0).getDate(); // 获取本月最后一天
    var cloneDate = new Date();
    cloneDate.setDate(endOfMonth);
    return getFullDate(cloneDate);
}

GLOBAL.getIntervalDays = function(dateStr1, dateStr2) {
	var time1 = getTimeByDateStr(dateStr1);
	var time2 = getTimeByDateStr(dateStr2);
	return (time2 - time1)/(24*60*60*1000);
}

GLOBAL.clearSearch = function() {
	$("#searchForm select").val(null).trigger("change");
}

$(document).keypress(function(e) {
    var eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
     if (eCode == 13){
         var o = $(this).find("[id='btnSearch']");	//查询按钮回车事件
         if (o.length > 0) {
        	 o[0].click();
         } 
     }
});
