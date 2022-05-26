package hochenchong.timer.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SensitiveWordUtilsTest {
    @BeforeAll
    public static void setUp() {
        // 初始化屏蔽字内容
        SensitiveWordUtils.init();
    }

    @Test
    public void testHasSensitiveWord() {
        Assertions.assertTrue(SensitiveWordUtils.hasSensitiveWord("你是三体人吗"));
        Assertions.assertFalse(SensitiveWordUtils.hasSensitiveWord("我是中国人"));
    }

    @Test
    public void testSearchSensitiveWordAndReplace() {
        Assertions.assertEquals("你是**人吗", SensitiveWordUtils.searchSensitiveWordAndReplace("你是三体人吗"));
        Assertions.assertEquals("我是中国人", SensitiveWordUtils.searchSensitiveWordAndReplace("我是中国人"));
    }

    @Test
    public void testSearchSensitiveWordAndReplaceChar() {
        Assertions.assertEquals("你是##人吗", SensitiveWordUtils.searchSensitiveWordAndReplace("你是三体人吗", '#'));
        Assertions.assertEquals("我是中国人", SensitiveWordUtils.searchSensitiveWordAndReplace("我是中国人", '#'));
    }
}