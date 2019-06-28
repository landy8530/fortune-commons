<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags"  %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<page:header>
<link rel="stylesheet" type="text/css" href="${jquery}/uploadify/uploadify.css">
<script src="${jquery}/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
<style type="text/css">
body {
	font: 13px Arial, Helvetica, Sans-serif;
}
</style>
	<script type="text/javascript">
		function flashChecker() {
			var hasFlash = 0;　　　　 //是否安装了flash
			var flashVersion = 0;　　 //flash版本

			if(document.all) {
				var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
				if(swf) {
					hasFlash = 1;
					VSwf = swf.GetVariable("$version");
					flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
				}
			} else {
				if(navigator.plugins && navigator.plugins.length > 0) {
					var swf = navigator.plugins["Shockwave Flash"];
					if(swf) {
						hasFlash = 1;
						var words = swf.description.split(" ");
						for(var i = 0; i < words.length; ++i) {
							if(isNaN(parseInt(words[i]))) continue;
							flashVersion = parseInt(words[i]);
						}
					}
				}
			}
			return { f: hasFlash, v: flashVersion };
		}




		function doStartUpload() {
			$('#uploadify').uploadify('upload', '*')
		}
		function doStopUpload() {
			$('#uploadify').uploadify('cancel', "*");
		}
		$(function(){
			var fls = flashChecker();
			var s = "";
			if(!fls.f) {
				$("#getflash").show();
				$(".video-box-1").hide();
			}
			$("#uploadify").uploadify(
					{
						'height' : 27,
						'width' : 80,
						'buttonText' : '选择图片',
						'swf' : '${jquery}/uploadify/uploadify.swf',
						'uploader' : '${base}/uploadImageDoc.do',
						'auto' : false,
						'multi' : true,
						'removeCompleted' : true,
						'cancelImg' : '${jquery}/uploadify/cancel.png',
						'fileTypeExts' : '*.jpg;*.jpge;*.gif;*.png',
						'fileSizeLimit' : '2MB',
						'uploadLimit' : 20,
						'onUploadSuccess' : function(file, data, response) {
							var json;
							eval("json="+data);
							var path=json.path;
							var imglist=$("#imglist")
							var rootpath=imglist.attr("rootpath");
							var img="<img   width='150' height='150' src='"+rootpath+"/"+path+"' /> <br/>"
							$("#imglist").append(img);
						},
						//加上此句会重写onSelectError方法【需要重写的事件】
						'overrideEvents' : [ 'onSelectError', 'onDialogClose' ],
						//返回一个错误，选择文件的时候触发
						'onSelectError' : function(file, errorCode, errorMsg) {
							alert("文件上传失败")
						},
						'onUploadComplete' : function(file) {
							//alert(file.name)
						}
					});			
		})

	</script>


</page:header>





<body>
<div id="getflash" style="display: none;text-align: center;margin-top: 150px;">
	<!-- <a href="http://www.adobe.com/go/getflashplayer" rel="nofollow" target="_blank" title="升级Flash插件">启用flash</a> -->
	<p><i class="iconfont_video_details" style="font-size: 30px;">&#xe625;</i></p>
	<a href="https://get.adobe.com/cn/flashplayer/" target="_blank">您的计算机尚未安装Flash，点击安装https://get.adobe.com/cn/flashplayer/</a>
</div>
<h1>测试图片上传</h1>
	<table width="100%" align="center" style="padding-left: 20px">
	  <tr>
	    <td width="30%" valign="top" >
		    <div class="col-xs-12">
		    	<div id="queue"></div>
				<input id="uploadify" name="file_upload" type="file" multiple="true">
					<a href="javascript:doStartUpload()">上传</a>| <a
						href="javascript:doStopUpload()">取消上传</a>
			</div>			
	    </td>
	    <td id="imglist" rootpath="${base}/attached">
	    </td>
	  </tr>
	</table>
</body>
</html>