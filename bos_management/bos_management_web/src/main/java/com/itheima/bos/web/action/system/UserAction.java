package com.itheima.bos.web.action.system;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
import org.springframework.stereotype.Controller;

import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:UserAction <br/>
 * Function: <br/>
 * Date: 2018年3月26日 下午9:35:57 <br/>
 */
@Namespace("/")
@Controller
@ParentPackage("struts-default")
@Scope("prototype")
public class UserAction extends CommonAction<User> {

    public UserAction() {
        super(User.class);
    }

    @Autowired
    private UserService userService;

    // 用户输入的验证码
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Action(value = "userAction_login",
            results = {@Result(name = "success", location = "/index.html", type = "redirect"),
                    @Result(name = "login", location = "/login.html", type = "redirect")

            })
    public String login() {

        // 获取服务器上的验证码
        String servercode =
                (String) ServletActionContext.getRequest().getSession().getAttribute("key");
        if (StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(servercode)
                && servercode.equals(checkcode)) {
            // 获取当前用户
            Subject subject = SecurityUtils.getSubject();

            AuthenticationToken token =
                    new UsernamePasswordToken(getModel().getUsername(), getModel().getPassword());
            try {
                subject.login(token);
                // 将用户存到域对象中,返回值有realm中定义
                User user = (User) subject.getPrincipal();
                return SUCCESS;
            } catch (IncorrectCredentialsException e) {
                e.printStackTrace();
                System.out.println("密码输入错误!");
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                System.out.println("用户名输入错误!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("未知错误!");

            }

        }

        return LOGIN;
    }

    @Action(value = "userAction_logout",
            results = {@Result(name = "success", location = "/login.html", type = "redirect")})
    public String logout() {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    // 属性驱动获取roleIds
    private Long[] roleIds;

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    @Action(value = "userAction_save", results = {
            @Result(name = "success", location = "/pages/system/userlist.html", type = "redirect")})
    public String save() {

        userService.save(roleIds, getModel());

        return SUCCESS;
    }

    // userAction_pageQuery
    @Action(value = "userAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable =new PageRequest(page-1, rows);
       
        Page<User> page= userService.finaAll(pageable);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"roles"});
        page2json(page, jsonConfig);
        
        return NONE;
    }
}
