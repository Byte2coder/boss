package com.itheima.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * ClassName:OrderAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午5:03:09 <br/>       
 */
@Namespace("/")
@Scope("prototype")
@Controller
@ParentPackage("struts-default")
public class OrderAction  extends ActionSupport implements ModelDriven<Order>{

    private Order model=new Order();
    @Override
    public Order getModel() {
        return model;
    }
    
    //使用属性驱动获取数据收件和发件的区域
    private String sendAreaInfo;
    private String recAreaInfo;
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    
    @Action(value="orderAction_add",results={
            @Result(name="success",location="/index.html",type="redirect")
    })
    public String saveOrder(){
        //先判空
        if (StringUtils.isNotEmpty(sendAreaInfo)) {
           String[] split = sendAreaInfo.split("/");
           //切割数据
           String province=split[0].substring(0, split[0].length()-1);
           String city=split[1].substring(0, split[1].length()-1);
           String district=split[2].substring(0, split[2].length()-1);
           //封装数据
           Area area=new Area();
           area.setProvince(province);
           area.setCity(city);
           area.setDistrict(district);
            
           model.setSendArea(area);
        }
        
        if (StringUtils.isNotEmpty(recAreaInfo)) {
            String[] split = sendAreaInfo.split("/");
            //切割数据
            String province=split[0].substring(0, split[0].length()-1);
            String city=split[1].substring(0, split[1].length()-1);
            String district=split[2].substring(0, split[2].length()-1);
            //封装数据
            Area area=new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            
            model.setRecArea(area);
        }
        
        //下单,需要webService
        WebClient.create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder")
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON)
        .post(model);
        
        return SUCCESS;
    }

}
  
