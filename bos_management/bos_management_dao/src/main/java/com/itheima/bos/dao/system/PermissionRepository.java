package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Permission;

/**  
 * ClassName:PermissionRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午3:53:22 <br/>       
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findById(Long permissionId);

    @Query("select p from Permission p inner join p.roles r inner join  r.users u where u.id=? ")
    List<Permission> findByuId(Long id);


}
  
