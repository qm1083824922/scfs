package com.scfs.domain.fi.entity;

import java.util.List;

/**
 * <pre>
 * 
 *  File: RecDetail.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator			
 *
 * </pre>
 */
public class RecDetail
{
    private Receive receive ;
    
    private List<RecLine> recLines;

    public Receive getReceive()
    {
        return receive;
    }

    public void setReceive(Receive receive)
    {
        this.receive = receive;
    }

    public List<RecLine> getRecLines()
    {
        return recLines;
    }

    public void setRecLines(List<RecLine> recLines)
    {
        this.recLines = recLines;
    }
}

