package entity;

/**
 * @program: BYSJ
 * @description: 模板文件实体类
 * @author: CN_ssh
 * @create: 2019-01-29 14:19
 **/

public class Templet {

    private String path;			//生成路径
    private String fileName;		//模板文件名
    private String allPath;			//完成路径		完整路径

    public String getAllPath() {
        return allPath;
    }
    public void setAllPath(String allPath) {
        this.allPath = allPath;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
