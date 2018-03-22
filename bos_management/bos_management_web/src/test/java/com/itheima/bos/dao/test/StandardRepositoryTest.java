package com.itheima.bos.dao.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepositoryTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午8:59:29 <br/>       
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {

    @Autowired
    private StandardRepository standardRepository;
    //@Test
    public void test() {
        List<Standard> list = standardRepository.findAll();
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    //保存
    //@Test
    public void test02() {
        Standard standard=new Standard();
        standard.setName("和平平");
        standard.setMaxWeight(250);
        standardRepository.save(standard);
        
    }
    //更新-save
    //@Test
    public void test03() {
        Standard standard=new Standard();
        standard.setId(2L);
        standard.setMaxWeight(250);
        standardRepository.save(standard);
        
    }
    //查询--id
    //@Test
    public void test04() {
        
        Standard standard = standardRepository.findOne(2L);
        System.out.println(standard);
        
    }
    //删除
    //@Test
    public void test05() {
        
        standardRepository.delete(2L);
        
    }
    //自定义查找name
    //@Test
    public void test06() {
        
         List<Standard> list = standardRepository.findByName("和平");
      System.out.println(list.get(0));  
    }
    //模糊查询
    //@Test
    public void test07() {
        
      List<Standard> list = standardRepository.findByNameLike("%和%");
      for (Standard standard : list) {
        
          System.out.println(standard);
        }
    }
    //@Test
    public void test08() {
        
        List<Standard> list = standardRepository.findByNameAndMaxWeight("和平", 1000);
        for (Standard standard : list) {
            
            System.out.println(standard);
        }
    }
    
    
    //@Test
    public void test09() {
        
        List<Standard> list = standardRepository.findByNameAndMaxWeight469979("和平", 1000);
        for (Standard standard : list) {
            
            System.out.println(standard);
        }
    }
   // @Test
    public void test10() {
        
        List<Standard> list = standardRepository.findByNameAndMaxWeight469979( 1000,"和平");
        for (Standard standard : list) {
            
            System.out.println(standard);
        }
    }
    
    //原生SQL
   // @Test
    public void test11() {
        
        List<Standard> list = standardRepository.findByNameAndMaxWeight469979ppp( "和平",1000);
        for (Standard standard : list) {
            
            System.out.println(standard);
        }
    }
    
    //更改类的操作
    //修改update
    //@Transactional
    //在测试用例中使用事务,修改方法会执行,但是又回滚了,
    //最终不会修改数据,需要将@Transactional加到定义方法的地方
   // @Test
    public void test12() {
        
       standardRepository.updateWeightByName(999, "和平");
    }
    
    //删除操作
    //@Test
    public void test13() {
        
      standardRepository.deleteByName("和平");
    }

}
  
