package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;


/**
 * 电子文件封装包
 */
@Setter
@Getter
@XStreamAliasType("电子文件封装包")
public class DZWJFZB implements ITransferString {
    @XStreamAsAttribute
    final String xmlns = "http://www.saac.gov.cn/standards/ERM/encapsulation";
    @XStreamAlias("xmlns:xsi")
    @XStreamAsAttribute
    final String xmlnsXsi = " http://www.w3.org/2001/XMLSchema-instance";
    @XStreamAlias("封装包类型")
    private String fzblx;
    @XStreamAlias("封装包类型描述")
    private String fzblxms;
    @XStreamAlias("封装包创建时间")
    private String fzbcjsj;
    @XStreamAlias("封装包创建单位")
    private String fzbcjdw;
    @XStreamAlias("封装内容")
    private FZNR fznr;

    public DZWJFZB() {
        this.fznr = new FZNR();
    }

    @Override
    public void emptyToNull() {
        fzblx = Strings.emptyToNull(fzblx);
        fzblxms = Strings.emptyToNull(fzblxms);
        fzbcjsj = Strings.emptyToNull(fzbcjsj);
        fzbcjdw = Strings.emptyToNull(fzbcjdw);
        fznr.emptyToNull();
    }

    @Override
    public void nullToEmpty() {
        fzblx = Strings.nullToEmpty(fzblx);
        fzblxms = Strings.nullToEmpty(fzblxms);
        fzbcjsj = Strings.nullToEmpty(fzbcjsj);
        fzbcjdw = Strings.nullToEmpty(fzbcjdw);
        fznr.nullToEmpty();
    }
}









