package com.itheima.bos.service.system;

import java.util.List;

import com.itheima.bos.domain.system.Menu;

/**  
 * ClassName:MenuService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:37:36 <br/>       
 */
public interface MenuService {
    
    List<Menu> findLevelOne();

    void save(Menu model);

}
  
