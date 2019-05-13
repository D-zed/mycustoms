package com.jinaup.upcustoms.pojo;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/9
 */
public class PayChannel {

    public static Map<Integer,String> CustomsCode= Maps.newHashMap();
    static {
        //1 微信  海关编码
        CustomsCode.put(1, "4403169D3W");
        //2 支付宝 海关编码    核验机构是2 网联
        CustomsCode.put(2, "31222699S7");

    }

}
