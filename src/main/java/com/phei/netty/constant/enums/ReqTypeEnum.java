package com.phei.netty.constant.enums;

import com.phei.netty.constant.CommonConstants;
import org.joda.time.DateTime;

import java.util.Arrays;

/**
 * @author wxweven
 * @date 2017/6/24
 */
public enum ReqTypeEnum {
    TIME("time"),
    DEFAULT("Bad Request");

    private String msg;

    ReqTypeEnum(String msg) {
        this.msg = msg;
    }

    public static ReqTypeEnum getByMsg(String msg) {
        return Arrays.stream(ReqTypeEnum.values())
                     .filter(item -> item.getMsg().equalsIgnoreCase(msg))
                     .findFirst()
                     .orElse(DEFAULT);
    }

    public static String getResultByReq(String req) {
        ReqTypeEnum reqTypeEnum = ReqTypeEnum.getByMsg(req);

        switch (reqTypeEnum) {
            case TIME:
                return DateTime.now().toString(CommonConstants.DEFAULT_DATE_FORMAT);
            default:
                return reqTypeEnum.getMsg();
        }
    }

    public String getMsg() {
        return msg;
    }
}
