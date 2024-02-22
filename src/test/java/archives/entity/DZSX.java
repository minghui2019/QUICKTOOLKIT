package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DZSX implements ITransferString {
    @XStreamAlias("格式信息")
    private String gsxx;
    @XStreamAlias("计算机文件名")
    private String jsjwjm;
    @XStreamAlias("计算机文件大小")
    private String jsjwjdx;
    @XStreamAlias("文档创建程序")
    private String wdcjcx;

    @Override
    public void emptyToNull() {
        gsxx = Strings.emptyToNull(gsxx);
        jsjwjm = Strings.emptyToNull(jsjwjm);
        jsjwjdx = Strings.emptyToNull(jsjwjdx);
        wdcjcx = Strings.emptyToNull(wdcjcx);
    }

    @Override
    public void nullToEmpty() {
        gsxx = Strings.nullToEmpty(gsxx);
        jsjwjm = Strings.nullToEmpty(jsjwjm);
        jsjwjdx = Strings.nullToEmpty(jsjwjdx);
        wdcjcx = Strings.nullToEmpty(wdcjcx);
    }
}