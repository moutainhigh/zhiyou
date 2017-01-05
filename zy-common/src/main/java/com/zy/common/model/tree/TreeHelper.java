package com.zy.common.model.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeHelper {
	
	public static <T> List<T> sortDeepth(Collection<T> entities, String parentId, TreeNodeResolver<T> treeNodeResolver) {
		List<T> result = new ArrayList<>();
		sortDeepth(entities, result, parentId, treeNodeResolver);
		return result;
	}

	private static <T> void sortDeepth(Collection<T> entities, Collection<T> result, String parentId, TreeNodeResolver<T> treeNodeResolver) {
		for (T entity : entities) {
			TreeNode treeNodeModel = treeNodeResolver.apply(entity);
			String treeNodeId = treeNodeModel.getId();
			String treeNodeParentId = treeNodeModel.getParentId();
			if (parentId == null && treeNodeParentId == null || treeNodeParentId != null && treeNodeParentId.equals(parentId)) {
				result.add(entity);
				for (T child : entities) {
					TreeNode childTreeNodeModel = treeNodeResolver.apply(child);
					String childTreeNodeId = childTreeNodeModel.getId();
					String childTreeNodeParentId = childTreeNodeModel.getParentId();

					if (childTreeNodeId == null && childTreeNodeParentId == null || childTreeNodeParentId != null && childTreeNodeParentId.equals(treeNodeId)) {
						sortDeepth(entities, result, treeNodeId, treeNodeResolver);
						break;
					}
				}
			}
		}
	}

	public static <T> List<T> sortBreadth(Collection<T> entities, String parentId, TreeNodeResolver<T> treeNodeResolver) {
		List<T> result = new ArrayList<>();
		sortBreadth(entities, result, parentId, treeNodeResolver);
		return result;
	}

	private static <T> void sortBreadth(Collection<T> entities, Collection<T> result, String parentId, TreeNodeResolver<T> treeNodeResolver) {
		List<T> plist = new ArrayList<>();
		for (T entity : entities) {
			TreeNode treeNodeModel = treeNodeResolver.apply(entity);
			String treeNodeParentId = treeNodeModel.getParentId();
			if (parentId == null && treeNodeParentId == null || treeNodeParentId != null && treeNodeParentId.equals(parentId)) {
				result.add(entity);
				plist.add(entity);
			}
		}
		for (T p : plist) {
			sortBreadth(entities, result, treeNodeResolver.apply(p).getId(), treeNodeResolver);
		}
	}

	public static <T> List<T> sortBreadth2(Collection<T> entities, String parentId, TreeNodeResolver<T> treeNodeResolver) {
		List<T> result = new ArrayList<>();
		Map<String, List<T>> childrenMap = entities.stream().collect(Collectors.groupingBy(v -> treeNodeResolver.apply(v).getParentId() == null ? "DEFAULT_NULL_KEY" : treeNodeResolver.apply(v).getParentId()));
		sortBreadth2(childrenMap, result, parentId, treeNodeResolver);
		return result;
	}


	private static <T> void sortBreadth2(Map<String, List<T>> childrenMap, Collection<T> result, String parentId, TreeNodeResolver<T> treeNodeResolver) {
		parentId = parentId == null ? "DEFAULT_NULL_KEY" : parentId;
		List<T> plist = childrenMap.get(parentId);
		if (plist == null) {
			return;
		}
		result.addAll(plist);
		for (T p : plist) {
			sortBreadth2(childrenMap, result, treeNodeResolver.apply(p).getId(), treeNodeResolver);
		}
	}

}
