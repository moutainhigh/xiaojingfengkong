package com.shangdao.phoenix.service;

import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplate;
import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NoticetemplateService implements InterfaceEntityService{
    @Autowired
    private  InitService initService;
    @Autowired
    private NoticeTemplateRepository noticeTemplateRepository;
    @Value("${work.weixin.agent.test.id}")
    private String wxworkAgentId;

    @Value("${work.weixin.agent.test.secret}")
    private String wxworkAgentSecret;



    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(NoticeTemplate.class).setEntityService(this);
    }
}
