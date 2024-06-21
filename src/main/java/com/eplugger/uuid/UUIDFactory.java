package com.eplugger.uuid;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.eplugger.uuid.entity.UUID;
import com.eplugger.uuid.entity.UUIDS;
import com.eplugger.uuid.utils.UUIDUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import top.tobak.util.Stack;
import top.tobak.xml.dom4j.utils.XmlParseUtils;

@Slf4j
public class UUIDFactory {
	private static class UUIDFactorySingleton {
		private static UUIDFactory instance = new UUIDFactory();
	}
	
	/**
	 * 单例模式对外提供访问唯一实例的入口
	 * @return
	 */
	public static UUIDFactory getInstance() {
		return UUIDFactorySingleton.instance;
	}
	
	private static final String URL_XML_UUIDS = "src/main/resource/uuid/UUID.xml";
	private static final Integer INIT_NUM_UUIDS = 1000;
	private UUIDS uuids;
	private Queue<UUID> queue;
	private boolean isStart;

	/**
	 * 开始消费
	 */
	public UUIDFactory start() {
		if (isStart) {
			log.warn("UUIDFactory has already started!");
			return this;
		}
		log.info("UUIDFactory start...");
		this.isStart = true;
		init();
		return this;
	}

	/**
	 * 初始化内部的Uuids
	 */
	private void init() {
		try {
			log.info("xml parse start...");
			UUIDS uuids = XmlParseUtils.toBean(UUIDS.class, URL_XML_UUIDS);
			queue = Queues.newArrayDeque(uuids);
			log.info("xml parse end...");
		} catch (Exception e) {
			log.error("Xml Parse error! path=[{}]", URL_XML_UUIDS, e);
			queue = Queues.newArrayDeque(produce());
		}

		log.debug("uuid初始化完成");
	}

	/**
	 * 对外提供获取count数量的UUID的堆栈
	 * @param count
	 * @return
	 */
	@Deprecated
	public Stack<String> getUUIDs(int count) {
		Preconditions.checkState(isStart, "UUIDFactory has not started!");

		if (this.uuids.size() < count) {
			this.uuids.addAll(produce(INIT_NUM_UUIDS));
		}
		Stack<String> uuidStack = new Stack<>();
		int i = 0;
		for (Iterator<UUID> iterator = uuids.iterator(); iterator.hasNext() && i < count; i++) {
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
	@Deprecated
	public String[] getUUIDsArray(int count) {
		Preconditions.checkState(isStart, "UUIDFactory has not started!");

		if (this.uuids.size() < count) {
			this.uuids.addAll(produce(INIT_NUM_UUIDS));
		}
		String[] uuidArray = new String[count];
		int i = 0;
		for (Iterator<UUID> iterator = uuids.iterator(); iterator.hasNext() && i < count; i++) {
			uuidArray[i] = iterator.next().getText();
			iterator.remove();
		}
		return uuidArray;
	}

	public List<String> costMany(int count) {
		Preconditions.checkState(isStart, "UUIDFactory has not started!");
		log.debug("cost {} uuid" ,count);
		List<String> uuids = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			if (queue.peek() == null) {
				queue.addAll(produce());
			}
			uuids.add(queue.poll().getText());
		}
		return uuids;
	}
	
	/**
	 * 对外提供获取1个UUID
	 * @return
	 */
	public String cost() {
		Preconditions.checkState(isStart, "UUIDFactory has not started!");
		return costOne();
	}

	private String costOne() {
		log.debug("cost one uuid");
		if (queue.peek() == null) {
			queue.addAll(produce());
		}
		return queue.poll().getText();
	}

	/**
	 * 结束uuid的消费，destroy前必须stop
	 * @return
	 */
	public UUIDFactory stop() {
		Preconditions.checkState(isStart, "UUIDFactory has not started!");
		isStart = false;
		log.info("UUIDFactory stop");
		return this;
	}

	/**
	 * 把剩余的uuids写出到xml文件后销毁uuids变量
	 */
	public void destroy() {
		if (isStart) {
			log.warn("UUIDFactory has not stopped!");
			return;
		}

		log.info("UUIDFactory destroy");
		UUIDS uuids = new UUIDS();
		int i = 0;
		while (queue.peek() != null) {
			UUID uuid = queue.poll();
			uuid.setId(++i);
			uuids.add(uuid);
		}

		try {
			Document document = XmlParseUtils.fromBean(uuids, URL_XML_UUIDS, true);
			log.debug("\n" + document.asXML());
		} catch (Exception e) {
			log.error("回收 uuid 失败！请注意清空当前xml[{}]文件！", URL_XML_UUIDS);
			e.printStackTrace();
		}

		queue.clear();
		queue = null;
		log.debug("销毁回收uuid");
	}
	/**
	 * 把堆栈中剩余uuid放回变量uuids中，接着备份并销毁uuids变量
	 * @param stack
	 * @throws IOException
	 */
	@Deprecated
	public void destroyUUIDs(Stack<String> stack) throws IOException {
		while (!stack.isEmpty()) {
			List<UUID> uuidList = this.uuids.getUuidList();
			uuidList.add(new UUID(0, stack.pop()));
		}
		
		this.destroy();
	}
	
	private List<UUID> produce() {
		return this.produce(INIT_NUM_UUIDS);
	}
	
	/**
	 * 构建count数量的UUID
	 * @param count
	 * @return
	 */
	private List<UUID> produce(int count) {
		log.info("生产uuid: [{}]", count);
		return UUIDUtils.generateUUID(count);
	}
	
	/**
	 * 测试阶段使用
	 */
	private void afterInitUUIDs() {
	}
}
