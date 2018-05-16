package com.shangdao.phoenix.service;

import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.entity.user.User.Source;
import com.shangdao.phoenix.entity.user.UserRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.CommonUtils;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import com.shangdao.phoenix.util.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserService implements InterfaceEntityService , UserDetailsService{

	@Autowired
	private InitService initService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Value("${salt}")
	private String salt;

	@Value("${work.weixin.enable}")
	private boolean workWeixinEnable;

	@Autowired
	private WorkWeixinContactService weixinService;

	@Autowired
	ActRepository actRepository;

	@Autowired
    private EntityManagerRepository entityManagerRepository;

	@Autowired
	SmsService smsService;


	//最好不要修改oldInstance
	public void create(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance){
		User user = (User)postBody;

		String username = user.getUsername();
		String password = user.getPassword();
		String password2 = user.getPassword2();

		User existUser = userRepository.findByUsernameAndSource(username, Source.WEB);
		if(existUser != null && existUser.getDeletedAt() != null){
			throw new OutsideRuntimeException(ExceptionResultEnum.USER_HAVE_DELETE);
		}
		if(existUser != null){
			throw new OutsideRuntimeException(ExceptionResultEnum.USER_NAME_EXIST);
		}

//		if (!smsService.checkSms(username,user.getSms())) {
//			throw new OutsideRuntimeException(6666,"短信验证码错误，请重新输入验证码");
//		}
		if (user.getBackend() == null) {
			throw new OutsideRuntimeException(ExceptionResultEnum.UNKNOW_USER_TYPE);
		}

		if(!password.equals(password2)){
			throw new OutsideRuntimeException(ExceptionResultEnum.PASSWORD_NOT_SAME);
		}
		String md5password = CommonUtils.MD5Encode(password, salt);
		user.setPassword(md5password);

		user.setEnabled(true);
		user.setReportCount(0L);
		user.setNotReadMessage(0);
		user.setLeader(false);

		if (user.getRoles() == null) {    //前端注册 设置角色
			Role userRole = roleRepository.findByCode("GUEST");
			HashSet<Role> roles = new HashSet<Role>();
			roles.add(userRole);
			user.setRoles(roles);
		}


		System.out.println("根据old的实体和业务逻辑，改变postObject后,包括其他数据库操作");
	}

	public void update(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance){
		User post = (User)postBody;
		User old = (User)oldInstance;
		Set<Role> roles = post.getRoles();
		//如果没有提交部门，部门只增加不减少
		/*if(roles!=null){
			throw new OutsideRuntimeException(7121, "只能在企业微信的用户编辑-职务里提交角色名字，用“,”分隔多个角色");
		}*/
	}

    public void delete(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        User post = (User)postBody;
        post.setEnabled(false);
    }

	@Override
	public UserDetails loadUserByUsername(String s){

		User user = userRepository.findByUsernameAndSource(s, Source.WEB);
		if (user == null) {
			throw new UsernameNotFoundException("用户名不存在");
		}
		System.out.println("username:" + user.getUsername());
		return new UserDetailsImpl(user);
	}


	@Override
	@PostConstruct
	public void registerService() {
		initService.getStructure(User.class).setEntityService(this);

	}
}
