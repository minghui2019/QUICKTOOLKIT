package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 来源
 */
@Setter
@Getter
public class LY implements ITransferString {
    @XStreamAlias("获取方式")
    private String hqfs;
    @XStreamAlias("来源描述")
    private String lyms;
    @XStreamAlias("移交包标识")
    private String yjbbs;
    @XStreamAlias("归档部门名称")
    private String gdbmmc;
    @XStreamAlias("全宗名称")
    private String qzmc;
    @XStreamAlias("业务系统名称")
    private String ywxtmc;

    @Override
    public void emptyToNull() {
        qzmc = Strings.emptyToNull(qzmc);
        hqfs = Strings.emptyToNull(hqfs);
        lyms = Strings.emptyToNull(lyms);
        gdbmmc = Strings.emptyToNull(gdbmmc);
        yjbbs = Strings.emptyToNull(yjbbs);
        ywxtmc = Strings.emptyToNull(ywxtmc);
    }

    @Override
    public void nullToEmpty() {
        qzmc = Strings.nullToEmpty(qzmc);
        hqfs = Strings.nullToEmpty(hqfs);
        lyms = Strings.nullToEmpty(lyms);
        gdbmmc = Strings.nullToEmpty(gdbmmc);
        yjbbs = Strings.nullToEmpty(yjbbs);
        ywxtmc = Strings.nullToEmpty(ywxtmc);
    }
}
