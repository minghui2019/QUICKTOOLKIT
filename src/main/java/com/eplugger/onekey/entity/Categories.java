package com.eplugger.onekey.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Dom4JTag
public class Categories {
    @Dom4JField(type = Dom4JFieldType.TAG)
    List<Category> categories = Lists.newArrayList();

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    @Override
    public String toString() {
        return "Categories{" + "categories=" + categories + '}';
    }

    public Map<String, Category> getCategoryMap() {
        List<Category> categories = this.getCategories();
        return categories.stream().filter(m -> !Strings.isNullOrEmpty(m.getBizName())).collect(Collectors.toMap(category -> {
            if ("1".equals(category.getFromBiz())) {
                return category.getTableName() + "," + category.getCodeColumn() + "," + category.getValueColumn();
            } else {
                return category.getBizName();
            }
        }, m -> m, (m1, m2) -> m1, LinkedHashMap::new));
    }
}
