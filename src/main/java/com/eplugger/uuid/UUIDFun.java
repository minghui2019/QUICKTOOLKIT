package com.eplugger.uuid;

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
	
	public Stack<Uuid> getUuids(int count) {
		if (this.uuids == null || this.uuids.size() < count) {
			this.uuids.addAll(buildUuids(1000));
		}
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
	
	public Uuid getUuid() {
		if (this.uuids.isEmpty()) {
			this.uuids.addAll(buildUuids(1000));
		}
		for (Iterator<Uuid> iterator = this.uuids.getUuidList().iterator(); iterator.hasNext(); ) {
			Uuid uuid = iterator.next();
			iterator.remove();
			return uuid;
		}
		return null;
	}
	

	public static void main(String[] args) throws Exception {
		UUIDFun uuidFun = new UUIDFun();
		uuidFun.buildUuids();
//		Uuids uuids = buildUuids(100000);
		
		uuidFun.consumeUuid();
		
		// Bean 转化为 XMLObject
    	XMLObject root = XMLObject.of(uuidFun.uuids).setRootElement(true).setDocumentType("uuids", null, "../dtd/UUID.dtd");

		String path = UUIDFun.class.getResource("/").getPath() + "../../src/main/resource/uuid/UUID.xml";
    	File retractFile = new File(path);
    	System.out.println(retractFile);
    	XMLParser.transfer(root, retractFile, false);
	}
	
	
	private void consumeUuid() {
		Stack<Uuid> uuids2 = getUuids(5);
		for (int i = 0; i < 3; i++) {
			Uuid pop = uuids2.pop();
			System.out.println(pop.getText());
		}
		
		afterConsumeUuid(uuids2);
	}
	
	private void afterConsumeUuid(Stack<Uuid> uuids2) {
		while (!uuids2.isEmpty()) {
			List<Uuid> uuidList = this.uuids.getUuidList();
			uuidList.add(uuids2.pop());
		}
	}
	
	public List<Uuid> buildUuids(int count) {
		List<Uuid> uuidList = Lists.newArrayList();
		for (String uuid : UUIDUtils.getUuids(count)) {
			uuidList.add(new Uuid(false, uuid));
		}
		return uuidList;
	}
	
	public void buildUuids() throws Exception {
		beforeBuildUuids();
		String xmlPath = UUIDFun.class.getResource("/").getPath() + "../../src/main/resource/uuid/UUID_Bak.xml";
        XMLParser xmlParser = new XMLParser(xmlPath);
        XMLObject root = xmlParser.parse();
    	FieldValueParserFactory.reg(new SimpleValueParser<Boolean>() {
        	@Override
        	public Class<Boolean> getPreciseType() {
        		return boolean.class;
        	}
        	@Override
        	public Boolean fromXml(Class<?> type, String value) {
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
