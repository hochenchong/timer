package hochenchong.timer.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class AESUtils {
    /**
     * 默认算法
     */
    private static final String DEFAULT_ALGORITHM = "AES";
    /**
     * 默认的 算法/工作模式/填充模式
     */
    private static final String DEFAULT_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    /**
     * 默认的密钥大小，128 位密钥 = 16 bytes Key
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 生成 AES 密钥
     *
     * @return 返回生成的密钥
     * @throws NoSuchAlgorithmException
     */
    public static byte[] genAesSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(DEFAULT_ALGORITHM);
        keyGenerator.init(DEFAULT_KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 加密
     *
     * @param key 密钥
     * @param sourceInput 原文
     * @return 密文
     * @throws GeneralSecurityException
     */
    public static byte[] encrypt(byte[] key, byte[] sourceInput) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, DEFAULT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(sourceInput);
    }

    /**
     * 解密
     *
     * @param key 密钥
     * @param cipherInput 密文
     * @return 原文
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] key, byte[] cipherInput) throws GeneralSecurityException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, DEFAULT_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(cipherInput);
    }
}
