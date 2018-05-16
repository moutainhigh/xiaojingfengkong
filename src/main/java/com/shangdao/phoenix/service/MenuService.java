package com.shangdao.phoenix.service;

import com.shangdao.phoenix.entity.button.Button;
import com.shangdao.phoenix.entity.button.ButtonRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.menu.Menu;
import com.shangdao.phoenix.entity.menu.MenuRepository;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class MenuService implements InterfaceEntityService {
    @Autowired
    private InitService initService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private EntityManagerRepository entityManagerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ButtonRepository buttonRepository;

    @PostConstruct
    public void init() {
        initMenu();
    }

    public void create(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        Menu post = (Menu) postBody;
        Menu same = menuRepository.findByName(post.getName());
        if (same != null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.MENU_NAME_REPEAT);
        }
        if (StringUtils.isBlank(post.getPath())) {
            throw new OutsideRuntimeException(ExceptionResultEnum.MENU_PATH_NEED);
        }

    }

    public void update(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        Menu post = (Menu) postBody;
        Menu old = (Menu) oldInstance;
        Menu same = menuRepository.findByName(post.getName());
        if (same != null && same.getId() != old.getId()) {
            throw new OutsideRuntimeException(ExceptionResultEnum.MENU_NAME_REPEAT);
        }
        if (StringUtils.isBlank(post.getPath())) {
            throw new OutsideRuntimeException(ExceptionResultEnum.MENU_PATH_NEED);
        }
    }

    private void initMenu() {
        if (menuRepository.count() == 0) {
            Set<Menu> menuSet = new HashSet<>();
            Set<Button> buttonSet = new HashSet<>();
            Menu homeManagerMenu = new Menu("首页管理", "/dashboard", 0, null,"fa-home",false);
            menuRepository.save(homeManagerMenu);
            menuSet.add(homeManagerMenu);

            Menu homeMenu = new Menu("首页", "", 1, homeManagerMenu,"fa-home",false);
            menuRepository.save(homeMenu);
            menuSet.add(homeMenu);

            Menu institutionsManagerMenu = new Menu("机构管理", "/organizationalManagenment", 100, null,"fa-bank",false);
            menuRepository.save(institutionsManagerMenu);
            menuSet.add(institutionsManagerMenu);

            Menu institutionsListMenu = new Menu("机构列表", "mechanismList", 101, institutionsManagerMenu,"",false);
            menuRepository.save(institutionsListMenu);
            menuSet.add(institutionsListMenu);
            Button addInstitutionsListButton = new Button("新增","add",institutionsListMenu);
            buttonRepository.save(addInstitutionsListButton);
            buttonSet.add(addInstitutionsListButton);
            Button editInstitutionsListButton = new Button("编辑","edit",institutionsListMenu);
            buttonRepository.save(editInstitutionsListButton);
            buttonSet.add(editInstitutionsListButton);
            Button onInstitutionsListButton = new Button("启用","on",institutionsListMenu);
            buttonRepository.save(onInstitutionsListButton);
            buttonSet.add(onInstitutionsListButton);
            Button offInstitutionsListButton = new Button("禁用","off",institutionsListMenu);
            buttonRepository.save(offInstitutionsListButton);
            buttonSet.add(offInstitutionsListButton);


            Menu institutionsVerifyMenu = new Menu("机构审核", "institutionalReview", 102, institutionsManagerMenu,"",false);
            menuRepository.save(institutionsVerifyMenu);
            menuSet.add(institutionsVerifyMenu);

            Menu institutionsAccountMenu = new Menu("机构账户", "account", 103, institutionsManagerMenu,"",false);
            menuRepository.save(institutionsAccountMenu);
            menuSet.add(institutionsAccountMenu);
            Button rechargeInstitutionsAccountButton = new Button("充值","recharge",institutionsAccountMenu);
            buttonRepository.save(rechargeInstitutionsAccountButton);
            buttonSet.add(rechargeInstitutionsAccountButton);
            Button withdrawingInstitutionsAccountButton = new Button("扣款","withdrawing",institutionsAccountMenu);
            buttonRepository.save(withdrawingInstitutionsAccountButton);
            buttonSet.add(withdrawingInstitutionsAccountButton);

            Menu permissionManagerMenu = new Menu("权限管理", "/authorityManagement", 200, null,"fa-user-secret",false);
            menuRepository.save(permissionManagerMenu);
            menuSet.add(permissionManagerMenu);

            Menu backUserMenu = new Menu("后台用户管理", "user", 201, permissionManagerMenu,"",false);
            menuRepository.save(backUserMenu);
            menuSet.add(backUserMenu);
            Button addBackUserMenu = new Button("新增","add",backUserMenu);
            buttonRepository.save(addBackUserMenu);
            buttonSet.add(addBackUserMenu);
            Button editBackUserMenu = new Button("编辑","edit",backUserMenu);
            buttonRepository.save(editBackUserMenu);
            buttonSet.add(editBackUserMenu);
            Button deleteBackUserMenu = new Button("删除","delete",backUserMenu);
            buttonRepository.save(deleteBackUserMenu);
            buttonSet.add(deleteBackUserMenu);
            Button onBackUserMenu = new Button("启用","on",backUserMenu);
            buttonRepository.save(onBackUserMenu);
            buttonSet.add(onBackUserMenu);
            Button offBackUserMenu = new Button("禁用","off",backUserMenu);
            buttonRepository.save(offBackUserMenu);
            buttonSet.add(offBackUserMenu);

            Menu roleMenu = new Menu("角色管理", "roleManagement", 202, permissionManagerMenu,"",false);
            menuRepository.save(roleMenu);
            menuSet.add(roleMenu);
            Button addRoleButton = new Button("新增","add",roleMenu);
            buttonRepository.save(addRoleButton);
            buttonSet.add(addRoleButton);
            Button editRoleButton = new Button("编辑","edit",roleMenu);
            buttonRepository.save(editRoleButton);
            buttonSet.add(editRoleButton);
            Button deleteRoleButton = new Button("删除","delete",roleMenu);
            buttonRepository.save(deleteRoleButton);
            buttonSet.add(deleteRoleButton);

            Menu departmentMenu = new Menu("部门管理", "departmentManagement", 203, permissionManagerMenu,"",false);
            menuRepository.save(departmentMenu);
            menuSet.add(departmentMenu);
            Button addDepartmentButton = new Button("新增","add",departmentMenu);
            buttonRepository.save(addDepartmentButton);
            buttonSet.add(addDepartmentButton);
            Button editDepartmentButton = new Button("编辑","edit",departmentMenu);
            buttonRepository.save(editDepartmentButton);
            buttonSet.add(editDepartmentButton);
            Button deleteDepartmentButton = new Button("删除","delete",departmentMenu);
            buttonRepository.save(deleteDepartmentButton);
            buttonSet.add(deleteDepartmentButton);


            Menu menuManagerMenu = new Menu("菜单管理", "menuManagement", 204, permissionManagerMenu,"",true);
            menuRepository.save(menuManagerMenu);
            menuSet.add(menuManagerMenu);
            Button addMenuManagerButton = new Button("新增","add",menuManagerMenu);
            buttonRepository.save(addMenuManagerButton);
            buttonSet.add(addMenuManagerButton);
            Button editMenuManagerButton = new Button("编辑","edit",menuManagerMenu);
            buttonRepository.save(editMenuManagerButton);
            buttonSet.add(editMenuManagerButton);
            Button deleteMenuManagerButton = new Button("删除","delete",menuManagerMenu);
            buttonRepository.save(deleteMenuManagerButton);
            buttonSet.add(deleteMenuManagerButton);


            Menu advertisingManagerMenu = new Menu("广告管理", "/sowingMap", 300, null,"fa-photo",false);
            menuRepository.save(advertisingManagerMenu);
            menuSet.add(advertisingManagerMenu);

            Menu shufflingManagerMenu = new Menu("轮播管理", "sowingMapList", 301, advertisingManagerMenu,"fa-photo",false);
            menuRepository.save(shufflingManagerMenu);
            menuSet.add(shufflingManagerMenu);
            Button addShufflingManagerButton = new Button("新增","add",shufflingManagerMenu);
            buttonRepository.save(addShufflingManagerButton);
            buttonSet.add(addShufflingManagerButton);
            Button editShufflingManagerButton = new Button("编辑","edit",shufflingManagerMenu);
            buttonRepository.save(editShufflingManagerButton);
            buttonSet.add(editShufflingManagerButton);
            Button deleteShufflingManagerButton = new Button("删除","delete",shufflingManagerMenu);
            buttonRepository.save(deleteShufflingManagerButton);
            buttonSet.add(deleteShufflingManagerButton);
            Button groundingShufflingManagerButton = new Button("上架","grounding",shufflingManagerMenu);
            buttonRepository.save(groundingShufflingManagerButton);
            buttonSet.add(groundingShufflingManagerButton);
            Button undercarriageShufflingManagerButton = new Button("下架","undercarriage",shufflingManagerMenu);
            buttonRepository.save(undercarriageShufflingManagerButton);
            buttonSet.add(undercarriageShufflingManagerButton);

            Menu noticeManagerMenu = new Menu("通知管理", "/notice", 400, null,"fa-info-circle",true);
            menuRepository.save(noticeManagerMenu);
            menuSet.add(noticeManagerMenu);

            Menu noticeListMenu = new Menu("通知列表", "noticeManage", 401, noticeManagerMenu,"",true);
            menuRepository.save(noticeListMenu);
            menuSet.add(noticeListMenu);
            Button addNoticeListButton = new Button("新增","add",noticeListMenu);
            buttonRepository.save(addNoticeListButton);
            buttonSet.add(addNoticeListButton);
            Button editNoticeListButton = new Button("编辑","edit",noticeListMenu);
            buttonRepository.save(editNoticeListButton);
            buttonSet.add(editNoticeListButton);
            Button deleteNoticeListButton = new Button("删除","delete",noticeListMenu);
            buttonRepository.save(deleteNoticeListButton);
            buttonSet.add(deleteNoticeListButton);

            Menu templateListMenu = new Menu("推送模板", "pushModel", 402, noticeManagerMenu,"",true);
            menuRepository.save(templateListMenu);
            menuSet.add(templateListMenu);
            Button addTemplateListButton = new Button("新增","add",templateListMenu);
            buttonRepository.save(addTemplateListButton);
            buttonSet.add(addTemplateListButton);
            Button deleteTemplateListButton = new Button("删除","delete",templateListMenu);
            buttonRepository.save(deleteTemplateListButton);
            buttonSet.add(deleteTemplateListButton);


            Menu userMenu = new Menu("用户管理", "/userManagement", 500, null,"fa-user",false);
            menuRepository.save(userMenu);
            menuSet.add(userMenu);

            Menu userListMenu = new Menu("用户列表", "userList", 501, userMenu,"",false);
            menuRepository.save(userListMenu);
            menuSet.add(userListMenu);
            Button onUserListButton = new Button("启用","on",userListMenu);
            buttonRepository.save(onUserListButton);
            buttonSet.add(onUserListButton);
            Button offUserListButton = new Button("禁用","off",userListMenu);
            buttonRepository.save(offUserListButton);
            buttonSet.add(offUserListButton);

            Menu reportMenu = new Menu("报告列表","reportList",502,userMenu,"",false);
            menuRepository.save(reportMenu);
            menuSet.add(reportMenu);

            Menu productManagerMenu = new Menu("产品管理", "/productManagement", 600, null,"fa-product-hunt",false);
            menuRepository.save(productManagerMenu);
            menuSet.add(productManagerMenu);

            Menu productListMenu = new Menu("产品列表", "productList", 601, productManagerMenu,"",false);
            menuRepository.save(productListMenu);
            menuSet.add(productListMenu);
            Button addProductListButton = new Button("新增","add",productListMenu);
            buttonRepository.save(addProductListButton);
            buttonSet.add(addProductListButton);
            Button editProductListButton = new Button("编辑","edit",productListMenu);
            buttonRepository.save(editProductListButton);
            buttonSet.add(editProductListButton);
            Button onProductListButton = new Button("启用","on",productListMenu);
            buttonRepository.save(onProductListButton);
            buttonSet.add(onProductListButton);
            Button offProductListButton = new Button("禁用","off",productListMenu);
            buttonRepository.save(offProductListButton);
            buttonSet.add(offProductListButton);


            Menu moduleMenu = new Menu("模块管理", "moduleList", 602, productManagerMenu,"",false);
            menuRepository.save(moduleMenu);
            menuSet.add(moduleMenu);
            Button addModuleButton = new Button("新增","add",moduleMenu);
            buttonRepository.save(addModuleButton);
            buttonSet.add(addModuleButton);
            Button editModuleButton = new Button("编辑","edit",moduleMenu);
            buttonRepository.save(editModuleButton);
            buttonSet.add(editModuleButton);
            Button onModuleButton = new Button("启用","on",moduleMenu);
            buttonRepository.save(onModuleButton);
            buttonSet.add(onModuleButton);
            Button offModuleButton = new Button("禁用","off",moduleMenu);
            buttonRepository.save(offModuleButton);
            buttonSet.add(offModuleButton);


            Menu supplyMenu = new Menu("接口管理", "supplyManagement", 603, productManagerMenu,"",false);
            menuRepository.save(supplyMenu);
            menuSet.add(supplyMenu);
            Button addSupplyButton = new Button("新增","add",supplyMenu);
            buttonRepository.save(addSupplyButton);
            buttonSet.add(addSupplyButton);
            Button editSupplyButton = new Button("编辑","edit",supplyMenu);
            buttonRepository.save(editSupplyButton);
            buttonSet.add(editSupplyButton);
            Button onSupplyButton = new Button("启用","on",supplyMenu);
            buttonRepository.save(onSupplyButton);
            buttonSet.add(onSupplyButton);
            Button offSupplyButton = new Button("禁用","off",supplyMenu);
            buttonRepository.save(offSupplyButton);
            buttonSet.add(offSupplyButton);


            Role developRole = roleRepository.findByCode("DEVELOPER");
            developRole.setMenus(menuSet);
            developRole.setButtons(buttonSet);
            roleRepository.save(developRole);


        }
    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(Menu.class).setEntityService(this);
    }

}
