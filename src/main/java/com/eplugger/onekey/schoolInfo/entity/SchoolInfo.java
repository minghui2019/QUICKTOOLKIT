package com.eplugger.onekey.schoolInfo.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum SchoolInfo {
    广州番禺职业技术学院("RDSYSEDUV85312046", "V8.5.3", "sqlserver"),
    广东机电职业技术学院("RDSYSEDUV85012743","V8.5.0", "oracle"),
    深圳市人工智能与机器人研究院("RDSYSCASV85220053", "V8.5.2", "oracle"),
    广州美术学院("RDSYSEDUV82210586", "V8.2.2", "oracle"),
    惠州学院("RDSYSEDUV830110577", "V8.3.0", "sqlserver"),
    广东工业大学("RDSYSEDUV856111845", "V8.5.6.1", "oracle"),
    梧州职业学院("RDSYSEDUV856114171", "V8.5.6.1", "oracle"),
    ;

    public String dbName;
    public String version;
    public String dbType;

    @Override
    public String toString() {
        return "你正在操作的学校为" + name() + " (" + dbName + ") ";
    }
}
