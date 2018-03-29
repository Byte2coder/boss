package com.itheima.bos.dao.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.Permission;

/**  
 * ClassName:PermissionRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午3:53:22 <br/>       
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findById(Long permissionId);


}
  
