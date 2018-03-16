package com.itheima.bos.web.action.base;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:SubAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:18:30 <br/>       
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class SubAreaAction extends CommonAction<SubArea> {

    public SubAreaAction() {
        super(SubArea.class);  
    }

    @Autowired
    public SubAreaService subAreaService;
    
    
    //保存数据
    @Action(value="subAreaAction_save",results={@Result(name="success",
                     location="/pages/base/sub_area.html",type="redirect")})
    public String save(){
        subAreaService.save(getModel());
        return SUCCESS;
    }
    
    
    
    //分页查询
    @Action("subArea_pageQuery")
    public String pageQuery() throws IOException{
        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
       Pageable pageable=new PageRequest(page-1, rows);
       Page<SubArea> page= subAreaService.findAll(pageable);
         
       JsonConfig jsonConfig=new JsonConfig();
       jsonConfig.setExcludes(new String[]{"fixedArea",""});
       
        page2json(page, jsonConfig);
        
        return NONE;
    }
    

}
  
