package com.itheima.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**  
 * ClassName:RedisTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月21日 下午9:18:17 <br/>       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Test
    public void test01(){
        //redisTemplate.opsForValue().set("name", "zsss");
   //设置时间的有效期,过期以后自动删除
        redisTemplate.opsForValue().set("telephone", "110", 10, TimeUnit.SECONDS);
       
        //删除
        redisTemplate.delete("name");
    }
    
}
  
