package com.sogou.lottery.util;

import com.netease.common.util.MD5Util;

public class MemcachedKeyUtil {
	
	/** -----------------Memcache Key Prefix-------------------start */
	private static final String SEPARATOR = "_";
	private static final String GOODS_KEY_PREFIX = "goods_";// 商品基本属性key前缀
	private static final String GOODS_SAMPLE_KEY_PREFIX = "goods_sample_";// 商品列表属性key前缀
	private static final String BRAND_KEY_PREFIX = "brand_";// 品牌属性key前缀
	// private static final String INDEX_AD_LIST_KEY = "index_ad_list_key";
	private static final String AD_LIST_KEY_PREFIX = "ad_list_";// 头图key前缀
	private static final String INDEX_CATEGORY_LIST_KEY = "index_category_list_key";
	private static final String INDEX_BRAND_LIST_KEY = "index_brand_list_key";
	private static final String INDEX_INFO_LIST_KEY = "index_info_list_key";
	private static final String INDEX_GOODS_INDEX_LIST_KEY = "index_goods_index_list_key";
	private static final String INDEX_TYPE_INDEX_LIST_KEY = "index_type_index_list_key";
	private static final String INDEX_NEW_HOT_BRAND_KEY = "index_new_20121225_hot_brand_key";// 新版首页热门品牌列表key
	private static final String INDEX_NEW_INDEX_TITLE_KEY = "index_new_20121225_index_title_key";// 新版首页热门品牌列表key
	private static final String INDEX_NEW_SLID_GOODS_KEY = "index_new_20121225_slid_goods_key";// 新版首页热门品牌列表key
	private static final String INDEX_NEW_ZONE_GOODS_KEY = "index_new_20121225_zone_goods_key";// 新版首页热门品牌列表key
	
	private static final String CATEGORY_ID_KEY_PREFIX = "category_id_";
	private static final String SUBCATEGORY_ID_KEY_PREFIX = "subcategory_id_";// 二级分类前缀
	private static final String TYPE_ID_KEY_PREFIX = "type_id_";// 三级分类前缀
	private static final String BRAND_ALL_LIST_KEY = "brand_all_list_key";// 全部品牌
	private static final String BRAND_DOMESTIC_RECOMMENT_LIST_KEY = "brand_domestic_recommend_list_key";// 推荐国产品牌
	private static final String BRAND_IMPORT_RECOMMENT_LIST_KEY = "brand_import_recommend_list_key";// 推荐国产品牌
	private static final String GEOGRAPHY_PROVINCE_LIST_KEY = "geography_province_list_key";// 省信息列表key
	private static final String GEOGRAPHY_PROVINCE_CONTAINS_CITY_LIST_PREFIX = "geography_province_contans_city_list_";// 省对应市信息列表的key前缀
	private static final String GEOGRAPHY_CITY_CONTAINS_DISTRICT_LIST_PREFIX = "geography_city_contans_district_list_";// 市对应区信息列表的key前缀
	private static final String GEOGRAPHY_CODE_PREFIX = "geography_code_";// code对应省市区信息的前缀
	private static final String YESTERDAY_HOT_SALES_GOODS_LIST_KEY = "yestreday_hot_sales_goods_id_list_key";// 昨日热销商品id
	private static final String ACTIVITY_HOT_SALES_GOODS_KEY = "activity_hot_sales_goods_id_key";// 总销量最高的活动商品id
	private static final String ACTIVITY_GOODS_PREFIX = "activity_goods_";// 活动商品前缀
	private static final String ERROR_ORDER_FORM_PREFIX = "error_order_form_";// 提交订单数据一致时保存订单页面商品信息的key
	
	private static final String LOGISTICS_DETAIL_PREFIX = "logistics_detail";// 物流信息key
	private static final String ZIXUN_INDEX_KEY = "zixun_index_key";// 资讯首页key
	private static final String HOT_QUERY_LIST_KEY = "hot_query_list_key";// 搜索热词key
	
	private static final String SIDEBAR_GOODS_DETAIL_FINAL_PURCHASE_PREFIX = "sidebar_goods_detail_final_purchase_";
	private static final String SIDEBAR_GOODS_DETAIL_ALSO_BUT_PREFIX = "sidebar_goods_detail_also_buy_";
	private static final String SIDEBAR_GOODS_DETAIL_SAME_EFFECTIVE_BRAND_SINGLE_GOODS_PREFIX = "sidebar_goods_detail_same_effective_brand_single_goods_";
	
