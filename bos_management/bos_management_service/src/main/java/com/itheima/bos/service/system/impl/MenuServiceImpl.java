package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;

/**  
 * ClassName:MenuServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:38:27 <br/>       
 */
@Transactional
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

   

    @Override
    public List<Menu> findLevelOne() {
          
        return menuRepository.findByParentMenuIsNull();
    }

    @Override
    public void save(Menu menu) {
       //判断用户是否要添加一级菜单,父菜单是否为null
        Menu parentMenu = menu.getParentMenu();
        if (parentMenu!=null && parentMenu.getId()==null) {
            menu.setParentMenu(null);
        }
        
        menuRepository.save(menu);
        
    }

    //分页查询
    @Override
    public Page<Menu> findAll(Pageable pageable) {
          
        return menuRepository.findAll(pageable);
    }

    @Override
    public List<Menu> findByUser(User user) {
         //判断是否是admin用户
        if ("admin".equals(user.getUsername())) {
            
            return menuRepository.findAll();
        }
        
        
        return menuRepository.findByUser(user.getId());
    }
    
    
    
}
  
