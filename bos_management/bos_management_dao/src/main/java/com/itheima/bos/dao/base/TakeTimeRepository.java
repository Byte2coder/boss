package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.TakeTime;

/**  
 * ClassName:TakeTimeRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月20日 上午9:06:42 <br/>       
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Long>{

    TakeTime findById(Long takeTimeId);

   
}
  
