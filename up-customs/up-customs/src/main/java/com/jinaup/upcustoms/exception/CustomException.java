package com.jinaup.upcustoms.exception;

import com.jinaup.upcustoms.exceptionEnum.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{

    private ExceptionEnum exceptionEnum;

}
