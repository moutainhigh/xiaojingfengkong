package com.shangdao.phoenix.service;

import java.util.Date;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActNotice;
import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.OutsideRuntimeException;

@Service
public class ActService implements InterfaceEntityService {

	// 保证bean创建顺序，没实际用处
	@Autowired
	private InitService initService;

	@Autowired
	private ActRepository actRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	

	// 最好不要修改oldInstance
	public void create(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
		Act post = (Act) postBody;
		_validActCode(post,post.getEntityManager());
		_validTargetState(post,post.getEntityManager());
		_validRoleCode(post,post.getEntityManager());
	}

	

	public void update(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
		Act post = (Act) postBody;
		Act old = (Act) oldInstance;
		if(post.getEntityManager()!=null){
			_validActCode(post,post.getEntityManager());
			_validTargetState(post,post.getEntityManager());
			_validRoleCode(post,post.getEntityManager());
		}else{
			_validActCode(post,old.getEntityManager());
			_validTargetState(post,old.getEntityManager());
			_validRoleCode(post,old.getEntityManager());
		}
		
	}

	@Override
	@PostConstruct
	public void registerService() {
		initService.getStructure(Act.class).setEntityService(this);

	}
	
	private void _validTargetState(Act post, EntityManager entityManager) {
		if(post.getTargetState()!=null){
			if(!entityManager.getHasStateMachine()){
				throw new OutsideRuntimeException(ExceptionResultEnum.ACT_OBJECT_MUST_MACHINE);
			}
		}
		
	}
	
	private void _validActCode(Act post, EntityManager entityManager) {
		if(post.getCode()!=null){
			
			Act findByEntityManagerIdAndCode = actRepository.findByEntityManagerIdAndCode(entityManager.getId(), post.getCode());
			if(findByEntityManagerIdAndCode!=null){
				if(post.getId() == findByEntityManagerIdAndCode.getId()){
					return;
				}else{
					throw new OutsideRuntimeException(ExceptionResultEnum.SAME_OBJECT_CODE_NOTSAME);
				}
			}else{
				return;
			}
		}else{
			return;
		}
	}
	
	private void _validRoleCode(Act post, EntityManager entityManager) {
		// TODO Auto-generated method stub
		
		
		if(post.getActNotices()!=null){
			Set<ActNotice> actNotices = post.getActNotices();
			for (ActNotice actNotice : actNotices) {
				_checkCode(actNotice.getRoleCode(),entityManager);
			}
		}
	}

	private void _checkCode(String noticeImmediatelyFirstRoleCode, EntityManager entityManager) {
		
		String[] projectRoles = new String[]{"MEMBER","MANAGER","SUBSCRIBER","DEPARTMENT"};
		for (String role : projectRoles) {
			if(role.equalsIgnoreCase(noticeImmediatelyFirstRoleCode)){
				if(!entityManager.getHasProject()){
					throw new OutsideRuntimeException(ExceptionResultEnum.ACT_OBJECT_MUST_PROJECT,role);
				}
				return;
			}
		}
		if(noticeImmediatelyFirstRoleCode.equalsIgnoreCase("CREATOR")){
			return;
		}
		Role findByCode = roleRepository.findByCode(noticeImmediatelyFirstRoleCode);
		if(findByCode==null){
			throw new OutsideRuntimeException(noticeImmediatelyFirstRoleCode,ExceptionResultEnum.NOT_EFFECTIVE_ROLECODE);
		}
		
		
	}
}
