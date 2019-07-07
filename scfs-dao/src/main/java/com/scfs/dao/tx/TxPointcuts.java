package com.scfs.dao.tx;

import org.aspectj.lang.annotation.Pointcut;

public class TxPointcuts {

    @Pointcut("com.scfs.dao.tx.SpringPointcuts.serviceLayer() && !@annotation(com.scfs.dao.tx.IgnoreTransactionalMark)")
    public void txMarkPointcut() {
    }
}

