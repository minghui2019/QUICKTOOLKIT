package com.eplugger.xml.dom4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.DocumentType;

import com.eplugger.annotation.Comment;
import com.eplugger.commons.lang3.StringUtils;
import com.eplugger.commons.lang3.reflect.FieldUtils;
import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.eplugger.xml.dom4j.parse.FieldValueParserFactory;
import com.eplugger.xml.dom4j.parse.SimpleValueParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * XML文件对象
 *
 * @author Huang.Yong
 */
@Slf4j
@Setter
@Getter
public class XMLObject implements Serializable {
    private static final long serialVersionUID = 7702755997734263716L;

    @Comment("属性列表")
    private Map<String, String> attrs = new HashMap<>();
    @Comment("子标签集合, Key: tagName, Value: List&lt;XMLObject&gt;")
    private Map<String, List<XMLObject>> childTags = new LinkedHashMap<>();
    @Comment("标签文本内容")
    private String content;
    @Comment("是否根节点")
    private boolean rootElement;
    @Comment("父级节点")
    private XMLObject parent;
    @Comment("标签名")
    private String tagName;
    @Comment("DocumentType")
    private XMLDocumentType docType;

    /**
     * 构建XML对象
     *
     * @param tagName 标签名
     */
    XMLObject(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 构建XML对象
     *
     * @param tagName 标签名
     * @param content 标签体
     */
    XMLObject(String tagName, String content) {
        this(tagName, content, new HashMap<String, String>());
    }

    /**
     * 构建XML对象
     *
     * @param tagName 标签名
     * @param content 标签体
     * @param attrs   标签属性表
     */
    XMLObject(String tagName, String content, Map<String, String> attrs) {
        this.tagName = tagName;
        this.content = content;
        this.attrs = attrs;
    }

    /**
     * 指定对象转换为XMLObject数据, 目标对象必须使用{@link Dom4JTag @XmlTag}注解
     *
     * @param data 目标对象
     * @param      <T> 对象类型
     * @return XMLObject实体, 目标对象为null时总是返回null
     */
    public static <T> XMLObject of(T data) {
        if (data == null)
            return null;

        Class<?> type = data.getClass();
        Dom4JTag xmlTag = type.getAnnotation(Dom4JTag.class);
        if (xmlTag == null) {
            log.error("实体类[" + type.toString() + "]没有被标记为 @Dom4JTag");
            throw new UnsupportedOperationException("实体类[" + type.toString() + "]没有被标记为 @Dom4JTag");
        }

        // 一级属性
        String tagName = getTargetTagName(xmlTag, type.getSimpleName());
        XMLObject xmlObject = new XMLObject(tagName);
        List<Field> fields = FieldUtils.getAllFieldsList(type);
        for (Field field : fields) {
            // 不处理静态和常量
            if (FieldUtils.isStaticOrFinal(field)) {
                log.error("实体类[" + type.toString() + "]的字段[" + field.getName() + "]为final或static");
                continue;
            }

            // 获取字段相关数据
            // @Dom4JField注解
            Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
            if (xmlField == null) {
                log.error("字段[" + field.getName() + "]没有被标记为 @Dom4JField");
                continue;
            }

            // 字段XML映射名称
            Class<?> fieldType = field.getType();
            String fieldName = getTargetTagName(xmlField, field.getName());

            // 获取字段值
            Object fieldValue = FieldUtils.getFieldValue(data, field);

            Dom4JFieldType dom4jFieldType = xmlField.type();
            if (Dom4JFieldType.ATTRIBUTE == dom4jFieldType) {
                getValueByAttribute(xmlObject, fieldType, fieldName, fieldValue);
                continue;
            }

            if (Dom4JFieldType.TAG == dom4jFieldType) {
                getValueByTag(xmlObject, field, fieldName, fieldValue);
                continue;
            }

            if (Dom4JFieldType.ELEMENT == dom4jFieldType) {
                getValueByElement(xmlObject, fieldType, fieldName, fieldValue);
            }
        }

        return xmlObject;
    }

    /**
     * 从标签属性获取值
     * 
     * @param xmlObject
     * @param fieldType
     * @param fieldName
     * @param fieldValue
     */
    private static void getValueByAttribute(XMLObject xmlObject, Class<?> fieldType, String fieldName, Object fieldValue) {
        SimpleValueParser<?> parser = FieldValueParserFactory.getFactory(fieldType);
        xmlObject.attrs.put(fieldName, parser.fromBean(fieldValue));
    }

    /**
     * 从文本内容获取值
     * 
     * @param xmlObject
     * @param fieldType
     * @param fieldName
     * @param fieldValue
     */
    private static void getValueByElement(XMLObject xmlObject, Class<?> fieldType, String fieldName, Object fieldValue) {
        List<XMLObject> children = xmlObject.childTags.computeIfAbsent(fieldName, k -> new ArrayList<>());
        SimpleValueParser<?> parser = FieldValueParserFactory.getFactory(fieldType);
        children.add(new XMLObject(fieldName, parser.fromBean(fieldValue)));
    }

    /**
     * 从子标签获取复杂属性值
     * 
     * @param xmlObject  目标对象
     * @param field      字段对象
     * @param fieldValue 源对象属性
     */
    private static void getValueByTag(XMLObject xmlObject, Field field, String fieldName, Object fieldValue) {
        boolean isFired = trySimpleValueByTag(xmlObject, field.getType(), fieldName, fieldValue);
        isFired = isFired || tryCollectionOrArray(xmlObject, field, fieldValue);
        isFired = isFired || tryCustomType(xmlObject, field.getType(), fieldValue);
        log.debug("处理结果: " + isFired);
    }

    /**
     * 使用子标签获取集合值
     * 
     * @param xmlObject
     * @param field
     * @param fieldValue
     * @return
     */
    private static boolean tryCollectionOrArray(XMLObject xmlObject, Field field, Object fieldValue) {
        Class<?> fieldType = field.getType();

        // 列表&数组
        FieldUtils.CollectionType collectionType = FieldUtils.isCollection(fieldType);
        if (collectionType == null || (FieldUtils.CollectionType.LIST != collectionType
                && FieldUtils.CollectionType.SET != collectionType))
            return false;

        // List & Set
        // 获取泛型类型
        // 如果没有泛型不设置当前值
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            // 泛型类型必须被 XmlTag 注解, 否则不予解析
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> childType = (Class<?>) pt.getActualTypeArguments()[0];
            if (!childType.isAnnotationPresent(Dom4JTag.class)) {
                log.warn("请检查泛型类型[" + childType.getName() + "]是否添加 @Dom4JTag 注解");
                return false;
            }

            Dom4JTag dom4jTag = childType.getAnnotation(Dom4JTag.class);
            String childTagName = getTargetTagName(dom4jTag, childType.getSimpleName());
            List<XMLObject> children = xmlObject.childTags.computeIfAbsent(childTagName, k -> new ArrayList<>());
            List<Object> list = com.eplugger.commons.collections.CollectionUtils.cast((List<?>) fieldValue);
            for (Object obj : list) {
                if (obj == null)
                    continue;
                XMLObject childTag = XMLObject.of(obj);
                children.add(childTag);
            }
            return true;
        }
        return false;
    }