	private static final String GOODS_BEST_PARTNER_PREFIX = "goods_best_partner_";// 商品最佳搭档的key前缀
	private static final String BEST_PARTNER_CONF_PREFIX = "best_partner_conf_";// 最佳搭档配置的key前缀
	
	private static final String MAIL_CLUB_RECENT_REDEEM_COUPON_LIST_KEY = "mail_club_recent_redeem_coupon_list_key";// 邮箱俱乐部落地页最近兑换用户列表key
	private static final String MAIL_CLUB_REDEEM_COUNT_KEY = "mail_club_redeem_count_key";// 邮箱俱乐部落地页已经兑换人数
	
	private static final String ACTIVITY_LIST_KEY = "activity_list_key";// 活动列表key
	private static final String ACTIVITY_LANDING_PAGE_GOODS_ID_LIST_PREFIX = "activity_landing_page_goods_id_list_";// 得到活动落地页面的活动商品id列表key
	private static final String ACTIVITY_SHOW_PREFIX = "activity_show_";// 活动落地页配置
	
	private static final String GOODS_COMBINE_PREFIX = "GOODS_COMBINATION_";// 商品组合套装的key前缀
	
	private static final String USER_PAID_INFO_KEY_PREFIX = "user_paid_info_";// 用户是否支付过的key
	
	private static final String DRAW_PRIZE_WIN_LIST_PREFIX = "draw_prize_win_list_"; // 抽奖模块中奖名单key
	private static final String DRAW_PRIZE_WEIBO_CHANCE_PREFIX = "draw_prize_weibo_chance_"; // 微博转发获得的抽奖机会
	private static final String DRAW_PRIZE_WEIBO_CHANCE_USED_PREFIX = "draw_prize_weibo_chance_used_"; // 微博转发获得的抽奖机会已使用
	private static final String DRAW_PRIZE_POINT_CHANCE_USED_PREFIX = "draw_prize_point_chance_"; // 积分兑换的抽奖机会
	private static final String DRAW_PRIZE_POINT_CHANCE_PREFIX = "draw_prize_point_chance_used_"; // 积分兑换的抽奖机会已使用
	
	public static final String TIANXIA3_DRAW_PRIZE_USER_DAILY_CHANCE_PREFIX = "tianxia3_draw_prize_user_daily_chance_";// 天下3抽奖用户抽奖机会
	public static final String TIANXIA3_USED_DRAW_PRIZE_CHANCE_PREFIX = "tianxia3_used_draw_prize_chance_";// 天下3抽奖用户已使用抽奖机会
	public static final String TIANXIA3_USER_WIN_PRIZE_PREFIX = "tianxia3_user_win_prize_"; // 天下3用户已抽中奖品
	
	private static final String DRAW_PRIZE_CHANCES_FIRST_BUY = "draw_prize_chances_first_buy";// 抽奖模块抽奖机会：第一次购买
	private static final String DRAW_PRIZE_CHANCES_DAILY = "";// 抽奖模块抽奖机会：24小时内一次
	private static final String DRAW_PRIZE_CHANCES_FIRST_LOGIN = "draw_prize_chances_first_login";
	
	private static final String CHECK_CODE_TIME_PREFIX = "check_code_time_";
	private static final String CHECK_CODE_PREFIX = "check_code_";
	private static final String USER_BIND_PREFIX = "user_bind_";
	
	private static final String MAY_ACTIVITY_TUAN_COUNT_PREFIX = "may_activity_tuan_count_";// 五月活动团购商品数量的key前缀
	
	/** -----------------Memcache Key Prefix-------------------start */
	
	/** -----------------Memcache Key-------------------start */
	// 得到Goods在Memcached中的key
	public static String getGoodsKey(long goodsId) {

		return GOODS_KEY_PREFIX + goodsId;
	}
	
	// 得到GoodsSample在Memcached中的key
	public static String getGoodsSampleKey(long goodsId) {

		return GOODS_SAMPLE_KEY_PREFIX + goodsId;
	}
	
	// 得到品牌在Memcached中的key
	public static String getBrandKey(long brandId) {

		return BRAND_KEY_PREFIX + brandId;
	}
	
