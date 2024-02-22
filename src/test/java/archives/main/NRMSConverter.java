package archives.main;

import java.util.List;

import archives.entity.NRMS;
import archives.entity.NRMSItem;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class NRMSConverter implements Converter {
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        NRMS nrms = (NRMS) source;
        List<NRMSItem> items = nrms.getItems();
        for (NRMSItem field : items) {
            if (field.getValue() == null) {
                continue;
            }
            String name = field.getName();
            name = name.replace("/", "或");
            name = name.replace("（", "");
            name = name.replace("）", "");
            name = name.replace("(", "");
            name = name.replace(")", "");
            writer.startNode(name);
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