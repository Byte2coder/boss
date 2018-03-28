package com.itheima.bos.service.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.User;

/**
 * ClassName:UserRealm <br/>
 * Function: <br/>
 * Date: 2018年3月26日 下午9:59:02 <br/>
 */
@Component

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    // 授权
    //info.addStringPermission("courierAction_pageQuery");//每一次访问都会调用,不好
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

      SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
       //授权 
      info.addStringPermission("courierAction_pageQuery");
      //授予角色
      info.addRole("admin");
      return info;
    }

    // 认证

    // 参数中的token就是subject.login(token)传入的token
    //UnknownAccountException,用户名不一致
    //IncorrectCredentialsException:密码错误
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        // 对传入的token进行类型强转
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();
        // 根据用户名查找用户
        User user = userRepository.findByUsername(username);
        // 查找得到
        if (user != null) {
            //
            Object principal = user;
            Object credentials = user.getPassword();
            String realmName = getName();
            AuthenticationInfo info =
                    new SimpleAuthenticationInfo(principal, credentials, realmName);

            //直接完成代码的比对
            return info;
        }

        // 找到--比对密码
        // 成功
        // 失败
        // 或者查找不到

        return null;
    }

}
