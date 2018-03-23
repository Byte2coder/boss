package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.FixedArea;

/**  
 * ClassName:FixedAreaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午9:09:01 <br/>       
 */
public interface FixedAreaRepository extends JpaRepository<FixedArea, Long>{

    FixedArea findById(Long fixedAreaId);

    @Query(value="insert into T_FIXEDAREA_COURIER values(?2 , ?1)  ",nativeQuery=true)
    @Modifying
    void updateBoth(Long courierId, Long fixedAreaId );
}
  
