package hochenchong.timer.util;

import hochenchong.timer.constant.SensitiveWordMatchType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 屏蔽字测试类
 *
 * @author hochenchong
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
        Assertions.assertTrue(SensitiveWordUtils.hasSensitiveWord("你是在测试吗"));
        Assertions.assertFalse(SensitiveWordUtils.hasSensitiveWord("我没有在测东西"));
    }

    @Test
    @DisplayName("测试替换屏蔽字")
    public void testSearchSensitiveWordAndReplace() {
        Assertions.assertEquals("你是在写**用例吗", SensitiveWordUtils.searchSensitiveWordAndReplace("你是在写测试用例吗"));
        Assertions.assertEquals("我真的没有在写", SensitiveWordUtils.searchSensitiveWordAndReplace("我真的没有在写"));
    }

    @Test
    @DisplayName("测试自定义替换屏蔽字")
    public void testSearchSensitiveWordAndReplaceChar() {
        Assertions.assertEquals("你是在写##用例吗", SensitiveWordUtils.searchSensitiveWordAndReplace("你是在写测试用例吗", '#'));
        Assertions.assertEquals("我真的没有在写", SensitiveWordUtils.searchSensitiveWordAndReplace("我真的没有在写", '#'));
    }

    @Test
    @DisplayName("测试打印所有符合的屏蔽字")
    public void testSearchAllSensitiveWords() {
        List<String> list = Arrays.asList("测试", "测试用例");
        List<String> allSensitiveWords = SensitiveWordUtils.searchAllSensitiveWords("这是测试用的吗？测试用例吗");
        Assertions.assertEquals(list.size(), allSensitiveWords.size());
        allSensitiveWords.forEach(s -> Assertions.assertTrue(list.contains(s)));
    }

    @Test
    @DisplayName("测试替换屏蔽字-替换匹配到的较长屏蔽字")
    public void testSearchLongerSensitiveWordAndReplace() {
        Assertions.assertEquals("这是**用的吗？****吗", SensitiveWordUtils.searchSensitiveWordAndReplace("这是测试用的吗？测试用例吗", SensitiveWordMatchType.MATCH_LONGER));
        Assertions.assertEquals("我真的没有在写", SensitiveWordUtils.searchSensitiveWordAndReplace("我真的没有在写"));
    }
}