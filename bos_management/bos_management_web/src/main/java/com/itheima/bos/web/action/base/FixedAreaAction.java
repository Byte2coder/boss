package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Customer;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:FixedAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午9:05:10 <br/>       
 */
@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
public class FixedAreaAction extends CommonAction<FixedArea> {

    public FixedAreaAction() {
        super(FixedArea.class); 
    }
    
    @Autowired
    private FixedAreaService fixedAreaService;
    
    
    
    @Action(value="fixedAreaAction_save",results={@Result(name="success",
                location="/pages/base/fixed_area.html",type="redirect")})
    public String save(){
        fixedAreaService.save(getModel());
        
        return SUCCESS;
    }

    @Action(value="fixedAreaAction_pageQuery")
    public String pageQuery() throws IOException{
        Pageable pageable=new PageRequest(page-1, rows);
        
        Page<FixedArea> page= fixedAreaService.findAll(pageable);
       
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        page2json(page, jsonConfig);
        
        return NONE;
    }
    
    //向CRM系统发起请求,查询未关联的客户
    @Action(value="fixedAreaAction_findUnAssociatedCustomers")
    public String findUnAssociatedCustomers() throws IOException{
        List<Customer> list= 
                (List<Customer>) WebClient.create("http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated")
             .accept(MediaType.APPLICATION_JSON)
             .type(MediaType.APPLICATION_JSON)
             .getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }
    
    //向CRM系统发起请求,查询定区关联的客户
    @Action(value="fixedAreaAction_findAssociatedCustomers")
    public String findAssociatedCustomers() throws IOException{
        
       List<Customer> list= (List<Customer>) WebClient.create("http://localhost:8180/crm/webService/customerService/findCustomersAssociated2FixedArea")
              .query("fixedAreaId", getModel().getId())
              .accept(MediaType.APPLICATION_JSON)
              .type(MediaType.APPLICATION_JSON)
              .getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }
    
    //使用属性驱动获取要关联到指定定区的客户ID
    private Long[] customerIds;
    
    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }
    
     
    
    //关联快递员
    @Action(value="fixedAreaAction_assignCustomers2FixedArea",
            results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String assignCustomers2FixedArea() throws IOException{
        WebClient.create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea")
                  .query("customerIds", customerIds)
                  .query("fixedAreaId", getModel().getId())
                  .accept(MediaType.APPLICATION_JSON)
                  .type(MediaType.APPLICATION_JSON)
                  .put(null);
        
        return SUCCESS;
    }
   
    
    //使用属性驱动获取快递员和时间的ID
    private Long courierId;
    private Long takeTimeId;
    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
    
    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    
    //关联快递员
     //fixedAreaAction_associationCourierToFixedArea.action
    @Action(value="fixedAreaAction_associationCourierToFixedArea",results={@Result(
            name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String associationCourierToFixedArea(){
        fixedAreaService.associationCourierToFixedArea(courierId,takeTimeId,getModel().getId());
        
        return SUCCESS;
    }
    
    
    
}
  