package com.example.web;

import java.util.*;

import com.example.domain.Permission;
import com.example.domain.Role;
import com.example.domain.User;
import com.example.domain.UserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.Filter;
import javax.sql.DataSource;

/**
 * Created by qiuzhanghua on 16/3/4.
 */

@Configuration
// @ConditionalOnBean(DataSource.class)
//@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class ShiroConfiguration {

  @Autowired
  private DataSource dataSource;


  @Autowired
  UserRepository userRepository;

  //  @Bean(name = "ShiroRealmImpl")
  public ShiroRealmImpl getShiroRealm() {
    return new ShiroRealmImpl();
  }

  @Bean
//  @DependsOn({"jacksonObjectMapper"})
  @Order(Ordered.LOWEST_PRECEDENCE)
  public JdbcRealm jdbcRealm() {
    System.out.println("================================");
    System.out.println(dataSource);
    System.out.println("================================");
    JdbcRealm realm = new JdbcRealm();
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
    realm.setCredentialsMatcher(credentialsMatcher);
    realm.setDataSource(dataSource);
    realm.init();
    return realm;
  }

  @Bean(name = "shiroEhcacheManager")
  public EhCacheManager getEhCacheManager() {
    EhCacheManager em = new EhCacheManager();
    em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
    return em;
  }

//  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
    daap.setProxyTargetClass(true);
    return daap;
  }

  @Bean(name = "securityManager")
  @Order(Ordered.LOWEST_PRECEDENCE)
  public DefaultWebSecurityManager getDefaultWebSecurityManager() {
    DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
//    dwsm.setRealm(getShiroRealm());
//    dwsm.setRealm(jdbcRealm());
    dwsm.setRealm(new JpaAuthorizingRealm(credentialMatcher()));
    System.out.println("*****************************************");
    System.out.println(dwsm.getRealms());
    System.out.println(userRepository);
    System.out.println("*****************************************");
    dwsm.setCacheManager(getEhCacheManager());
    return dwsm;
  }

    @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
    AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
    aasa.setSecurityManager(getDefaultWebSecurityManager());
    return new AuthorizationAttributeSourceAdvisor();
  }

  private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

//  @Bean(name = "shiroFilter")
//  @Order(Ordered.LOWEST_PRECEDENCE)
//  public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
//    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//    shiroFilterFactoryBean
//        .setSecurityManager(getDefaultWebSecurityManager());
//    shiroFilterFactoryBean.setLoginUrl("/login");
//    shiroFilterFactoryBean.setSuccessUrl("/sa/index");
//    filterChainDefinitionMap.put("/sa/**", "authc");
//    filterChainDefinitionMap.put("/**", "anon");
//    shiroFilterFactoryBean
//        .setFilterChainDefinitionMap(filterChainDefinitionMap);
//    return shiroFilterFactoryBean;
//  }

//  @AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
  @Bean(name="shiroxx")
  public FilterRegistrationBean contextFilterRegistrationBean()  throws Exception{
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(shiroFilter());
//    registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return registrationBean;
  }

//    @Bean(name = "shiroFilter")
//  @Order(Ordered.LOWEST_PRECEDENCE)
  public AbstractShiroFilter shiroFilter()  throws Exception {
    ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
    Map<String, String> filterChainDefinitionMapping = new HashMap<>();
    filterChainDefinitionMapping.put("/api/health", "authc,roles[guest],ssl[8443]");
    filterChainDefinitionMapping.put("/login", "authc");
    filterChainDefinitionMapping.put("/logout", "logout");
    shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
    shiroFilter.setSecurityManager(getDefaultWebSecurityManager());
    shiroFilter.setLoginUrl("/login");
    Map<String, Filter> filters = new HashMap<>();
    filters.put("anon", new AnonymousFilter());
    filters.put("authc", new FormAuthenticationFilter());
    LogoutFilter logoutFilter = new LogoutFilter();
    logoutFilter.setRedirectUrl("/login?logout");
    filters.put("logout", logoutFilter);
    filters.put("roles", new RolesAuthorizationFilter());
    filters.put("user", new UserFilter());
    shiroFilter.setFilters(filters);
    return (AbstractShiroFilter) shiroFilter.getObject();
  }

  class ShiroRealmImpl extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
      String loginName = (String)principals.fromRealm(getName()).iterator().next();
      SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      Set<String> roles = new HashSet<String>();
      roles.add("admin");
      info.setRoles(roles);
      return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
      UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
      return new SimpleAuthenticationInfo("daniel","123456",getName());
    }
  }



  class JpaAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    public JpaAuthorizingRealm(CredentialsMatcher credentialsMatcher) {
      this.setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
      final String username = (String) principals.getPrimaryPrincipal();

      final User user = userRepository.findByLogin(username);

      final Set<String> roles = new HashSet<>(user.getRoles().size());
      for (final Role role : user.getRoles()) {
        roles.add(role.getRoleName());
      }

      final Set<org.apache.shiro.authz.Permission> permissions = new HashSet<>(user.getPermissions().size());
      for (final Permission permission : user.getPermissions()) {
        permissions.add(new WildcardPermission(permission.getPermission()));
      }

      final SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);
      authorizationInfo.setObjectPermissions(permissions);

      return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
      if (!(token instanceof UsernamePasswordToken)) {
        throw new IllegalStateException("Token has to be instance of UsernamePasswordToken class");
      }

      final UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;

      if (userPassToken.getUsername() == null) {
        throw new AccountException("Null usernames are not allowed by this realm.");
      }

      final User user = userRepository.findByLogin(userPassToken.getUsername());

      final SimpleAccount simpleAccount = new SimpleAccount(user.getLogin(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()),
          REALM_NAME);

      return simpleAccount;
    }

//  @Override
//  public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//    super.setCredentialsMatcher(credentialsMatcher);
//  }

  }


  @Bean
  public CredentialsMatcher credentialMatcher() {
    final HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
    credentialsMatcher.setHashIterations(HASH_ITERATIONS);
    return credentialsMatcher;
  }

  public static final String REALM_NAME = "MY_REALM";
  public static final int HASH_ITERATIONS = 200;


  public static final int SALT_LENGTH = 80;
  public static final int PASSWORD_LENGTH = 64;

  public static String getSalt() {
    return new SecureRandomNumberGenerator().nextBytes(60).toBase64();
  }

  public static String hashPassword(final String value, final String salt) {
    final Sha256Hash sha256Hash = new Sha256Hash(value, salt, HASH_ITERATIONS);
    return sha256Hash.toHex();
  }


}