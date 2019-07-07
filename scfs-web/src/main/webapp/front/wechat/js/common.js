//全局对象
window.GLOBAL = {};

window.MOBILE = {};

GLOBAL.host = "https://www.scferp.com/scfs/"; //生产环境or测试环境
GLOBAL.selectUrl = 'common/selected/query?key=';
GLOBAL.permUrl = 'common/perm/query';

GLOBAL.collapse = function () {
    $(".collapse-title").each(function () {
        var $this = $(this);
        var $collapseContent = $this.next(".collapse-content");
        if($collapseContent.length <= 0){
            $collapseContent = $this.next().next(".collapse-content");
        }
        if ($collapseContent.closest(".collapse-box").length > 0) {
            $collapseContent.unwrap(".collapse-box");
        }
        var $box = $collapseContent.wrap('<div class="collapse-box"></div>');
        $this.click(function () {
            $(this).toggleClass("expanded");
            console.log($box);
            $box.toggleClass("hidden");
        })
    })
}


MOBILE.confirm = function (msg, fn) {
    $(".msg-box").remove();
    var html = '<div class="msg-box"><div class="m-mask"></div><div class="m-content-box msg-anim"><div class="m-title"></div><div class="m-msg-text">' + msg + '</div><div class="m-footer"><button class="sure-btn" style="margin-right:20px;">确定</button><button class="cancel-btn">取消</button></div></div></div>';
    var $box = $(html);
    $box.appendTo($("body"));
    var $sureBtn = $box.find(".sure-btn");
    var $cancelBtn = $box.find(".cancel-btn");
    $sureBtn.click(function () {
        $box.remove();
        fn && fn();
    })
    $cancelBtn.click(function () {
        $box.remove();
    })
}

MOBILE.msg = function (msg, fn) {
    $(".msg-box").remove();
    var html = '<div class="msg-box"><div class="m-mask"></div><div class="m-content-box msg-anim"><div class="m-title"></div><div class="m-msg-text">' + msg + '</div><div class="m-footer"><button class="sure-btn">确定</button></div></div></div>';
    var $box = $(html);
    $box.appendTo($("body"));
    var $button = $box.find(".sure-btn");
    setTimeout(function () {
        $box.remove();
        fn && fn();
    }, 3000);
    $button.click(function () {
        $box.remove();
        fn && fn();
    })
}
/**
 * 如果不需要请求参数，则第二个参数设置为null
 * 如果要提交请求时，需要将按钮禁用，请将第5个参数设置为要禁用的按钮（jq对象）
 * 如果需要指定contentType, 请将第四个参数设置为true
 */
