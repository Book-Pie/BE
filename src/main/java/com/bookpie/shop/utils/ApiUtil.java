package com.bookpie.shop.utils;

import org.springframework.http.HttpStatus;


public class ApiUtil {

    //Api 응답 성공시 data를 ApiResult 클래스로 감싸서 리턴
    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>(true,data,null);
    }

    //예외 상황 발상 시 에러메세지를 ApiResult 클래스로 감싸서 리턴
    public static <T> ApiResult<T> error(String message,HttpStatus status){
        return new ApiResult<>(false,null,new ApiError(message,status));
    }

    public static <T> ApiResult<T> error(Throwable throwable,HttpStatus status){
        return new ApiResult<>(false,null,new ApiError(throwable,status));
    }


    /*
        API 응답 결과를 감싸는 제네릭 클래스
     */
    public static class ApiResult<T>{
        private final boolean success;
        private final T data;
        private final ApiError error;

        private ApiResult(boolean success,T data,ApiError error){
            this.success = success;
            this.data = data;
            this.error = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public T getData() {
            return data;
        }

        public ApiError getError() {
            return error;
        }

        @Override
        public String toString() {
            return "ApiResult{" +
                    "success=" + success +
                    ", data=" + data +
                    ", error=" + error +
                    '}';
        }
    }


    /*
        예외 발생시 ApiResult 에 들어가는 ApiError 클래스
     */
    public static class ApiError{
        private String message;
        private final int status;

        private ApiError(String message,HttpStatus status){
            this.message = message;
            this.status = status.value();
        }

        private ApiError(Throwable throwable,HttpStatus status){
            this(throwable.getMessage(),status);
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "ApiError{" +
                    "message='" + message + '\'' +
                    ", status=" + status +
                    '}';
        }
    }
}
