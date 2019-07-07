package com.scfs.common;

import com.scfs.common.utils.http.HttpInvoker;

public class HttpInvokerTest {
    public static void main(String[] args) throws Exception {
        System.out.println(HttpInvoker.get("http://baidu.com"));
    }
}
