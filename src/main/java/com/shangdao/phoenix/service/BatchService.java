package com.shangdao.phoenix.service;

import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.util.HTTPResponse;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BatchService {
    @Autowired
    PostMethodService postMethodService;

    private final static Logger logger = LoggerFactory.getLogger(PostMethodService.class);

    @Transactional
    public HTTPResponse batchDelete(String entityName, Long[] ids) {

        if (ids == null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.CHOOSE_DELETE_OBJECT);
        }
        if (ids.length == 0) {
            throw new OutsideRuntimeException(ExceptionResultEnum.CHOOSE_DELETE_OBJECT);
        }

        for (Long id : ids) {
            String body = "{\"id\":\"" + id + "\"}";
            System.out.println(body);
            postMethodService.postDispatch(entityName, "delete", "all", body);
        }

        return new HTTPResponse();
    }

}
