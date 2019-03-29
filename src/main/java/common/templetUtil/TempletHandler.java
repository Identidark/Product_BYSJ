package common.templetUtil;

import common.file.FileUtil;
import common.wordUtil.NameHandler;
import entity.Column;
import entity.Table;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: BYSJ
 * @description: 模板处理工具类
 * @author: CN_ssh
 * @create: 2019-03-10 14:16
 **/

public class TempletHandler {

    /**
     * 根据目录查找所有子模板
     * @param basePath 各级模板所在目录
     * @return Map:{模板名:模板内容,模板名:模板内容,...}
     */
    public static Map<String ,String > getTempletList(String basePath)
    {
        Map<String ,String> map=new HashMap();

        //递归显示C盘下所有文件夹及其中文件			该目录下
        File root = new File(basePath);
        try {
            map=showAllFiles(basePath,root);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    static Map<String ,String > showAllFiles( String basePath, File dir) throws Exception
    {

        Map<String ,String> map=new HashMap();

        File[] fs = dir.listFiles();
        for(int i=0; i<fs.length; i++){
            File file=new File(fs[i].getAbsolutePath());
            //将读取的子模板的内容放在map集合中

            if(fs[i].isDirectory()){
                try{
                    map.putAll(  showAllFiles(basePath,fs[i])  );		//深入遍历，追加内容
                }catch(Exception e)
                {

                }
            }else
            {
                map.put(file.getName(), FileUtil.getContent(fs[i].getAbsolutePath()) )  ;
            }
        }
        return map;
    }

    /**
     * 替换表级模板
     * @param oldContent 原文本
     * @param map 子替换符号列表
     * @param tables 该数据库表集合
     * @return
     */
    public static String createContentForTable(String oldContent, Map<String,String> map , List<Table> tables )
    {
        //循环所有子替换符
        for(String ks :map.keySet())
        {
            String thf="<"+ks+">";//替换符号
            if(oldContent.indexOf(thf)>=0)
            {
                String foreachContent=map.get(ks);//循环体
                StringBuilder createContent= new StringBuilder();
                for( Table table : tables )
                {
                    boolean b=true;//控制开关
                    //根据模板生成新内容
                    if(b)
                    {
                        String newContent= foreachContent.replace("[table]", table.getName());
                        newContent=newContent.replace("[Table]", NameHandler.getClassName(table.getName()) );

                        newContent=newContent.replace("[table2]", table.getName2() );
                        newContent=newContent.replace("[Table2]", NameHandler.getClassName(table.getName2()));

                        if(table.getKey()!=null){
                            oldContent= oldContent.replace("[key]", table.getKey());//备注
                        }
                        if(table.getKey2()!=null){
                            oldContent= oldContent.replace("[key2]", table.getKey2());//备注
                        }
                        if(table.getKeyType()!=null){
                            oldContent= oldContent.replace("[keyType]", table.getKeyType());//备注
                        }
                        if(table.getKey2Upper()!=null){
                            oldContent= oldContent.replace("[Key2]", table.getKey2Upper());//大写主键
                        }
                        if(table.getComment()==null || table.getComment().equals("")){
                            table.setComment(table.getName2());
                        }
                        newContent= newContent.replace("[comment]", table.getComment());//备注
                        createContent.append(newContent);
                    }
                }
                oldContent=oldContent.replace(thf, createContent.toString());//替换主体内容
            }
        }


        return oldContent;
    }


    /**
     * 替换列级模板
     * @param oldContent 原文本
     * @param map 子替换符号列表		{模板名:模板内容,....}
     * @param table 当前表		{name:...,columns:[列1,列2,...],...}
     * @return
     */
    public static String createContent(String oldContent,  Map<String,String> map ,Table table)
    {
        //循环所有子替换符
        for(String ks :map.keySet() )
        {
            String thf="<"+ks+">";//替换符号			待替换处模板名标识

            if(oldContent.indexOf(thf)>=0)		//待处理文本中是否有待替换内容
            {
                String foreachContent=map.get(ks);//循环体		替换内容

                StringBuilder createContent= new StringBuilder();

                for( Column column : table.getColumns() )
                {
                    //TODO 控制什么？？？？？
                    boolean b=true;//控制开关
                    //过滤判断当前模板是列级模板哪一个，结合判断确定对应处理的列，主要用于组装实体类、业务逻辑语句
                    //只循环主键
                    if(ks.indexOf(".key")>=0)		//是含有.key的
                    {
                        if(!column.getColumnKey().equals("PRI"))		//当前列不是主键列
                        {
                            b=false;//不是主键
                        }
                    }
                    //只循环非主键
                    if(ks.indexOf(".nokey")>=0)
                    {
                        if(column.getColumnKey().equals("PRI"))
                        {
                            b=false;//不是主键
                        }
                    }

                    //只循环String 类型
                    if(ks.indexOf(".String")>=0)
                    {
                        if(!column.getColumnType().equals("String"))
                        {
                            b=false;//不是String
                        }
                    }

                    //只循环String 类型
                    if(ks.indexOf(".Integer")>=0)
                    {
                        if(!column.getColumnType().equals("Integer"))
                        {
                            b=false;//不是String
                        }
                    }

                    //只循环Long类型
                    if(ks.indexOf(".Long")>=0)
                    {
                        if(!column.getColumnType().equals("Long"))
                        {
                            b=false;//不是String
                        }
                    }

                    //只循环Date 类型
                    if(ks.indexOf(".Date")>=0)
                    {
                        if(!column.getColumnType().equals("java.util.Date"))
                        {
                            b=false;//不是String
                        }
                    }
                    //根据模板生成新内容
                    if(b)
                    {
                        System.out.println("替换符号内容："+foreachContent);
                        String newContent= foreachContent.replace("[column]", column.getColumnName());		//字段名称
                        newContent=newContent.replace("[Column]", NameHandler.getClassName(column.getColumnName()) );		//首字母大写

                        newContent=newContent.replace("[column2]", column.getColumnName2() );		//驼峰命名
                        newContent=newContent.replace("[Column2]", NameHandler.getClassName(column.getColumnName2()) );		//首字母大写

                        newContent=newContent.replace("[type]", column.getColumnType());//java类型
                        newContent=newContent.replace("[dbtype]", column.getColumnDbType());//数据库类型
                        if(column.getColumnComment()==null || column.getColumnComment().equals("")){
                            column.setColumnComment(column.getColumnName());//设置为名称
                        }
                        newContent= newContent.replace("[columnComment]", column.getColumnComment());//备注
                        createContent.append(newContent);
                        System.out.println("替换后内容："+newContent);
                    }
                }
                oldContent=oldContent.replace(thf, createContent.toString());//替换主体内容
            }
        }

        oldContent= oldContent.replace("[table]", table.getName());
        oldContent= oldContent.replace("[Table]", NameHandler.getClassName(table.getName()) );
        oldContent= oldContent.replace("[table2]", table.getName2());
        oldContent= oldContent.replace("[Table2]", NameHandler.getClassName(table.getName2()) );

        if(table.getComment()==null || table.getComment().equals("")){
            table.setComment(table.getName2());
        }
        oldContent= oldContent.replace("[comment]", table.getComment());//备注


        if(table.getKey()!=null){
            oldContent= oldContent.replace("[key]", table.getKey());// 主键
        }
        if(table.getKey2()!=null){
            oldContent= oldContent.replace("[key2]", table.getKey2());//主键驼峰
        }
        if(table.getKeyType()!=null){
            oldContent= oldContent.replace("[keyType]", table.getKeyType());//主键类型
        }
        if(table.getKey2Upper()!=null){
            oldContent= oldContent.replace("[Key2]", table.getKey2Upper());//大写主键
        }
        return oldContent;
    }


    /**
     * 替换全局替换符
     * @param oldContent 待处理内容(替换前内容)
     * @param map 界面输入信息 + 目录结构路径
     * @return
     */
    public static String createContent(String oldContent,Map<String,String> map)
    {
        //循环所有子替换符
        for(String ks :map.keySet())
        {
            oldContent= oldContent.replace("["+  ks + "]", map.get(ks));
        }

        return oldContent;
    }


}
