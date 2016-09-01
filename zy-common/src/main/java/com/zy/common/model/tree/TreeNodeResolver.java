package com.zy.common.model.tree;

import java.util.function.Function;

@FunctionalInterface
public interface TreeNodeResolver<T> extends Function<T, TreeNode> {
	
}