package com.eplugger.onekey.addModule;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import top.tobak.common.io.FileUtils;
import top.tobak.common.lang.StringUtils;
import com.eplugger.onekey.entity.Field;
import com.eplugger.utils.DBUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Constants {
	private Constants() {}
	
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static Map<String, List<Field>> superClassFieldMap = Maps.newHashMap();
	private static Map<String, List<Field>> otherFieldMap = Maps.newHashMap();
	private static final Map<String, String> ASSOCIATION_MAP = Maps.newHashMap();
	private static Map<String, String> fullClassNameMap = Maps.newHashMap();

	static {
		initFullClassNameMap();
		initSuperClassFieldMap();
		initOtherFieldMap();
	}
	
	public static String getFullClassNameMap(String key) {
		if (fullClassNameMap.isEmpty()) {
			initFullClassNameMap();
		}
		return fullClassNameMap.get(key);
	}

	private static void initFullClassNameMap() {
		String jsonStr = FileUtils.readFile4String("src/main/resource/other/fullClassNames.json");
		fullClassNameMap = gson.fromJson(jsonStr, new TypeToken<HashMap<String, String>>() {}.getType());
	}

	public static String putFullClassNameMap(String key, String value) {
		return fullClassNameMap.put(key, value);
	}
	
	public static final String MANY_TO_ONE = "ManyToOne";
	public static final String ONE_TO_MANY = "OneToMany";
	public static String getAssociationMap(String key) {
		if (ASSOCIATION_MAP.isEmpty()) {
			ASSOCIATION_MAP.put(ONE_TO_MANY, "@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)");
			ASSOCIATION_MAP.put(MANY_TO_ONE, "@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)");
		}
		return ASSOCIATION_MAP.get(key);
	}
	

	public static List<Field> getSuperClassFieldMap(String key) {
		if (superClassFieldMap.isEmpty()) {
			Constants.initSuperClassFieldMap();
		}
		return superClassFieldMap.get(key);
	}

	private static void initSuperClassFieldMap() {
		String jsonStr = FileUtils.readFile4String("src/main/resource/other/superClassFields.json");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
		Constants.initSuperClassFieldMap(jsonObject);
	}

	private static void initSuperClassFieldMap(JsonObject jsonObject) {
		initApplyInfoClassFieldMap(jsonObject);
		initApplyBookClassFieldMap(jsonObject);
		initProductSuperClassFieldMap(jsonObject);
		initProductAuthorSuperClassFieldMap(jsonObject);
		initProjectSuperClassFieldMap(jsonObject);
		initProjectMemberSuperClassFieldMap(jsonObject);

		initCheckBusinessEntitySuperClassFieldMap(jsonObject);
		initBizEntitySuperClassFieldMap(jsonObject);
	}

	private static void initApplyBookClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.ApplyBook.toString();
		superClassFieldMap.put(key, fromJson(jsonObject, key));
	}

	private static void initApplyInfoClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.ApplyInfo.toString();
		superClassFieldMap.put(key, fromJson(jsonObject, key));
	}
	
	/**
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static List<Field> fromJson(JsonObject jsonObject, String key) {
		return gson.fromJson(jsonObject.get(key).getAsJsonArray(), new TypeToken<List<Field>>() {}.getType());
	}

	private static void initBizEntitySuperClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.BizEntity.toString();
		superClassFieldMap.put(key, fromJson(jsonObject, key));
	}

	private static void initCheckBusinessEntitySuperClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.CheckBusinessEntity.toString();
		superClassFieldMap.put(key, fromJson(jsonObject, key));
	}

	private static void initProductAuthorSuperClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.ProductAuthor.toString();
		List<Field> fields = fromJson(jsonObject, key);
		for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
			Field field = iterator.next();
			if (!"V8.5.3".equals(DBUtils.getEadpDataType()) && "srScore".equals(field.getFieldId())) {
				iterator.remove();
			}
		}
		superClassFieldMap.put(key, fields);
	}

	private static void initProductSuperClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.Product.toString();
		List<Field> fields = fromJson(jsonObject, key);
		HashSet<String> set = Sets.newHashSet("subjectClassId", "subjectId", "schoolSign");
		for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
			Field field = iterator.next();
			if (!"V8.5.3".equals(DBUtils.getEadpDataType()) && set.contains(field.getFieldId())) {
				iterator.remove();
			}
		}
		superClassFieldMap.put(key, fields);
	}

	private static void initProjectMemberSuperClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.ProjectMember.toString();
		List<Field> fields = fromJson(jsonObject, key);
		for (Iterator<Field> iterator = fields.iterator(); iterator.hasNext();) {
			Field field = iterator.next();
			if (!"V8.5.3".equals(DBUtils.getEadpDataType()) && "srScore".equals(field.getFieldId())) {
				iterator.remove();
			}
		}
		superClassFieldMap.put(key, fields);
	}

	private static void initProjectSuperClassFieldMap(JsonObject jsonObject) {
		String key = SuperClassName.Project.toString();
		List<Field> fields = fromJson(jsonObject, key);
		superClassFieldMap.put(key, fields);
	}


	public static void main(String[] args) throws IOException {
		String jsonStr = FileUtils.readFile4String("src/main/resource/other/superClassFields.json");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
		JsonArray jsonArray = jsonObject.get(SuperClassName.ProjectMember.toString()).getAsJsonArray();
		
		Type type = new TypeToken<List<Field>>() {}.getType();
		List<Field> applyInfoClassFields = gson.fromJson(jsonArray, type);
		for (Field field : applyInfoClassFields) {
			if (field.isTranSient()) {
				continue;
			}
			field.setTableFieldId(StringUtils.lowerCamelCase2UnderScoreCase(field.getFieldId()));
		}
		System.out.println(applyInfoClassFields);
		String json = gson.toJson(applyInfoClassFields);
		System.out.println(json);

		
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		String json = gson.toJson(superClassFieldMap);
//		FileUtils.write("src/main/resource/other/superClassFields.json", json);
	}

	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @return
	 */
	public static List<Field> addSuperClassFields(String superClass) {
		return addSuperClassFields(Enum.valueOf(SuperClassName.class, superClass));
	}

	private static void initOtherFieldMap() {
		String jsonStr = FileUtils.readFile4String("src/main/resource/other/otherFields.json");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
		Constants.initOtherFieldMap(jsonObject);
	}
	private static void initOtherFieldMap(JsonObject jsonObject) {
		otherFieldMap.put(SuperClassName.Product.toString(), fromJson(jsonObject, SuperClassName.Product.toString()));
		otherFieldMap.put(SuperClassName.Project.toString(), fromJson(jsonObject, SuperClassName.Project.toString()));
	}
	public static List<Field> getOtherFieldMap(String key) {
		if (otherFieldMap.isEmpty()) {
			String jsonStr = FileUtils.readFile4String("src/main/resource/other/otherFields.json");
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
			Constants.initOtherFieldMap(jsonObject);
		}
		return otherFieldMap.get(key);
	}
	public static List<Field> getOtherFields(String entity, String joinColumn, String tableName) {
		List<Field> fields = getOtherFields(Enum.valueOf(SuperClassName.class, entity));
		if (fields == null) {
			return Lists.newArrayList();
		}
		joinColumn = StringUtils.lowerCamelCase2UnderScoreCase(joinColumn);
		for (Field field : fields) {
			field.getAppendSearch().setRelativeField(joinColumn);
			field.getAppendSearch().setValue(String.format(field.getAppendSearch().getValue(), joinColumn, tableName, joinColumn));
		}
		return fields;
	}

	private static List<Field> getOtherFields(SuperClassName superClass) {
		switch (superClass) {
			case Project: // 6
				return getOtherFieldMap(SuperClassName.Project.toString());
			case Product: // 4
				return getOtherFieldMap(SuperClassName.Product.toString());
			default: // 0
				break;
		}
		return Lists.newArrayList();
	}
	
	/**
	 * 继承父类需要添加的字段
	 * @param superClass
	 * @return
	 */
	private static List<Field> addSuperClassFields(SuperClassName superClass) {
		List<Field> fields = Lists.newArrayList();
		int step = -1;
		switch (superClass) {
		case ApplyInfo: // 8
			if (step == -1 || step == 8) {
				fields.addAll(Constants.getSuperClassFieldMap(SuperClassName.ApplyInfo.toString()));
				step = 1;
			}
		case ApplyBook: // 7
			if (step == -1 || step == 7) {
				fields.addAll(Constants.getSuperClassFieldMap(SuperClassName.ApplyBook.toString()));
				step = 2;
			}
		case Project: // 6
			if (step == -1 || step == 6) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.Project.toString()));
				step = 2;
			}
		case ProjectMember: // 5
			if (step == -1 || step == 5) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.ProjectMember.toString()));
				step = 0;
			}
		case Product: // 4
			if (step == -1 || step == 4) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.Product.toString()));
				step = 2;
			}
		case ProductAuthor: // 3
			if (step == -1 || step == 3) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.ProductAuthor.toString()));
				step = 0;
			}
		case CheckBusinessEntity: // 2
			if (step == -1 || step == 2) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.CheckBusinessEntity.toString()));
				step = 1;
			}
		case BusinessEntity:
		case BizEntity: // 1
			if (step == -1 || step == 1) {
				fields.addAll(getSuperClassFieldMap(SuperClassName.BizEntity.toString()));
			}
			break;
		default: // 0
			break;
		}
		return fields;
	}
}
