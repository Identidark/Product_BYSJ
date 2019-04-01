package phase1_DatabaseView.util;

import common.xmlUtil.XmlUtil;
import entity.Column;
import entity.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @program: BYSJ
 * @description: 数据库信息操作工具类
 * @author: CN_ssh
 * @create: 2019-01-30 14:24
 **/

public class DatabaseUtil {

    private String dbType;                  //数据库类型
    private String driverName;
    private String userName;
    private String passWord;
    private String url;
    private String dbName;                  //数据库名称
    private String ip;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    /**
     * 获取所有数据库名
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<String> getSchemas() throws ClassNotFoundException, SQLException
    {

        Class.forName(driverName);
        Connection connection = java.sql.DriverManager.getConnection(url, userName, passWord);
        //获取数据库对象实例
        DatabaseMetaData metaData = connection.getMetaData();
        //获取所有数据库信息
        ResultSet rs = metaData.getCatalogs();
        List<String> list=new ArrayList<String>();
        while(rs.next())
        {
            //获取数据库名存入集合中
            list.add( rs.getString(1));
        }
        rs.close();
        connection.close();
        return list;
    }

    /**
     * 获取表及字段信息
     * @param
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Table> getDbInfo() throws ClassNotFoundException, SQLException
    {
        //加载转换器
        Map<String, String> convertMap = XmlUtil.readNu("typeConverter.xml");

        Class.forName(driverName);
        Properties props =new Properties();
        props.put("remarksReporting","true");
        props.put("user", userName);
        props.put("password", passWord);
        //确定指定的数据库		TODO [db]哪里来的？？？？？？？？数据库界面readConfig读取基础数据库信息模板后指定的
        if(dbName!=null  ){
            url=url.replace("[db]", dbName);
        }

        //确定数据库连接url
        if(ip!=null && !ip.equals("")){
            url=url.replace("[ip]", ip);
        }else
        {
            url=url.replace("[ip]", "127.0.0.1");
        }

        Connection connection = java.sql.DriverManager.getConnection(url, props);



        DatabaseMetaData metaData = connection.getMetaData();

        String schema=null;
        String catalog=null;
        //如果是oracle数据库
        if(dbType!=null && dbType.toUpperCase().indexOf("ORACLE")>=0)
        {
            schema=userName.toUpperCase();
            catalog = connection.getCatalog();
        }
        //获取所有表名
        ResultSet tablers = metaData.getTables(catalog, schema, null, new String[]{"TABLE"});

        List<Table> list=new ArrayList<Table>();
        while(tablers.next())
        {
            entity.Table table=new Table();
            String tableName=tablers.getString("TABLE_NAME");

            //如果为垃圾表
            if(tableName.indexOf("=")>=0 || tableName.indexOf("$")>=0)
            {
                continue;
            }

            //判断 表名为全大写 ，则转换为小写
            if(tableName.toUpperCase().equals(tableName))
            {
                table.setName(tableName.toLowerCase());
            }else
            {
                table.setName(tableName);
            }

            //设置注释
            table.setComment(tablers.getString("REMARKS"));

            //获得主键
            ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);
            List<String> keys=new ArrayList<String>();
            while(primaryKeys.next())
            {
                String keyname=primaryKeys.getString("COLUMN_NAME");
                //判断 主键名为全大写 ，则转换为小写
                if(keyname.toUpperCase().equals(keyname))
                {
                    keyname=keyname.toLowerCase();//转换为小写
                }
                keys.add(keyname);
            }
            System.out.println("信息："+catalog+"   "+schema+"   "+tableName);

            //获得所有列
            ResultSet columnrs = metaData.getColumns(catalog, schema, tableName, null);

            List<Column> columnList=new ArrayList<Column>();
            while(columnrs.next())
            {
                Column column=new Column();
                String columnName=  columnrs.getString("COLUMN_NAME");
                //判断 表名为全大写 ，则转换为小写
                if(columnName.toUpperCase().equals(columnName))
                {
                    columnName=columnName.toLowerCase();//转换为小写
                }
                column.setColumnName(columnName);

                //TODO 原始类型?????????????????????????????????
                String columnDbType = columnrs.getString("TYPE_NAME");
                column.setColumnDbType(columnDbType);//数据库原始类型

                String typeName = convertMap.get(columnDbType);//获取转换后的类型
                if(typeName==null)
                {
                    typeName=columnrs.getString("TYPE_NAME");
                }
                column.setColumnType(typeName);

                String remarks = columnrs.getString("REMARKS");//备注
                if(remarks==null)
                {
                    remarks=columnName;
                }
                column.setColumnComment(remarks);

                if(keys.contains(columnName))//如果该列是主键
                {
                    column.setColumnKey("PRI");
                    table.setKey(column.getColumnName());
                }else
                {
                    column.setColumnKey("");
                }
                int decimal_digits =columnrs.getInt("DECIMAL_DIGITS");//小数位数
                if(decimal_digits>0)
                {
                    column.setColumnType("Double");//如果是小数则设置为Double
                }

                column.setDecimal_digits(decimal_digits);//
                column.setColums_size(columnrs.getInt("COLUMN_SIZE"));//字段长度

                columnList.add(column);
            }
            columnrs.close();
            table.setColumns(columnList);

            list.add(table );

        }
        tablers.close();
        connection.close();
        return list;
    }

}
