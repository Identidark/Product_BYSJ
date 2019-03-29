package common.templetUtil;

import entity.Templet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: BYSJ
 * @description: 获取模板对象集合工具类
 * @author: CN_ssh
 * @create: 2019-03-04 14:29
 **/

public class TempletGetUtil {

    /**
     * 根据目录查找所有模板文件
     *
     * @param basePath 工程框架模板所在目录
     * @return 该工程模板所有模板对象集合
     */
    public static List<Templet> getTempletList(String basePath) {
        List<Templet> list = null;

        //递归显示目标路径下所有文件夹及其中文件
        File root = new File(basePath);
        try {
            list = showAllFiles(basePath, root);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    //获取所有模板文件的Templet对象
    final static List<Templet> showAllFiles(String basePath, File dir) throws Exception {
        List<Templet> list = new ArrayList();

        // listFiles()方法是返回某个目录下所有文件和目录的绝对路径，返回的是File数组
        File[] fs = dir.listFiles();
        for (int i = 0; i < fs.length; i++) {

            // TODO 优化：先判断再组装Templet对象
            Templet templet = new Templet();
            templet.setAllPath(fs[i].getAbsolutePath());//原目录

            File file = new File(fs[i].getAbsolutePath());

            templet.setPath(file.getParent().replace(basePath, "")); //相对目录
            templet.setFileName(file.getName());//文件名

            //若是文件夹则进入该文件夹继续递归查找所有文件
            if (fs[i].isDirectory()) {
                try {
                    list.addAll(showAllFiles(basePath, fs[i]));
                } catch (Exception e) {

                }
            } else {
                list.add(templet);
            }
        }
        return list;
    }

}
