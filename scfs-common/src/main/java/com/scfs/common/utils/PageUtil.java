package com.scfs.common.utils;

/**
 * 分页工具类
 * @author Administrator
 *
 */
public class PageUtil {

    /**
     * 根据总数和每页显示的条数，得到总页数
     * @param totalCount
     * @param pageCount
     * @return
     */
    public static int getTotalPage(int totalCount,int pageCount){
        int page = totalCount/pageCount;
        if(totalCount%pageCount ==0){
            return page;
        }else{
            return page+1;
        }
    }

    /**
     * 直接返回偏移量
     * @param offset 偏移量
     * @param pageCount
     * @return
     */
    public static int getOffSet(int offset, int pageCount){
    	return offset;
        //return (page-1)*pageCount;
    }
    
    /**
     * 根据第几页和每页显示的条数，得到偏移值
     * @param page 第一页为1
     * @param pageCount
     * @return
     */
    public static int getPageOffSet(int page, int pageCount){
        return (page-1)*pageCount;
    }

    public static void main(String[] args) {
        System.out.println(getTotalPage(100,32));
        System.out.println(getOffSet(1,20));
    }

}
