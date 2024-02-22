package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class XSTZ implements ITransferString {
    @XStreamAlias("文件组合类型")
    private String wjzhlx;
    @XStreamAlias("件数")
    private String js;
    @XStreamAlias("页数")
    private Integer ys;
    @XStreamAlias("语种")
    private String yz;
    @XStreamAlias("稿本")
    private String gb;

    @Override
    public void emptyToNull() {
        wjzhlx = Strings.emptyToNull(wjzhlx);
        yz = Strings.emptyToNull(yz);
        gb = Strings.emptyToNull(gb);
        js = Strings.emptyToNull(js);
    }

    @Override
    public void nullToEmpty() {
        wjzhlx = Strings.nullToEmpty(wjzhlx);
        yz = Strings.nullToEmpty(yz);
        gb = Strings.nullToEmpty(gb);
        js = Strings.nullToEmpty(js);
    }
}