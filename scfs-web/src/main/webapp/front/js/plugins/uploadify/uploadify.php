<?php
header( 'Content-Type:text/html;charset=utf-8 ');
/*
Uploadify
Copyright (c) 2012 Reactive Apps, Ronnie Garcia
Released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/

// Define a destination

/*$targetFolder = $_SERVER['DOCUMENT_ROOT'].'/uploads/json.json';
$current = json_encode($_POST);
file_put_contents($targetFolder, $current, FILE_APPEND);

$verifyToken = md5('unique_salt' . $_POST['timestamp']);

file_put_contents($targetFolder, $verifyToken, FILE_APPEND);

file_put_contents($targetFolder, json_encode($_FILES), FILE_APPEND);
file_put_contents($targetFolder, json_encode($_SERVER), FILE_APPEND);*/

/*
$logFile = $_SERVER['DOCUMENT_ROOT'].DIRECTORY_SEPARATOR.'uploads'.DIRECTORY_SEPARATOR.'json.json';

$targetFolder = DIRECTORY_SEPARATOR.'uploads'.DIRECTORY_SEPARATOR.$_POST['folder']; // Relative to the root

$verifyToken = md5('unique_salt' . $_POST['timestamp']);

if (!empty($_FILES) && $_POST['token'] == $verifyToken) {
	$tempFile = $_FILES['Filedata']['tmp_name'];
	$targetPath = $_SERVER['DOCUMENT_ROOT'] . $targetFolder;
	$targetFile = rtrim($targetPath,DIRECTORY_SEPARATOR) . DIRECTORY_SEPARATOR  . $_FILES['Filedata']['name'];

	// Validate the file type
	$fileTypes = array('jpg','jpeg','gif','png'); // File extensions
	$fileParts = pathinfo($_FILES['Filedata']['name']);

	if (in_array($fileParts['extension'],$fileTypes)) {
		move_uploaded_file($tempFile,$targetFile);
		file_put_contents($logFile, 'success'.time().'|'.$targetFile, FILE_APPEND);
		echo $targetFolder . DIRECTORY_SEPARATOR  . $_FILES['Filedata']['name'];
	}
	else {
		file_put_contents($logFile, 'fail'.time(), FILE_APPEND);
		echo 'Invalid file type.';
	}
}
*/

$logFile = $_SERVER['DOCUMENT_ROOT'].'/uploads'.'/json.json';

$targetFolder = '/uploads/'.$_POST['folder'].'/'.date('Y').'/'.date('m').'/'.date('d'); // Relative to the root

$verifyToken = md5('unique_salt' . $_POST['timestamp']);

if (!empty($_FILES) && $_POST['token'] == $verifyToken) {
	$tempFile = $_FILES['Filedata']['tmp_name'];
	$targetPath = $_SERVER['DOCUMENT_ROOT'] . $targetFolder;
    $suffix = strrchr($_FILES['Filedata']['name'], '.');  //文件后缀

    //目录不存在创建
    if (!file_exists($targetPath)) {
        mkdir($targetPath,0777,true);
    }

    //字符串长度
    $count = strlen($_FILES['Filedata']['name']);
    $mbCount = mb_strlen($_FILES['Filedata']['name'], 'utf8');

    //文件名为纯英文、纯数字、英数混排
    if($count === $mbCount){
        $midCount = strrpos($_FILES['Filedata']['name'], '.');
        $dirName = substr($_FILES['Filedata']['name'], 0, $midCount);
    }else{
        //含有中文字符
        $midCount = mb_strrpos($_FILES['Filedata']['name'], '.', 0, 'utf8');
        $dirName = mb_substr($_FILES['Filedata']['name'], 0, $midCount, 'utf8');
    }

    $newPath = $dirName .'_'. time().rand(100,999) . $suffix; //新的文件名, 数据库保存的

	// Validate the file type
	$fileTypes = array('jpg','JPG','jpeg','gif','png','doc','txt','xls','rar','pdf','xlsx','bmp'); // File extensions
	$fileParts = pathinfo($_FILES['Filedata']['name']);

	if (in_array($fileParts['extension'],$fileTypes)) {

        $sysNewPath = (DIRECTORY_SEPARATOR == '\\') ? iconv('UTF-8', 'gb2312//IGNORE', $newPath) : $newPath; //根据不用系统转译编码
        // $sysNewPath = $newPath;
        $sysTargetFile = rtrim($targetPath,'/') . '/' . $sysNewPath; //最后在系统中显示的文件名


        //php.ini upload_max_filesize 修改为10M(默认2M) hejiao 2016-01-26
		$rs = move_uploaded_file($tempFile,$sysTargetFile);
        if($rs){
            file_put_contents($logFile, '{"status":"true","time":"' . date('Y-m-d H:i:s') . '","path":"' . $sysTargetFile . '"}'.PHP_EOL, FILE_APPEND);
            echo $targetFolder . '/' . $newPath;
        } else {
            file_put_contents($logFile, '{"status":"false","time":"' . date('Y-m-d H:i:s') . '"}'.PHP_EOL, FILE_APPEND);
            file_put_contents($logFile, 'fail'.time(), FILE_APPEND);
            echo 'failed';
        }
	}
	else {
		file_put_contents($logFile, '{"status":"false","time":"' . date('Y-m-d H:i:s') . '"}'.PHP_EOL, FILE_APPEND);
        file_put_contents($logFile, 'fail'.time(), FILE_APPEND);
		echo 'Invalid file type';
	}
}


/*
if (!empty($_FILES)) {

    //把上次完成的图片文件名返回给前台
    //'name' 就是之前写 《jquery.uploadify动态传递表单元素》里面动态附加值
    // echo $_FILES['Filedata']['name'];
    $tmpDir = $_POST['note'];
    $fileName = iconv("UTF-8","GB2312",$_FILES["Filedata"]["name"]);
    //echo $fileName;
    $file = $_FILES["Filedata"]["tmp_name"];
    //fwrite($f,$tmpDir);
    //复制原图
    $path = "uptemp/".$tmpDir."_org/";
    //echo $path;
    if(!is_dir($path))
       mkdir($path);
    if (copy($file, $path . $fileName)){
        $_ft =$file;
    }else{
        $_ft ="no";
    }

    //生成缩略图
    $fileName = str_replace(" ","\ ",$fileName);
    //$fileName = iconv("UTF-8","GB2312",$_GET['newname']);
    $pathTmb = "uptemp/".$tmpDir."/";
    $extPos = strrpos($fileName,".");
    $secfile= substr($fileName,0,$extPos);

    $tmbName = $secfile.".jpg";
    if(!is_dir($pathTmb))
       mkdir($pathTmb);
    //echo $pathTmb."<br>";
    if(!file_exists($pathTmb.$tmbName)){
        //IMAGEMAGICKDIR这个就要根据您自己装的imagemagick目录来写了
        $cmd = IMAGEMAGICKDIR."/convert -resize 110x110 ". $path.$fileName." ".$pathTmb.$tmbName;
    //echo $cmd;
    system( $cmd );

    }
    $tmbName = str_replace("\ "," ",$tmbName);
    readfile($pathTmb.$tmbName);
    exit;
    //转码
    $fileName = iconv("GB2312","UTF-8",$secfile);

}

// 获取文件扩展名
// @param $fileName 上传文件的原文件名
function getExt($fileName){
	$ext = explode(".", $fileName);
	$ext = $ext[count($ext) - 1];
	return strtolower($ext);
}

*/