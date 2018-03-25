package com.itheima.bos.web.action.take_delivery;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WayBillService;
import com.itheima.bos.web.action.CommonAction;

/**
 * ClassName:WaybillAction <br/>
 * Function: <br/>
 * Date: 2018年3月25日 下午9:04:33 <br/>
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class WayBillAction extends CommonAction<WayBill> {

    @Autowired
    private WayBillService wayBillService;

    public WayBillAction() {
        super(WayBill.class);
    }

    @Action("wayBillAction_save")
    public String save() throws IOException {
        String msg = "0";
        try {
            
            wayBillService.save(getModel());
        } catch (Exception e) {
            e.printStackTrace();
            msg = "1";

            ServletActionContext.getResponse().getWriter().write(msg);

        }

        return NONE;
    }

}
