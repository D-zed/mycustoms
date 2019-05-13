package com.jinaup.upcustoms.exceptionEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum  ExceptionEnum {
    CATEGORY_NOT_FOUND(404,"商品分类没有找到"),
    BRAND_SAVE_ERROR(404,"品牌添加异常"),
    Image_File_TYPE_EXCEPTION(404,"品牌添加异常"),
    STOCK_NOT_FOUND(404,"库存未找到异常"),
    Goods_IS_NOT_FOUND(404,"商品未找到"),
    SPEC_PARAM_NOT_FOUND(404,"参数列表未找到"),
    SAVE_GOODS_EXCEPTION(404,"保存商品异常"),
    GOODS_SKU_NOT_FOUND(404,"商品没找到"),
    SPU_DETAIL_NOT_FOUND(404,"商品详情没找到"),
    BRAND_NOT_FOUND(404,"品牌未找到"),
    PASSWORD_IS_ERROR(404,"密码错误"),
    USER_DONT_EXSIST(404,"用户不存在");



    private int code;
    private String msg;

    ExceptionEnum(int i, String 用户不存在) {

    }

    public void getcode(){
        System.out.println(this.code+this.msg);
    }

}
