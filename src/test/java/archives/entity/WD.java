package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAliasType("文档")
public class WD implements ITransferString {
    @XStreamAlias("文档序号")
    private String wdxh;
    @XStreamAlias("文档链接")
    private String wdlj;
    @XStreamAlias("电子属性")
    private DZSX dzsx;
    @XStreamAlias("电子签名")
    private DZQM dzqm;

    public WD() {
        this.dzsx = new DZSX();
        this.dzqm = new DZQM();
    }

    @Override
    public void emptyToNull() {
        wdxh = Strings.emptyToNull(wdxh);
        wdlj = Strings.emptyToNull(wdlj);
        dzsx.emptyToNull();
        dzqm.emptyToNull();
    }

    @Override
    public void nullToEmpty() {
        wdxh = Strings.nullToEmpty(wdxh);
        wdlj = Strings.nullToEmpty(wdlj);
        dzsx.nullToEmpty();
        dzqm.nullToEmpty();
    }
}