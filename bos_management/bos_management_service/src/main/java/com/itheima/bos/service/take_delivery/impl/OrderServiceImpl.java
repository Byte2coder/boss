package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService;

/**
 * ClassName:OrderServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月23日 下午5:42:45 <br/>
 */
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private FixedAreaRepository fixedAreaRepository;
   
    @Autowired
    private WorkBillRepository workBillRepository;
    
    @Override
    public void saveOrder(Order order) {

        // 将瞬时态的area转化为持久态的area
        Area sendArea = order.getSendArea();
        if (sendArea != null) {
            // 持久化对象
            Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
            order.setSendArea(sendAreaDB);
        }

        Area recArea = order.getRecArea();
        if (recArea != null) {
            // 持久化对象
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(),
                    recArea.getCity(), recArea.getDistrict());
            order.setRecArea(recAreaDB);
        }

        //保存订单
        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderTime(new Date());  
        orderRepository.save(order);
        
        //自动分单
        String sendAddress = order.getSendAddress();
      
        if (StringUtils.isNotEmpty(sendAddress)) {
            
            //--根据发货地址完全匹配
            //让crm系统根据发件地址查询定区ID
            String fixedAreaId = 
                    WebClient.create("http://localhost:8180/crm/webService/customerService/findFixedAreaIdByAddress")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .query("address", sendAddress)
                    .get(String.class);
            
            //System.out.println(fixedAreaId);
            //需要先判断fixedAreaId不为空,才可以走模糊查询的方法
            if(StringUtils.isNotEmpty(fixedAreaId)){
                
                //根据定区ID查询定区
                FixedArea fixedArea=  fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));
                if (fixedArea!=null) {
                    //查询快递员
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (!couriers.isEmpty()) {
                        //安排快递员取件
                        Iterator<Courier> iterator = couriers.iterator();
                        Courier courier = iterator.next();
                        //指派该快递员
                        order.setCourier(courier);
                        //生成工单
                        WorkBill workBill=new WorkBill();
                        
                        workBill.setAttachbilltimes(0);
                        workBill.setBuildtime(new Date());
                        workBill.setCourier(courier);
                        workBill.setOrder(order);
                        workBill.setPickstate("新单");
                        workBill.setRemark(order.getRemark());
                        workBill.setSmsNumber("11111");
                        workBill.setType("新");
                        
                        workBillRepository.save(workBill);
                        
                        //发送短信,推送通知
                        order.setOrderType("自动分单");
                        //中断代码的执行
                        return;
            }
             }
          }else {
              //根据发货地址模糊匹配
              
              //已经持久化了order对象,所以order查询出来的是持久态对象
              Area sendArea2 = order.getSendArea();
              if (sendArea2!=null) {
                Set<SubArea> subareas = sendArea2.getSubareas();
                for (SubArea subArea : subareas) {
                    String assistKeyWords = subArea.getAssistKeyWords();
                    String keyWords = subArea.getKeyWords();
                    if (sendAddress.contains(keyWords)||
                            sendAddress.contains(assistKeyWords)) {
                        FixedArea fixedArea2 = subArea.getFixedArea();
                        if (fixedArea2!=null) {
                          Set<Courier> couriers = fixedArea2.getCouriers();
                          if (!couriers.isEmpty()) {
                              //指派快递员
                              Iterator<Courier> iterator = couriers.iterator();
                              Courier courier = iterator.next();
                              order.setCourier(courier);
                              //生成工单
                              WorkBill workBill=new WorkBill();
                              workBill.setAttachbilltimes(0);
                              workBill.setBuildtime(new Date());
                              workBill.setCourier(courier);
                              workBill.setOrder(order);
                              workBill.setPickstate("新单 ");
                              workBill.setRemark(order.getRemark());
                              workBill.setSmsNumber("22222");
                              workBill.setType("新");
                            //保存分单
                              workBillRepository.save(workBill);
                              
                              //发送通知,短信
                              order.setOrderType("人工分单");
                              
                              //中断代码执行
                              return;
                           }
                            
                        }
                    }
                }
            }
            
        }
       
        
        }
        
         
        
        
    }

}
