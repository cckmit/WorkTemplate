package org.blaze.file.system.utils;

/**
 * 自定义异常信息
 *
 * @author xuwei
 */
public class ToolsException extends Exception {

    public ToolsException(int code, String format) {
        super(String.valueOf(code), new Throwable(format));
    }

    /**
     * 异常常规配置
     */
    public interface ToolsExceptionConstant {
        int NOTEXSITERROR_CODE = 100;
        /**
         * 异常提示
         */
        String NOTEXSITERROR_MSG = "压缩文件不存在，路径%s";
        int EXSITERROR_CODE = 101;
        String EXSITERROR_MSG = "打包文件已存在，文件夹路径%s,文件名称%s";
        int EMPTYERROR_CODE = 102;
        String EMPTYERROR_MSG = "无需文件压缩,路径:%s";
    }
}
