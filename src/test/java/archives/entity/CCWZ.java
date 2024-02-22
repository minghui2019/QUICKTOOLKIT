package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CCWZ implements ITransferString {
    @XStreamAlias("当前位置")
    private String dqwz;
    @XStreamAlias("脱机载体编号")
    private String tjztbh;
    @XStreamAlias("脱机载体存址")
    private String tjztcz;
    @XStreamAlias("缩微号")
    private String swh;

    @Override
    public void emptyToNull() {
        tjztcz = Strings.emptyToNull(tjztcz);
        dqwz = Strings.emptyToNull(dqwz);
        tjztbh = Strings.emptyToNull(tjztbh);
        swh = Strings.emptyToNull(swh);
    }

    @Override
    public void nullToEmpty() {
        tjztcz = Strings.nullToEmpty(tjztcz);
        dqwz = Strings.nullToEmpty(dqwz);
        tjztbh = Strings.nullToEmpty(tjztbh);
        swh = Strings.nullToEmpty(swh);
    }
}