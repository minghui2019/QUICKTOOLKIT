package com.eplugger.uuid;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.eplugger.util.Stack;
import com.eplugger.uuid.entity.Uuid;
import com.eplugger.uuid.entity.Uuids;
import com.eplugger.uuid.utils.UUIDUtils;
import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;
import com.eplugger.xml.dom4j.parse.FieldValueParserFactory;
import com.eplugger.xml.dom4j.parse.SimpleValueParser;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class UUIDFun {
	private Uuids uuids;
	
	public void setUuids(Uuids uuids) {
		this.uuids = uuids;
	}
	public Uuids getUuids() {
		return uuids;
	}
	public Stack<Uuid> getUuids(int count) {
		Stack<Uuid> uuidStack = new Stack<Uuid>();
		List<Uuid> uuidList = this.uuids.getUuidList();
		int i = 0;
		for (Iterator<Uuid> iterator = uuidList.iterator(); iterator.hasNext() && i < count; i++) {
			Uuid uuid = iterator.next();
			uuidStack.push(uuid);
			iterator.remove();
		}
		return uuidStack;
	}
	

	public static void main(String[] args) throws Exception {
		UUIDFun uuidFun = new UUIDFun();
		uuidFun.buildUuids();
//		Uuids uuids = buildUuids(100000);
		
		// Bean 转化为 XMLObject
    	XMLObject root = XMLObject.of(uuidFun.getUuids()).setRootElement(true).setDocumentType("uuids", null, "../dtd/UUID.dtd");

		String path = UUIDFun.class.getResource("/").getPath() + "../../src/main/resource/uuid/UUID.xml";
    	File retractFile = new File(path);
    	System.out.println(retractFile);
    	XMLParser.transfer(root, retractFile, true);
	}
	
	
	public static Uuids buildUuids(int count) {
		Uuids uuids = new Uuids();
		List<Uuid> uuidList = uuids.getUuidList();
		for (String uuid : UUIDUtils.getUuids(count)) {
			uuidList.add(new Uuid(false, uuid));
		}
		return uuids;
	}
	
	public void buildUuids() throws Exception {
		beforeBuildUuids();
		String xmlPath = UUIDFun.class.getResource("/").getPath() + "../../src/main/resource/uuid/UUID_Bak.xml";

        XMLParser xmlParser = new XMLParser(xmlPath);
        XMLObject root = xmlParser.parse();
    	assertNotNull(root);
    	FieldValueParserFactory.reg(new SimpleValueParser<Boolean>() {
        	@Override
        	public Class<Boolean> getPreciseType() {
        		return boolean.class;
        	}
        	@Override
        	public Boolean fromXml(Class<?> type, String value) {
        		value = Strings.emptyToNull(value);
        		if (Strings.isNullOrEmpty(value)) {
        			return false;
        		}
        		return Boolean.valueOf(value);
        	}
		});
    	this.uuids = root.toBean(Uuids.class);
    	afterBuildUuids();
	}
	private void afterBuildUuids() {
		List<Uuid> uuidList = this.uuids.getUuidList();
		List<Uuid> uuidList_ = Lists.newArrayList();
		for (int i = 0; i < 20; i++) {
			uuidList_.add(uuidList.get(i));
		}
		this.uuids.setUuidList(uuidList_);
	}
	private void beforeBuildUuids() {
		if (this.uuids != null) {
			this.uuids = null;
		}
	}
}
