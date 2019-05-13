package com.jinaup.upcustoms.exception;

import lombok.*;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/7
 */
@AllArgsConstructor
@Getter
public enum CustomInfoStatus {
      customsuccessstatus("10000","成功"),
      customfailstatus("20000","失败");
      //customexceptionstatus("20001","其他异常");

      private final String code;
      private final String msg;

   /*  private CustomInfoStatus(String code, String msg) {
            this.code = code;
            this.msg = msg;
      }*/

     /* public String getCode() {
            return code;
      }

      public String getMsg() {
            return msg;
      }*/
}
