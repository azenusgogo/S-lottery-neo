package com.sogou.lottery.web.web.datapartner;

import com.sogou.lottery.web.service.user.dto.CityDto;
import com.sogou.lottery.web.service.user.dto.DrawBankDto;
import com.sogou.lottery.web.service.user.dto.ProvinceDto;
import com.sogou.lottery.web.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by haojiaqi on 14-4-22.
 */
@Controller
@RequestMapping("/data/sport/7m")
public class MatchData7MController extends BaseController {

    /**
     * 赛事信息
     */
    protected final static String CTL_MATCH = "/match/{mId}/{type}";
    protected final static String FTL_MATCH = "data/7m/match";

    /**
     * 赛事信息页面返回属性key
     */
    protected final static String MATCH_URL = "matchUrl";
    protected final static String MATCH_ID = "mId";
    protected final static String MATCH_TYPE = "type";

    //分析页面
    protected final static String FENXI_PAGE_URL = "http://sogou.7m.cn/analyse.shtml";

    //亚盘页面
    protected final static String YAPAN_PAGE_URL = "http://sogou.7m.cn/asia.shtml";

    //欧赔页面
    protected final static String OUPEI_PAGE_URL = "http://sogou.7m.cn/1x2.shtml";


    /**
     * 描述：返回7m赛事信息调用url
     * 分析页面:http://sogou.7m.cn/analyse.shtml?id=XXX
     * 亚盘页面:http://sogou.7m.cn/asia.shtml?id=XXX
     * 欧赔页面:http://sogou.7m.cn/1x2.shtml?id=XXX
     * @param matchId
     * @param type 参数值分别为“1,2,3”,对应“析、亚、欧”的类型
     * @param model
     * @return
     */
    @RequestMapping(CTL_MATCH)
    public String match(@PathVariable("mId") String matchId,@PathVariable("type") Integer type,ModelMap model) {
        String url = null;
        switch (type){
            case 1:
                url = FENXI_PAGE_URL + "?id="+matchId;
                break;
            case 2:
                url = YAPAN_PAGE_URL + "?id="+matchId;
                break;
            case 3:
                url = OUPEI_PAGE_URL + "?id="+matchId;
                break;
        }
        model.put(MATCH_URL,url);
        model.put(MATCH_ID,matchId);
        model.put(MATCH_TYPE,type);
        return FTL_MATCH;
    }


}
