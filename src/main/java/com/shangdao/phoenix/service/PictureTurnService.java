package com.shangdao.phoenix.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.pictureturn.PictureTurns;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;

@Service
public class PictureTurnService implements InterfaceEntityService {

    @Autowired
    private InitService initService;
    @Autowired
    private EntityManagerRepository entityManagerRepository;
    @Autowired
    private ActRepository actRepository;

    @PostConstruct
    public void init() {
        EntityManager turnManager = entityManagerRepository.findByName("pictureTurns");
        Act clickAct = actRepository.findByEntityManagerIdAndCode(turnManager.getId(), "click");
        if (clickAct == null) {
            clickAct = new Act(turnManager, "点击", "click");
            actRepository.save(clickAct);
        }
    }

    public void click(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        PictureTurns posTurns = (PictureTurns) postBody;
        PictureTurns oldTurns = (PictureTurns) oldInstance;
        posTurns.setClickTimes(oldTurns.getClickTimes() + 1L);
    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(PictureTurns.class).setEntityService(this);

    }

}
