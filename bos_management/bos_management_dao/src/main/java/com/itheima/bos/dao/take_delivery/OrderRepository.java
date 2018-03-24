package com.itheima.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.take_delivery.Order;

/**  
 * ClassName:OrderRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午5:40:38 <br/>       
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}
  
