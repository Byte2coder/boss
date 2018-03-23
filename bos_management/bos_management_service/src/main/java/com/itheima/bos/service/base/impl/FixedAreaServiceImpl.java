package com.itheima.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.service.base.FixedAreaService;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午9:08:07 <br/>       
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements  FixedAreaService {

    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    
    @Autowired
    private CourierRepository courierRepository;
    
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    
    @Autowired
    private SubAreaRepository subAreaRepository;
    
    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }
    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
          
         
        return fixedAreaRepository.findAll(pageable);
    }
    @Override
    public void associationCourierToFixedArea(Long courierId, Long takeTimeId, Long fixedAreaId) {
       //查出对象 
        //持久态--更新对象
       Courier courier=  courierRepository.findById(courierId);
      TakeTime takeTime= takeTimeRepository.findById(takeTimeId);
       FixedArea fixedArea= fixedAreaRepository.findById(fixedAreaId);
    
       //建立快递员和时间的关联
       courier.setTakeTime(takeTime);
    
      //快递员关联定区 不能执行,因其放弃外键维护 courier.getFixedAreas().add(fixedArea);
    
       //定区关联快递员
       fixedArea.getCouriers().add(courier);
        
      // fixedAreaRepository.updateBoth(courierId, fixedAreaId);
    }
    
    //关联分区到指定的分区
    @Override
    public void assignSubAreas2FixedArea(Long fixedAreaId, Long[] subAreaIds) {
        //先将所有的分区解绑
        //查询出的持久态对象
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Set<SubArea> subareas = fixedArea.getSubareas();
       //解绑操作,subArea负责维护外键,subArea负责解绑
        for (SubArea subArea : subareas) {
        subArea.setFixedArea(null);
      }
        
        //再将选中的分区关联到指定的定区
        for (Long subAreaId : subAreaIds) {
            SubArea subArea = subAreaRepository.findOne(subAreaId);
            subArea.setFixedArea(fixedArea);
        }
        
        
    }

  
    
    
}
  