GLOBAL.ajax = function (url, data, callBack, contentType, $btn, isAsync) {
    var isAsync = isAsync && isAsync == true ? true : false;
    var d = {'s': 1, 'redirectURL': base64encode(utf16to8(location.href))};
    if (data == null) {
        data = d;
    }else{
        if(data instanceof Object ){
            data = $.extend({},  data, d);
        }else{
            url=url+"?s=1&redirectURL="+base64encode(utf16to8(location.href));
            // data = $.extend({}, JSON.parse(data), d);
            // data = JSON.stringify(data);
        }
    }

    var options = {
        url: GLOBAL.host + url,
        type: "post",
        data: data,
        dataType: "json",
        async: isAsync,
        timeout: 5000,
        //contentType: "application/json",
        success: function (e) {
            if (e.login) {
                location.href = e.redirectURL;
                return;
            }

            if (e && e.success != undefined) {
                if (!e.success && e.msg) {
                    MOBILE.msg(e.msg);
                } else {
                    if (e.items) {
                        for (var i = 0; i < e.items.length; i++) {
                            for (var j in e.items[i]) {
                                e.items[i][j] = e.items[i][j] == null ? "" : e.items[i][j];
                            }
                        }
                    }
                    callBack && callBack(e);
                }
                //拉取下拉框选项时走这里
            } else {
                if (e.items) {
                    for (var i = 0; i < e.items.length; i++) {
                        for (var j in e.items[i]) {
                            e.items[i][j] = e.items[i][j] == null ? "" : e.items[i][j];
                        }
                    }
                }
                callBack && callBack(e);
            }

            if ($btn) {
                setTimeout(function () {
                    $btn.removeClass("processing");
                }, 2500)
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            var status = XMLHttpRequest.status;
            switch (status) {
                case 400:
                    MOBILE.msg("请求参数有误");
                    break;
                case 403:
                    MOBILE.msg("请求被禁止");
                    break;
                case 404:
                    MOBILE.msg("请求的资源不存在");
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
                            MOBILE.msg("请求超时，请重试");
                        }
                    } catch (e) {
                        MOBILE.msg(response_text);
                    }
                    break;
                default:
                    MOBILE.msg("服务器不可用");
            }
            if ($btn) {
                setTimeout(function () {
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
GLOBAL.initSelect = function () {
    $(".js-select").each(function () {
        var $this = $(this);
        var key = $this.data("url");
        var needEmptyOption = $this.data("empty-option");
        if (!key) {
            return;
        }
        var options = needEmptyOption ? "" : "<option value=''></opotion>";
        GLOBAL.ajax(GLOBAL.selectUrl + key, null, function (data) {
            var items = data.items;
            if (items) {
                for (var k = 0; k < items.length; k++) {
                    options += '<option value="' + items[k].code + '">' + items[k].value + '</opotion>';
                }
                $this.append(options);
            }
        });

        $this.change(function () {
            var thisValue = $(this).val();
            var thisId = $(this).attr("id");
            $("[pid='" + thisId + "']").each(function () {
                var $this = $(this),
                    key = $this.data("url");
                var needEmptyOption = $this.data("empty-option");
                if (!key) {
                    return;
                }
                var options = needEmptyOption ? "" : "<option value=''></opotion>";
                GLOBAL.ajax(GLOBAL.selectUrl + key + "&pId=" + thisValue, null, function (data) {
                    var items = data.items;
                    if (items) {
                        for (var k = 0; k < items.length; k++) {
                            options += '<option value="' + items[k].code + '">' + items[k].value + '</opotion>';
                        }
                        $this.empty().append(options).change();
                    }
                })
            })
        })
    })

    $("[pid]").change(function () {
        var $this = $(this);
        var thisId = $this.attr("id");
        var thisValue = $this.val();
        $("[pid='" + thisId + "']").each(function () {
            var $this = $(this),
                url = $this.data("url");
            var needEmptyOption = $this.data("empty-option");
            if (!url) {
                return;
            }
            var options = needEmptyOption ? "" : "<option value=''></opotion>";
            GLOBAL.ajax(GLOBAL.selectUrl + url + "&pId=" + thisValue, null, function (data) {
                var items = data.items;
                if (items) {
                    for (var k = 0; k < items.length; k++) {
                        options += '<option value="' + items[k].code + '">' + items[k].value + '</opotion>';
                    }
                }
                $this.empty().append(options).change();
            })
        })
    })
}

//初始化表单数据
GLOBAL.getEditData = function ($from, dataId) {
    //编辑页面表单控件值初始化
    var formUrl = $from.data("url");
    var editData = null;
    GLOBAL.ajax(formUrl, {id: dataId, ids: dataId}, function (e) {
        var item = e.items;
        if (!item) {
            return;
        }
        if (item.length == 1) {
            editData = item[0];
        } else if (item.length > 1) {
            MOBILE.msg('返回数据异常！', {icon: 2});
            return;
        } else {
            editData = item;
        }
        if (editData) {
            //下拉框在文档中出现的顺序很重要，后面的下拉框取值，依赖于前面下拉框的值
            $(".js-initform", $from).each(function () {
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

                    GLOBAL.ajax("common/selected/query?key=" + url, null, function (data) {
                        if (data) {
                            options = needEmptyOption ? "" : "<option value=''></opotion>";
                            var items = data.items;
                            if (items) {
                                for (var k = 0; k < items.length; k++) {
                                    options += '<option value="' + items[k].code + '">' + items[k].value + '</opotion>';
                                }
                                $this.append(options);
                                $this.val(editData[id]);
                            }
                        }
                    })
                    // $this.change(function(){
                    //     var thisValue = $(this).val();
                    //     var thisId = $(this).attr("id");
                    //     $("[pid='"+thisId+"']").each(function(){
                    //         var $_this = $(this),
                    //         key = $_this.data("url"),
                    //         _options = "<option value=''></opotion>";
                    //         GLOBAL.ajax(GLOBAL.selectUrl + key + "&pId=" + thisValue, null, function(data){
                    //             for(var k = 0; k < data.length; k++){
                    //                 _options += '<option value="'+data[k].code+'">'+data[k].value+'</opotion>';
                    //             }
                    //             $_this.empty().append(_options);
                    //         })
                    //     })
                    // })
                } else if (this.nodeName.toLowerCase() == "label") {
                    if (editData[id]) {
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
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
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
Number.prototype.toThounds = function () {
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

GLOBAL.permissionBtn = function ($btns) {
    $btns.each(function () {
        var $this = $(this);
        var permissionUrl = $(this).data("permissionurl");
        if (!permissionUrl) {
            return;
        }
        GLOBAL.ajax(GLOBAL.permUrl, {url: permissionUrl}, function (e) {
            var isShow = e.success ? "show" : "hide";
            $this[isShow]();
        });
    });
}

/**
 * 获取URL参数(类似PHP的$_GET)
 *
 * @param {string} name 参数
 * @param {string} str  待获取字符串
 *
 * @return {string} 参数值
 */
GLOBAL.getParam = function (name, str) {
    var pattern = new RegExp('[\?&]' + name + '=([^&]+)', 'g');
    str = str || location.search;
    var arr, match = '';

    while ((arr = pattern.exec(str)) !== null) {
        match = arr[1];
    }

    return match;
};

Array.prototype.max = function () {
    var max = this[0];
    var len = this.length;
    for (var i = 1; i < len; i++) {
        if (this[i] > max) {
            max = this[i];
        }
    }
    return max;
}

Array.prototype.min = function () {
    var min = this[0];
    var len = this.length;
    for (var i = 1; i < len; i++) {
        if (this[i] < min) {
            min = this[i];
        }
    }
    return min;
}

GLOBAL.floatTool = function () {

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
        var strfi = floatNum + ''
        var dotPos = strfi.indexOf('.')
        var len = strfi.substr(dotPos + 1).length
        var times = Math.pow(10, len)
        var intNum = parseInt(floatNum * times + 0.5, 10)
        ret.times = times
        ret.num = intNum
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
                return result = function () {
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

Number.prototype.add = function (num) {
    if (Math.floor(num) === num) {
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.add(this, num);
}
Number.prototype.subtract = function (num) {
    if (Math.floor(num) === num) {
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.subtract(this, num);
}
Number.prototype.multiply = function (num) {
    if (Math.floor(num) === num) {
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.multiply(this, num);
}
Number.prototype.divide = function (num) {
    if (Math.floor(num) === num) {
        new Error("运算数不是Float类型, 请检查");
    }
    return GLOBAL.floatTool.divide(this, num);
}


/*
 * Interfaces:
 * utf8 = utf16to8(utf16);
 * utf16 = utf8to16(utf8);
 */
function utf16to8(str) {
    var out, i, len, c;
    out = "";
    len = str.length;
    for(i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
    }
    return out;
}

/*
 * Interfaces:
 * b64 = base64encode(data);
 * data = base64decode(b64);
 */
var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var base64DecodeChars = new Array(
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);
function base64encode(str) {
    var out, i, len;
    var c1, c2, c3;
    len = str.length;
    i = 0;
    out = "";
    while(i < len) {
        c1 = str.charCodeAt(i++) & 0xff;
        if(i == len)
        {
            out += base64EncodeChars.charAt(c1 >> 2);
            out += base64EncodeChars.charAt((c1 & 0x3) << 4);
            out += "==";
            break;
        }
        c2 = str.charCodeAt(i++);
        if(i == len)
        {
            out += base64EncodeChars.charAt(c1 >> 2);
            out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
            out += base64EncodeChars.charAt((c2 & 0xF) << 2);
            out += "=";
            break;
        }
        c3 = str.charCodeAt(i++);
        out += base64EncodeChars.charAt(c1 >> 2);
        out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
        out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));
        out += base64EncodeChars.charAt(c3 & 0x3F);
    }
    return out;
}
function base64decode(str) {
    var c1, c2, c3, c4;
    var i, len, out;
    len = str.length;
    i = 0;
    out = "";
    while(i < len) {
        /* c1 */
        do {
            c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
        } while(i < len && c1 == -1);
        if(c1 == -1)
            break;
        /* c2 */
        do {
            c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
        } while(i < len && c2 == -1);
        if(c2 == -1)
            break;
        out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
        /* c3 */
        do {
            c3 = str.charCodeAt(i++) & 0xff;
            if(c3 == 61)
                return out;
            c3 = base64DecodeChars[c3];
        } while(i < len && c3 == -1);
        if(c3 == -1)
            break;
        out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
        /* c4 */
        do {
            c4 = str.charCodeAt(i++) & 0xff;
            if(c4 == 61)
                return out;
            c4 = base64DecodeChars[c4];
        } while(i < len && c4 == -1);
        if(c4 == -1)
            break;
        out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
    }
    return out;
}