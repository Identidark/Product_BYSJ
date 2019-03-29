package common.wordUtil;

/**
 * @program: BYSJ
 * @description: 表名/列名处理工具类
 * @author: CN_ssh
 * @create: 2019-02-15 15:32
 **/

public class NameHandler {

    /**
     * 去掉表名前缀
     * @param tableName 表名
     * @return
     */
    public static String getTableName2(String tableName){

        tableName=tableName.toLowerCase();//全部转换为小写
        int index = tableName.indexOf("_");//下划线的位置
        if(index==-1){
            return tableName;
        }
        String name= tableName.substring(index+1);//从下划线开始截取

        return getColumnName2(name);//去掉下划线
    }

    /**
     * 去掉表名前缀 + 驼峰处理
     * @param name 表名
     * @return
     */
    public static String getColumnName2(String name){

        while(true){

            int i=name.indexOf("_");//取下划线
            System.out.println("i="+i);
            if(i==-1){
                break;//跳出
            }
            String n= name.substring(i+1,i+2).toUpperCase();//取出需要转换的字母进行转换

            name = name.substring(0,i) + n + name.substring(i+2 ) ;

        }
        return name;
    }


    /**
     * 首字母大写
     * @param str 表名/列名
     * @return
     */
    public static String getClassName(String str){
        String s0=str.substring(0, 1).toUpperCase();
        return s0+str.substring(1);
    }


}
