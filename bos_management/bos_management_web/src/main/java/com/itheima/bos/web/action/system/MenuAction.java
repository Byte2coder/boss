package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:MenuAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:33:23 <br/>       
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class MenuAction extends CommonAction<Menu>{

    public MenuAction() {
        super(Menu.class);  
    }

    @Autowired
    private MenuService menuService;
    
    @Action(value = "menuAction_findLevelOne")
    public String findLevelOne() throws IOException {
       List<Menu> list=menuService.findLevelOne();
       
       JsonConfig jsonConfig=new JsonConfig();
      //发生死循环,忽略parentMenu
       jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu"});
       
       list2json(list, jsonConfig);
       
        return NONE;
    }
    
    @Action(value = "menuAction_save", results = {
            @Result(name = "success", location = "/pages/system/menu.html", type = "redirect")})
    public String save() {
        System.out.println();
        menuService.save(getModel());
        return SUCCESS;
    }
    
    //struts框架在封装框架时优先封装给模型对象(page有重名)
    @Action(value = "menuAction_pageQuery")
    public String pageQuery() throws IOException {
        
      Pageable pageable=new PageRequest(Integer.parseInt(getModel().getPage())-1, rows);
      Page<Menu> page= menuService.findAll(pageable);
        JsonConfig jsonConfig=new JsonConfig();
        //parentMenu,会产生死循环,要忽略
        jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu"});
        page2json(page, jsonConfig);
        return NONE;
    }
    
    //menuAction_findByUser
    @Action("menuAction_findByUser")
    public String findByUser() throws IOException{
     //使用框架获取用户
      Subject subject = SecurityUtils.getSubject();
      User user = (User) subject.getPrincipal();
      List<Menu> list= menuService.findByUser(user);
      JsonConfig jsonConfig=new JsonConfig();
      //parentMenu,会产生死循环,要忽略
      jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu","children"});
      list2json(list, jsonConfig);
        return NONE;
    }
    
}
  
