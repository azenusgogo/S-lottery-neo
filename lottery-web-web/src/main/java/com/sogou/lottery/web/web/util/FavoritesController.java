package com.sogou.lottery.web.web.util;

import com.sogou.lottery.web.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 网站收藏夹
 */
@Controller
public class FavoritesController extends BaseController {

    /**
     * 保存桌面图标，采用get请求获取
     * @param response
     */
    @RequestMapping("/desktop/nav/save")
    public void genDesktopNavigation(HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode("搜狗彩票.url", "UTF-8"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            String content = "[{000214A0-0000-0000-C000-000000000046}]\n" +
                    "Prop3=19,2\n" +
                    "[InternetShortcut]\n" +
                    "BASEURL=http://cp.sogou.com/\n" +
                    "URL=http://cp.sogou.com/\n" +
                    "IconFile=http://cp.sogou.com/favicon.ico\n" +
                    "IDList=\n";
            response.getWriter().write(content);
            response.setHeader("Content-Length", String.valueOf(content.length()));
            response.flushBuffer();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
