package com.shangdao.phoenix.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.shangdao.phoenix.service.BatchService;

@RestController
@RequestMapping(value = "/batch")
public class BatchController {
    
    @Autowired
    private BatchService batchService;
    
    @RequestMapping(path = "/{entityName}/batchDelete",method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(@PathVariable String entityName, @RequestBody Map<String, Long []> ids){
        Long [] list =  ids.get("ids");
        
        return batchService.batchDelete(entityName, list);
    }

}
