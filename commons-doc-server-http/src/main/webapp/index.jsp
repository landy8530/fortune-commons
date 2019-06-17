<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		/*
			$(function() {
				$('#uploadify').uploadify({
					'formData'     : {
						'timestamp' : '11111',
						'token'     : '2222222222'
					},
					'swf'      : '${jquery}/uploadify/uploadify.swf',
					'uploader' : '${base}/upload.do',
					'buttonText ':"选择",
		            'auto': false,
		            'multi': true
		            
				});
			});*/

		

		function doStartUpload() {
			$('#uploadify').uploadify('upload', '*')
		}
		function doStopUpload() {
			$('#uploadify').uploadify('cancel', "*");
		}
		$(function(){
			
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