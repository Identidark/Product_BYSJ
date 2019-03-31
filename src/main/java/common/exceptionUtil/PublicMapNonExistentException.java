package common.exceptionUtil;

/**
 * @program: BYSJ
 * @description: 二阶段初始化文件publicMap.xml不存在异常
 * @author: CN_ssh
 * @create: 2019-03-30 14:58
 **/

public class PublicMapNonExistentException extends RuntimeException{

    public PublicMapNonExistentException(String message) {
        super(message);
    }
}
