package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**
 * ClassName:ImageAction <br/>
 * Function: <br/>
 * Date: 2018年3月26日 下午2:50:57 <br/>
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class ImageAction extends ActionSupport {

    // 使用属性驱动获取文件
    private File imgFile;

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    // 使用属性驱动获取文件名
    private String imgFileFileName;

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    @Action("imageAction_upload")
    public String upload() throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            // 保存文件的文件夹
            String path = "/upload";
            // 获取文件的磁盘路径
            ServletContext servletContext = ServletActionContext.getServletContext();
            String realPath = servletContext.getRealPath(path);

            // 获取文件的后缀名
            String suffix = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));

            // 生成唯一标示字符串
            String uName = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

            // 文件名
            String destFileName = uName + suffix;

            File destFile = new File(realPath + "/" + destFileName);

            FileUtils.copyFile(imgFile, destFile);

            // 获取本项目路径
            String contextPath = servletContext.getContextPath();
            // 上传成功
            map.put("error", 0);
            map.put("url", contextPath + "/upload/" + destFileName);
        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("error", 1);
            map.put("message", e.getMessage());

        }

        // 获取json字符串
        String json = JSONObject.fromObject(map).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

        return NONE;
    }

    @Action("imageAction_manager")
    public String manager() throws IOException {

        // 图片扩展名
        String[] fileTypes = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};
        // 保存文件的文件夹
        String path = "/upload";
        // 获取文件的磁盘路径
        ServletContext servletContext = ServletActionContext.getServletContext();
        String realPath = servletContext.getRealPath(path);

        File currentPathFile = new File(realPath);

        // 遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt =
                            fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        JSONObject result = new JSONObject();
        // 获取本项目路径
        String contextPath = servletContext.getContextPath();
        result.put("current_url", contextPath + "/upload/");
        result.put("file_list", fileList);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result.toString());
        return NONE;
    }
    

}
