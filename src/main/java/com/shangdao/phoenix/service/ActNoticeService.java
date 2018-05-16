package com.shangdao.phoenix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangdao.phoenix.entity.act.ActNotice;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;

@Service
public class ActNoticeService implements InterfaceEntityService{
    
    @Autowired
    private InitService initService;
    
    public void create(PostMethodWrapper postMethodWrapper, Object postBody,Object oldInstance){
        ActNotice actNotice = (ActNotice) postBody;
        
    }
    
    
    @Override
    public void registerService() {
        initService.getStructure(ActNotice.class).setEntityService(this);
    }

}