    /**
     * 使用子标签获取简单值
     *
     * @param bean      数据对象
     * @param fieldType 字段类型
     * @param fieldName 字段名称
     * @param target    目标标签
     * @return 成功处理返回true, 否则返回false(需要其他方式处理)
     */
    private static boolean trySimpleValueByTag(XMLObject xmlObject, Class<?> fieldType, String fieldName, Object fieldValue) {
        // 只处理简单对象
        if (!FieldUtils.isSimpleType(fieldType))
            return false;

        getValueByElement(xmlObject, fieldType, fieldName, fieldValue);
        return true;
    }

    /**
     * 使用子标签获取自定义类型值
     * 
     * @param xmlObject
     * @param field
     * @param fieldValue
     * @return
     */
    private static boolean tryCustomType(XMLObject xmlObject, Class<?> fieldType, Object fieldValue) {
        // 自定义类型需要加 Dom4JTag 注解
        if (!fieldType.isAnnotationPresent(Dom4JTag.class))
            return false;
        if (fieldValue == null)
            return false;

        Dom4JTag fieldTypeXmlTag = fieldType.getAnnotation(Dom4JTag.class);
        String childTagName = getTargetTagName(fieldTypeXmlTag, fieldType.getSimpleName());
        List<XMLObject> children = xmlObject.childTags.computeIfAbsent(childTagName, k -> new ArrayList<>());

        XMLObject childTag = XMLObject.of(fieldValue);
        children.add(childTag);
        return true;
    }

