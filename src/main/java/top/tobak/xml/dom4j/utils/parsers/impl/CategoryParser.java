package top.tobak.xml.dom4j.utils.parsers.impl;

import java.io.File;
import java.io.IOException;

import com.eplugger.onekey.entity.Categories;
import lombok.extern.slf4j.Slf4j;
import top.tobak.common.io.FileUtils;
import top.tobak.xml.dom4j.utils.XmlParseUtils;
import top.tobak.xml.dom4j.utils.parsers.AbstractXmlParser;

@Slf4j
public class CategoryParser extends AbstractXmlParser<Categories> {

    @Override
    protected void beforeFromBean(String path) {
        super.beforeFromBean(path);
        this.setDocType("Categories", null, "../dtd/Categories.dtd");
        File file = new File(getPath());
        try {
            FileUtils.backupSrcFile(file);
        } catch (IOException e) {
            log.error("文件备份失败，查看文件路径[path=" + getPath() + "]");
        }
    }

    @Override
    public void register() {
        XmlParseUtils.registerParser(Categories.class, this);
    }
}
