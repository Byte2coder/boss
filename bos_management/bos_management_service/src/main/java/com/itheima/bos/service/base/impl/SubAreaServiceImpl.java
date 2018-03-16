package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:22:35 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;
    @Override
    public void save(SubArea subArea) {
        
      subAreaRepository.save(subArea) ;          
    }
    @Override
    public Page<SubArea> findAll(Pageable pageable) {
          
        return subAreaRepository.findAll(pageable);
    }

}
  
