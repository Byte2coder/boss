package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月13日 下午12:01:14 <br/>       
 */
public interface StandardService {

    void save(Standard standard);

    Page<Standard> findAll(Pageable pageable);
}
  
