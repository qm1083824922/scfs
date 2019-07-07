/**
 * 自定义文件上传插件
 * 任意jquery对象都可调用此插件，从而转换成上传控件
 */
(function(){

	var $ELEMENT = null;
	var MAXFILENUMBER = 3;
	var timer = 0;
	var watcher = null;
	var fileAccept = "";
	var xhr = new XMLHttpRequest();
	var response = null;

	$.fn.upload = function(opts){
		var defaults = {};
		var options = $.extend(defaults, opts);
		if(options.fileType){
			var fileType = {
				"img": "image/jpg,image/jpeg,image/png,image/gif,image/bmp",
				"doc": "application/msword",
				"docx": "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				"pdf": "application/pdf",
				"ppt": "application/vnd.ms-powerpoint",
				"xls": "application/vnd.ms-excel",
				"zip": "aplication/zip",
				"xlsx": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				"txt": "text/plain"
			}
			var fileAcceptArray = options.fileType.split(",");
			for(var ftype = 0; ftype < fileAcceptArray.length; ftype++){
				fileAccept += fileType[$.trim(fileAcceptArray[ftype])]
				fileAccept += ","
			}
			fileAccept = fileAccept.substring(0, fileAccept.length - 1);
		}

		return this.each(function(){
			var $this = $(this);
			var isMultiple = options.isMultiple == false ? "" : "multiple";
            var newHtml = '<div class="upload-box"><input type="file" name="file" accept="'+fileAccept+'" ' + isMultiple + ' style="display:none"><span class="icon-plus">+</span><span class="upload-txt">上传</span></div>';
            var $newUploadBtn = $(newHtml);
            $(this).replaceWith($newUploadBtn);
            $ELEMENT = $newUploadBtn.find('[type="file"]');
            $newUploadBtn.click(function(){
            	$ELEMENT.click();
            })
            $ELEMENT.click(function(e){
            	e.stopPropagation();
            }).change(function() { 
				var fd = new FormData();// html5新增的对象,可以包装字符,二进制信息
				var files = this.files;
				var html = '<div class="upload-preview-box"><div class="preview-box-body"><ul class="file-list-box clearfix">';
				var fileAttrs = [];
				if(options.param){
					for(var i in options.param){
						fd.append(i, options.param[i]);
					}	
				}
				var fl = Math.min(files.length, MAXFILENUMBER);

				for(var j = 0; j < fl; j++){
					var fileName = files[j].name;
					var fileSize = files[j].size;
					var fileType = files[j].type;
					fd.append(this.name, files[j]);
					
					fileAttrs.push({
						"name": fileName,
						"size": fileSize,
						"type": fileType
					})
					var reader = new FileReader();
			        reader.readAsDataURL(files[j]);
			        reader.onload = function (e) {
			        	var filePreview = '<p class="is-img-file" style="background-image:url('+this.result+'); background-size:cover"></p>';
			        	html += '<li title="'+fileName+'">'+filePreview+'<p class="file-title">'+fileName+'</p></li>';
			        }
				}
				
				setTimeout(function(){
					html += '</ul></div><div class="preview-box-footer"><p><span class="progress-box"><span class="percent">0%</span><i class="progress-bar"></i></span></p><button class="btn start btn-sm btn-primary mt10 mb5">开始上传</button></div></div>';
					layer.closeAll();

					if(files.length){
						layer.ready(function(){
							var idx = layer.open({
								type: 1,
							  	shade: false,
							  	title: false, //不显示标题
							  	shade: [0.1, '#aaa'],
							  	content: html, //捕获的元素
							  	cancel: function(){
							  		$ELEMENT.val(null);
							  	}
							})
							for(var k = 0; k < fileAttrs.length; k++){
								$('.file-list-box li:eq(' + (k) + ')').attr("title", fileAttrs[k].name)
								$('.file-list-box li:eq(' + (k) + ')').find(".file-title").text(fileAttrs[k].name);
							}
							var boxW = (files.length > MAXFILENUMBER ? MAXFILENUMBER : files.length) * 175;
							layer.style(idx, {
							  width: boxW >= 350 ? boxW : 350
							})
						})
					}

					$("button.start").click(function(){
						upload(options, fd);
						watcher = setInterval(function(){
							console.log(timer);
							if(timer >= 60 && !response){
								clearInterval(watcher);
								xhr.abort();
								layer.msg("请求超时，请重试！",{icon: 2}, function(){
									layer.closeAll();
								});
							}
							timer++;
						},1000)
					})

				}, 100);
			});
		});
	};

	function upload(options, fd) {
		xhr.open('POST', options.url, true);
		// 异步传输  xhr.upload 这是html5新增的api,储存了上传过程中的信息
		xhr.upload.onprogress = function(ev) {
			var percent = 0;
			if (ev.lengthComputable) {
				percent = 100 * ev.loaded / ev.total;
				$('.percent').text(parseInt(percent) + "%");
				$(".progress-bar").width(percent + '%');

				var newPro = percent >= 100 ? 88 : percent;
				$('.percent')[0].style.left = newPro + "%";
				if(percent >= 100 ){
					layer.load(3);
				}
			}
		};
		xhr.onload = function(e){
			clearInterval(watcher);
			$ELEMENT.val(null);
			response = JSON.parse(e.target.response);
			var isSuccess = response.success;

			layer.closeAll();
			if(isSuccess){
				layer.msg("上传成功！");
				if(options.success){
					setTimeout(function(){
						layer.closeAll();
					},2000);
					options.success();
				}
			}else if(response.msg){
				layer.msg(response.msg, {icon: 2}, function(){
					layer.closeAll();
				});
			}
		}

		xhr.onerror = function(e){
			clearInterval(watcher);
			$ELEMENT.val(null);
			layer.msg("上传错误！", {icon: 2}, function(){
				layer.closeAll();
			});
		}
		xhr.send(fd);
	}

	var preTimer = null;
	$("body").on("mouseenter", ".is-img-file", function(e){
		var _this = this;
		preTimer = setTimeout(function(){
			var base64 = _this.style["background-image"].split('"')[1];
			if($("#preImg").length){
				$("#preImg").attr("src", base64);
			}else{
				$("body").append('<img src="'+base64+'" id="preImg" style="position:absolute;top:100px;left:100px;width:400px;box-shadow:0 0 20px 2px rgba(100,100,100,.6);z-index:999999999;">');
			}
		},300)
	}).on("mouseleave", ".is-img-file", function(e){
		clearTimeout(preTimer);
		$("#preImg").remove();
	})

})(jQuery);