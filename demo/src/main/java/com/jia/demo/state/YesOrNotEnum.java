package com.jia.demo.state;

/**
 * @author x
 * @date 2020/8/20 15:16
 */
public enum YesOrNotEnum {
    Y(true, "Y", 1),
    N(false, "N", 0);

    private Boolean flag;
    private String desc;
    private Integer code;

    private YesOrNotEnum(Boolean flag, String desc, Integer code) {
        this.flag = flag;
        this.desc = desc;
        this.code = code;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            YesOrNotEnum[] arr = values();
            int len = arr.length;

            for(int i = 0; i < len; i++) {
                YesOrNotEnum s = arr[i];
                if (s.getCode().equals(status)) {
                    return s.getDesc();
                }
            }

            return "";
        }
    }

    public Boolean getFlag() {
        return this.flag;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getCode() {
        return this.code;
    }
}
