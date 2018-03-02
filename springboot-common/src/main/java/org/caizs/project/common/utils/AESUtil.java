package org.caizs.project.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public final class AESUtil {

  private final static Logger logger = LoggerFactory.getLogger(AESUtil.class);


  /**
   * 使用AES算法加密
   *
   * @param content 待加密内容
   * @param key 加密密匙
   * @return 加密后的字符串
   */
  public static String encrypt(String content, String key) {

    String encryptStr = null;

    try {
      Cipher aesECB = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
      aesECB.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] result = aesECB.doFinal(content.getBytes());
      encryptStr = Base64.encodeBase64String(result);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return encryptStr;
  }


  /**
   * AES解密
   *
   * @param content 待解密内容
   * @param key 解密密钥
   * @return 解密后的字符串
   */
  public static String decrypt(String content, String key) {

    String decryptStr = null;

    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      byte[] result = Base64.decodeBase64(content);
      decryptStr = new String(cipher.doFinal(result));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return decryptStr;
  }


  /**
   * 生成AES Key
   *
   * @return 返回生成的key
   */
  public static String generateKey() {

    String genKey = null;
    try {
      KeyGenerator keygen = KeyGenerator.getInstance("AES");
      keygen.init(128);
      Key key = keygen.generateKey();
      byte[] keyBytes = key.getEncoded();
      genKey = Base64.encodeBase64String(keyBytes);
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage(), e);
    }

    return genKey;
  }
}
