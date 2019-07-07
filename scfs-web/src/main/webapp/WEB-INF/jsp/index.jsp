<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
</head>
<form action="/scf/login" method="post">
	名称：<input type="text" name="userName" /><br /> 密码:<input type="text"
		name="password" /> <br /> <input type="submit" name="提交" value="提交">
</form>

<p />
<p />
<p />

<form id="formFile" action="/scf/po/import" method="post"
	enctype="multipart/form-data">
	导入excel: <input type="file" name="importFile" id="importFile" /> <input
		type="submit" name="提交" value="提交">
</form>

</html>