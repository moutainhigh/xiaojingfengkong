package com.shangdao.phoenix.util;

/**
 * @author huanghengkun
 * @date 2018/04/10
 */
public class HttpTableResponse extends HTTPListResponse {

    public HttpTableResponse(Object data, long total, long pageSize, long pageNumber) {
        super(data, total, pageSize, pageNumber);
    }

    public void buildTable() {
        String htmlText = ((AbstractTable) getData()).build();
        setData(htmlText);
    }
}
