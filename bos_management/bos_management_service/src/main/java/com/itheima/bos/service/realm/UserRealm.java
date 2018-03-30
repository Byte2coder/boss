package com.itheima.bos.service.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
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
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    // 授权
    // info.addStringPermission("courierAction_pageQuery");//每一次访问都会调用,不好
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 查询当前用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        // 根据当前用户,查询对应的角色和权限
        //角色是权限的集合,但是角色不包含所有权限
        if ("admin".equals(user.getUsername())) {
            //管理员获取所有角色和所有权限
            List<Role> roles = roleRepository.findAll();
            List<Permission> permissions = permissionRepository.findAll();
            for (Permission permission : permissions) {
                info.addStringPermission(permission.getKeyword());
            }
            for (Role role : roles) {
                info.addRole(role.getKeyword());
            }
        } else {
            //uId不写成Uid是防止框架自动默认为自身属性
            List<Role> roles =  roleRepository.findByuId(user.getId());
            List<Permission> permissions =permissionRepository.findByuId(user.getId());

            for (Permission permission : permissions) {
                info.addStringPermission(permission.getKeyword());
            }
            for (Role role : roles) {
                info.addRole(role.getKeyword());
            }
        }
        return info;
    }

    // 认证

    // 参数中的token就是subject.login(token)传入的token
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
            // 找到--比对密码
            // 成功
            Object principal = user;
            Object credentials = user.getPassword();
            String realmName = getName();
            AuthenticationInfo info =
                    new SimpleAuthenticationInfo(principal, credentials, realmName);

            // 直接完成代码的比对
            return info;
        }
        // 失败
        // 或者查找不到
        return null;
    }

}
