## Product_BYSJ
毕业设计专用

## 代码重构：
    1.数据库视图部分
        DataBaseUtil  --->  DatabaseView
        Util          --->  DataBaseUtil
        Unicode       --->  UnicodeHandler
        Utils         --->  NameHandler
        ConfigXml     --->  DBConfigXml
        DatabaseXml   --->  DBinfo2Xml
        XmlUtil       --->  XmlUtil
    2.代码视图部分
        CodeUtil      --->  CodeView
        Code          --->  CodeUtil
        TempletUtil   --->  TempletGetUtil
        ClientTempletUtil  --->  TempletHandler
        FileUtil      --->  FileUtil


## 开发文档相关：
    概要设计说明书 （开发人员）
        概要设计说明书又可称系统设计说明书，这里所说的系统是指程序系统。编写的目的是说明对程序系统的设计考虑，包括程序系统的基本处理。流程、程序系统的组织结构、模块划分、功能分配、接口设计。运行设计、数据结构设计和出错处理设计等，为程序的详细设计提供基础。

    详细设计规格说明书（开发人员）
        详细设计说明书又可称程序设计说明书。编写目的是说明一个软件系统各个层次中的每一个程序（每人模块或子程序）的设计考虑。如果项目比较简单，层次较少，本文件可以不单独编写，有关内容合并入概要设计说明书。

## UI界面：Matisse Form
    http://www.360doc.com/content/15/1123/15/1106320_515246074.shtml

## SwingUtilities.invokeLater：
    Swing中事件处理和绘画代码都在一个单独的线程中执行，这个线程就叫做事件分发线程。这就确保了事件处理器都能串行的执行，并且绘画过程不会被事件打断。为了避免死锁的可能，你必须极度小心从事件分发线程中创建、修改、查询Swing组件以及模型。在Swing Tutorial的所有示例中，我们只在ComponentEventDemo中遇到一个问题。在那个样例中，有时候当你载入样例后，它并不会启动。因为 如果在文本域还没实现的时候就去更新会出现死锁，但是其他的时候没有意外的话它也是会正常启动。为了避免线程问题，建议你使用invokeLater在事件分发线程中为所有新应用程序创建GUI。

## rootPane.setLayout(new BorderLayout())：
    使用BorderLayout时，中间的面板会随着窗体的变化而变化，其他区域的大小根据添加组件多少而变化。

## BorderLayout
    https://blog.csdn.net/lirx_tech/article/details/50779009
    BorderLayout——方位布局：
        1) 布局分为东西南北中五个方位，每个方位中只能放一个组件，如果同时放了多个组件，那么后放入的就会覆盖之前放入的，这点非常值得注意！
        2) 如果添加组件时没有指定放在哪个方位则默认放在中；
        3) 当BorderLayout布局的容器改变大小（发生拉伸）时，南北位只会水平伸缩，东西位只会垂直伸缩，而中会同时在水平和垂直方向上伸缩；
        4) Window系（Frame、Dialog）和ScrollPane都是默认方位布局的；
        5) 在对框架使用setLayout设定好方位布局之后，就可以在add组件的时候可以指定具体将组件放入那个方位中，这就Container类中对add的重载：


## JTabbedPane.setPreferredSize():
    setPreferredSize需要在使用布局管理器的时候使用，布局管理器会获取空间的preferredsize，因而可以生效。例如borderlayout在north中放入一个panel，panel的高度可以通过这样实现：panel.setPreferredSize(new Dimension(0, 100));这样就设置了一个高度为100的panel，宽度随窗口变化。

## windowClosing与windowClosed:
    1. windowClosing是关闭时调用的

    2. windowClosed是关闭状态下调用的，windowClosing使用方法dispose();方法可以自动调用windowClosed。

## File.separator
    separatorChar

    public static final char separatorChar

    与系统有关的默认名称分隔符。此字段被初始化为包含系统属性 file.separator 值的第一个字符。在 UNIX 系统上，此字段的值为 '/'；在 Microsoft Windows 系统上，它为 '\\'。

    separator

    public static final String separator

    与系统有关的默认名称分隔符，为了方便，它被表示为一个字符串。此字符串只包含一个字符，即 separatorChar。

## this.getClass().getResourceAsStream(filePath);
    获取在classpath路径下的资源文件的输入流
    Class.getResourceAsStream(String path) ： path 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从ClassPath根下获取。其只是通过path构造一个绝对路径，最终还是由ClassLoader获取资源。

## FileNotFoundException系统找不到指定的路径：
https://blog.csdn.net/yqs_love/article/details/51959776
    触发原因：路径问题（确信）
        第一反应是路径问题，网上摆渡了很多也大多都是路径有误所造成的，基本是没跑了，
        但是问题就在于这个路径上，相对路径总是找不到资源换成绝对路径就可以了，而考虑到这个小工程后面可能要变为exe可执行文件，即环境随时发生变动，使用绝对路径肯定不好

