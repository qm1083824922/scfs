<?php
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

//$targetFolder = '/uploads/'.$_POST['folder']; // Relative to the root
$targetFolder = '/uploads/pdm-product-pic/'.$_POST['folder'].'/'.date('Y').'/'.date('m').'/'.date('d').'/source-img';

$verifyToken = md5('unique_salt' . $_POST['timestamp']);

if (!empty($_FILES) && $_POST['token'] == $verifyToken) {
	$tempFile = $_FILES['Filedata']['tmp_name'];
	$targetPath = $_SERVER['DOCUMENT_ROOT'] . $targetFolder;
	//目录不存在创建
	if (!file_exists($targetPath)) {
		mkdir($targetPath,0777,true);
	}
	//$targetFile = rtrim($targetPath,'/') . '/' . $_FILES['Filedata']['name'];
	//新文件名
	$file_ext = pathinfo($_FILES['Filedata']['name'], PATHINFO_EXTENSION);
	$new_file_name = date("YmdHis") . '_' . rand(10000, 99999) . '.' . $file_ext;
	$targetFile = rtrim($targetPath,'/') . '/' . $new_file_name;

	// Validate the file type
	// $fileTypes = array('jpg','JPG','jpeg','gif','png'); // File extensions
	$fileTypes = array('jpg','JPG'); // File extensions
	$fileParts = pathinfo($_FILES['Filedata']['name']);

	if (in_array($fileParts['extension'],$fileTypes)) {
		move_uploaded_file($tempFile,$targetFile);
		$newfileType = file_type($targetFile);   // 验证文件真实类型
		if(in_array($newfileType, $fileTypes)){
	        file_put_contents($logFile, '{"statua":"true","time":"' . date('Y-m-d H:i:s') . '","path":"' . $targetFile . '","data":"'.json_encode($fileParts).'"}', FILE_APPEND);
			echo $targetFolder . '/' . $new_file_name;
		}else{
			echo 'Invalid file type';
		}
	}
	else {
		file_put_contents($logFile, '{"statua":"false","time":"' . date('Y-m-d H:i:s') . '","data":"'.json_encode($fileParts).'"}', FILE_APPEND);
        file_put_contents($logFile, 'fail'.time(), FILE_APPEND);
		echo 'Invalid file type';
	}
}

// 验证图片真实类型
function file_type($filename)
{
    $file = fopen($filename, "rb");
    $bin = fread($file, 2); //只读2字节
    fclose($file);
    $strInfo = @unpack("C2chars", $bin);
    $typeCode = intval($strInfo['chars1'].$strInfo['chars2']);
    $fileType = '';
    switch ($typeCode)
    {
        case 7790:
            $fileType = 'exe';
            break;
        case 7784:
            $fileType = 'midi';
            break;
        case 8297:
            $fileType = 'rar';
            break;
        case 8075:
            $fileType = 'zip';
            break;
        case 255216:
            $fileType = 'jpg';
            break;
        case 7173:
            $fileType = 'gif';
            break;
        case 6677:
            $fileType = 'bmp';
            break;
        case 13780:
            $fileType = 'png';
            break;
        default:
            $fileType = 'unknown: '.$typeCode;
    }

    //Fix
    if ($strInfo['chars1']=='-1' AND $strInfo['chars2']=='-40' ) return 'jpg';
    if ($strInfo['chars1']=='-119' AND $strInfo['chars2']=='80' ) return 'png';

    return $fileType;
}