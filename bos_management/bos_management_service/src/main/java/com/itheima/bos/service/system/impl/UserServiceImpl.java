package com.itheima.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;

/**  
 * ClassName:UserServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 上午9:04:54 <br/>       
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void save(Long[] roleIds, User user) {
         //持久化user对象
        userRepository.save(user);
        //判断
        if (roleIds!=null && roleIds.length>0) {
            for (Long roleId : roleIds) {
                Role role=new Role();
                role.setId(roleId);
                user.getRoles().add(role);
            }
        }
        
        
    }

    @Override
    public Page<User> finaAll(Pageable pageable) {
          
        return userRepository.findAll(pageable);
    }

}
  
