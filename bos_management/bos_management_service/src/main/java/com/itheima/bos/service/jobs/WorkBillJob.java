package com.itheima.bos.service.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.utils.MailUtils;

/**
 * ClassName:WorkBillJob <br/>
 * Function: <br/>
 * Date: 2018年3月30日 下午8:00:30 <br/>
 */
@Component
public class WorkBillJob {

    @Autowired
    private WorkBillRepository workBillRepository;

    public void sendMail() {
        List<WorkBill> list = workBillRepository.findAll();

        SimpleDateFormat sdf = new SimpleDateFormat("ss-MM-hh dd-MM-yyyy");
        Date date = new Date();
        String sDate = sdf.format(date);

        String emailBody = "编号\t快递员\t取件状态\t时间<br/>";
        for (WorkBill workBill : list) {
            emailBody += workBill.getId() + "\t" + workBill.getCourier() + "\t"
                    + workBill.getPickstate() + "\t" + sDate + "<br/>";
        }

        MailUtils.sendMail("ok@store.com", "工单信息统计", emailBody);
        System.out.println("邮件已经发送!");
    }
}
