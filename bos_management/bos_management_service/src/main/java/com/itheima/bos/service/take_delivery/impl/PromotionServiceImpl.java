package com.itheima.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.PromotionRepository;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.take_delivery.PromotionService;

/**  
 * ClassName:PromotionServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月31日 下午5:02:41 <br/>       
 */
@Transactional
@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void save(Promotion promotion) {
          
        promotionRepository.save(promotion);
        
    }
    
}
  
