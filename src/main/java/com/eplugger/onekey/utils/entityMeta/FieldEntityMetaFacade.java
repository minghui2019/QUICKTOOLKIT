package com.eplugger.onekey.utils.entityMeta;

import com.eplugger.common.lang.CustomStringBuilder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FieldEntityMetaFacade {
    private EntityMetaField field;

    private static final String SQL_SYS_CFG_EDIT_TABLE = "update SYS_CFG_EDIT_TABLE set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_TABLE = "update SYS_CFG_TABLE set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_FORM = "update SYS_CFG_FORM set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_EXPORT = "update SYS_CFG_EXPORT set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_STAT = "update SYS_CFG_STAT set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_IMPORT = "update SYS_CFG_IMPORT set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_ECHART = "update SYS_CFG_ECHART set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_CFG_SQL_TABLE = "update SYS_CFG_SQL_TABLE set DATA_TYPE=':dataType' where name=':name' and sceneId in (select id from SYS_CFG_SCENE where beanid=':beanId');";
    private static final String SQL_SYS_ENTITY_META = "update SYS_ENTITY_META set DATA_TYPE=':dataType' where name=':name';";

    @Override
    public String toString() {
        CustomStringBuilder csb = new CustomStringBuilder();
        csb.append("--").append(field.getBeanId()).append(" (").append(field.getName()).appendln(")");
        csb.appendln(SQL_SYS_CFG_EDIT_TABLE.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_TABLE.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_FORM.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_EXPORT.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_STAT.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_IMPORT.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_ECHART.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_CFG_SQL_TABLE.replace(":dataType", field.getDataType()).replace(":name", field.getName()).replace(":beanId", field.getBeanId()));
        csb.appendln(SQL_SYS_ENTITY_META.replace(":dataType", field.getDataType()).replace(":name", field.getName()));
        return csb.toString();
    }
}
