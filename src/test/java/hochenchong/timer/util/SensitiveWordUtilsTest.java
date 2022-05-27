package hochenchong.timer.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 屏蔽字测试类
 */
public class SensitiveWordUtilsTest {
    @BeforeAll
    public static void setUp() {
        // 初始化屏蔽字内容
        SensitiveWordUtils.init();
    }

    @Test
    @DisplayName("测试是否包含屏蔽字")
    public void testHasSensitiveWord() {
        Assertions.assertTrue(SensitiveWordUtils.hasSensitiveWord("你是三体人吗"));
        Assertions.assertFalse(SensitiveWordUtils.hasSensitiveWord("我是中国人"));
    }

    @Test
    @DisplayName("测试替换屏蔽字")
    public void testSearchSensitiveWordAndReplace() {
        Assertions.assertEquals("你是**人吗", SensitiveWordUtils.searchSensitiveWordAndReplace("你是三体人吗"));
        Assertions.assertEquals("我是中国人", SensitiveWordUtils.searchSensitiveWordAndReplace("我是中国人"));
    }

    @Test
    @DisplayName("测试自定义替换屏蔽字")
    public void testSearchSensitiveWordAndReplaceChar() {
        Assertions.assertEquals("你是##人吗", SensitiveWordUtils.searchSensitiveWordAndReplace("你是三体人吗", '#'));
        Assertions.assertEquals("我是中国人", SensitiveWordUtils.searchSensitiveWordAndReplace("我是中国人", '#'));
    }

    @Test
    @DisplayName("测试打印所有符合的屏蔽字")
    public void testSearchAllSensitiveWords() {
        List<String> list = Arrays.asList("三体", "三体人");
        List<String> allSensitiveWords = SensitiveWordUtils.searchAllSensitiveWords("你是三体人吗？人三体人");
        Assertions.assertEquals(list.size(), allSensitiveWords.size());
        allSensitiveWords.forEach(s -> Assertions.assertTrue(list.contains(s)));
    }
}