	// --------------------首页 key 开始------------------
	
	// 得到首页广告头图列表key-----------------------------------尚未作废，等资讯功能上线之后作废
	// public static String getIndexAdListKey() {
	//
	// return INDEX_AD_LIST_KEY;
	// }
	
	// 得到首页商品分类列表key
	public static String getIndexCategoryListKey() {

		return INDEX_CATEGORY_LIST_KEY;
	}
	
	// 得到首页品牌列表key
	public static String getIndexBrandListKey() {

		return INDEX_BRAND_LIST_KEY;
	}
	
	// 得到首页资讯列表key
	public static String getIndexInfoListKey() {

		return INDEX_INFO_LIST_KEY;
	}
	
	// 得到首页各适用人群的商品列表key
	public static String getIndexGoodsIndexListKey() {

		return INDEX_GOODS_INDEX_LIST_KEY;
	}
	
	// 得到首页各适用人群的类别列表key
	public static String getIndexTypeIndexListKey() {

		return INDEX_TYPE_INDEX_LIST_KEY;
	}
	
	// 得到新版首页顶部滑动商品key
	public static String getIndexNew20121225SlidGoodsKey() {

		return INDEX_NEW_SLID_GOODS_KEY;
	}
	
	// 得到新版首页热门品牌key
	public static String getIndexNew20121225HotBrandKey() {

		return INDEX_NEW_HOT_BRAND_KEY;
	}
	
	// 得到新版首页标签云key
	public static String getIndexNew20121225IndexTitleKey() {

		return INDEX_NEW_INDEX_TITLE_KEY;
	}
	
	// 得到新版首页各分类商品key
	public static String getIndexNew20121225ZoneGoodsKey() {

		return INDEX_NEW_ZONE_GOODS_KEY;
	}
	
	// --------------------首页 key 结束------------------
	
	// --------------------分类页 key 开始------------------
	public static String getCategoryKey(long categoryId) {

		return CATEGORY_ID_KEY_PREFIX + categoryId;
	}
	
	// 得到二级分类id的key
	public static String getSubcategoryKey(long id) {

		return SUBCATEGORY_ID_KEY_PREFIX + id;
	}
	
	// 得到三级分类id的key
	public static String getTypeKey(long id) {

		return TYPE_ID_KEY_PREFIX + id;
	}
	
	// --------------------分类页 key 结束------------------
	// --------------------品牌专区 key 开始----------------
	
	// 得到推荐国产品牌
	public static String getLocalReommendBrandListKey() {

		return BRAND_DOMESTIC_RECOMMENT_LIST_KEY;
	}
	
	// 得到推荐进口品牌
	public static String getImportReommendBrandListKey() {

		return BRAND_IMPORT_RECOMMENT_LIST_KEY;
	}
	
	// 得到全部品牌
	public static String getAllBrandListKey() {

		return BRAND_ALL_LIST_KEY;
	}
	
	// -------------------品牌介绍页key 结束----------------
	// --------------------省市区信息key 开始----------------
	// 得到省信息列表的key
	public static String getProvinceListKey() {

		return GEOGRAPHY_PROVINCE_LIST_KEY;
	}
	
	// 得到某个省对应的市信息列表的key
	public static String getProvince2CityListKey(long provinceCode) {

		return GEOGRAPHY_PROVINCE_CONTAINS_CITY_LIST_PREFIX + provinceCode;
	}
	
	// 得到某个市对应的区信息列表的key
	public static String getCity2DistrictListKey(long cityCode) {

		return GEOGRAPHY_CITY_CONTAINS_DISTRICT_LIST_PREFIX + cityCode;
	}
	
	// 得到code对应的省市区信息的key
	public static String getGeographyInfo(long code, int type) {

		return GEOGRAPHY_CODE_PREFIX + "type_" + type + "_code_" + code;
	}
	
	// --------------------省市区信息 key 结束----------------
	
	public static String getYesterdayHotSalesGoodsIdList() {

		return YESTERDAY_HOT_SALES_GOODS_LIST_KEY;
	}
	
	public static String getActivityHotGoods() {

		return ACTIVITY_HOT_SALES_GOODS_KEY;
	}
	
