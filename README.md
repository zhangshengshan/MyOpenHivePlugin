# README

# TODO

- [ ] 开发新的SQL FORMATTER用于格式化SQL代替现有的三方库以及Intellij_Platform的Formatter
- 
- [x] completion for keywords and functions **_关键字和函数的自动补全_**
- [x] uppercase and lowercase support 大写小写支持
- [x] usage find provider 使用查找
- [x] formatter 格式化 虽然实现，但是没有借助idea 内部的方法实现， 而是借助了三方的formmater
- [x] folding builder 折叠构建器
- [x] Mather support and jump to definition 支持匹配和跳转到定义
- [x] folding support
- [x] live templates support demo(select/date/from_unixtime/other common templates)
- [x] Graphviz can not plot properly when used in prd env 产品环境下，Graphviz 不能正常绘图
- [x] dot 命令需要显示指定，需要进行dot命令的检测， 环境变量等的配置， 如果没有则弹窗告警。
- [x] jump to definitions for table name and column name
- [x] get project from a tool window
- [ ] optimize the strategy of folding 折叠策略优化, set key=value 开始的SQL不能正常fold
- [x] surround with support
- [ ] extract with part support
- [ ] code style support
- [ ] structure view support all tables extract
- [ ] Graphviz can sequence is a little wrong when nested select clauses are used in a hive file
- [ ] GraphViz 设置默认的存储路径和SVG名称 java.security.PrivilegedActionException: java.security.PrivilegedActionException:
  java.io.FileNotFoundException: test.dot (Read-only file system)
- [ ] https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/psi_demo/src/main/java/org/intellij/sdk/psi/PsiNavigationDemoAction.java
- [ ] 图形化界面的Hierachy工具 仿照本文写一个HIVE的Action
- [ ] display syntax error by the
  parser https://plugins.jetbrains.com/docs/intellij/syntax-errors.html#controlling-syntax-errors-highlighting
- [ ] MyToolWindow 中如果输入的文件名称有问题，现在没有错误提示，需要进行修正˚
- [ ] Studio TableTaskCode
- [ ] Studio 任务编辑 实例运维 提交历史
- [ ] Studio Hierachy 榫卯结构 设计
- [x] 在工具栏中设置可以配置的sessionId 并且进行持久化功能的存储。
- [ ] 拖拉拽的榫卯设计雏形可行性尝试。
- [x] 完善配置功能页面，增加配置项的说明，以及配置项的默认值。
- [ ] 执行按钮的快捷键设置还有问题，可以做成可以配置的
- [ ] Hierachy绘图样式的配置
- [x] Hierachy绘图column参数的选择和配置
- [ ] Hierachy Color Config 
- [ ] 颜色支持Node类型自定义， 也支持成套的颜色配置
- [ ] 输出地址可以配置
- [x] hierachy中存在死循环
- [ ] 根据Hierachy 字段级或者表级的依赖关系下载HQL代码
- [ ] 处理资源文件
- [x] 脚本中所有的库表都extract出来且计数
- [ ] 抽取抽表格拷贝到看板中
- [ ] 生成的文件SQL或者SVG 可以配置路径
- [ ] Graph支持增加属性设置
- [ ] Graph UpStream And DownStream Support Layers Config 
- [ ] Graph 中嵌入的地址不对
- [ ] Graph 中commet 里面的特殊字符，可能导致失败， 需要进行告警检查
- [ ] Graph 中的表名，字段名，需要进行大小写的转换
- [ ] extracttable names optimize 过滤别名
- [x] gradle release plugin 发布插件
- [x] comment support
- [x] graphviz integration but still depend on other library 图形化集成
- [ ] ParserDefinition for HiveQL 存在比较多的缺陷，需要进一步完善
- [ ] HiveSyntaxHighlighter further more adjustments (several colorScheme for example) 高亮支持
- [ ] add navigation support 添加导航支持
- [ ] plugin.xml refactoring 插件配置文件重构
- [ ] generate hive sql coding guidelines hive sql编码规范
- [ ] 格式的调优与可定义实现
- [ ] 本工程编译需要mix java 和 scala , 编译的时候存在相互依赖， 需要手动处理， 1、先编译JAVA， 2， 再编译Scala , 3 在编译JAVA， 从长远看需要处理。 
- [ ] 针对ICON缺失的问题进行处理解决。

## 不重要紧急

- [x] add scala support 添加scala支持

## 不重要不紧急

~~- [ ] alibaba java coding guidelines 阿里巴巴java编码规范~~
~~- [ ] much more beautiful icons 更多漂亮的图标~~

- [ ] add ui support designed by java.swings components 添加java.swing组件设计的ui支持
- [ ] Several ColorSchemes should be designed and implemented and should them be picked by user 多种颜色主题应该被设计和实现，用户可以选择
- [ ] icons could be config by user 图标可以被用户配置


- **1.0** 2022-09-16 initial release 初始版本

## Features

- Keymapping for moving carel to matching brace (Ctrl+M)



 ```sql
ON
CONCAT(hi_expense.owner_employee_email,SUBSTRING(hi_expense.checkin_date,1,10))
=
vcc_info.ck
```对于这类关联条件不能正常展示
