package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
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
          
        menuRepository.save(menu);
        
    }
    
    
    
}
  
