package hochenchong.timer.constant;

public enum SensitiveWordMatchType {
    /**
     * 较短匹配，匹配到最小的就返回
     */
    MATCH_SHORTER(1),
    /**
     * 较长匹配，匹配到最长的返回
     */
    MATCH_LONGER(2),
    ;

    private int type;
    SensitiveWordMatchType(int type) {
        this.type = type;
    }
}
