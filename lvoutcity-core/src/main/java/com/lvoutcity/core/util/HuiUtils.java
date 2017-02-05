/**
 * 
 */
package com.lvoutcity.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @ClassName: EasyUiUtils 
 * @Description: 帮助类
 * @author James w.s
 * @date 2016年5月19日 下午5:30:36 
 *
 */
public class HuiUtils {

	/** @time 2013-8-20  下午03:41:35
	 * @param nodeList
	 * @return List<Node>
	 * @description 转换成树形list
	 * @since 1.0
	 */
	public static List<ZTree> toTreeList(List<ZTree> nodeList, String rootSuperId) {
		List<ZTree> nodes = new ArrayList<ZTree>();
		//String id = null;
		String superId = null;
		Map<String,ZTree> map = getMapNode(nodeList).get(0);
		for(ZTree node : nodeList){
			//id = node.getId();
			superId = node.getSuperId();
			if(map.containsKey(superId)){
				map.get(superId).getChildren().add(node);
			}
			else{
				if(LvoutcityUtils.isNotNull(rootSuperId) && superId.equals(rootSuperId)){
					nodes.add(node);
				}
				else if(LvoutcityUtils.isNull(rootSuperId)){
					nodes.add(node);
				}
				//map.put(id, node);
			}
		}
		return nodes;
	}
	
	/**
	 * @time 2013-8-21  下午04:35:19
	 * @param all
	 * @param checked
	 * @param rootSuperId
	 * @return List<Node>
	 * @description 
	 * @since 1.0
	 */
	public static List<ZTree> toTreeList(List<ZTree> all, List<ZTree> checked, String rootSuperId){
		List<ZTree> nodes = new ArrayList<ZTree>();
		//String id = null;
		String superId = null;
		Map<String,ZTree> map = getMapNode(all).get(0);
		for(ZTree node : all){
			//id = node.getId();
			if(checked.contains(node)){
				node.setChecked(true);
			}
			superId = node.getSuperId();
			if(map.containsKey(superId)){
				map.get(superId).getChildren().add(node);
			}
			else{
				if(LvoutcityUtils.isNotNull(rootSuperId) && superId.equals(rootSuperId)){
					nodes.add(node);
				}
				else if(LvoutcityUtils.isNull(rootSuperId)){
					nodes.add(node);
				}
				//map.put(id, node);
			}
		}
		return nodes;
	}
	
	/**
	 * @time 2013-10-23  下午03:07:55
	 * @param all
	 * @param checked
	 * @param rootSuperId
	 * @return List<Node>
	 * @description to tree
	 * @since 1.0
	 */
	public static List<ZTree> toTreeListPerm(List<ZTree> all, List<ZTree> checked, String rootSuperId){
		List<ZTree> nodes = new ArrayList<ZTree>();
		//String id = null;
		String superId = null;
		List<Map<String,ZTree>> list = getMapNode(all);
		Map<String,ZTree> map = list.get(0);
		Map<String,ZTree> superMap = list.get(1);
		for(ZTree node : all){
			//id = node.getId();
			if(checked.contains(node) && (node.isRoleMark()
					|| (!node.isRoleMark() && !superMap.containsKey(node.getId())))){
				node.setChecked(true);
			}
			superId = node.getSuperId();
			if(map.containsKey(superId)){
				map.get(superId).getChildren().add(node);
			}
			else{
				if(LvoutcityUtils.isNotNull(rootSuperId) && superId.equals(rootSuperId)){
					nodes.add(node);
				}
				else if(LvoutcityUtils.isNull(rootSuperId)){
					nodes.add(node);
				}
				//map.put(id, node);
			}
		}
		return nodes;
	}

	/** @time 2013-8-21  上午09:38:44
	 * @param nodes
	 * @return
	 * @return Map<String,Node>
	 * @throws 
	 * @description 
	 * @since 1.0
	 */
	private static List<Map<String, ZTree>> getMapNode(List<ZTree> nodes) {
		Map<String,ZTree> map = new HashMap<String, ZTree>();
		Map<String,ZTree> superMap = new HashMap<String, ZTree>();
		List<Map<String,ZTree>> list = new ArrayList<Map<String,ZTree>>();
		for(ZTree node : nodes){
			if(!map.containsKey(node.getId())){
				map.put(node.getId(), node);
				superMap.put(node.getSuperId(), node);
			}
		}
		list.add(map);
		list.add(superMap);
		return list;
	}

}
