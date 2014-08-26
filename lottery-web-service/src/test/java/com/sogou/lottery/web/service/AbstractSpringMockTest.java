package com.sogou.lottery.web.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.sogou.lottery.base.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AbstractSpringMockTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	protected Log LOG = LogFactory.getLog(getClass());
	private AtomicInteger counter = new AtomicInteger();
	
	@Before
	public void before() {
	
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * 实例化一个测试Vo
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T newInstance(Class<T> clazz) {
	
		try {
			T obj = clazz.newInstance();
			PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(clazz);
			for (PropertyDescriptor p : props) {
				Method write = PropertyUtils.getWriteMethod(p);
				Class<?> cl = p.getPropertyType();
				if (write == null) {
					if (!Class.class.equals(cl)) {
						LOG.warn("Instance error, not found setter method for field [" + clazz.getSimpleName() + "].[" + p.getName() + "], checking pls");
					}
					continue;
				}
				Object arg = null;
				if (cl.equals(short.class) || cl.equals(Short.class)) {
					arg = (short) 1;
				} else if (cl.equals(int.class) || cl.equals(Integer.class)) {
					arg = (int) 1;
				} else if (cl.equals(long.class) || cl.equals(Long.class)) {
					if (p.getName().contains("amount") || p.getName().contains("Amount")) {
						arg = 1234567L;
					}
					if (p.getName().contains("id") || p.getName().contains("Id")) {
						arg = System.currentTimeMillis() + counter.addAndGet(1);
					} else {
						arg = 1L;
					}
				} else if (cl.equals(boolean.class) || cl.equals(Boolean.class)) {
					arg = true;
				} else if (cl.equals(float.class) || cl.equals(Float.class)) {
					arg = (float) 1.1;
				} else if (cl.equals(double.class) || cl.equals(Double.class)) {
					arg = (double) 1.1;
				} else if (cl.equals(String.class)) {
					if (p.getName().contains("id") || p.getName().contains("Id") || p.getName().contains("key") || p.getName().contains("Key")) {
						arg = System.currentTimeMillis() + "ID" + counter.addAndGet(1);
					} else {
						arg = "123456test232";
					}
				} else if (Date.class.isAssignableFrom(cl)) {
					if (p.getName().contains("update") || p.getName().contains("create")) {
						arg = new Timestamp(System.currentTimeMillis());
					} else {
						arg = new Timestamp(DateUtil.formatDate("2014-03-22").getTime());
					}
				}
				if (!cl.equals(Class.class)) {
					// System.out.println("---------- " + cl.getName());
					write.invoke(obj, arg);
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 比较两个list内对象是否完全相同，对象必须是实现了toString()方法才OK,不比较对象内的updateTime或者createTime字段
	 * 
	 * @param ll
	 * @param lr
	 * @throws Exception
	 */
	public <T> void assertListEquals(List<T> ll, List<T> lr) throws Exception {
	
		Assert.assertEquals(ll.size(), lr.size());
		if (ll.size() == 0) {
			throw new IllegalArgumentException();
		}
		String[] tabl = convertToString(ll);
		String[] tabr = convertToString(lr);
		Assert.assertArrayEquals(tabl, tabr);
	}
	
	@SuppressWarnings("unchecked")
	private <T> String[] convertToString(List<T> ll) throws Exception {
	
		List<String> left = Lists.newArrayList();
		for (T l : ll) {
			T ol = (T) BeanUtils.cloneBean(l);
			PropertyDescriptor desc;
			try {
				desc = PropertyUtils.getPropertyDescriptor(ol, "createTime");
			} catch (NoSuchMethodException e) {
				desc = null;
			}
			if (desc != null) {
				Method write = PropertyUtils.getWriteMethod(desc);
				write.invoke(ol, (Date) null);
			}
			desc = null;
			try {
				desc = PropertyUtils.getPropertyDescriptor(ol, "updateTime");
			} catch (NoSuchMethodException e) {
				desc = null;
			}
			if (desc != null) {
				Method write = PropertyUtils.getWriteMethod(desc);
				write.invoke(ol, (Date) null);
			}
			desc = null;
			left.add(ol.toString());
		}
		String[] tab = left.toArray(new String[0]);
		Arrays.sort(tab);
		return tab;
	}
	
	protected final Object unwrapProxy(Object bean) throws Exception {
	
		/*
		 * If the given object is a proxy, set the return value as the object
		 * being proxied, otherwise return the given object.
		 */
		if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
			Advised advised = (Advised) bean;
			bean = advised.getTargetSource().getTarget();
		}
		return bean;
	}
}