    /**
     * 添加标签属性
     *
     * @param attrName  属性名
     * @param attrValue 属性值
     */
    public void addAttr(String attrName, String attrValue) {
        getAttrs().put(attrName, attrValue);
    }

    /**
     * 添加子标签
     *
     * @param xmlObject 子标签对象
     */
    public void addChildTag(XMLObject xmlObject) {
        Map<String, List<XMLObject>> localSubTags = getChildTags();

        // 验证是否已存在当前标签
        String tagName = xmlObject.tagName;
        List<XMLObject> subTags = localSubTags.computeIfAbsent(tagName, k -> new ArrayList<>());

        // 添加标签
        subTags.add(xmlObject);
    }

    /**
     * 追加到指定标签节点之后(外部)
     *
     * @param markerNode 标记性节点
     * @param sameLevel  当前节点必须和标记节点处于相同层次(<i>将相同节点几种到一起更便于维护和管理</i>)
     * @return boolean true-成功. false-失败,当前为根节点或<i>node</i>为根节点/漂浮状态时,
     *         或当前节点和标记节点不是同名节点(当<i>mustBeSameTagName = true</i>时)
     */
    public boolean appendAfter(XMLObject markerNode, boolean sameLevel) {
        // 追加前验证
        boolean valid = validationOuterEdit(markerNode);
        if (!valid)
            return false;

        // 同级元素验证
        if (sameLevel && !StringUtils.equals(tagName, markerNode.tagName)) {
            log.debug("标记节点必须和当前节点同名!");
            return false;
        }

        // 获取marker父节点
        XMLObject markerParent = markerNode.getParent();

        // 获取marker的同级节点列表, 找到marker在列表中的位置
        List<XMLObject> markerLevelChildren = markerParent.getChildTags(markerNode.getTagName());
        LinkedList<XMLObject> linkedList = Lists.newLinkedList(markerLevelChildren);
        int markerIdx = linkedList.indexOf(markerNode);

        // 漂浮当前节点
        this.setFloating();

        // 把当前节点追加到marker同级节点中, 位置在marker之前
        linkedList.add(markerIdx, this);
        markerLevelChildren.clear();
        markerLevelChildren.addAll(linkedList);

        // 当前节点父节点设置为marker父节点
        this.setParent(markerParent);

        return true;
    }

