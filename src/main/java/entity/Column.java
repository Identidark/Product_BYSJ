package entity;

/**
 * @program: BYSJ
 * @description: 列级实体类
 * @author: CN_ssh
 * @create: 2019-01-28 14:30
 **/

public class Column {
    private String columnName;					// 列名称
    private String columnName2;					// 列名称(处理后的列名称)
    private String columnType;					// 列类型
    private String columnDbType;				// 列数据库类型
    private String columnComment;				// 列备注D
    private String columnKey;					// 是否是主键
    private int decimal_digits;					// DECIMAL_DIGITS;//小数位数
    private int colums_size;					// COLUMN_SIZE  字段长度

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName2() {
        return columnName2;
    }

    public void setColumnName2(String columnName2) {
        this.columnName2 = columnName2;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnDbType() {
        return columnDbType;
    }

    public void setColumnDbType(String columnDbType) {
        this.columnDbType = columnDbType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public int getDecimal_digits() {
        return decimal_digits;
    }

    public void setDecimal_digits(int decimal_digits) {
        this.decimal_digits = decimal_digits;
    }

    public int getColums_size() {
        return colums_size;
    }

    public void setColums_size(int colums_size) {
        this.colums_size = colums_size;
    }
}
