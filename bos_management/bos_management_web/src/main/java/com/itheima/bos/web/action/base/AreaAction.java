package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.AreaService;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:AreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午7:46:42 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class AreaAction extends ActionSupport implements ModelDriven<Area>{

    private Area model=new Area();
    
    @Autowired
    private AreaService areaService;
    
    @Override
    public Area getModel() {  
        return model;
    }

    //先使用属性驱动获取上传文件
    private File file;
    public void setFile(File file) {
        this.file = file;
    }
    @Action(value="areaAction_importXLS",results={
            @Result(name="success",location="/pages/base/area.html",type="redirect")})
    public  String importXLS(){
        
      /*  String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);*/
      //用来保存数据的集合
        List<Area> list=new ArrayList<Area>();
        try {
            HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(file));
       
            //读取工作簿
            HSSFSheet sheet = workbook.getSheetAt(0);
            //遍历
            for (Row row : sheet) {
                //第一行数据是标题,不需要
                if (row.getRowNum()==0) {
                    continue;
                }
                //读取表格数据
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();
           
                //截取掉最后一个字符
                province = province.substring(0,province.length()-1);
                city = city.substring(0,city.length()-1);
                district = district.substring(0,district.length()-1);
            
                //获取城市编码
               String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
            
               //获取简码
               String[] headByString = PinYin4jUtils.getHeadByString(province+city+district, true);
                String shortcode = PinYin4jUtils.stringArrayToString(headByString);
            
                Area area=new Area();
                area.setProvince(province);
                area.setCity(city);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);
                list.add(area);
    
            }
            areaService.save(list);
        
            //释放资源
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();  
            
        }
        
        
        return SUCCESS;
    }
    
    
    //使用属性驱动获取数据
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    // AJAX请求不需要跳转页面
    @Action(value = "areaAction_pageQuery")
    public String pageQuery() throws IOException {
       // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable=new PageRequest(page-1, rows);
        Page<Area> page= areaService.findAll(pageable);
         //总数据条数
        long total = page.getTotalElements();
        //当前页面要实现的内容
        List<Area> list = page.getContent();
        
        Map<String ,Object> map=new HashMap<String, Object>();
        
        map.put("total", total);
        map.put("rows", list);
        
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        //转换数据为json
        String json = JSONObject.fromObject(map,jsonConfig).toString();
        
        HttpServletResponse response = ServletActionContext.getResponse();
        
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
        
        return NONE;
    }
    
}
  
