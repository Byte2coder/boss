package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
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
import com.itheima.bos.web.action.CommonAction;
import com.itheima.utils.FileDownloadUtils;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
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
public class AreaAction extends CommonAction<Area>{
  
    //既是无参,又可以调用父类的构造函数
    public AreaAction() {
        super(Area.class);  
    }


    @Autowired
    private AreaService areaService;
    
    

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
    
    
   
    
    // AJAX请求不需要跳转页面
    @Action(value = "areaAction_pageQuery")
    public String pageQuery() throws IOException {
       // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable=new PageRequest(page-1, rows);
        Page<Area> page= areaService.findAll(pageable);
         //总数据条数
        
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        
        page2json(page, jsonConfig);
        
        
        return NONE;
    }
    
    
    
    //使用属性驱动获取q值增强搜索查询功能
    private String q;
    public void setQ(String q) {
        this.q = q;
    }
    @Action(value = "areaAction_findAll")
    public String findAll() throws IOException {
        List<Area> list ;
        if (StringUtils.isNotEmpty(q)) {
          list= areaService.findByQ(q); 
        }else{
            
            Page<Area> page= areaService.findAll(null);
             list= page.getContent();
        }
       
        
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
       
        list2json(list, jsonConfig);
      
        return NONE;
    }
    
    //创建文件
    //创建sheet
    //创建行
    //创建列
    //写入数据
    @Action("areaAction_exportExcel")
    public String exportExcel() throws IOException{
     
        Page<Area> page = areaService.findAll(null);
        List<Area> list = page.getContent();
        
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        //创建标题行
        HSSFRow titleRow = sheet.createRow(0);
        //写入每一列数据
        titleRow.createCell(0).setCellValue("省");
        titleRow.createCell(1).setCellValue("市");
        titleRow.createCell(2).setCellValue("区");
        titleRow.createCell(3).setCellValue("邮编");
        titleRow.createCell(4).setCellValue("简码");
        titleRow.createCell(5).setCellValue("城市编码");
        
        //遍历写入数据
        for (Area area : list) {
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum+1);
          //写入每一列数据
            dataRow.createCell(0).setCellValue(area.getProvince());
            dataRow.createCell(1).setCellValue(area.getCity());
            dataRow.createCell(2).setCellValue(area.getDistrict());
            dataRow.createCell(3).setCellValue(area.getPostcode());
            dataRow.createCell(4).setCellValue(area.getShortcode());
            dataRow.createCell(5).setCellValue(area.getCitycode());
        }
        //创建文件名称
        String filename="城市分区信息.xls";
        
        ServletContext servletContext = ServletActionContext.getServletContext();
        HttpServletResponse response = ServletActionContext.getResponse();
       
        //放在解决乱码之前,是防止重新的编码改变filename的值
        //获取mimeType
        String mimeType = servletContext.getMimeType(filename);
       
        
        //解决乱码
        //获取请求头
        HttpServletRequest request = ServletActionContext.getRequest();
        //获取浏览器类型
        String header = request.getHeader("User-Agent");
        filename = FileDownloadUtils.encodeDownloadFilename(filename, header);
        
        
        //获取输出流,两头一流
        //设置信息头
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition",
                "attachment; filename=" + filename);
        
        //获取流
        ServletOutputStream outputStream = response.getOutputStream();
       
        
        workbook.write(outputStream);
        
        
        return NONE;
    }
}
  