	// 得到活动商品按照品牌和商品类型的key
	public static String getActivityGoodsKey(Long brandId, Integer goodsType) {

		return ACTIVITY_GOODS_PREFIX + "_" + brandId + "_" + goodsType;
	}
	
	// --------------------订单部分 key 开始----------------
	// 提交订单数据一致时保存订单页面商品信息的key
	public static String getErrorOrderFormKey(String sessionId) {

		return ERROR_ORDER_FORM_PREFIX + sessionId;
	}
	
	// --------------------订单部分 key 结束----------------
	
	// --------------------订单部分 key 开始----------------
	// 获取物流公司详情key
	public static String getLogisticsDetailKey(String cpName) {

		return LOGISTICS_DETAIL_PREFIX + MD5Util.get(cpName, "utf-8");
	}
	
	// --------------------订单部分 key 结束----------------
	
	// --------------------头图key 开始----------------
	// 得到头图列表的key
	public static String getAdListKey(int adType) {

		return AD_LIST_KEY_PREFIX + "_" + adType;
	}
	
	// --------------------头图key 结束----------------
	
	// --------------------资讯首页 key 开始------------------
	
	// 得到资讯首页内容列表key
	public static String getZixunIndexKey() {

		return ZIXUN_INDEX_KEY;
	}
	
	// --------------------资讯首页 key 结束----------------
	
	// --------------------搜索部分 key 开始----------------
	// 获取搜索热词key
	public static String getHotQueryKey() {

		return HOT_QUERY_LIST_KEY;
	}
	
	// --------------------搜索部分 key 结束----------------
	
	// -------------------侧边栏商品详情页key 开始----------------
	
	// 得到侧边栏商品详情页面用户最终购买key
	public static String getSidebarGoodsDetailUserFinalPurchaseKey(long goodsId) {

		return SIDEBAR_GOODS_DETAIL_FINAL_PURCHASE_PREFIX + goodsId;
	}
	
	// 得到侧边栏商品详情页用户也购买key
	public static String getSidebarGoodsDetailUserAlsoBuyKey(long goodsId) {

		return SIDEBAR_GOODS_DETAIL_ALSO_BUT_PREFIX + goodsId;
	}
	
	// 得到侧边栏某商品详情页热销品牌key
	public static String getSidebarGoodsDetailSameEffectiveBrandForSingleGoods(long goodsId) {

		return SIDEBAR_GOODS_DETAIL_SAME_EFFECTIVE_BRAND_SINGLE_GOODS_PREFIX + goodsId;
	}
	
	// -------------------侧边栏商品详情页key 结束----------------
	// -------------------商品详情页最佳搭档key 开始----------------
	
	// 得到商品最佳搭档key
	public static String getGoodsBestPartnerKey(long categoryId, long goodsId) {

		// --modified by tmgan on 2013.04.16--
		return GOODS_BEST_PARTNER_PREFIX + categoryId + SEPARATOR + goodsId;
	}
	
	// 得到最佳搭档配置key
	public static String getBestPartnerConfKey(long bestPartnerConfId) {

		return BEST_PARTNER_CONF_PREFIX + bestPartnerConfId;
	}
	
	// -------------------商品详情页最佳搭档key 结束----------------
	
	// --------------------邮箱俱乐部落地页key开始-------------------
	// 邮箱俱乐部落地页最近兑换用户列表key
	public static String getMailClubRedeemCouponListKey() {

		return MAIL_CLUB_RECENT_REDEEM_COUPON_LIST_KEY;
	}
	
	// 邮箱俱乐部落地页已经兑换人数
	public static final String getMailClubRedeemCountKey() {

		return MAIL_CLUB_REDEEM_COUNT_KEY;
	}
	
	// --------------------邮箱俱乐部落地页key结束-------------------
	// -------------------活动列表页key 开始----------------
	public static String getActivityListKey() {

		return ACTIVITY_LIST_KEY;
	}
	
	// 得到活动落地页面的活动商品id列表key
	public static String getActivityLandingPageGoodsList(long activityShowId) {

		return ACTIVITY_LANDING_PAGE_GOODS_ID_LIST_PREFIX + activityShowId;
	}
	
	// 得到活动配置key
	public static String getActivityShowKey(long activityShowId) {

		return ACTIVITY_SHOW_PREFIX + activityShowId;
	}
	
	// -------------------活动列表页key 结束----------------
	
