package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.itheima.bos.domain.base.Area;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CommonAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午9:34:29 <br/>       
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {

    private T model;
    
    private Class<T> clazz;
    
    public CommonAction(Class<T> clazz ) {
        try {
            model = clazz.newInstance();//*1
        } catch (Exception e) {
            e.printStackTrace();          
        }
        this.clazz=clazz;
    }

    //*1放在下面方法时,如果方法在调用时会发生两次实例化,第一次会传入数据,
       //但是调用getModel方法时会重新实例化导致结果没有数据
    //解决方法1.是进行判断2.或者将实例化放在构造函数中
    @Override
    public T getModel() {
          
        return model;
    }
    
    //使用属性驱动获取数据
    protected int page;
    protected int rows;

    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public void page2json( Page<T> page,JsonConfig jsonConfig) throws IOException{
        
        //总数据条数
        long total = page.getTotalElements();
       
        List<T> list = page.getContent();
        
        Map<String ,Object> map=new HashMap<String, Object>();
        
        map.put("total", total);
        map.put("rows", list);
        String json;
        if (jsonConfig!=null) {
            
             json = JSONObject.fromObject(map,jsonConfig).toString();
        }else {
            
             json = JSONObject.fromObject(map).toString();
        }
        //转换数据为json
        
        HttpServletResponse response = ServletActionContext.getResponse();
        
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    public void list2json(List list,JsonConfig jsonConfig) throws IOException{
      
        String json;
        if (jsonConfig!=null) {
            json= JSONArray.fromObject(list,jsonConfig).toString();
        }else {
            json= JSONArray.fromObject(list).toString();
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
  
