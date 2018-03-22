package com.itheima.bos.fore.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:CustomerAction <br/>
 * Function: <br/>
 * Date: 2018年3月21日 下午2:42:28 <br/>
 */
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
@Namespace("/")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    private Customer model = new Customer();

    @Override
    public Customer getModel() {
        return model;
    }

    @Action("customerAction_sendSMS")
    public String sendSMS() throws Exception {

        // 随机生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println(code);
        ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);
        String telephone = model.getTelephone();
        SmsUtils.sendSms(telephone, code);

        return NONE;
    }
    
    //使用属性驱动获取输入的电话
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    //customerAction_checkTelephone
    @Action(value="customerAction_checkTelephone",results={
            @Result(name="success",location="/signup.html",type="redirect"),
            @Result(name="error",location="/signup.html",type="redirect")})
    public String checkTelephone() throws IOException{
        
        if (StringUtils.isNotEmpty(telephone)) {
            
            //查询数据库,获取数据库中的电话
            Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/save")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", telephone)
                    .get(Customer.class);
            if (customer!=null) {
                ServletActionContext.getResponse().getWriter().write("true");  
                return ERROR;
            }
            
       
        }
        ServletActionContext.getResponse().getWriter().write("false");  
        return SUCCESS;
        
        
    }
    
    

    // 使用属性驱动获取用户输入的验证码
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // customerAction_regist.action
    @Action(value = "customerAction_regist",
            results = {
                    @Result(name = "success", location = "/signup-success.html", type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html", type = "redirect")})
    public String regist() {

        // 校验 验证码
        String serverCode =
                (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        if (StringUtils.isNotEmpty(serverCode) && StringUtils.isNotEmpty(checkcode)
                && serverCode.equals(checkcode)) {
            WebClient.create("http://localhost:8180/crm/webService/customerService/save")
                    .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                    .post(model);
            
            //发送激活邮件
      String activeCode = RandomStringUtils.randomNumeric(32); 
      //存储验证码
    redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1, TimeUnit.DAYS);  
    MailUtils.sendMail(model.getEmail(), "激活邮件", 
                      "感谢注册,请在24小时之内点击本<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="+activeCode+"&telephone="+model.getTelephone()+"'>链接</a>激活账号");
            
          return SUCCESS;
        }

        return ERROR;
    }
    
    //http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated
   
    //使用属性驱动获取激活码
    private String  activeCode;
    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
    
    //激活验证码
    @Action(value = "customerAction_active",
            results = {
                    @Result(name = "success", location = "/login.html", type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html", type = "redirect")
                    })
    public String active() {
        //比对激活码
        String sCode = redisTemplate.opsForValue().get(model.getTelephone());
        if (StringUtils.isNotEmpty(sCode) &&  StringUtils.isNotEmpty(activeCode) && sCode.equals(activeCode)) {
         
            WebClient.create("http://localhost:8180/crm/webService/customerService/active")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .query("telephone", model.getTelephone())
            .put(null);
            
            return SUCCESS;
        }
        
        
       return ERROR; 
    }

    //属性驱动获取校验码
    
    @Action(value = "customerAction_login",
            results = {
                    @Result(name = "error", location = "/login.html", type = "redirect"),
                    @Result(name = "success", location = "/index.html", type = "redirect"),
                    @Result(name = "unActived", location = "/unActived.html", type = "redirect")
                    })
    public String login() {
        
        //判断验证码是否一致
        String vcode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
        
        if (StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(vcode)
                && vcode.equals(checkcode)) {
            //检查用户是否激活
            Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/isActived")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .query("telephone", model.getTelephone())
                .get(Customer.class);
            //type是integer ,有可能为null,必须先判断排除
            if (customer!=null && customer.getType()!=null ) {
                if (customer.getType()==1) {
                    //登录
                    
                    Customer customer2 = WebClient.create("http://localhost:8180/crm/webService/customerService/isActived")
                            .accept(MediaType.APPLICATION_JSON)
                            .type(MediaType.APPLICATION_JSON)
                            .query("telephone", model.getTelephone())
                            .query("password", model.getPassword())
                            .get(Customer.class);
                    //存储对象
                    ServletActionContext.getRequest().getSession().setAttribute("user", customer2);
                    
                    return SUCCESS;
                }else {
                    //注册但未激活
                    return "unActived";
                }
                
                
            }
            
        }
        
        return ERROR;
    }
}
