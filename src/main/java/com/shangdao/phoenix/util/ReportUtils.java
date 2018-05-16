package com.shangdao.phoenix.util;

import java.util.Date;

public class ReportUtils {


    public static String  createReportNo(){

        ;
        return DateUtils.getStrByDate(new Date(),"yyyyMMddHHmmss").substring(2)+(int)((Math.random()*9+1)*1000);
    }
}
