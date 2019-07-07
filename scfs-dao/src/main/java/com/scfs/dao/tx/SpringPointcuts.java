package com.scfs.dao.tx;

import org.aspectj.lang.annotation.Pointcut;

public class SpringPointcuts {

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void springService() {
    }

    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void springComponent() {
    }

    @Pointcut("springService() || springComponent()")
    public void serviceLayer() {
    }
}

