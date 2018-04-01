package com.itheima.bos.fore.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.PageBean;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**  
 * ClassName:PromotionAction <br/>  
 * Function:  <br/>  
 * Date:     2018年4月1日 下午3:44:24 <br/>       
 */
@Namespace("/")
@Scope("prototype")
@Controller
@ParentPackage("struts-default")
public class PromotionAction extends ActionSupport {
    
    //使用属性驱动获取属性
    private int pageIndex;//当前页吗
    private int pageSize;
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    @Action("promotionAction_pageQuery")
   public String pageQuery() throws IOException{
       //调用后台数据请求 
        PageBean pageBean = WebClient.create("http://localhost:8080/bos_management_web/webService/promotionService/findAll4Fore")
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON)
       .query("pageIndex", pageIndex)
       .query("pageSize", pageSize)
        .get(PageBean.class);
        
        String json = JSONObject.fromObject(pageBean).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
        
        return NONE;
    }

}
  
