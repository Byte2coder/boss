package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Role;

/**  
 * ClassName:RoleRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午4:24:13 <br/>       
 */
public interface RoleRepository extends JpaRepository<Role, Long>{

    //下面的省略的select * ,其实查询的是所有表中的所有字段
    //查完把结果封装给对象,封装了user和role两个对象,
    //所以产生了相同集合里的有多个对象,导致类型不匹配ClassCastException:
    @Query("select r from Role r inner join r.users u where u.id= ? ")
    List<Role> findByuId(Long id);

}
  