	// -------------------商品详情页组合商品key 开始----------------
	// 得到商品组合套装的key
	public static String getGoodsCombineKey(long goodsId) {

		return GOODS_COMBINE_PREFIX + goodsId;
	}
	
	// -------------------商品详情页组合商品key 结束----------------
	
	// -------------------用户是否支付key 开始----------------
	public static String getUserPaidInfoKey(String accountId) {

		return USER_PAID_INFO_KEY_PREFIX + accountId;
	}
	
	// -------------------用户是否支付key 结束----------------
	
	// --------------------抽奖模块key开始--------------------------
	// 抽奖模块中奖名单key
	public static final String getDrawPrizeWinListKey(String linkId) {

		return DRAW_PRIZE_WIN_LIST_PREFIX + linkId;
	}
	
	// 抽奖模块 第一次登录key
	public static final String getDrawprizeChancesFirstLogin(String accountId) {

		return DRAW_PRIZE_CHANCES_FIRST_LOGIN + accountId;
	}
	
	// 抽奖模块 第一次购买key
	public static final String getDrawprizeChancesFirstBuy(String accountId) {

		return DRAW_PRIZE_CHANCES_FIRST_BUY + accountId;
	}
	
	// 抽奖模块 24小时一次的抽奖机会key
	public static final String getDrawprizeChancesDaily(String accountId) {

		return DRAW_PRIZE_CHANCES_DAILY + accountId;
	}
	
	// 微博转发抽奖机会
	public static final String getDrawPrizeWeiboChanceKey(String accountId, String yearMonth) {

		return DRAW_PRIZE_WEIBO_CHANCE_PREFIX + accountId + "_" + yearMonth;
	}
	
	// 微博转发抽奖机会已使用
	public static final String getDrawPrizeWeiboChanceUsedKey(String accountId, String yearMonth) {

		return DRAW_PRIZE_WEIBO_CHANCE_USED_PREFIX + accountId + "_" + yearMonth;
	}
	
	// 积分兑换换抽奖机会
	public static final String getDrawPrizePointChanceKey(String accountId, String date) {

		return DRAW_PRIZE_POINT_CHANCE_PREFIX + accountId + "_" + date;
	}
	
	// 积分兑换抽奖机会已使用
	public static final String getDrawPrizePointChanceUsedKey(String accountId, String date) {

		return DRAW_PRIZE_POINT_CHANCE_USED_PREFIX + accountId + "_" + date;
	}
	
	// 天下3抽奖用户抽奖机会
	public static final String getTianxia3UserChanceKey(String accountId, String date) {

		return TIANXIA3_DRAW_PRIZE_USER_DAILY_CHANCE_PREFIX + accountId + "_" + date;
	}
	
	// 天下3抽奖用户当天已使用抽奖机会
	public static final String getTianxia3UsedDrawPrizeChanceKey(String accountId, String date) {

		return TIANXIA3_USED_DRAW_PRIZE_CHANCE_PREFIX + accountId + "_" + date;
	}
	
	// 天下3抽奖用户已抽中奖品key
	public static final String getTianxia3UserWinPrizeKey(String accountId) {

		return TIANXIA3_USER_WIN_PRIZE_PREFIX + accountId;
	}
	
	// --------------------抽奖模块key结束--------------------------
	
	// --------------------------绑定手机获取验证码key---------------
	public static final String getCheckCodeSendTimeKey(String mobile) {

		return CHECK_CODE_TIME_PREFIX + mobile;
	}
	
	public static final String getCheckCodeKey(String mobile) {

		return CHECK_CODE_PREFIX + mobile;
	}
	
	public static String getAccountBindInfoKey(String accountId) {

		return USER_BIND_PREFIX + accountId;
	}
	
	public static String getMobileBindInfoKey(String mobile) {

		return USER_BIND_PREFIX + mobile;
	}
	
	// --------------------------绑定手机获取验证码key结束---------------
	
	// --------------------------五月活动团购商品次数的key---------------
	public static final String getMayActivityTuanGoodsCount(long goodsId) {

		return MAY_ACTIVITY_TUAN_COUNT_PREFIX + goodsId;
	}
	
	// --------------------------五月活动团购商品次数的key结束---------------
	
	/** -----------------Memcache Key-------------------end */
}
