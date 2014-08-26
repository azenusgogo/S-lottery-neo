package com.sogou.lottery.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.patchca.background.BackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.filter.predefined.DiffuseRippleFilterFactory;
import org.patchca.filter.predefined.DoubleRippleFilterFactory;
import org.patchca.filter.predefined.WobbleRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.AbstractCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;

public class CaptchaUtil {
	
	private static Log LOG = LogFactory.getLog(CaptchaUtil.class);
	
	private static MyCaptchaService cs = null;
	private static ColorFactory cf = null;
	private static RandomWordFactory wf = null;
	// private static Random r = new Random();
	private static CurvesRippleFilterFactory crff = null;
	// private static MarbleRippleFilterFactory mrff = null;
	private static DoubleRippleFilterFactory drff = null;
	private static WobbleRippleFilterFactory wrff = null;
	private static DiffuseRippleFilterFactory dirff = null;
	
	private static void init() {
	
		if (!(cs != null && cf != null && wf != null && crff != null && drff != null && wrff != null && dirff != null)) {
			synchronized (CaptchaUtil.class) {
				if (!(cs != null && cf != null && wf != null && crff != null && drff != null && wrff != null && dirff != null)) {
					cs = new MyCaptchaService();
					cf = new SingleColorFactory(new Color(25, 60, 170));
					wf = new MyWordFactory();
					crff = new CurvesRippleFilterFactory(cs.getColorFactory());
					drff = new DoubleRippleFilterFactory();
					wrff = new WobbleRippleFilterFactory();
					dirff = new DiffuseRippleFilterFactory();
					// mrff = new MarbleRippleFilterFactory();
					cs.setWordFactory(wf);
					cs.setColorFactory(cf);
					cs.setWidth(120);
					cs.setHeight(50);
				}
			}
		}
	}
	
	/**
	 * @param os
	 *            response.getOutputStream()
	 * @return 验证码的內容
	 * @other response.setContentType("image/png"); response.setHeader("cache",
	 *        "no-cache");os.flush();os.close();
	 */
	public static String getCaptcha(OutputStream os) {
	
		init();
		// // 随机产生5种效果（filter）
		// switch (r.nextInt(5)) {
		// case 0:
		// cs.setFilterFactory(crff);
		// break;
		// case 1:
		// cs.setFilterFactory(mrff);
		// break;
		// case 2:
		// cs.setFilterFactory(drff);
		// break;
		// case 3:
		// cs.setFilterFactory(wrff);
		// break;
		// case 4:
		// cs.setFilterFactory(dirff);
		// break;
		// }
		// 暂时只用双波纹，比较简单
		cs.setFilterFactory(drff);
		LOG.debug("cs.filterFactory:" + cs.getFilterFactory().getClass());
		String patchca = null;
		try {
			patchca = EncoderHelper.getChallangeAndWriteImage(cs, "png", os);
		} catch (IOException e) {
			LOG.error(e, e);
		}
		return patchca;
	}
	
	private static class MyCaptchaService extends AbstractCaptchaService {
		
		public MyCaptchaService() {
		
			// 文本内容
			wordFactory = new MyWordFactory();
			// 字体
			fontFactory = new RandomFontFactory();
			// 效果
			textRenderer = new BestFitTextRenderer();
			// 背景
			backgroundFactory = new MyCustomBackgroundFactory();
			// 字体颜色
			colorFactory = new SingleColorFactory(new Color(25, 60, 170));
			// 样式(曲线波纹加干扰线)
			filterFactory = new CurvesRippleFilterFactory(colorFactory);
			// 图片长宽
			width = 100;
			height = 50;
		}
	}
	private static class MyWordFactory extends RandomWordFactory {
		
		public MyWordFactory() {
		
			// 文本范围和长度
			characters = "23456789";
			minLength = 4;
			maxLength = 4;
		}
	}
	
	private static class MyCustomBackgroundFactory implements BackgroundFactory {
		
		private Random random = new Random();
		
		public void fillBackground(BufferedImage image) {
		
			Graphics graphics = image.getGraphics();
			int imgWidth = image.getWidth();
			int imgHeight = image.getHeight();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, imgWidth, imgHeight);
			// 画100个噪点
			for (int i = 0; i < 100; ++i) {
				int rInt = random.nextInt(255);
				int gInt = random.nextInt(255);
				int bInt = random.nextInt(255);
				graphics.setColor(new Color(rInt, gInt, bInt));
				int xInt = random.nextInt(imgWidth - 3);
				int yInt = random.nextInt(imgHeight - 2);
				// 随机旋转角度
				int sAngleInt = random.nextInt(360);
				int eAngleInt = random.nextInt(360);
				// 随机大小
				int wInt = random.nextInt(6);
				int hInt = random.nextInt(6);
				graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);
				// 画5条干扰线
				if (i % 20 == 0) {
					int xInt2 = random.nextInt(imgWidth);
					int yInt2 = random.nextInt(imgHeight);
					graphics.drawLine(xInt, yInt, xInt2, yInt2);
				}
			}
			
		}
	}
}
