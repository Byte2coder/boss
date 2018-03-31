package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.take_delivery.PromotionService;
import com.itheima.bos.web.action.CommonAction;

/**
 * ClassName:PromotionAction <br/>
 * Function: <br/>
 * Date: 2018年3月31日 下午5:03:21 <br/>
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class PromotionAction extends CommonAction<Promotion> {

    public PromotionAction() {
        super(Promotion.class);
    }

    @Autowired
    private PromotionService promotionService;

    private File titleImgFile;
    private String titleImgFileFileName;
    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }
    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Action(value = "promotionAction_save", results = {@Result(name = "success",
            location = "/pages/take_delivery/promotion.html", type = "redirect")})
    public String save() throws IOException {

        Promotion promotion = getModel();
        // 保存封面图片

        try {
            // 指定图片保存路径
            String dirPath = "/upload";
            // 获取文件的磁盘路路径
            ServletContext servletContext = ServletActionContext.getServletContext();
            String dirRealPath = servletContext.getRealPath(dirPath);
            // 获取文件的后缀名
            String suffix = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
            // 生成唯一标识符
            String filename =
                    UUID.randomUUID().toString().replaceAll("-", "").toUpperCase() + suffix;

            // 保存文件
            File destFile = new File(dirRealPath + filename);
            FileUtils.copyFile(titleImgFile, destFile);
            promotion.setTitleImg("/upload/" + filename);
        } catch (Exception e) {
            e.printStackTrace();
            promotion.setTitleImg(null);

        }

        promotion.setStatus("1");

        promotionService.save(promotion);

        return SUCCESS;
    }

}
