package com.sogou.lottery.web.service.sensitive;

/**
 * 树形AC自动机节点类
 * 
 * @author lvzhenyu
 */
public class ACTreeNode {
	
	private ACTreeNode father; // 父节点
	private ACTreeNode failurePointer; // 失败指针
	private ACTreeNode firstChild, lastChild; // 第一个孩子，最后一个孩子
	private ACTreeNode leftBrother, rightBrother;// 左兄弟，右兄弟
	private boolean isWordEnd;
	private char data;
	
	public ACTreeNode() {
	
		isWordEnd = false;
		father = null;
		failurePointer = null;
		firstChild = lastChild = null;
		leftBrother = rightBrother = null;
	}
	
	public ACTreeNode(char c) {
	
		data = c;
		isWordEnd = false;
		father = null;
		failurePointer = null;
		firstChild = lastChild = null;
		leftBrother = rightBrother = null;
	}
	
	// 在子节点的数据中寻找data，如果找到，返回子节点，否则返回null
	public ACTreeNode searchChildData(char c) {
	
		ACTreeNode p = firstChild;
		if (p != null) {
			if (c < p.data || c > lastChild.data) {
				return null;
			}
			while (p != null) {
				if (p.data == c) {
					return p;
				}
				p = p.rightBrother;
			}
			return null;
		} else {
			return null;
		}
		
	}
	
	// 判断是否是叶节点
	public boolean isLeaf() {
	
		return firstChild == null;
	}
	
	public void setWordEndTrue() {
	
		isWordEnd = true;
	}
	
	// 判断此节点是否是word的结束节点
	public boolean isWordEnd() {
	
		return isWordEnd;
	}
	
	// 设置数据
	public void setData(char c) {
	
		data = c;
	}
	
	public char getData() {
	
		return data;
	}
	
	// 设置父节点
	public void setFather(ACTreeNode p) {
	
		father = p;
	}
	
	public ACTreeNode getFather() {
	
		return father;
	}
	
	// 设置失败指针
	public void setFailurePointer(ACTreeNode p) {
	
		failurePointer = p;
	}
	
	public ACTreeNode getFailurePointer() {
	
		return failurePointer;
	}
	
	// 设置左兄弟
	public void setLeftBrother(ACTreeNode p) {
	
		leftBrother = p;
	}
	
	public ACTreeNode getLeftBrother() {
	
		return leftBrother;
	}
	
	// 设置右兄弟
	public void setRightBrother(ACTreeNode p) {
	
		rightBrother = p;
	}
	
	public ACTreeNode getRightBrother() {
	
		return rightBrother;
	}
	
	public ACTreeNode getFirstChild() {
	
		return firstChild;
	}
	
	public ACTreeNode getLastChild() {
	
		return lastChild;
	}
	
	/*
	 * 插入一个子节点，如果此子节点已经存在，则返回，如果不存在，则插入; 返回值为节点的对象。
	 */
	public ACTreeNode insertChild(ACTreeNode p) {
	
		// 如果当前子节点为空
		if (firstChild == null) {
			firstChild = lastChild = p;
			p.setFather(this);
			return firstChild;
		} else {
			// 从当前子节点开始往后遍历，直到找到一个比要插入的p的data小的，插入。
			// 目的是保证子节点按data从小到大排序
			ACTreeNode temp = firstChild;
			while (temp != null) {
				if (p.data < temp.data) {
					break;
				} else if (p.data == temp.data) {
					return temp;
				} else {
					temp = temp.rightBrother;
				}
			}
			if (temp == null) {// 插入的子节点是子节点中最大的，需要改变lastChild
				lastChild.rightBrother = p;
				p.leftBrother = lastChild;
				lastChild = p;
				p.setFather(this);
				return p;
			} else { // 在temp前插入p
				if (temp == firstChild) {
					firstChild = p;
					
					p.rightBrother = temp;
					temp.leftBrother = p;
				} else {
					temp.leftBrother.rightBrother = p;
					p.leftBrother = temp.leftBrother;
					
					temp.leftBrother = p;
					p.rightBrother = temp;
				}
				p.setFather(this);
				return p;
			}
		}
	}
}
