package entity;

import java.util.List;

/**
 * @program: BYSJ
 * @description: 表级实体类
 * @author: CN_ssh
 * @create: 2019-01-28 14:28
 **/

public class Table {

    private String name;				// 表名称
    private String name2;				// 处理后的表名称
    private String comment;				// 介绍
    private String key;					// 主键列
    private String key2;				// 主键列（驼峰）
    private String key2Upper;			// 主键列（驼峰）
    private String keyType;				// 主键类型
    private List<Column> columns;		// 列集合

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey2Upper() {
        return key2Upper;
    }

    public void setKey2Upper(String key2Upper) {
        this.key2Upper = key2Upper;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
