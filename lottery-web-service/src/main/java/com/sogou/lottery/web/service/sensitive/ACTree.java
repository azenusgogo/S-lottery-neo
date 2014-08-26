package com.sogou.lottery.web.service.sensitive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * 树形AC自动机类，用来解决多模式匹配问题
 * 
 * @author lvzhenyu
 */
public class ACTree {
	
	private ACTreeNode root;
	
	public ACTree() {
	
		root = null;
	}
	
	public ACTree(String[] dataArray) {
	
		root = new ACTreeNode();
		bulidACTree(dataArray);
	}
	
	public ACTree(List<String> dataArray) {
	
		root = new ACTreeNode();
		bulidACTree(dataArray);
	}
	
	public ACTree(String patternFileName) {
	
		root = new ACTreeNode();
		bulidACTree(patternFileName);
	}
	
	// 进行多模式匹配，返回第一个匹配的字符串
	public String findFirstKeyWord(String str) {
	
		Vector<String> result = findAllKeyWords(str);
		if (result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}
	
	String getStringWithNode(ACTreeNode p) {
	
		if (p == root) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		while (p != root) {
			result.insert(0, new Character(p.getData()).toString());
			p = p.getFather();
		}
		return result.toString();
	}
	
	// 进行多模式匹配，返回所有的匹配的字符串
	public Vector<String> findAllKeyWords(String str) {
	
		// 保存结果的Vector
		Vector<String> result = new Vector<String>();
		ACTreeNode p = root, child = null;
		for (int i = 0; i < str.length(); ++i) {
			// 如果当前节点的子节点data与str[i]不同且当前非root节点，节点就一直向其失败指针上溯
			while ((child = p.searchChildData(str.charAt(i))) == null && p != root) {
				p = p.getFailurePointer();
			}
			// 在子节点中没有找到与str[i]相同的节点，则将p设为root，child设为p。
			if (child == null) {
				p = root;
				child = p;
			} else {// 否则（即找到了，或者p为root），则将p设为child，即匹配上了一个字符
				p = child;
			}
			// child向其失败指针上溯，直到child到达root或者child为一个词尾节点
			while (child != root && !child.isWordEnd()) {
				child = child.getFailurePointer();
				// System.out.println("切换");
			}
			if (child.isWordEnd()) {
				// System.out.println(getStringWithNode(child));
				result.add(getStringWithNode(child));
				// System.out.println(getStringWithNode(child));
			}
		}
		return result;
	}
	
	// 从模式（关键词）文件中读取出所有的模式，并初始化，即先把此树建为Trie树，然后再设置失败指针
	public void bulidACTree(String patternFileName) {
	
		setTrieTree(patternFileName);
		setAllNodesFailurePointers();
	}
	
	public void bulidACTree(List<String> dataArray) {
	
		setTrieTree(dataArray);
		setAllNodesFailurePointers();
	}
	
	public void bulidACTree(String[] dataArray) {
	
		setTrieTree(dataArray);
		setAllNodesFailurePointers();
	}
	
	private void setTrieTree(String[] dataArray) {
	
		String tempString = null;
		ACTreeNode p = null, q = null;
		
		// 将tempString插入ACTree树中
		for (int j = 0; j < dataArray.length; ++j) {
			tempString = dataArray[j];
			p = root;
			q = null;
			for (int i = 0; i < tempString.length(); ++i) {
				char c = tempString.charAt(i);
				
				// 如果能找到这个c，则返回其节点，为q
				if ((q = p.searchChildData(c)) != null) {
					p = q;// 把p设为q
					// System.out.print("已经有 ");
					// System.out.println(c);
					// 如果找不到c，则需要插入c
				} else {
					p = p.insertChild(new ACTreeNode(c));
					// System.out.print("插入 ");
					// System.out.println(c);
				}
				// 如果是最后一个char，则需要设置词尾标志
				if (i == tempString.length() - 1) {
					p.setWordEndTrue();
				}
			}
		}
	}
	
	private void setTrieTree(List<String> dataArray) {
	
		String tempString = null;
		ACTreeNode p = null, q = null;
		
		// 将tempString插入ACTree树中
		for (int j = 0; j < dataArray.size(); ++j) {
			tempString = dataArray.get(j);
			p = root;
			q = null;
			// 将tempString插入ACTree树中
			for (int i = 0; i < tempString.length(); ++i) {
				char c = tempString.charAt(i);
				
				// 如果能找到这个c，则返回其节点，为q
				if ((q = p.searchChildData(c)) != null) {
					p = q;// 把p设为q
					// System.out.print("已经有 ");
					// System.out.println(c);
					// 如果找不到c，则需要插入c
				} else {
					p = p.insertChild(new ACTreeNode(c));
					// System.out.print("插入 ");
					// System.out.println(c);
				}
				// 如果是最后一个char，则需要设置词尾标志
				if (i == tempString.length() - 1) {
					p.setWordEndTrue();
				}
			}
			
		}
	}
	
	// 从模式（关键词）文件中读取出所有的模式,然后设置为Trie树
	private void setTrieTree(String patternFileName) {
	
		File file = new File(patternFileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			ACTreeNode p = null, q = null;
			while ((tempString = reader.readLine()) != null) {
				p = root;
				q = null;
				// 将tempString插入ACTree树中
				for (int i = 0; i < tempString.length(); ++i) {
					char c = tempString.charAt(i);
					
					// 如果能找到这个c，则返回其节点，为q
					if ((q = p.searchChildData(c)) != null) {
						p = q;// 把p设为q
						// System.out.print("已经有 ");
						// System.out.println(c);
						// 如果找不到c，则需要插入c
					} else {
						p = p.insertChild(new ACTreeNode(c));
						// System.out.print("插入 ");
						// System.out.println(c);
					}
					// 如果是最后一个char，则需要设置词尾标志
					if (i == tempString.length() - 1) {
						p.setWordEndTrue();
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 设置整棵树的失败指针
	private void setAllNodesFailurePointers() {
	
		Queue<ACTreeNode> queue = new LinkedList<ACTreeNode>();
		root.setFailurePointer(root);
		queue.offer(root);
		ACTreeNode p = null, child = null, father = null, failurePointer = null;
		while (queue.isEmpty() == false) {
			p = queue.poll();
			if ((father = p.getFather()) != null) {
				failurePointer = father.getFailurePointer();
				child = failurePointer.searchChildData(p.getData());
				if (child == null || child == p) {
					/*
					 * System.out.print(p.getData());
					 * System.out.println(" failure是 root");
					 */
					p.setFailurePointer(root);
				} else {
					/*
					 * System.out.print(p.getData());
					 * System.out.print(" failure是 ");
					 * System.out.println(child.getData());
					 */
					p.setFailurePointer(child);
				}
			}
			for (child = p.getFirstChild(); child != null; child = child.getRightBrother()) {
				queue.offer(child);
			}
		}
	}
	
	// For test
	public void printACTree() {
	
		ACTreeNode p = null;
		for (p = root.getFirstChild(); p != null; p = p.getRightBrother()) {
			System.out.print(p.getData());
			System.out.print(" ");
			printACTree(p);
		}
	}
	
	public void printACTree(ACTreeNode pnode) {
	
		if (pnode == null) {
			return;
		} else {
			ACTreeNode p = null;
			
			for (p = pnode.getFirstChild(); p != null; p = p.getRightBrother()) {
				System.out.print(p.getData());
				System.out.print(" ");
				if (p.isWordEnd()) {
					System.out.println(getStringWithNode(p));
				}
				if (p.isLeaf()) {
					System.out.println(" ");
				} else {
					System.out.print(" ");
				}
				printACTree(p);
			}
		}
	}
}
