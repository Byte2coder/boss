package com.itheima.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;

/**  
 * ClassName:PermissionActionImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午3:51:20 <br/>       
 */
@Transactional
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Override
    public Page<Permission> findAll(Pageable pageable) {
          
        
        return permissionRepository.findAll(pageable);
    }
    @Override
    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

}
  
