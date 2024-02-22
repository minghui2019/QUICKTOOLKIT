package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAliasType("审批流程")
public class SPLC implements ITransferString {
    @XStreamAlias("节点名称")
    private String jdmc;
    @XStreamAlias("生成时间")
    private String scsj;
    @XStreamAlias("处理人")
    private String clr;
    @XStreamAlias("处理时间")
    private String clsj;
    @XStreamAlias("操作类型")
    private String czlx;
    @XStreamAlias("审核意见")
    private String shyj;

    @Override
    public void emptyToNull() {
        jdmc = Strings.emptyToNull(jdmc);
        scsj = Strings.emptyToNull(scsj);
        clr = Strings.emptyToNull(clr);
        clsj = Strings.emptyToNull(clsj);
        czlx = Strings.emptyToNull(czlx);
        shyj = Strings.emptyToNull(shyj);
    }

    @Override
    public void nullToEmpty() {
        jdmc = Strings.nullToEmpty(jdmc);
        scsj = Strings.nullToEmpty(scsj);
        clr = Strings.nullToEmpty(clr);
        clsj = Strings.nullToEmpty(clsj);
        czlx = Strings.nullToEmpty(czlx);
        shyj = Strings.nullToEmpty(shyj);
    }
}
