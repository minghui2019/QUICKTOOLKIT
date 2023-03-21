package com.eplugger.onekey.viewFile;

import com.eplugger.onekey.viewFile.entity.ModuleViews;
import com.eplugger.xml.dom4j.utils.ParseXmlUtils;

public class AutoViewFileConstant {
    public static ModuleViews getModuleViews() {
        try {
            ModuleViews moduleViews = ParseXmlUtils.toBean("src/main/resource/view/ModuleView.xml", ModuleViews.class);
            return moduleViews;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModuleViews();
    }
}