## JDBC的元数据操作:
    获取数据库所有表 :http://blog.sina.com.cn/s/blog_707a9f0601014y1a.html
    Wrapper接口下的DatabaseMetaData接口提供了getTables()方法（参照源码1521）
    .getTables(p1, p2, p3, p4)方法：MySQL
        p1：数据库名
        p2：模式（数据库的登录名）
        p3：表名，一般为null
        p4：类型标准（数组格式），一般使用"TABLE"获取所有类型为TABLE的表
            可选：TABLE  /  VIEW  /  SYSTEM TABLE  /  GLOBAL TEMPORARY  /  LOCAL TEMPORARY  /  ALIAS  /  SYNONYM
        [注意]：
            1.参数p1可以直接使用数据库连接实例对象中的getCatalog()方法返回的ResultSet值填充，也可以不填/null
            2.若针对于Oracle数据库，参数p1对应数据库实例，参数p2必须大写，否则将无法获取到相应的数据
            3.参数p3若想获取所有表，可以直接设置为null，若设置为特定的表名称，则将返回该表的具体信息，相当于过滤条件
    此方法返回一个ResultSet对象，包含了表的信息：10列，
    TABLE_CAT                 String   表类别        （可为 null）
    TABLE_SCHEM               String   表模式        （可为 null）
    TABLE_NAME                String   表名称
    TABLE_TYPE                String   表类型
    REMARKS                   String   表的解释性注释
    TYPE_CAT                  String   类型的类别    （可为 null）
    TYPE_SCHEM                String   类型模式      （可为 null）
    TYPE_NAME                 String   类型名称      （可为 null）
    SELF_REFERENCING_COL_NAME String   有类型表指定 "identifier" 列的名称（可为 null）
    REF_GENERATION            String
    可以通过此方法获取表中相关内容以便后续操作

## 界面刷新：
    http://www.bubuko.com/infodetail-731980.html
    Java Swing中，界面刷新是线程同步的，也就是说同一时间，只有一个线程能执行刷新界面的代码。如果要多次不断地刷新界面，必须在多线程中调用刷新的方法。

## Unicode转换：
    https://blog.csdn.net/jemeryshen/article/details/78745085
    Unicode通常用两个字节表示一个字符，原有的英文编码从单字节变成双字节，只需要把高字节全部填为0就可以，
    在文字处理方面，统一码为每一个字符而非字形定义唯一的代码（即一个整数）

## Java_GUI_Swing:dispose()方法
    https://www.cnblogs.com/longbaobao/articles/2233449.html
    https://blog.csdn.net/linhu007/article/details/17208863
    https://zhidao.baidu.com/question/222382081.html                dispose()与setVisible()的问题

## setLocationRelativeTo():
    如果组件当前未显示或者 c 为 null，则此窗口将置于屏幕的中央。

## JFileChooser：文件选择对话框
    基本概述：
        JDK1.2版本后，javax.swing.JComponent类下的实现子类JFileChooser为用户提供了一种
        简单的机制来选择所需文件
    常用构造：
        参数用以指定对话框打开时所在路径
        JFileChooser()                           用户默认目录
        JFileChooser(File currentDirectory)      指定文件路径【常用】
        JFileChooser​(FileSystemView fsv)         系统文件目录
        [注意]：
            可以使用FileSystemView接口获取一些系统文件目录的File对象进行File操作
    常用方法：
        setFileSelectionMode(int p1)             选择模式
            注:p1可选
                DIRECTORIES_ONLY：               仅显示目录
                FILES_ONLY：                     仅显示文件
                FILES_AND_DIRECTORIES：          文件 + 目录【常用】
        showDialog(Component p1,String p2)       视图设置
            p1:显示位置
            p2:自定义触发按钮名
            [注意]:
                1.参数p1为打开对话框时所依赖的容器或触发弹出动作的组件，对话框将以此为基准设定显示位置
                  this：程序中央
                  null：屏幕中央
                  组件：组件中央(如“...”按钮)
                2.官方已提供常用对话框相应方法
                  showOpenDialog(Component p1)             文件打开对话框
                  showSaveDialog(Component p1)             文件保存对话框
        addActionListener​(ActionListener p1)     添加动作监听器  
            p1:用以监听用户某些动作，并触发相应处理操作
        addChoosableFileFilter​(FileFilter p1)    添加可选过滤器
            p1:用以过滤此选择对话框中可显示的文件
        setMultiSelectionEnabled(boolean p1)     复选设置
            p1:是否允许选择多个目标，true允许/false禁止
        getSelectedFile()                        返回选中的文件
        getSelectedFiles()                       返回选中的多个文件
            [注意]:
                前置需要开启复选设置，返回的多个文件将以数组形式存储，即File[]

## listFiles()方法:
    是返回某个目录下所有文件和目录的绝对路径，返回的是File数组

## 配置数据库方言的作用和好处：
    Hibernate底层依然使用SQL语句来执行数据库操作，虽然所有关系型数据库都支持使用标准SQL语句，但所有数据库都对标准SQL进行了一些扩展，所以在语法细节上存在一些差异，因此Hibernate需要根据数据库来识别这些差异。
    举例来说，我们在MySQL数据库里进行分页查询，只需使用limit关键字就可以了；而标准SQL并不支持limit关键字，例如Oracle则需要使用行内视图的方式来进行分页。同样的应用程序，当我们在不同数据库之间迁移时，底层数据库的访问细节会发生改变，而Hibernate也为这种改变做好了准备，现在我们需要做的是：告诉Hibernate应用程序的底层即将使用哪种数据库——这就是数据库方言。
    一旦我们为Hibernate设置了合适的数据库方言，Hibernate将可以自动应付底层数据库访问所存在的细节差异。
    如果一个系统可能运行于多种数据库，或者同时使用多种数据库，使用Hibernate将会给你带来很多的方便，Hibernate底层是通过dialect包来对各种数据库的差异进行抽象的。Dialect类中实现每种数据库相同的东西，而不同数据库对应会有该类的一个扩展实现，最终通过DialectFactory来决定创建哪一个类。通常我们都会指定hibernate.dialect属性，那直接创建该属性对应的类。如果我们没有指定该属性，那么由Hibernate自己决定选择合适的方言

## 待优化问题：
    1.不选模板直接生成代码将会引发空指针异常
