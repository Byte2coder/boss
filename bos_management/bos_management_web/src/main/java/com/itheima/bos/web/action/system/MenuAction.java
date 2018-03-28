package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Menu;
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
}
  
