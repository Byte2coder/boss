package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

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

import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
import com.itheima.bos.web.action.CommonAction;
import com.opensymphony.xwork2.util.location.Location;

import net.sf.json.JsonConfig;

/**
 * ClassName:RoleAction <br/>
 * Function: <br/>
 * Date: 2018年3月29日 下午4:22:34 <br/>
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class RoleAction extends CommonAction<Role> {

    public RoleAction() {
        super(Role.class);
    }

    @Autowired
    private RoleService roleService;
    // roleAction_pageQuery
    @Action(value = "roleAction_pageQuery")
    public String pageQuery() throws IOException {

        Pageable pageable=new PageRequest(page-1, rows);
        Page<Role> page=roleService.findALL(pageable);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"users","permissions","menus"});
        page2json(page, jsonConfig);
        return NONE;
    }
    
    

}
