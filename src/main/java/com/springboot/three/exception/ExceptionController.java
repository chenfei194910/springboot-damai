package com.springboot.three.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.three.constant.ResponseCode;
import com.springboot.three.domain.ReturnData;

@ControllerAdvice
@ResponseBody
public class ExceptionController {
    private String NullPointerExceptionStr="空指针异常";
    private String ArrayIndexOutOfBoundsStr="数组越界异常";
    private String ClassCastExceptionStr="类型转换异常";
    private int ERROR_CODE = 400;

    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler: Code: %s Detail: %s";

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ReturnData nullPointerExceptionHandler(NullPointerException ex) {
        return resultFormat(ERROR_CODE, new Exception(NullPointerExceptionStr));
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public ReturnData classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(ERROR_CODE,  new Exception(ClassCastExceptionStr));
    }


    //数组越界异常
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ReturnData ArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        return resultFormat(ERROR_CODE, new Exception(ArrayIndexOutOfBoundsStr));
    }

    //其他错误
    @ExceptionHandler({Exception.class})
    public ReturnData exception(Exception ex) {
        return resultFormat(ERROR_CODE, new Exception(ResponseCode.OPERATION_ERROR.getMsg()));
    }

    private <T extends Throwable> ReturnData resultFormat(Integer code, T ex) {
        ex.printStackTrace();
        return ReturnData.build(code, ex.getMessage());
    }

}
