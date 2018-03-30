package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 上午9:04:38 <br/>       
 */
public interface UserService {

    void save(Long[] roleIds, User user);

    Page<User> finaAll(Pageable pageable);

}
  
