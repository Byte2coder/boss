package com.itheima.bos.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;

/**  
 * ClassName:RoleServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午4:25:34 <br/>       
 */
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    public Page<Role> findALL(Pageable pageable) {
        
        return roleRepository.findAll(pageable);
    }

    //此种方式更加节省资源,减少数据库查询
    @Override
    public void save(Role role, String menuIds, Long[] permissionIds) {
          //持久化对象
          roleRepository.save(role);
          
          //建立role和menu的关系
          if (StringUtils.isNotEmpty(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
              Menu menu=new Menu();
              menu.setId(Long.parseLong(menuId));
               role.getMenus().add(menu);
            }
        }
          //建立role和permission的关系
        if (permissionIds!=null) {
            for (Long permissionId : permissionIds) {
              Permission permission=  new Permission();
              permission.setId(permissionId);
              role.getPermissions().add(permission);
            }
        }
    }
    
    public void save2(Role role, String menuIds, Long[] permissionIds) {
        
        roleRepository.save(role);
        
        //建立role和menu的关系
        if (StringUtils.isNotEmpty(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                Menu menu=  menuRepository.findOne(Long.parseLong(menuId));
                role.getMenus().add(menu);
            }
        }
        //建立role和permission的关系
        if (permissionIds!=null) {
            for (Long permissionId : permissionIds) {
                Permission permission=  permissionRepository.findOne(permissionId);
                role.getPermissions().add(permission);
            }
        }
    }

}
  
