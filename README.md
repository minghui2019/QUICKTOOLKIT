## 模块加字段过程
### 1. 利用com.eplugger.onekey.addField.AddFieldMain.testCreateFieldXml()，提供字段翻译并生成XML文件方法，或者手动编辑Field.xml文件
### 2. 执行com.eplugger.onekey.addField.AddFieldMain.testCreateSqlAndJavaFile()，生成sql文件和java文件
### 3. 然后把java文件的代码复制到相应类，sql文件检查无误后直接执行即可。
### 4. 最后配置场景

### 代码示例
#### 
```java
/**
 * <pre>
 * 创建字段数据XML
 * 文件位置：src/main/resource/field/Field.xml
 * @throws Exception
 */
@Test
public void testCreateFieldXml() throws Exception {
    TextTrans.createFieldXml("", list);
}

/**
 * 字段翻译并生成常规的Field.xml文件方法
 * @param src 待翻译中文，全角顿号（、）隔开
 * @param moduleTableList {@link com.eplugger.onekey.entity.ModuleTable ModuleTable}列表
 * @throws Exception
 */
public static void createFieldXml(String src, List<ModuleTable> moduleTableList) throws Exception {}
```

```java
/**
 * <pre>
 * 根据Field.xml生成加字段需要的java代码和sql语句
 * @throws Exception
 */
@Test
public void testCreateSqlAndJavaFile() throws Exception {
    AddFieldFun.createSqlAndJavaFile();
}
```

```java
/**
 * <pre>
 * 创建字典SQL
 * version：eadp版本，"V8.5.3"; "V8.5.2"; "V8.5.0"; "V8.3.0"; "V3.1.0"...
 * keyArray：字典代码数组
 * valueArray：字典值数组
 * @throws Exception
 */
@Test
public void testCreateCategorySqlFile() throws Exception {
    String categoryName = "PROJECT_PROPERTIES"; //常量名
    String bizName = "项目属性"; //业务名称
    String bizType = AddCategoryEntry.BIZ_TYPE[1]; //业务类型
    // 字典代码
    String[] keyArray = "1,2,3,4,5,6,7,8".split(",");
    // 字典值
    String[] valueArray = "独立纵向项目、主持的纵向合作项目、参与的纵向合作项目、学会项目、科研平台、子课题、开放课题、其他".split("、");
    AddCategoryEntry.createCategorySqlFile(categoryName, bizName, bizType, "V8.5.3", keyArray, valueArray);
}
```
### `Field.xml`示例
#### 常规的String
```xml
<Field>
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType>String</dataType>
    <precision></precision>
</Field>
```
#### 带字典的String
```xml
<Field>
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType>String</dataType>
    <categoryName></categoryName>
    <precision>32</precision>
</Field>
```
#### 常规的Double, Integer, java.sql.Date...
```xml
<Field>
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType>Double</dataType>
</Field>
```
#### 虚拟字段
```xml
<Field tranSient="true">
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType>String</dataType>
</Field>
```
#### 带AppendSearch的虚拟字段
```xml
<Field tranSient="true">
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType></dataType>
    <AppendSearch relativeField="**ID">
        <value>SELECT...</value>
    </AppendSearch>
</Field>
```
#### 其他属性解释
##### onlyMeta，默认false，仅生成系统元数据sql
```xml
<Field onlyMeta="true">
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType></dataType>
    <precision></precision>
</Field>
```
##### 一对多属性
```xml
<Field association="OneToMany">
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType>List</dataType>
    <joinColumn>关联列</joinColumn>
    <genericity>泛型类</genericity>
    <orderBy>可选</orderBy>
</Field>
```
##### 多对一属性，updateInsert="true" -> @Column(updatable=false, insertable=false)
```xml
<Field association="ManyToOne" updateInsert="true">
    <fieldId></fieldId>
    <fieldName></fieldName>
    <dataType></dataType>
    <joinColumn>关联列</joinColumn>
</Field>
```
**注意：**

@Column(updatable=false, insertable=false)加在关联列上，维护关联关系是需要维护关联对象属性

@Column(updatable=false, insertable=false)加在关联对象上，维护关联关系是需要维护关联列属性

## 系统加模块
### 这部分适配性比较差
### 1. 编辑src/main/resource/module/Module.xml文件

示例：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE Modules SYSTEM '../dtd/Module.dtd'>
<Modules>
    <Module ignore="false">
        <MainModule>
            <beanId>paper</beanId>
            <moduleName>Paper</moduleName>
            <tableName>BIZ_PAPER</tableName>
            <moduleZHName>论文</moduleZHName>
            <packageName>com.eplugger.business.paper</packageName>
            <superClassMap>
                <param key="entity" value="CheckBusinessEntity" />
                <param key="bo" value="CheckBusinessBO" />
                <param key="action" value="CheckBusinessAction" />
            </superClassMap>
            <Fields>
                <Field>
                    <fieldId>name</fieldId>
                    <fieldName>论文题目</fieldName>
                    <dataType>String</dataType>
                    <precision>300</precision>
                </Field>
                <Field association="OneToMany">
                    <fieldId>authors</fieldId>
                    <fieldName>论文作者</fieldName>
                    <dataType>List</dataType>
                    <joinColumn>PAPER_ID</joinColumn>
                    <genericity>PaperAuthor</genericity>
                    <orderBy>orderId asc</orderBy><!-- 可选 -->
                </Field>
            </Fields>
        </MainModule>
        <AuthorModule>
            <beanId>paperAuthor</beanId>
            <moduleName>PaperAuthor</moduleName>
            <tableName>BIZ_PAPER_AUTHOR</tableName>
            <moduleZHName>论文作者</moduleZHName>
            <packageName>com.eplugger.business.paper</packageName>
            <superClassMap>
                <param key="entity" value="ProductAuthor" />
            </superClassMap>
            <Fields>
                <Field>
                    <fieldId>paperId</fieldId>
                    <fieldName>论文ID</fieldName>
                    <dataType>String</dataType>
                    <precision>32</precision>
                </Field>
                <Field association="ManyToOne" ignoreImport="true">
                    <fieldId>paper</fieldId>
                    <fieldName>论文</fieldName>
                    <dataType>Paper</dataType>
                    <joinColumn>PAPER_ID</joinColumn>
                </Field>
            </Fields>
        </AuthorModule>
    </Module>
</Modules>
```

### 2. 代码入口，com.eplugger.onekey.addModule.AddModuleMain.testAddMultiModuleFun1()

## 数据共享视图sql
### 1. 修改
### 2. 生成模块字段对照Excel表格，com.eplugger.onekey.viewFile.AutoViewFile.generationTabelExcel()
### 3. 自动生成视图语句文件，com.eplugger.onekey.viewFile.AutoViewFile.generationViewSql()