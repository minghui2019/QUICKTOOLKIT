package com.eplugger.uuid;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.eplugger.util.Stack;
import com.eplugger.uuid.entity.Uuid;
import com.eplugger.uuid.entity.Uuids;
import com.eplugger.xml.dom4j.XMLObject;
import com.eplugger.xml.dom4j.XMLParser;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UUIDFun {
	private static class UUIDFunSingleton {
		private static UUIDFun instance = new UUIDFun();
	}
	
	private UUIDFun() {
		initUuids();
	}
	
	/**
	 * 单例模式对外提供访问唯一实例的入口
	 * @return
	 */
	public static UUIDFun getInstance() {
		return UUIDFunSingleton.instance;
	}
	
	private static final String URL_XML_UUIDS = "src/main/resource/uuid/UUID.xml";
	private static final Integer INIT_NUM_UUIDS = 1000;
	private Uuids uuids;
	
	/**
	 * 对外提供获取count数量的UUID的堆栈
	 * @param count
	 * @return
	 */
	public Stack<String> getUuids(int count) {
		if (this.uuids == null) {
			initUuids();
		}
		if (this.uuids.size() < count) {
			this.uuids.addAll(buildUuids(INIT_NUM_UUIDS));
		}
		Stack<String> uuidStack = new Stack<>();
		int i = 0;
		for (Iterator<Uuid> iterator = this.uuids.getUuidList().iterator(); iterator.hasNext() && i < count; i++) {
			uuidStack.push(iterator.next().getText());
			iterator.remove();
		}
		return uuidStack;
	}
	
	/**
	 * 对外提供获取count数量的UUID的数组
	 * @param count
	 * @return
	 */
	public String[] getUuidsArray(int count) {
		if (this.uuids == null) {
			initUuids();
		}
		if (this.uuids.size() < count) {
			this.uuids.addAll(buildUuids(INIT_NUM_UUIDS));
		}
		String[] uuidArray = new String[count];
		int i = 0;
		for (Iterator<Uuid> iterator = this.uuids.getUuidList().iterator(); iterator.hasNext() && i < count; i++) {
			uuidArray[i] = iterator.next().getText();
			iterator.remove();
		}
		return uuidArray;
	}
	
	public List<String> getUuidsList(int count) {
		log.debug("开始消费uuid");
		return Lists.newArrayList(getUuidsArray(count));
	}
	
	/**
	 * 对外提供获取1个UUID
	 * @return
	 */
	public String getUuid() {
		log.debug("开始消费uuid");
		if (this.uuids == null) {
			initUuids();
		}
		if (this.uuids.isEmpty()) {
			this.uuids.addAll(buildUuids(INIT_NUM_UUIDS));
		}
		for (Iterator<Uuid> iterator = this.uuids.getUuidList().iterator(); iterator.hasNext(); ) {
			Uuid uuid = iterator.next();
			iterator.remove();
			return uuid.getText();
		}
		return null;
	}
	
	/**
	 * 把堆栈中剩余uuid放回变量uuids中，接着备份并销毁uuids变量
	 * @param stack
	 * @throws IOException
	 */
	public void destroyUuids(Stack<String> stack) throws IOException {
		while (!stack.isEmpty()) {
			List<Uuid> uuidList = this.uuids.getUuidList();
			uuidList.add(new Uuid(0, stack.pop()));
		}
		
		this.destroyUuids();
	}
	
	/**
	 * 把剩余的uuids写出到xml文件后销毁uuids变量
	 * @throws IOException
	 */
	public void destroyUuids() {
		int i = 0;
		for (Uuid uuid : this.uuids.getUuidList()) {
			uuid.setId(++i);
		}
		// Bean 转化为 XMLObject
    	XMLObject root = XMLObject.of(this.uuids).setRootElement(true).setDocumentType("uuids", null, "../dtd/UUID.dtd");

    	try {
			XMLParser.transfer(root, new File(URL_XML_UUIDS), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.uuids = null;
    	log.debug("销毁回收uuid");
	}
	
	/**
	 * 构建count数量的UUID
	 * @param count
	 * @return
	 */
	public List<Uuid> buildUuids(int count) {
		List<Uuid> uuidList = Lists.newArrayList();
		int i = 0;
		for (String uuid : this.randomUUID(count)) {
			uuidList.add(new Uuid(++i, uuid));
		}
		return uuidList;
	}
	
	/**
	 * 初始化内部的Uuids
	 */
	private void initUuids() {
        XMLParser xmlParser = new XMLParser(URL_XML_UUIDS);
		try {
			XMLObject root = xmlParser.parse();
			this.uuids = root.toBean(Uuids.class);
			afterInitUuids();
		} catch (Exception e) {
			this.uuids = new Uuids();
		}
		log.debug("uuid初始化完成");
	}
	
	/**
	 * 测试阶段使用
	 */
	private void afterInitUuids() {
	}
	
	public String[] randomUUID(int count) {
		String[] uuids = new String[count];
		for (int i = 0; i < count; i++) {
			uuids[i] = UUID.randomUUID().toString().replace("-", "");
		}
		return uuids;
	}
}
