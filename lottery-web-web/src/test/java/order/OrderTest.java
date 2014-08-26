package order;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sogou.lottery.base.util.HttpPoolUtil;

public class OrderTest {
	
	public String url = "http://localhost/ajax/login/order/bet.html";
	private final static String ENCODE = "UTF-8";
	
	@Test
	public void test() {
	
		fail("Not yet implemented");
	}
	
	@Test
	public void testUser() {
	
		Map<String,String> params = new HashMap<>();
		params.put("gameid", "k3gx");
		params.put("periodNo ", "2014038");
		params.put("price", "2");
		params.put("rawBetNumbers", "AAA_111");
		params.put("sourceType", "1");
		
		String response = HttpPoolUtil.sendPostHttpRequestStatic(url, params, null, ENCODE, 3000, 3000, ENCODE);
		
		// JSONObject obj = JSONObject.fromObject(response);
		// String retcode = obj.getString("retcode");
		//
		// assertEquals(9999996, obj.get("retcode"));
		// System.out.println(obj.get("retcode"));
		
	}
	
}
