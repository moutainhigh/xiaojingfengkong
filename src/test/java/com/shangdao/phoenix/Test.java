package com.shangdao.phoenix;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.report.ReportRepository;

import junit.framework.TestCase;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { Application.class })
public class Test extends TestCase{

    @Autowired
    private ReportRepository reportRepository;

    
    public void testReport() throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost("http://zscx.osta.org.cn/jiandingApp/command/buzhongxin/ecCertSearchAll");
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("CID", "42138119701008884x");
        jsonParam.put("Name", "邱明霞");
        jsonParam.put("tableName", "CERT_T");
        jsonParam.put("tableName1", "000000");
        jsonParam.put("CertificateID", "");
        jsonParam.put("PortID", "");
        jsonParam.put("province", "-1");
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        System.out.println();

        HttpResponse resp = client.execute(httpPost);
        
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
             System.out.println(respContent);
        }

        Document document = Jsoup.parse(respContent);
        Elements elements = document.select("form[id=Info]").get(0).select("input");
        for (Element element : elements) {
            Attributes attributes = element.attributes();
            System.out.println(attributes.get("name"));
            System.out.println(attributes.get("value"));
            
        }

    }

}
