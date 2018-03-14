package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
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

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CourierAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午8:05:17 <br/>
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage(value = "struts-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

    private Courier model = new Courier();

    @Autowired
    private CourierService courierService;

    @Override
    public Courier getModel() {

        return model;
    }

    @Action(value = "courierAction_save", results = {
            @Result(name = "success", location = "/pages/base/courier.html", type = "redirect")})
    public String save() {
        courierService.save(model);
        return SUCCESS;
    }
    
    private int page;
    private int rows;
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "courierAction_pageQuery") 
    public String pageQuery() throws IOException {

       Pageable pageable=new PageRequest(page-1, rows);
      Page<Courier> page= courierService.findAll(pageable);
        
      long total = page.getTotalElements();
      List<Courier> content = page.getContent();
      //封装数据
      Map<String, Object> map=new HashMap<String, Object>();
      
      map.put("total", total);
      map.put("rows", content);
      
      //灵活控制输出的字段
      JsonConfig jsonConfig=new JsonConfig();
      jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
      //转换成json数组
      //实际开发的时候,为了提高计算机的性能,把前台不需要的数据
      String json = JSONObject.fromObject(map,jsonConfig).toString();
      
      HttpServletResponse response = ServletActionContext.getResponse();
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().write(json);
      
        return NONE;
    }

    //使用属性驱动获取要删除的快递员的ids
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    //courierAction_batchDel
    @Action(value = "courierAction_batchDel",results={@Result(
              name="success",location="/pages/base/courier.html",type="redirect")}) 
    public String batchDel() throws IOException {
       
        courierService.batchDel(ids);
        return SUCCESS;
    }
    
}
