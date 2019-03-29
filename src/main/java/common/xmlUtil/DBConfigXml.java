package common.xmlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: BYSJ
 * @description: 数据库信息初始化容器类
 * @author: CN_ssh
 * @create: 2019-02-19 14:54
 **/

public class DBConfigXml {

    //准备存储各类数据库与对应数据库属性信息的map容器
    public static  Map<String, Map<String,String>> readConfig()
    {

        //mysql
        Map mysqlMap=new HashMap<String,String>();
        mysqlMap.put("databaseTYPE", "MYSQL");
        mysqlMap.put("driverName", "com.mysql.jdbc.Driver");
        mysqlMap.put("url", "jdbc:mysql://[ip]:3306/[db]?characterEncoding=UTF8");
        mysqlMap.put("dialect", "org.hibernate.dialect.MySQLInnoDBDialect");		//设置数据库方言，保证在使用hibernate时能够自动适配，不用更改任何代码的情况下运行于多种环境
        mysqlMap.put("generator", "<![CDATA[<generator class=\"native\"></generator>]]>");			//由Hibernate根据底层数据库自行判断采用identity、hilo、sequence其中一种作为主键生成方式。


        //oracle
        Map oracleMap=new HashMap<String,String>();
        oracleMap.put("databaseTYPE", "ORACLE");
        oracleMap.put("driverName", "oracle.jdbc.driver.OracleDriver");
        oracleMap.put("url", "jdbc:oracle:thin:@[ip]:1521:ORCL");
        oracleMap.put("dialect", "org.hibernate.dialect.Oracle10gDialect");
        oracleMap.put("generator", "<![CDATA[<generator class=\"org.hibernate.id.SequenceGenerator\"><param name=\"sequence\">[table]_seq</param></generator>]]>");


        Map map=new HashMap();
        map.put("MYSQL", mysqlMap);
        map.put("ORACLE", oracleMap);
        return map;
    }

}
