package hochenchong.timer.util;

import hochenchong.timer.constant.SensitiveWordMatchType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 屏蔽字工具类
 *
 * @author hochenchong
 * @since 2022-05-27
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SensitiveWordUtils {
    /**
     * 屏蔽字 map
     */
    private static HashMap sensitiveWordMap = new HashMap<>();
    /**
     * 默认的屏蔽字替换字符
     */
    private static final char REPLACE_CHAR = '*';
    /**
     * 屏蔽字匹配标识
     */
    private static final String END_FLAG = "IsEnd";
    /**
     * 默认的屏蔽字文件
     */
    private static final String SENSITIVE_WORD_FILE_PATH = "static/sensitiveWord.txt";
    /**
     * 屏蔽字文件的最后修改日期，以此判断是否需要重新加载（类似于 Unix make 逻辑，《UNIX传奇：历史与回忆》）
     */
    private static long lastModifiedTime = -1;
    /**
     * 默认的匹配规则，匹配较小的
     */
    private static final SensitiveWordMatchType DEFAULT_MATCH_TYPE = SensitiveWordMatchType.MATCH_SHORTER;

    /**
     * 判断校验的文本是否需要屏蔽的内容
     *
     * @param text 要校验的文本
     * @return true：有，false：没有
     */
    public static boolean hasSensitiveWord(String text) {
        char[] chars = text.toLowerCase().toCharArray();
        int i = 0;
        while (i < chars.length) {
            int len = checkSensitiveWord(chars, i);
            if (len > 0) {
                return true;
            }
            ++i;
        }
        return false;
    }

    /**
     * 查找校验的文本是否包含屏蔽字，并将屏蔽字替换为 *
     *
     * @param text 要校验的文本
     * @return 返回处理过屏蔽字的文本
     */
    public static String searchSensitiveWordAndReplace(String text) {
        return searchSensitiveWordAndReplace(text, REPLACE_CHAR, DEFAULT_MATCH_TYPE);
    }

    /**
     * 查找校验的文本是否包含屏蔽字，并将屏蔽字替换为对应的字符
     *
     * @param text 要校验的文本
     * @param replaceChar 替换的字符
     * @return 返回处理过屏蔽字的文本
     */
    public static String searchSensitiveWordAndReplace(String text, char replaceChar) {
        return searchSensitiveWordAndReplace(text, replaceChar, DEFAULT_MATCH_TYPE);
    }

    /**
     * 查找校验的文本是否包含屏蔽字，并将屏蔽字替换为 *
     *
     * @param text 要校验的文本
     * @param matchType 匹配类型
     * @return 返回处理过屏蔽字的文本
     */
    public static String searchSensitiveWordAndReplace(String text, SensitiveWordMatchType matchType) {
        return searchSensitiveWordAndReplace(text, REPLACE_CHAR, matchType);
    }

    /**
     * 查找校验的文本是否包含屏蔽字，并将屏蔽字替换为对应的字符
     *
     * @param text 要校验的文本
     * @param replaceChar 替换的字符
     * @param matchType 匹配类型
     * @return 返回处理过屏蔽字的文本
     */
    public static String searchSensitiveWordAndReplace(String text, char replaceChar, SensitiveWordMatchType matchType) {
        int i = 0;

        StringBuilder sb = new StringBuilder(text);
        char[] chars = text.toLowerCase().toCharArray();
        while (i < chars.length) {
            int len = checkSensitiveWord(chars, i, matchType);
            if (len > 0) {
                for (int j = 0; j < len; j++) {
                    sb.setCharAt(i+j, replaceChar);
                }
                i += len;
            } else {
                ++i;
            }
        }
        return sb.toString();
    }

    /**
     * 找出需要校验文本，匹配到的所有屏蔽字
     *
     * @param text 要校验的文本
     * @return 屏蔽字列表
     */
    public static List<String> searchAllSensitiveWords(String text) {
        List<String> allSensitiveWords = new ArrayList<>();
        char[] chars = text.toLowerCase().toCharArray();
        int i = 0;
        while (i < chars.length) {
            List<Integer> lens = checkAllSensitiveWord(chars, i);
            i++;
            // 如果没有匹配到到长度，则直接返回
            if (lens == null) {
                continue;
            }
            // 拼接屏蔽字列表，规律，后面到字符串包含前面的字符
            StringBuilder sb = new StringBuilder();
            // 保险起见，排个序，从小到大
            lens = lens.stream().sorted().toList();
            for (Integer len : lens) {
                for (int k = sb.length(); k < len; k++) {
                    // 这里需要 -1，前面 i++ 了
                    sb.append(chars[i + k - 1]);
                }
                allSensitiveWords.add(sb.toString());
            }
        }
        // 可能会有重复，需要去重
        return allSensitiveWords.stream().distinct().toList();
    }

    /**
     * 初始化屏蔽字内容
     */
    public synchronized static void init() {
        // 根据屏蔽字文件的修改时间来判断是否需要重新加载，如果修改时间不同，则重新加载
        ClassPathResource classPathResource = new ClassPathResource(SENSITIVE_WORD_FILE_PATH);
        if (!needReloadSensitiveWord(classPathResource)) {
            return;
        }

        Set<String> wordsTemp = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 将屏蔽字内容都转换为小写
                String word = line.trim().toLowerCase();
                if (StringUtils.hasText(word))
                    wordsTemp.add(line.trim().toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initSensitiveWordMap(wordsTemp);
    }

    /**
     * 判断是否需要重新加载屏蔽字文件
     *     根据文件的最后修改时间来判断
     *
     * @param classPathResource 屏蔽字资源
     * @return 是否需要重新加载屏蔽字内容
     */
    private static boolean needReloadSensitiveWord(ClassPathResource classPathResource) {
        try {
            File file = classPathResource.getFile();
            if (!file.exists()) {
                return Boolean.FALSE;
            }

            if (lastModifiedTime != file.lastModified()) {
                lastModifiedTime = file.lastModified();
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    /**
     * 根据屏蔽字集合，生成屏蔽字 Map，使用 DFA 算法来匹配
     *
     * @param collection 屏蔽字集合（以处理前后空格，以及转换为小写）
     */
    private static synchronized void initSensitiveWordMap(Collection<String> collection) {
        // 避免扩容带来的消耗
        HashMap newInnerWordMap = new HashMap<>(collection.size());

        for (String key : collection) {
            // 用来按照相应的格式保存敏感词库数据
            char[] chars = key.toCharArray();
            final int size = chars.length;

            // 每一个新词的循环，直接将结果设置为当前 map，所有变化都会体现在结果的 map 中
            HashMap currentMap = newInnerWordMap;
            for (int i = 0; i < size; i++) {
                // 截取敏感词当中的字，在敏感词库中字为 HashMap 对象的 Key 键值
                char charKey = chars[i];
                // 如果集合存在
                Object wordMap = currentMap.get(charKey);
                // 如果集合存在
                if (!ObjectUtils.isEmpty(wordMap)) {
                    // 直接将获取到的 map 当前当前 map 进行继续的操作
                    currentMap = (HashMap) wordMap;
                } else {
                    // 不存在则构建一个新的 map，同时将 isEnd 设置为 false
                    HashMap<String, Boolean> newWordMap = new HashMap<>(8);
                    newWordMap.put(END_FLAG, false);
                    // 将新的节点放入当前 map 中
                    currentMap.put(charKey, newWordMap);
                    // 将新节点设置为当前节点，方便下一次节点的循环。
                    currentMap = newWordMap;
                }
                // 判断是否为最后一个，添加是否结束的标识。
                if (i == size - 1) {
                    currentMap.put(END_FLAG, true);
                }
            }
        }

        // 最后更新为新的 map，保证更新过程中旧的数据可用
        sensitiveWordMap = newInnerWordMap;
    }

    /**
     * 从文本的第几位下标开始校验是否有符合的屏蔽字
     *
     * @param chars 要校验的文本
     * @param beginIndex 下标，从 0 开始
     * @return 返回屏蔽字的长度，大于 0 代表有屏蔽字
     */
    private static int checkSensitiveWord(char[] chars, int beginIndex) {
        return checkSensitiveWord(chars, beginIndex, DEFAULT_MATCH_TYPE);
    }

    /**
     * 从文本的第几位下标开始校验是否有符合的屏蔽字
     *
     * @param chars 要校验的文本
     * @param beginIndex 下标，从 0 开始
     * @param matchType 匹配模式，返回较长屏蔽字，还是较短屏蔽字
     * @return 返回屏蔽字的长度，大于 0 代表有屏蔽字
     */
    private static int checkSensitiveWord(char[] chars, int beginIndex, SensitiveWordMatchType matchType) {
        int sensitiveWordLen = 0;

        HashMap curMap = sensitiveWordMap;
        int size = chars.length;
        int len = 0;
        for (int i = beginIndex; i < size; i++) {
            char c = chars[i];
            HashMap temp = (HashMap) curMap.get(c);
            // 如果为空，则跳出循环
            if (temp == null) {
                break;
            }
            len++;
            // 如果获取标识为 true，则代表匹配到屏蔽字
            if (Boolean.TRUE == temp.get(END_FLAG)) {
                // 如果只要匹配较短的屏蔽字，则直接跳出循环，否则继续循环
                if (matchType == SensitiveWordMatchType.MATCH_SHORTER) {
                    return len;
                }
                sensitiveWordLen = len;
            }
            curMap = temp;
        }
        return sensitiveWordLen;
    }

    /**
     * 从文本的第几位下标开始校验是否有符合的屏蔽字
     *
     * @param chars 要校验的文本
     * @param beginIndex 下标，从 0 开始
     * @return 返回屏蔽字的长度列表，所有匹配到的都返回
     */
    private static List<Integer> checkAllSensitiveWord(char[] chars, int beginIndex) {
        int len = 0;
        HashMap curMap = sensitiveWordMap;
        List<Integer> list = null;

        int size = chars.length;
        for (int i = beginIndex; i < size; i++) {
            char c = chars[i];
            HashMap temp = (HashMap) curMap.get(c);
            // 如果为空，则跳出循环
            if (temp == null) {
                break;
            }
            len++;
            // 如果获取标识为 true，则代表匹配到屏蔽字，长度加入列表
            if (Boolean.TRUE == temp.get(END_FLAG)) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(len);
            }
            curMap = temp;
        }
        return list;
    }
}
