package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.FixedArea;
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
    //查询已关联指定定区的分区
    @Override
    public List<SubArea> findAssociatedSubAreas(Long id) {
         FixedArea fixedArea = new FixedArea();
         fixedArea.setId(id);
         
        
        return subAreaRepository.findByFixedArea(fixedArea);
    }
    
    //查询未关联定区的分区
    @Override
    public List<SubArea> findUnAssociatedSubAreas() {
          
          
        return subAreaRepository.findByFixedAreaIsNull();
    }

}
  
