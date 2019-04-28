package com.wy.utils;

import com.wy.entity.Traveler;
import com.wy.entity.User;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Created by WY
 * @Description:
 * @Date: 2019/4/28 21:13
 * @Since JDK 1.8
 */

public class BeanCopyUtil {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			User user = new User();
			user.setAddress("ts");
			user.setId(121);
			Traveler traveler = new Traveler();
			/*测试 BeanUtils 性能*/
//			BeanUtils.copyProperties(user,traveler);
			/*测试此工具类性能*/
			copyProperties(user,traveler);
//			copyProperties(user, traveler, "id", "address");
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * 复制相同属性名的属性
	 *
	 * @param sourceObj 源对象
	 * @param targetObj 目标对象
	 */
	public static void copyProperties(Object sourceObj, Object targetObj) {
		Objects.requireNonNull(sourceObj);
		Objects.requireNonNull(targetObj);
		Method[] methods = sourceObj.getClass().getMethods();
		Map<String, Object> valueMap = new HashMap<>();
		for (Method method : methods) {
			if (method.getName().contains("get")) {
				invokeReadMethod(sourceObj, method, valueMap);
			}
		}
		invokeWriteMethod(targetObj, valueMap);
	}

	/**
	 * 根据传入的参数名称复制对象属性
	 *
	 * @param sourceObj 源对象
	 * @param targetObj 目标对象
	 * @param params    参数集合
	 */
	public static void copyProperties(Object sourceObj, Object targetObj, String... params) {
		Objects.requireNonNull(sourceObj);
		Objects.requireNonNull(targetObj);
		assertParamsNotNull(params);
		Map<String, Object> valueMap = new HashMap<>();
		for (String param : params) {
			String readMethodName = getReadMethodName(param);
			try {
				Method method = sourceObj.getClass().getMethod(readMethodName, null);
				valueMap = invokeReadMethod(sourceObj, method, valueMap);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		invokeWriteMethod(targetObj, valueMap);
	}

	/**
	 * 执行 set 方法
	 *
	 * @param targetObj 执行对象
	 * @param valueMap  值集合
	 */
	private static void invokeWriteMethod(Object targetObj, Map<String, Object> valueMap) {
		Method[] methods = targetObj.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (valueMap.containsKey(methodName)) {
				try {
					method.invoke(targetObj, valueMap.get(methodName));
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("方法：" + methodName + " 参数类型不匹配！");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 执行 get 方法
	 *
	 * @param sourceObj 源对象
	 * @param method    get 方法对象
	 * @param valueMap  值集合
	 */

	private static Map<String, Object> invokeReadMethod(Object sourceObj, Method method, Map<String, Object> valueMap) {
		try {
			Object value = method.invoke(sourceObj);
			valueMap.put(method.getName().replace("get", "set"), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueMap;
	}

	/**
	 * 根据参数名获取 set 方法名
	 *
	 * @param param 参数名
	 */
	public static String getWriteMethodName(String param) {
		assertParamNotNull(param);
		return "set" + param.replace(param.substring(0, 1), param.substring(0, 1).toUpperCase());
	}

	/**
	 * 根据参数名获取 get 方法名
	 *
	 * @param param 参数名
	 */
	public static String getReadMethodName(String param) {
		assertParamNotNull(param);
		return "get" + param.replace(param.substring(0, 1), param.substring(0, 1).toUpperCase());
	}


	/**
	 * 判断参数是否为空
	 *
	 * @param params 参数名集合
	 */
	private static void assertParamsNotNull(String... params) {
		if (params == null || params.length <= 0) {
			throw new RuntimeException("参数集合不能为空");
		}
		for (String param : params) {
			assertParamNotNull(param);
		}
	}


	/**
	 * 判断参数是否为空
	 *
	 * @param param 参数名
	 */
	private static void assertParamNotNull(String param) {
		if (Objects.isNull(param) || "".equals(param)) {
			throw new RuntimeException("参数不能为空！");
		}
	}

}

