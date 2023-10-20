package com.eplugger.xstream;

import java.util.List;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Test;
public class Test3 {
    @Test
    public void testName() {
        XStream xStream = new XStream();
        xStream.registerConverter(new NRMSConverter());
        xStream.alias("内容描述", NRMS.class);

        NRMS contentDescription = new NRMS();
        contentDescription.addItem("案卷题名", "高职院校学生顶岗实习期间职业心理辅导探讨－以广州番禺职业技术学院为例");
        contentDescription.addItem("题名", "高职院校学生顶岗实习期间职业心理辅导探讨－以广州番禺职业技术学院为例");
        contentDescription.addItem("责任者", "易苹苹");

        // 添加更多字段
        contentDescription.addItem("新字段1", "新字段值1");
        contentDescription.addItem("新字段2", "新字段值2");

        String xml = xStream.toXML(contentDescription);
        System.out.println(xml);
    }
}


@Setter
@Getter
@XStreamAlias("内容描述")
class NRMS {
    List<NRMSItem> items = Lists.newArrayList();

    public NRMS addItem(NRMSItem item) {
        items.add(item);
        return this;
    }

    public NRMS addItem(String name, String value) {
        return addItem(new NRMSItem(name, value));
    }
}

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
class NRMSItem {
    private String name;
    private String value;
}

class NRMSConverter implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        NRMS nrms = (NRMS) source;
        List<NRMSItem> items = nrms.getItems();
        for (NRMSItem field : items) {
            writer.startNode(field.getName());
            writer.setValue(field.getValue());
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        NRMS nrms = new NRMS();
        List<NRMSItem> items = Lists.newArrayList();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String name = reader.getNodeName();
            String value = reader.getValue();
            items.add(new NRMSItem(name, value));
            reader.moveUp();
        }
        nrms.setItems(items);
        return nrms;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(NRMS.class);
    }
}