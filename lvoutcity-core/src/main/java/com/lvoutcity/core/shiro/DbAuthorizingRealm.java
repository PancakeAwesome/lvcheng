package com.lvoutcity.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/** 
* @ClassName: DbAuthorizingRealm 
* @Description: Shiro AuthorizingRealm
* @author Gdh
* @date 2016年4月25日 上午11:26:39 
*  
*/
public class DbAuthorizingRealm extends AuthorizingRealm{

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		// 获取用户权限
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
	   // 验证用户名及密码
		
		return null;
	}

}
