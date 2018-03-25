package com.itheima.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.WayBillRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WayBillService;

/**  
 * ClassName:WaybillServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午9:03:28 <br/>       
 */
@Transactional
@Service
public class WayBillServiceImpl implements WayBillService {

    @Autowired
    private WayBillRepository wayBillRepository;

    @Override
    public void save(WayBill wayBill) {
          
        wayBillRepository.save(wayBill);
        
    }
}
  
