package com.itheima.activemq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.itheima.utils.SmsUtils;

/**  
 * ClassName:SmsConsumer <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午5:45:35 <br/>       
 */
@Component
public class SmsConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
          MapMessage mapMessage=(MapMessage) message;
         try {
            String tel = mapMessage.getString("tel");
            String code = mapMessage.getString("code");
            //System.out.println(tel+"....."+code);
            SmsUtils.sendSms(tel, code);
        } catch (Exception e) {
            e.printStackTrace();  
            
        }
        
    }

}
  
