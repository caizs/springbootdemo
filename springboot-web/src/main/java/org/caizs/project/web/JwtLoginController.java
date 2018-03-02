package org.caizs.project.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.caizs.project.common.utils.TokenUtil;
import org.caizs.project.domain.User;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * jwt 登录demo示例
 */
@RestController
public class JwtLoginController {


  /**
   * 登录
   */
  @GetMapping("jwt/login")
  public LoginResp login(String phone, String password) {

    // 模拟登录成功取到用户信息
    User user = new User();
    user.setId(555);
    user.setName("zhangsan");

    return createLoignResp(user.getId(), user.getName());
  }

  /**
   * 刷新access_token
   */
  @GetMapping("app/refresh-token")
  public LoginResp refreshToken(@RequestParam("refresh_token") String refreshToken) {
    Claims claims = TokenUtil.parseJwtToken(refreshToken);
    Long userId = (Long) claims.get("userId");
    String loginName = claims.getSubject();

    return createLoignResp(userId, loginName);
  }

  /**
   * 创建登录成功信息
   */
  private LoginResp createLoignResp(long userId, String loginName) {
    // 1小时
    String accessToken = TokenUtil.genJwtToken(userId, loginName, 3600);
    // 15天
    String newRefreshToken = TokenUtil.genJwtToken(userId, loginName, 86400 * 15);

    LoginResp loginResp = new LoginResp();
    loginResp.setAccessToken(accessToken);
    loginResp.setRefreshToken(newRefreshToken);
    loginResp.setExpiresIn(3600);
    return loginResp;
  }

  @Data
  public static class LoginResp {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private long expiresIn;

  }


}
