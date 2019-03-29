package common.xmlUtil;

import common.unicodeUtil.UnicodeHandler;
import common.wordUtil.NameHandler;
import entity.Column;
import entity.Table;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import phase1_DatabaseView.util.DatabaseUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: BYSJ
 * @description: 数据库信息转XML工具类
 * @author: CN_ssh
 * @create: 2019-02-22 10:58
 **/

public class DBinfo2Xml {

    /**
     * 生成配置文件db.xml（数据库属性 + 表 + 列）
     *
     * @param util				数据元信息 + 获取数据库名 + 获取表字段信息
     * @param propertyMap		数据库属性信息(数据库类型、驱动、url、方言、主键生成策略)
     * @param outPath			输出路径
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static  void writeDatabaseXml(DatabaseUtil util, Map<String,String> propertyMap, String outPath) throws ClassNotFoundException, SQLException
    {
        Document doc = DocumentHelper.createDocument();//
        doc.setXMLEncoding("utf-8");

        //数据元
        Element root= doc.addElement("db");
        root.addAttribute("name", util.getDbName());//
        root.addAttribute("driverName", util.getDriverName());//
        root.addAttribute("userName", util.getUserName());//
        root.addAttribute("passWord", util.getPassWord());//
        root.addAttribute("url", util.getUrl());//

        for(String key:propertyMap.keySet())
        {
            Element element = root.addElement("property");
            element.addAttribute("name", key);
            element.setText(propertyMap.get(key));
        }

        //获取所有表及相关信息
        List<Table> tableList= util.getDbInfo();

        //对主键个数为1的表进行处理，挂载表节点
        for(Table table : tableList)
        {
            int keycount=0;//主键数量

            String keyType="";
            for(Column column : table.getColumns()){
                if(column.getColumnKey().equals("PRI")){
                    keycount++;				//获取主键数量
                    keyType=column.getColumnType();
                }
            }
            if(keycount==1){//如果是只有一个主键
                //System.out.println("读取表："+table.getName());
                Element tableElement=root.addElement("table");//
                tableElement.addAttribute("name", table.getName() );//表名称
                tableElement.addAttribute("name2", NameHandler.getTableName2(table.getName()) );  //处理后的表名称（去掉前缀）
                tableElement.addAttribute("comment",  UnicodeHandler.toUnicodeString(table.getComment()) );
                tableElement.addAttribute("key", table.getKey() );
                tableElement.addAttribute("key2", NameHandler.getColumnName2( table.getKey())  );//转驼峰格式
                tableElement.addAttribute("Key2", NameHandler.getClassName(NameHandler.getColumnName2( table.getKey()))    );//转驼峰格式，首字母大写

                tableElement.addAttribute("keyType", keyType );//主键类型

                //挂载表中列节点
                for(Column column : table.getColumns())
                {
                    //System.out.println("读取字段："+column.getColumnName());
                    Element columnElement=  tableElement.addElement("column");
                    columnElement.addAttribute("name", column.getColumnName());//字段名称
                    columnElement.addAttribute("name2", NameHandler.getColumnName2(column.getColumnName())  );////转驼峰格式
                    columnElement.addAttribute("type", column.getColumnType());//类型
                    columnElement.addAttribute("dbtype", column.getColumnDbType());
                    columnElement.addAttribute("comment",  UnicodeHandler.toUnicodeString( column.getColumnComment()));
                    columnElement.addAttribute("key",  column.getColumnKey());
                    columnElement.addAttribute("decimal_digits", String.valueOf(column.getDecimal_digits()));
                    columnElement.addAttribute("colums_size", String.valueOf(column.getColums_size()));
                    //主键数量
                    if(column.getColumnKey().equals("PRI")){
                        keycount++;
                    }

                }
            }

        }
        writeXml(outPath, doc);
    }

    /**
     * 生成方法
     * @param outPath
     * @param doc
     */
    private static void writeXml(String outPath,Document doc)
    {
        try {
            String xmlFileName="db.xml";
            //控制XML格式：有空格换行
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");//

            XMLWriter writer = null;//

            File file = new File(outPath + File.separatorChar +xmlFileName);

            if(!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(doc);
            writer.close();


        } catch (IOException e) {

        }
    }

    /**
     * 返回属性信息		读取生成的db.XML,获取所需的该数据库属性相关信息
     * @param xmlPath db.xml文件所在目录路径
     * @return 该数据库属性map容器：数据库类型、驱动、url、方言、主键生成策略
     */
    public static Map<String,String> readProperty(String xmlPath)
    {
        Map<String,String> map=new HashMap<String, String>();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(xmlPath+ File.separatorChar +"db.xml");

            Element dbe= doc.getRootElement();
            List<Element> elist= dbe.elements();
            for(Element e:elist)
            {
                if(e.getName().equals("property")){
                    map.put(e.attributeValue("name"), e.getText());
                }
            }

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }


    /**
     * 返回表名称		读取生成的db.XML,获取所需的该数据库表相关信息
     * @param xmlPath 生成的db.XML路径
     * @return [{表1:[列1,列2,...]},...]
     */
    public static List<Table> readDatabaseXml(String xmlPath)
    {
        List<Table> list=new ArrayList();
        //解析XML
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(xmlPath+ File.separatorChar + "db.xml");
            //根节点——数据库节点
            Element dbe= doc.getRootElement();
            //子节点——表节点
            List<Element> elist= dbe.elements();
            for(Element e:elist)
            {
                if(e.getName().equals("table")){
                    //组装表对象
                    Table table=new Table();
                    table.setName(e.attributeValue("name"));
                    table.setName2(e.attributeValue("name2"));
                    table.setKeyType(e.attributeValue("keyType"));
                    table.setComment( UnicodeHandler.decodeUnicode(   e.attributeValue("comment")));
                    table.setKey(e.attributeValue("key"));
                    table.setKey2(e.attributeValue("key2"));
                    table.setKey2Upper(e.attributeValue("Key2"));//大写主键名
                    List<Column> columns=new ArrayList();
                    List<Element> elist2=  e.elements(); //字段列表
                    //子节点——列节点
                    for(Element e2:elist2)
                    {
                        //组装列对象
                        Column column=new Column();
                        column.setColumnName(e2.attributeValue("name"));
                        column.setColumnName2(e2.attributeValue("name2"));
                        column.setColumnType(e2.attributeValue("type"));
                        column.setColumnDbType(e2.attributeValue("dbtype"));
                        column.setColumnComment(UnicodeHandler.decodeUnicode( e2.attributeValue("comment")));
                        column.setColumnKey(e2.attributeValue("key"));
                        columns.add(column);
                    }
                    table.setColumns(columns);
                    list.add(table);
                }

            }

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }



}
