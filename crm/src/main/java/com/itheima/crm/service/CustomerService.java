package com.itheima.crm.service; 
import java.util.*;
/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午8:21:13 <br/>       
 */

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CustomerService {



    @GET
    @Path("/findAll")
    List<Customer> findAll();
  
    @GET
    @Path("/findCustomersUnAssociated")
    List<Customer> findCustomersUnAssociated();
    
    //查询已关联到指定定区的客户
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);

   //定区 ID,要关联的数据
    //根据定区ID,把关联到这个定区的所有客户全部解绑
    //要关联的数据和定区ID进行绑定
    
     @PUT
     @Path("/assignCustomers2FixedArea")
     void assignCustomers2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId,@QueryParam("customerIds") Long[] customerIds);
     
}
  
