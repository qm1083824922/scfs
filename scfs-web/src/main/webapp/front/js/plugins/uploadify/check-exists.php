<?php
/*
Uploadify
Copyright (c) 2012 Reactive Apps, Ronnie Garcia
Released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/

// Define a destination
$targetFolder = '/upload/' . $_POST['floder'] . '/' ; // Relative to the root and should match the upload folder in the uploader script

// $filename = iconv("UTF-8","gb2312", $_POST['filename'] ); // 中文乱码

if (file_exists($_SERVER['DOCUMENT_ROOT'] . $targetFolder . $_POST['filename'] )) {
	echo 1;
} else {
	echo 0;
}