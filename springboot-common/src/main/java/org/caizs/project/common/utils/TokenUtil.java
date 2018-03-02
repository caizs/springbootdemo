package org.caizs.project.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Created by bjf on 2017/8/24.
 */
public class TokenUtil {

  private static final String TOKEN_KEY = "i3tmk8bzx89lxx=="; // 必须16位

  /**
   * 生成access_token
   */
  public static String genToken(long userId, String ip) {

    StringBuilder sb = new StringBuilder();
    sb.append(userId).append("|");
    sb.append(ip).append("|");
    sb.append(System.currentTimeMillis());

    //AES加密
    String encrypt = AESUtil.encrypt(sb.toString(), TOKEN_KEY);

    return encrypt;
  }

  /**
   * 验证token，对应genToken
   */
  public static Boolean verifyToken(String token) {

    //AES解密
    String decrypt = AESUtil.decrypt(token, TOKEN_KEY);

    return decrypt == null ? Boolean.FALSE : Boolean.TRUE;
  }


  /**
   * 生成jwt token
   *
   * @param timeout 过期时间（秒 ）
   * @desc 1.为了保证安全，因为token的有效期不要太长，一般1到2小时为宜。 2.在生成token的同时应生成一个有效期较长的refresh_Token，后续由客户端定时根据refreshToken来获取最新的token
   */
  public static String genJwtToken(long userId, String loginName, long timeout) {
    Date now = new Date();

    return Jwts.builder()
               // 设置用户
               .setSubject(loginName)
               // 设置签发者
               .setIssuer("live")
               // 设置签发时间
               .setIssuedAt(now)
               // 设置自定义参数
               .claim("userId", userId)
               // 设置算法和加密密钥
               .signWith(SignatureAlgorithm.HS256, TOKEN_KEY)
               // 设置过期时间
               .setExpiration(new Date(now.getTime() + timeout * 1000))
               .compact();
  }

  /**
   * 解密jwt token
   */
  public static Claims parseJwtToken(String token) {
    Claims claims = null;
    try {
      claims = Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return claims;
  }

}
