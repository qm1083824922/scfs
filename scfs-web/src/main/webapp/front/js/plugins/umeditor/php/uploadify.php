<?php
    header("Content-Type:text/html;charset=utf-8");
    error_reporting( E_ERROR | E_WARNING );
    date_default_timezone_set("Asia/chongqing");
    //上传配置

    $callback=isset($_GET['callback']) ? $_GET['callback'] : null;

    $targetFolder = '/uploads/pdm-check/'.date('Y').'/'.date('m').'/'.date('d'); // Relative to the root

    if (!empty($_FILES)) {
        $tempFile = $_FILES['upfile']['tmp_name'];
        $targetPath = $_SERVER['DOCUMENT_ROOT'] . $targetFolder;
        $suffix = strrchr($_FILES['upfile']['name'], '.');  //文件后缀

        //目录不存在创建
        if (!file_exists($targetPath)) {
            mkdir($targetPath,0777,true);
        }

        //字符串长度
        $count = strlen($_FILES['upfile']['name']);
        $mbCount = mb_strlen($_FILES['upfile']['name'], 'utf8');

        //文件名为纯英文、纯数字、英数混排
        if($count === $mbCount){
            $midCount = strrpos($_FILES['upfile']['name'], '.');
            $dirName = substr($_FILES['upfile']['name'], 0, $midCount);
        }else{
            //含有中文字符
            $midCount = mb_strrpos($_FILES['upfile']['name'], '.', 0, 'utf8');
            $dirName = mb_substr($_FILES['upfile']['name'], 0, $midCount, 'utf8');
        }

        $newPath = $dirName .'_'. time().rand(100,999) . $suffix; //新的文件名, 数据库保存的

        // Validate the file type
        $fileTypes = array('jpg','JPG','jpeg','gif','png','doc','txt','xls','rar','pdf','xlsx'); // File extensions
        $fileParts = pathinfo($_FILES['upfile']['name']);

        if (in_array($fileParts['extension'],$fileTypes)) {

            $sysNewPath = (DIRECTORY_SEPARATOR == '\\') ? iconv('UTF-8', 'gb2312//IGNORE', $newPath) : $newPath; //根据不用系统转译编码
            // $sysNewPath = $newPath;
            $sysTargetFile = rtrim($targetPath,'/') . '/' . $sysNewPath; //最后在系统中显示的文件名

            move_uploaded_file($tempFile,$sysTargetFile);
            // $info = $targetFolder . '/' . $newPath;
            $info = array(
                "originalName" => $_FILES['upfile']['name'] ,
                "name" => $newPath ,
                "url" => $targetFolder . '/' . $newPath ,
                "size" => $_FILES['upfile']['size'] ,
                "type" => $fileParts['extension'] ,
                "state" => 'SUCCESS'
            );
        }
        else {
            $info = array();
        }
    }

    /**
     * 返回数据  $info  必须为 array()  有问题请参考imageUp.php 文件   linchenghao 2016-1-1
            $info = array(
                "originalName" => $_FILES['upfile']['name'] ,
                "name" => $newPath ,
                "url" => $targetFolder . '/' . $newPath ,
                "size" => $_FILES['upfile']['size'] ,
                "type" => $fileParts['extension'] ,
                "state" => 'SUCCESS'
            );
     */
    if($callback) {
        echo '<script>'.$callback.'('.json_encode($info).')</script>';
    } else {
        echo json_encode($info);
    }
