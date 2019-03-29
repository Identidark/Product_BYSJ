package phase2_CodeDealView.util;

import common.file.FileUtil;
import common.templetUtil.TempletGetUtil;
import common.templetUtil.TempletHandler;
import common.wordUtil.NameHandler;
import common.xmlUtil.DBinfo2Xml;
import entity.Table;
import entity.Templet;

import java.util.List;
import java.util.Map;

/**
 * @program: BYSJ
 * @description: 代码相关操作工具类
 * @author: CN_ssh
 * @create: 2019-02-28 16:49
 **/

public class CodeUtil {

    /**
     * 代码生成
     * @param pathMap 路径参数封装 {columnTempletPath:模板路径，...}
     * @param publicMap  全局替换符   界面输入信息 + 目录结构路径
     */
    public static void create(Map<String,String> pathMap, Map<String,String> publicMap)
    {
        //变量定义
        String projectTempletPath=pathMap.get("projectTempletPath");//框架模板所在目录
        String tablleTempletPath=pathMap.get("tablleTempletPath");//表级模板所在目录
        String columnTempletPath=pathMap.get("columnTempletPath");//列级模板所在目录
        String xmlPath=pathMap.get("xmlPath");//数据库信息文件
        String codePath=pathMap.get("codePath");//代码输出目录

        //追加到全局替换符******************
        Map<String, String> propertyMap = DBinfo2Xml.readProperty(xmlPath);				//读取数据库属性信息
        publicMap.putAll(propertyMap);		//追加另一个Map对象到当前Map集合对象，它会把另一个Map集合对象中的所有内容添加到当前Map集合对象。
        //******************************

        List<Table> tableList = DBinfo2Xml.readDatabaseXml(xmlPath);//得到该数据库中表的集合

        Map<String ,String> tableTempletMap= TempletHandler.getTempletList(tablleTempletPath);//表级模板MAP
        Map<String ,String> columnTempletMap= TempletHandler.getTempletList(columnTempletPath);//列级模板MAP
        List<Templet> list = TempletGetUtil.getTempletList(projectTempletPath);//获取此项目所有模板集合
        //循环所有模板
        for(Templet t:list  )
        {
            System.out.println("处理模板："+ t.getPath()+"   --- "+t.getFileName());

            //如果是模板类文件
            if( FileUtil.isTemplatFile( t.getAllPath()) ){

                //读取模板文件内容
                String content=FileUtil.getContent(t.getAllPath());

                //替换表级模板部分
                content= TempletHandler.createContentForTable(content, tableTempletMap, tableList);

                //如果文件名包含表替换符号则循环输出
                if(t.getFileName().indexOf("[table]")>=0  || t.getFileName().indexOf("[Table]")>=0  ||
                        t.getFileName().indexOf("[table2]")>=0  || t.getFileName().indexOf("[Table2]")>=0){
                    //针对于每一个表
                    for(Table table:tableList)
                    {
                        //输出的文件名
                        String outFile=t.getFileName().replace("[table]", table.getName());		//字段名称
                        outFile=outFile.replace("[Table]", NameHandler.getClassName(table.getName()) );		//字段名称首字母大写

                        outFile=outFile.replace("[Table2]", NameHandler.getClassName(table.getName2()) );		//驼峰格式首字母大写
                        outFile=outFile.replace("[table2]", table.getName2() );		//驼峰格式

                        //得到模板内容
                        String outContent=content;

                        //替换列级模板部分
                        outContent= TempletHandler.createContent(outContent, columnTempletMap, table);

                        //全局替换符嵌套替换符处理********

                        System.out.println("全局替换符嵌套替换符处理********"+table.getName());
						/*for(String k:publicMap.keySet() ){
							String value = publicMap.get(k).replace("[table]", table.getName());
							publicMap.put(k,value );
							System.out.println("key:"+k+"  value:"+value);
						}		*/

                        //全局替换
                        outContent= TempletHandler.createContent(outContent, publicMap);

                        outContent=outContent.replace("[table]", table.getName());

                        //输出的路径(经过全局替换)
                        String outPath= TempletHandler.createContent(codePath+"/"+ t.getPath()+"/"+outFile, publicMap)  ;
                        //在新的文件中去掉模板文件标记***********(TFILE)
                        outPath=outPath.replace("(TFILE)", "");
                        //写入文件
                        FileUtil.setContent(outPath, outContent);
                    }
                }else //不用循环的文件
                {
                    String outPath= TempletHandler.createContent(codePath+"/"+ t.getPath()+"/"+t.getFileName(), publicMap);
                    //在新的文件中去掉模板文件标记***********(TFILE)
                    outPath=outPath.replace("(TFILE)", "");

                    content= TempletHandler.createContent(content, publicMap);
                    FileUtil.setContent(outPath, content);//写入文件
                }
            }else  //非模板文件直接拷贝
            {
                String newPath= TempletHandler.createContent( codePath+"/"+ t.getPath()+"/"+t.getFileName(), publicMap);

                FileUtil.copyFile(t.getAllPath(), newPath);
            }
        }
        System.out.println("代码成功生成!");
    }

}
