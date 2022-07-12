package hochenchong.timer.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;

public class AESUtilsTest {

    @Test
    @DisplayName("测试生成 AES 密钥")
    public void testGenAesSecretKey() throws Exception {
        // 128位密钥 = 16 bytes key
        byte[] bytes = AESUtils.genAesSecretKey();
        Assertions.assertEquals(16, bytes.length);
    }

    @Test
    @DisplayName("测试 AES 加密与解密")
    public void testAesEncryptAndDecrypt() throws Exception {
        byte[] key = AESUtils.genAesSecretKey();
        String str = "测试 AES 加密与解密";
        byte[] cipherText = AESUtils.encrypt(key, str.getBytes(StandardCharsets.UTF_8));
        byte[] decrypt = AESUtils.decrypt(key, cipherText);
        Assertions.assertEquals(str, new String(decrypt, StandardCharsets.UTF_8));
    }
}