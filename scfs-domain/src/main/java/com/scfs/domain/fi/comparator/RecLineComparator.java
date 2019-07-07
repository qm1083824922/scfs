package com.scfs.domain.fi.comparator;

import java.util.Comparator;

import com.scfs.domain.fi.entity.RecLine;

/**
 * <pre>
 * 
 *  File: RecLineComparator.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月2日				Administrator			
 *
 * </pre>
 */
public class RecLineComparator implements Comparator<RecLine>
{
    
    /**
     * 1.凭证日期越早，核销优先级越高（最高权重)
     * 2.id越小，核销优先级越高
     */
    @Override
    public int compare(RecLine o1, RecLine o2)
    {
        long time1 = o1.getVoucherDate().getTime();
        long time2 = o2.getVoucherDate().getTime();
        if (time1 < time2) {
            return -1;
        } else if (time1 > time2) {
            return 1;
        } else {
            Integer id1 = o1.getId();
            Integer id2 = o2.getId();
            if (id1 < id2) {
                return -1;
            }else if(id1 > id2) {
                return 1;
            }else {
                return 0;
            }
        }
    }
}

