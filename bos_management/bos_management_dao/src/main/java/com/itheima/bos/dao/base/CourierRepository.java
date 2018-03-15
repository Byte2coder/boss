package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午8:21:45 <br/>       
 */
//JpaSpecificationExecutor不单独使用,
//一般是和JpaRepository一起使用
public interface CourierRepository extends JpaRepository<Courier, Long>,
                 JpaSpecificationExecutor<Courier>{

    @Query("update Courier set deltag=1 where id = ?")
    @Modifying
    void updateDelTagById(Long id);


}
  
