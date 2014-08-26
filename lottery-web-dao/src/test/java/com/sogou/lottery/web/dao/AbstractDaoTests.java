/**
 * 
 */
package com.sogou.lottery.web.dao;

import java.beans.PropertyDescriptor;
import java.io.Reader;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author liaoxu
 */
public abstract class AbstractDaoTests {
	
	protected Log LOG = LogFactory.getLog(getClass());
	protected static SqlSessionFactory sqlSessionFactory;
	protected SqlSession sqlSession;
	private AtomicInteger counter = new AtomicInteger();
	
	@BeforeClass
	public static void setUp() throws Exception {
	
		// create a SqlSessionFactory
		Reader reader = Resources.getResourceAsReader("mybatis-web-dao-test.xml");
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		reader.close();
		
		// populate in-memory database
		// SqlSession session = sqlSessionFactory.openSession();
		// Connection conn = session.getConnection();
		// reader =
		// Resources.getResourceAsReader("config/mybatis/files/hqlbd.sql");
		// ScriptRunner runner = new ScriptRunner(conn);
		// runner.setLogWriter(null);
		// runner.runScript(reader);
		// reader.close();
		// session.close();
	}
	
	@Before
	public void getSqlSession() {
	
		sqlSession = sqlSessionFactory.openSession();
	}
	
	@After
	public void close() {
	
		sqlSession.close();
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
				if (cl.equals(short.class) || cl.equals(int.class) || cl.equals(Integer.class) || cl.equals(Short.class)) {
					arg = 1;
				} else if (cl.equals(long.class) || cl.equals(Long.class)) {
					if (p.getName().contains("amount") || p.getName().contains("Amount")) {
						arg = 1234567L;
					}
					if (p.getName().contains("id") || p.getName().contains("Id")) {
						arg = System.currentTimeMillis() + counter.addAndGet(1);
					} else {
						arg = 1L;
					}
				} else if (cl.equals(double.class) || cl.equals(float.class) || cl.equals(Double.class) || cl.equals(Float.class)) {
					arg = 1.1;
				} else if (cl.equals(String.class)) {
					if (p.getName().contains("id") || p.getName().contains("Id") || p.getName().contains("key") || p.getName().contains("Key")) {
						arg = System.currentTimeMillis() + "ID" + counter.addAndGet(1);
					} else {
						arg = "123456test232";
					}
				} else if (Date.class.isAssignableFrom(cl)) {
					arg = new Timestamp(System.currentTimeMillis());
				}
				if (!cl.equals(Class.class)) {
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
	
		List<String> left = new ArrayList<>();
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
}
