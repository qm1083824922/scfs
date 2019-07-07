/*
 * IE Alert! jQuery plugin
 * Version 2.1
 * Author: David Nemes | @nmsdvid
 * http://nmsdvid.com/iealert/
 */

(function ($) {
    function initialize($obj, support, title, text, overlayClose, closeBtn){
        var panel = "";
        if(closeBtn){panel = "<i class='close-btn'></i>";}
        panel += "<span>" + title + "</span>";
        panel += "<p> " + text + "</p>";
        panel += "<div class='browser'>";
        panel += "<ul>";
        panel += "<li><a class='chrome' href='https://www.google.com/chrome/' target='_blank'></a></li>";
        panel += "<li><a class='firefox' href='http://www.mozilla.org/en-US/firefox/new/' target='_blank'></a></li>";
        panel += "<li><a class='ie9' href='http://windows.microsoft.com/en-US/internet-explorer/downloads/ie/' target='_blank'></a></li>";
        panel += "<li><a class='safari' href='http://www.apple.com/safari/download/' target='_blank'></a></li>";
        panel += "<li><a class='opera' href='http://www.opera.com/download/' target='_blank'></a></li>";
        panel += "</ul>";
        panel += "</div>";

        var overlay = $("<div id='ie-alert-overlay'></div>");
        var iepanel = $("<div id='ie-alert-panel'>" + panel + "</div>");

        var docHeight = $(document).height();

        overlay.css("height", docHeight);


        function active() {
            $obj.prepend(iepanel);
            $obj.prepend(overlay);

            var iePanel = $('#ie-alert-panel'),
                ieOverlay = $('#ie-alert-overlay'),
                closeOverLay = iePanel.find(".close-btn");

            if (closeBtn === true) {
                closeOverLay.click(function () {
                    iePanel.fadeOut(100);
                    ieOverlay.fadeOut("slow");
                });
            }

            if (overlayClose === true) {
                ieOverlay.click(function () {
                    iePanel.fadeOut(100);
                    $(this).fadeOut("slow");
                });
            }

            if (ie === 6) {
                iepanel.addClass("ie6-style");
                overlay.css("background", "#d6d6d6");
                $obj.css("margin", "0");
            }
        }

        if (support === "ie9") {            // the modal box will appear on IE9, IE8, IE7, IE6
            if (ie < 10) {
                active();
            }
        } else if (support === "ie8") {     // the modal box will appear on IE8, IE7, IE6
            if (ie < 9) {
                active();
            }
        } else if (support === "ie7") {     // the modal box will appear on IE7, IE6
            if (ie < 8) {
                active();
            }
        } else if (support === "ie6") {     // the modal box will appear only on IE6 and below
            if (ie < 7) {
                active();
            }
        }

    }

    ; //end initialize function

    $.fn.iealert = function (options) {
        var defaults = {
            support:"ie8",
            title:"你知道你的Internet Explorer过时了吗?",
            text:"本系统基于bootstrap3.0开发，对低版本IE浏览器兼容不太理想，为了更好的体验本系统,我们建议您升级到最新版本的IE浏览器或选择另一个web浏览器，下面是一些高级浏览器供你选择.",
            overlayClose:true,
            closeBtn: true
        };

        var option = $.extend(defaults, options);

        return this.each(function () {
        	
	    	ie = (function(){
	 
			    var undef,
			        v = 3,
			        div = document.createElement('div'),
			        all = div.getElementsByTagName('i');
			    
			    while (
			        div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->',
			        all[0]
			    );
			    
			    return v > 4 ? v : undef;
	    
	    	 }());

	    	 // If browser is Internet Explorer
             if (ie >= 5) {
                var $this = $(this);
                initialize($this, option.support, option.title, option.text,option.overlayClose, option.closeBtn);
             }

        });

    };
})(jQuery);
