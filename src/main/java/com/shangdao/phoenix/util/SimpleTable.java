package com.shangdao.phoenix.util;

import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/10
 */
public class SimpleTable extends AbstractTable {
    @Override
    public String build() {
        StringBuilder htmlText = new StringBuilder();
        htmlText.append("<table>");
        htmlText.append("<thead>");
        htmlText.append("<tr>");
        for (String item : head) {
            htmlText.append("<th>");
            htmlText.append("<div>");
            htmlText.append(item);
            htmlText.append("</div>");
            htmlText.append("</th>");
        }
        htmlText.append("</tr>");
        htmlText.append("</thead>");
        htmlText.append("<tbody>");
        System.out.println(body);
        for (Map<String, Object> row : body) {
            htmlText.append("<tr>");
            for (String item : head) {
                htmlText.append("<th>");
                htmlText.append("<div>");
                htmlText.append(convertValue(row, item));
                htmlText.append("</div>");
                htmlText.append("</th>");
            }
            htmlText.append("</tr>");
        }
        htmlText.append("</tbody>");
        htmlText.append("</table>");
        return htmlText.toString();
    }
}