    /**
     * 追加到指定标签节点之前(外部)
     *
     * @param markerNode 标记性节点
     * @param sameLevel  当前节点必须和标记节点处于相同层次(<i>将相同节点几种到一起更便于维护和管理</i>)
     * @return boolean true-成功. false-失败,当前为根节点或<i>node</i>为根节点/漂浮状态时,
     *         或当前节点和标记节点不是同名节点(当<i>mustBeSameTagName = true</i>时)
     */
    public boolean appendBefore(XMLObject markerNode, boolean sameLevel) {
        boolean valid = validationOuterEdit(markerNode);
        if (!valid)
            return false;

        // 当前节点和标记节点必须是同名节点
        if (sameLevel && !StringUtils.equals(tagName, markerNode.tagName)) {
            log.debug("标记节点必须和当前节点同名!");
            return false;
        }

        // 获取标记节点父节点
        XMLObject markerNodeParent = markerNode.getParent();

        // 获取父节点中标记节点子节点集合
        List<XMLObject> markerLevelChildren = markerNodeParent.getChildTags(markerNode.getTagName());
        LinkedList<XMLObject> linkedList = Lists.newLinkedList(markerLevelChildren);

        // 找到标记节点在集合中的位置
        int markerIdx = linkedList.indexOf(markerNode);

        // 设置当前节点为漂浮状态
        this.setFloating();

        // 将当前节点移动到标记节点之前
        linkedList.add(markerIdx, this);
        markerLevelChildren.clear();
        markerLevelChildren.addAll(linkedList);

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof XMLObject))
            return false;
        XMLObject xmlObject = (XMLObject) o;
        return rootElement == xmlObject.rootElement && Objects.equals(attrs, xmlObject.attrs)
                && Objects.equals(childTags, xmlObject.childTags) && Objects.equals(content, xmlObject.content)
                && Objects.equals(parent, xmlObject.parent) && tagName.equals(xmlObject.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attrs, childTags, content, rootElement, parent, tagName);
    }

    /**
     * 获取当前节点下所有的子节点, 包括当前元素和后代元素
     *
     * @param childTagName 子节点标签名
     * @return List&lt;XMLObject&gt; 指定标签列表
     */
    public List<XMLObject> getAllChildTags(String childTagName) {
        List<XMLObject> result = Lists.newArrayList();
        getAllChild(this, childTagName, result);
        return result;
    }

    /**
     * 获取指定属性, 如果属性不存在或没有值, 总是返回{@link StringUtils#EMPTY}
     *
     * @param attrName 属性名
     * @return String 属性值
     */
    public String getAttr(String attrName) {
        String attrVal = getAttrs().get(attrName);
        return StringUtils.isNotEmpty(attrVal, true) ? attrVal : StringUtils.EMPTY;
    }

    /**
     * 获取标签属性列表
     *
     * @return Map&lt;String,String&gt; 标签属性集合, Key:属性名, Value:属性值
     */
    public Map<String, String> getAttrs() {
        if (this.attrs == null)
            this.attrs = Maps.newHashMap();

        return this.attrs;
    }

    /**
     * 获取指定子标签
     *
     * @param tagName 子标签名
     * @param index   第几个 <i>tagName</i> 指定的子标签
     * @return XMLObject XML 节点对象
     */
    public XMLObject getChildTag(String tagName, int index) {
        List<XMLObject> subTags = getChildTags(tagName);
        if (index >= subTags.size() || index < 0)
            return null;

        return subTags.get(index);
    }

    /**
     * 获取所有子标签
     *
     * @return Map&lt;String,List&lt;XMLObject&gt;&gt; 子标签集合, Key:标签名,
     *         Value:当前标签下所有与标签名关联的一级子标签
     */
    public Map<String, List<XMLObject>> getChildTags() {
        if (this.childTags == null)
            this.childTags = Maps.newHashMap();

        return this.childTags;
    }

    /**
     * 获取子标签
     *
     * @param tagName 标签名
     * @return List&lt;XMLObject&gt; 当前标签包含的所有子标签, 总是返回合法的列表对象
     */
    public List<XMLObject> getChildTags(String tagName) {
        return getChildTags().computeIfAbsent(tagName, k -> Lists.newArrayList());
    }

    /**
     * 是否包含指定属性名
     *
     * @param attrName 属性名
     * @return boolean true-包含指定属性, false-不包含指定属性
     */
    public boolean hasAttr(String attrName) {
        return this.getAttrs().containsKey(attrName);
    }

    /**
     * 验证是否包含目标子标签
     *
     * @param subTag 目标子标签
     * @return boolean true-包含, false-不包含
     */
    public boolean hasChildTag(XMLObject subTag) {
        if (childTags == null || subTag == null)
            return false;

        String subTagName = subTag.getTagName();
        List<XMLObject> list = getChildTags().get(subTagName);
        return list != null && list.contains(subTag);
    }

    /**
     * 是否包含混合属性
     * 
     * @return
     */
    public boolean hasMixedContent() {
        return StringUtils.isEmpty(this.content, true) && this.childTags.isEmpty();
    }

    /**
     * 插入到指定节点之后(内部)
     *
     * @param parentNode 父节点
     * @return boolean true-成功, false-失败,当前为根节点或当前节点为漂浮状态时
     */
    public boolean insertAfter(XMLObject parentNode) {
        boolean valid = validationInnerEdit(parentNode);
        if (!valid)
            return false;

        // 从原有的父节点中移出当前节点
        this.setFloating();

        // 获取当前节点在目标父节点的同级元素
        List<XMLObject> currLevelChildren = parentNode.getChildTags(this.getTagName());

        // 将当前节点添加到目标父节点内容的最末尾
        LinkedList<XMLObject> linkedList = Lists.newLinkedList(currLevelChildren);
        linkedList.addLast(this);
        currLevelChildren.clear();
        currLevelChildren.addAll(linkedList);

        return true;
    }

    /**
     * 插入到指定节点之前(内部)
     *
     * @param parentNode 父节点, 父节点不能是漂浮状态
     * @return boolean true-成功, false-失败,当前为根节点
     */
    public boolean insertBefore(XMLObject parentNode) {
        boolean valid = validationInnerEdit(parentNode);
        if (!valid)
            return false;

        // 获取目标父节点的子节点集合,
        // 子节点集合与当前节点名相关
        List<XMLObject> parentChildren = parentNode.getChildTags(this.getTagName());

        // 从原来所属父节点中移出当前节点
        setFloating();

        // 将当前节点插入到子节点第一个
        LinkedList<XMLObject> linkedList = Lists.newLinkedList(parentChildren);
        parentChildren.clear();
        linkedList.addFirst(this);
        parentChildren.addAll(linkedList);

        // 设置父节点
        this.setParent(parentNode);

        return true;
    }

    /**
     * 映射为实体类; 要注册自定义解析器需要在 {@link FieldValueParserFactory} 中注册 并且必须在调用当前方法之前.
     *
     * @param cls 实体类字节码
     * @param     <T> 实体类泛型
     * @return 实体类对象
     * @see FieldValueParserFactory 字段实例解析器工厂
     */
    public <T> T toBean(Class<T> cls) {
        validExpectTagName(cls);

        T bean;
        try {
            bean = cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 获取字段列表
        List<Field> fields = FieldUtils.getAllFieldsList(cls);
        for (Field field : fields)
            if (!FieldUtils.isStaticOrFinal(field))
                setValue(bean, field);

        return bean;
    }

    /**
     * 指定控件解析为列表
     *
     * @param childTagName 子标签名
     * @param cls          实体类字节码
     * @param              <T> 实体类类型
     * @return 实体类列表
     * @see #toBean(Class)
     */
    public <T> List<T> toBeans(String childTagName, Class<T> cls) {
        List<T> beans = Lists.newArrayList();

        List<XMLObject> children = getAllChildTags(childTagName);
        for (XMLObject xmlObject : children) {
            T bean = xmlObject.toBean(cls);
            beans.add(bean);
        }

        return beans;
    }

    /**
     * 校验当前节点是否漂浮状态(只能代表当前节点, 而不能代表其上级节点是否也是漂浮状态)
     *
     * @return boolean true-漂浮状态, false-不是漂浮状态
     */
    private boolean isFloating() {
        // 不是根节点, 并且没有相应的父节点时
        return !this.isRootElement() && this.getParent() == null;
    }

    /**
     * 追加到result中
     *
     * @param xmlObject    xml节点元素
     * @param childTagName 目标节点名称
     * @param result       用于保存节点列表
     */
    private void appendTag(XMLObject xmlObject, String childTagName, List<XMLObject> result) {
        if (StringUtils.equalsIgnoreCase(xmlObject.getTagName(), childTagName))
            result.add(xmlObject);
    }

    /**
     * 获取所有子节点
     *
     * @param xmlObject    节点对象
     * @param childTagName 目标节点名称
     * @param result       用于保存节点列表
     */
    private void getAllChild(XMLObject xmlObject, String childTagName, List<XMLObject> result) {
        appendTag(xmlObject, childTagName, result);

        // 验证是否还有后代元素
        Map<String, List<XMLObject>> children = xmlObject.getChildTags();
        if (children.size() <= 0)
            return;

        // 遍历所有后代标签
        for (Entry<String, List<XMLObject>> child : children.entrySet()) {
            List<XMLObject> descendants = child.getValue();

            if (descendants.size() <= 0) {
                appendTag(xmlObject, childTagName, result);
                continue;
            }

            for (XMLObject descendant : descendants) {
                getAllChild(descendant, childTagName, result);
            }
        }
    }

    /**
     * 从父节点中将当前节点移出
     */
    private void setFloating() {
        if (isRootElement())
            return;

        XMLObject currParent = getParent();
        if (currParent == null)
            return;

        Map<String, List<XMLObject>> parentChildren = currParent.getChildTags();

        // 从父节点的子节点集合中删除当前节点
        String currTagName = getTagName();
        List<XMLObject> currLevelChildren = parentChildren.get(currTagName);
        currLevelChildren.remove(this);

        // 删除无效的子节点记录
        if (CollectionUtils.isEmpty(currLevelChildren))
            parentChildren.remove(currTagName);
        this.parent = null;
    }

    /**
     * 编辑节点前结构验证, 当前节点不是root节点且父节点不是漂浮状态
     *
     * @param targetParent 目标父节点
     * @return true-验证通过, false-验证失败
     */
    private boolean validationInnerEdit(XMLObject targetParent) {
        if (StringUtils.isBlank(tagName)) {
            log.debug("当前节点未指定有效标签名称");
            return false;
        }

        if (this.isRootElement()) {
            log.debug("根节点不允许被插入到其他节点中");
            return false;
        }

        if (StringUtils.isBlank(targetParent.tagName)) {
            log.debug("目标节点未指定有效标签名称");
            return false;
        }

        if (targetParent.isFloating()) {
            log.debug("目标节点不稳定, 不允许插入其他节点");
            return false;
        }

        return true;
    }

    /**
     * 验证是否能执行编辑, 相对于标记节点
     *
     * @param markerNode 标记节点
     * @return boolean true-验证通过, false-验证失败
     */
    private boolean validationOuterEdit(XMLObject markerNode) {
        if (equals(markerNode)) {
            log.debug("目标节点不能是当前节点");
            return false;
        }

        // 当前节点不能是根节点
        if (isRootElement()) {
            log.debug("根节点不支持当前操作");
            return false;
        }

        // 标记节点不能是根节点, 且不能为漂浮状态
        if (markerNode.isRootElement()) {
            log.debug("目标节点不支持当前操作");
            return false;
        }

        if (markerNode.isFloating()) {
            log.debug("目标节点不稳定, 不允许插入其他节点");
            return false;
        }

        return true;
    }

    /**
     * 设置值
     *
     * @param bean  数据对象
     * @param field 字段
     */
    private void setValue(Object bean, Field field) {
        // 不处理没有添加字段注解的属性
        Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
        if (xmlField == null)
            return;

        String name = getTargetTagName(xmlField, field.getName());
        Dom4JFieldType type = xmlField.type();
        try {
            switch (type) {
            // 属性直接映射
            case ATTRIBUTE:
                XMLObject target = parseFieldPath(xmlField);
                if (target != null) {
                    String value = target.getAttr(name);
                    setFieldValue(bean, field, value);
                }
                break;
            // 子标签
            case TAG:
                setValueByTag(bean, field, xmlField);
                break;
            // 文本值
            case ELEMENT:
                XMLObject childTag = this.getChildTag(name, 0);
                if (childTag != null) {
                    setFieldValue(bean, field, childTag.getContent());
                }
                break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e + field.getName());
        }
    }

    /**
     * 从子标签填充复杂属性值
     *
     * @param bean     目标对象
     * @param field    字段对象
     * @param xmlField XML字段映射注解
     */
    private void setValueByTag(Object bean, Field field, Dom4JField xmlField) throws Exception {
        // 支持 path + hierarchy 寻路
        XMLObject target = parseFieldPath(xmlField);
        if (target == null)
            return;
        
        boolean isFired = trySimpleValueByTag(bean, field, target);
        isFired = isFired || tryCollectionOrArray(bean, field);
        isFired = isFired || tryCustomType(bean, field);
        log.debug("path&hierarchy处理结果: " + isFired);
    }

    /**
     * 尝试自定义类型
     *
     * @param bean  数据对象
     * @param field 字段对象
     * @return 成功处理返回true, 否则返回false(需要其他方式处理)
     */
    private boolean tryCustomType(Object bean, Field field) throws IllegalAccessException {
        Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
        Class<?> fieldType = field.getType();
        // 自定义类型
        if (!fieldType.isAnnotationPresent(Dom4JTag.class))
            return false;

        // 复合属性只能解析唯一的子标签
        // 如果标签出现多个证明实体类属性类型定义错误
        String childTagName = getTargetTagName(xmlField, fieldType.getSimpleName());
        List<XMLObject> children = getChildTags(childTagName);
        if (children.size() > 1)
            throw new RuntimeException("期望唯一子标签[" + childTagName + "]实际找到[" + children.size() + "]条");
        if (children.size() == 0)
            return false;

        FieldUtils.setFieldValue(bean, field, children.get(0).toBean(fieldType));
        return true;
    }

    /**
     * 尝试设置列表或者数组
     *
     * @param bean  数据对象
     * @param field 字段对象
     * @return 成功处理返回true, 否则返回false(需要其他方式处理)
     */
    private boolean tryCollectionOrArray(Object bean, Field field) throws IllegalAccessException {
        Dom4JField xmlField = field.getAnnotation(Dom4JField.class);
        Class<?> fieldType = field.getType();

        // 列表&数组
        FieldUtils.CollectionType collectionType = FieldUtils.isCollection(fieldType);

        // List & Set
        if (FieldUtils.CollectionType.LIST == collectionType || FieldUtils.CollectionType.SET == collectionType) {
            // 获取泛型类型
            // 如果没有泛型不设置当前值
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                // 泛型类型必须被 Dom4JTag 注解, 否则不予解析
                ParameterizedType pt = (ParameterizedType) genericType;
                Class<?> type = (Class<?>) pt.getActualTypeArguments()[0];
                if (!type.isAnnotationPresent(Dom4JTag.class)) {
                    log.warn("请检查泛型类型[" + type.getName() + "]是否添加 @Dom4JTag 注解");
                    return true;
                }

                String childTagName = getTargetTagName(xmlField, type.getSimpleName());
                List<?> val = toBeans(childTagName, type);

                // 如果目标集合是Set集合, 从List集合转
                if (FieldUtils.CollectionType.SET == collectionType)
                    FieldUtils.setFieldValue(bean, field, new LinkedHashSet<>(val));
                else
                    FieldUtils.setFieldValue(bean, field, val);
            }
            return true;
        }

        // Array
        if (FieldUtils.CollectionType.ARRAY == collectionType) {
            Class<?> type = fieldType.getComponentType();
            String childTagName = getTargetTagName(xmlField, type.getSimpleName());
            List<?> val = toBeans(childTagName, type);
            field.set(bean, val.toArray());
            return true;
        }
        return false;
    }

    /**
     * 获取属性映射的目标标签或属性名称
     *
     * @param dom4jField  映射注解
     * @param defaultName 默认名称
     * @return 映射名称
     */
    private static String getTargetTagName(Dom4JField dom4jField, String defaultName) {
        return StringUtils.defaultIfBlank(dom4jField.name(), defaultName);
    }
    private static String getTargetTagName(Dom4JTag dom4jTag, String defaultName) {
        return StringUtils.defaultIfBlank(dom4jTag.value(), defaultName);
    }

    /**
     * 使用子标签设置简单值
     *
     * @param bean   数据对象
     * @param field  字段对象
     * @param target 目标标签
     * @return 成功处理返回true, 否则返回false(需要其他方式处理)
     */
    private boolean trySimpleValueByTag(Object bean, Field field, XMLObject target) throws Exception {
        Class<?> fieldType = field.getType();
        Dom4JField xmlField = field.getAnnotation(Dom4JField.class);

        // 只处理简单对象
        if (!FieldUtils.isSimpleType(fieldType))
            return false;

        // 子节点个数检查
        String childTagName = getTargetTagName(xmlField, fieldType.getSimpleName());
        List<XMLObject> children = target.getChildTags(childTagName);

        if (children.size() > 1)
            throw new RuntimeException("期望唯一子标签[" + childTagName + "]实际找到[" + children.size() + "]条");
        if (children.size() == 0)
            return false;

        setFieldValue(bean, field, children.get(0).content);
        return true;
    }

    /**
     * 解析字段路径配置
     *
     * @param xmlField 字段配置
     * @return 目标XML节点
     */
    private XMLObject parseFieldPath(Dom4JField xmlField) {
        String[] path = xmlField.path();
        XMLObject target = this;

        List<String> rightPaths = new ArrayList<>();
        for (String node : path) {
            int tagIndex = 0;
            String[] split = node.split("\\[");
            if (2 < split.length) {
                log.info("成功解析路径: " + rightPaths);
                throw new RuntimeException("无效的子标签索引规则: " + node + ", 最多支持一维数组索引.");
            }

            if (2 == split.length) {
                node = split[0];
                String indexStr = split[1].substring(0, split[1].length() - 2);
                try {
                    tagIndex = Integer.parseInt(indexStr);
                } catch (NumberFormatException e) {
                    log.info("成功解析路径: " + rightPaths);
                    throw new RuntimeException("索引下标[" + indexStr + "]解析失败不能转化为数字", e);
                }
            }

            rightPaths.add(node);
            target = target.getChildTag(node, tagIndex);
        }
        return target;
    }

    /**
     * 校验期望标签名, 校验失败抛出异常
     *
     * @param cls 实体类字节码
     * @param     <T> 实体类泛型
     */
    private <T> void validExpectTagName(Class<T> cls) {
        Dom4JTag xmlTag = cls.getAnnotation(Dom4JTag.class);
        if (xmlTag == null)
            return;

        String expectName = StringUtils.trimToEmpty(xmlTag.value());
        if (StringUtils.isNotBlank(expectName) && !expectName.equals(tagName))
            throw new RuntimeException("期望标签名[" + expectName + "]与实际标签名[" + tagName + "]不一致");
    }

    /**
     * 设置字段值
     *
     * @param bean  数据对象
     * @param field 字段对象
     * @param value 字段值
     */
    private void setFieldValue(Object bean, Field field, String value) {
        Class<?> type = field.getType();
        SimpleValueParser<?> parser = FieldValueParserFactory.getFactory(type);
        FieldUtils.setFieldValue(bean, field, parser.fromXml(type, value));

        // Reflects.setJsonValue(bean, field, value);
    }

    /**
     * 检查是否包含有效子标签
     *
     * @return 包含有效子标签返回true, 否则返回false
     */
    public boolean hasEffectiveChildren() {
        Map<String, List<XMLObject>> childTags = getChildTags();
        if (childTags == null || childTags.size() == 0)
            return false;

        for (Entry<String, List<XMLObject>> me : childTags.entrySet())
            if (CollectionUtils.isNotEmpty(me.getValue()))
                return true;

        return false;
    }

    @Override
    public String toString() {
        return "XMLObject [attrs=" + attrs + ", childTags=" + childTags + ", content=" + content + ", tagName=" + tagName + "]";
    }
    
    public XMLObject setParent(XMLObject parent) {
        this.parent = parent;
        return this;
    }
    
    public XMLObject setRootElement(boolean rootElement) {
        this.rootElement = rootElement;
        return this;
    }
    
    public XMLObject setDocumentType(DocumentType documentType) {
        this.docType = new XMLDocumentType(documentType.getName(), documentType.getPublicID(), documentType.getSystemID());
        return this;
    }
    
    public XMLObject setDocumentType(String name, String publicID, String systemID) {
        this.docType = new XMLDocumentType(name, publicID, systemID);
        return this;
    }
}