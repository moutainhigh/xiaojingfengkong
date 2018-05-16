package com.shangdao.phoenix.service;

import javax.annotation.PostConstruct;

import com.shangdao.phoenix.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.OutsideRuntimeException;

import java.util.Set;

@Service
public class RoleService implements InterfaceEntityService{
    
    @Autowired
    InitService initService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EntityManagerRepository entityManagerRepository;
    
    @PostConstruct
    public void initRoles(){
        long roleCount = roleRepository.count();
        if(roleCount == 3){
            EntityManager roleEntityManager = entityManagerRepository.findByName("role");
            
            Role groupManager = new Role(roleEntityManager,"GROUPMANAGER","集团管理员","集团管理员");
            roleRepository.save(groupManager);
            
            Role enterpriseManager = new Role(roleEntityManager,"ENTERPRISEMANAGER","企业管理员","企业管理员");
            roleRepository.save(enterpriseManager);
            
            Role teamManager = new Role(roleEntityManager,"TEAMMANAGER","团队管理员","团队管理员");
            roleRepository.save(teamManager);
            
            Role adminRole = new Role(roleEntityManager,"ADMIN","后台管理员","后台管理员");
            roleRepository.save(adminRole);
        }
    }
    
    public void create(PostMethodWrapper postMethodWrapper,Object postBody, Object oldInstance){
        Role post = (Role) postBody;
        Role sameName = roleRepository.findByName(post.getName());
        if(sameName != null){
            if(sameName.getDeletedAt() != null){
                throw new OutsideRuntimeException(ExceptionResultEnum.ROLE_NAME_HAVE_USER_DELETE);
            }else{
                throw new OutsideRuntimeException(ExceptionResultEnum.ROLE_NAME_CANNOT_REPEAT);
            }
        }

        Role sameCode = roleRepository.findByCode(post.getCode());
        if(sameCode != null){
            if(sameCode.getDeletedAt() != null){
                throw new OutsideRuntimeException(ExceptionResultEnum.ROLE_CODE_HAVE_USER_DELETE);
            }else{
                throw new OutsideRuntimeException(ExceptionResultEnum.ROLE_CODE_CANNOT_REPEAT);
            }
        }

    }
    
    public void update(PostMethodWrapper postMethodWrapper,Object postBody, Object oldInstance){
        Role post = (Role) postBody;
        Role old = (Role) oldInstance;
        Role sameName = roleRepository.findByName(post.getName());
        if(sameName != null && sameName.getId() != post.getId()){
            throw new OutsideRuntimeException(ExceptionResultEnum.ROLE_NAME_CANNOT_REPEAT);
        }
    }

    public void delete(PostMethodWrapper postMethodWrapper,Object postBody, Object oldInstance){
        Role old = (Role) oldInstance;
        Set<User> users = old.getUsers();
        if(users != null && users.size() > 0){
            throw new OutsideRuntimeException(ExceptionResultEnum.ROLE_HAVA_USER);
        }
    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(Role.class).setEntityService(this);
    }

}
