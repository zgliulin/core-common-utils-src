package javax.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 集合常用操作
 * @author Tanyongde
 *
 */
public class ListUtils {
	
	private ListUtils(){}
	
	/**
	 * 清空集合中空元素
	 *
	 *
	 *
	 * <br/>作者： 谭勇德
	 * 编写日期： Mar 16, 2011
	 * 版本 V1.0
	 * @param list    
	 * @throws
	 */
	public static void removeEmpty(List list){
		if(null != list){
			for (Iterator it = list.iterator();it.hasNext();){
				Object obj = it.next();   
				if (null == obj){   
					it.remove();
				}   
			}   
		}
	}
	
	/**
	 * 去除重复元素 
	 *
	 * @param args
	 * @return    
	 * @throws
	 */
	public  static List removeRepeat(List args){
		List<String> result = new ArrayList<String>();
		Set set = new HashSet();
		for (Object obj : args) {
			set.add(obj);
		}
		result.addAll(set);
		return result;
	}
	
	/**
	 * 根据指定字段去除重复元素 
	 *
	 * @param objList
	 * @param idName
	 * @return    
	 * @throws
	 */
	public  static List removeRepeat(List objList,String idName){
		List<Object> result = new ArrayList();
		Map<Object, Object> map = new HashMap<Object, Object>();
		String methodName = "get" + StringUtils.upperCaseFirstChar(idName);
		for (Object element : objList) {
			Object key;
			try {
				key = BeanUtils.invoke(element, methodName, new Class[0],
						new Object[0]);
				map.put(key, element);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		for (Object v : map.values()) {
			result.add(v);
		}
		return result;
	}
	
	/**
	 *
	 * 去除字符串数组中重复数据
	 *
	 * @param args
	 * @return    
	 * @throws
	 */
	public  static String [] removeRepeat(String [] args){
		Set<String> set = new HashSet<String>();
		for (String string : args) {
			set.add(string);
		}
		String [] result = new String [set.size()];
		set.toArray(result);
		return result;
	}
	
	
	
	/**
	 * 
	 * @param types
	 *            值
	 * @param appendLeft
	 *            在每个值的左边加上字符,例如单引号' 例如双引号" 例如百分号%
	 * @param appendRight
	 *            在每个值的右边加上字符,例如单引号' 例如双引号" 例如百分号%
	 */
	public  static String splitRiskCode(List types, String appendLeft,
			String appendRight) {
		StringBuffer sb = new StringBuffer();
		// String[] types = riskCode.split(",");
		for (int i = 0; i < types.size(); i++) {
			if (i > 0 && i < types.size()) {
				sb.append(",");
			}
			sb.append(appendLeft + types.get(i).toString() + appendRight);
		}
		return sb.toString();
	}

	/**
	 * 
	 * 分页,将一个List按分页指定的分页大小拆分
	 *
	 *
	 * <br/>作者： 谭勇德
	 * 编写日期： Dec 15, 2011
	 * 版本 V1.0
	 * @param datas
	 * @param pageSize
	 * @return    
	 * @throws
	 */
	public  static List<List> splitList(List datas,int pageSize){
		List result = new ArrayList();
		if(!(null == datas || 0 == datas.size())){
			if(0 != pageSize){
				int pageNo = (datas.size() % pageSize == 0) ? datas.size() / pageSize : (datas.size() / pageSize) + 1;
				for (int i = 1; i <= pageNo; i++) {
					result.add(pagination(datas,i,pageSize));
				}
			}else{
				result.add(new ArrayList().addAll(datas));
			}
		}
		return result;
	}
	
	/**
	 * 
	 *  分页,按页码和分页大小分页
	 *
	 * @param datas
	 * @param pageNo
	 * @param pageSize
	 * @return    
	 * @throws
	 */
	public  static List pagination(List datas, int pageNo, int pageSize) {
		List result = new ArrayList();
		if (null != datas) {
			int index = (pageNo * pageSize <= datas.size() ? pageNo * pageSize
					: datas.size());

			if (0 != datas.size()) {
				// 在 内存中 进行分页
				for (int j = (pageNo - 1) * pageSize; j < index; j++) {

					result.add(datas.get(j));

				}
			}
		}
		return result;
	}
	
	/**
	 * 连接数据中的元素，最后返回连接后的字符串
	 * @param list
	 * @param joinstr
	 * @return
	 */
	public static String join(List list,String joinstr){
		if(list == null || list.size() == 0){return "";}
		return list.toString().replaceFirst("\\[|", "").replaceFirst("\\]$", "").replaceAll("\\s", "").replaceAll(",",joinstr);
	}
}
