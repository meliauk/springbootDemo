package com.life.hz.enums;

public enum NOtificationTypeEnum {

    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论"),
    ;

    private int type;
    private String name;

    public int getType() {
        return type;
    }


    public String getName() {
        return name;
    }


    NOtificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(int type){
        for (NOtificationTypeEnum notificationTypeEnum : NOtificationTypeEnum.values()) {
            if(notificationTypeEnum.getType() == type ){
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
