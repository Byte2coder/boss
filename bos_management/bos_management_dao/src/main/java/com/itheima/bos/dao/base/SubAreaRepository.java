package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:24:56 <br/>       
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Long> {

    //查询未关联定区的分区
    List<SubArea> findByFixedAreaIsNull();
   
    //查询关联到指定定区的分区
    //单一对象时可以作为参数传递进去,若为集合则不可
    //传入参数必须指定id属性
    List<SubArea> findByFixedArea(FixedArea fixedArea);
}
  
