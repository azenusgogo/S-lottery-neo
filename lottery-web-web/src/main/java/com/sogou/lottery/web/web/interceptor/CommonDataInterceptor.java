package com.sogou.lottery.web.web.interceptor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.vo.game.Game;
import com.sogou.lottery.cache.business.service.GameCacheService;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.web.web.dto.index.GameDescDto;

/**
 * 基础拦截器，预放置所有页面需要的公共信息
 * 
 * @author huangtao
 */
public class CommonDataInterceptor implements HandlerInterceptor {
	
	@Resource(name = "gameCacheServiceImpl")
	private GameCacheService gameCacheService;
	
	public final static String MODEL_KEY_GAME_CONFIGS = "gameConfigs";
	public final static String MODEL_KEY_GAME_COMMON = "commonGames";
	public final static String MODEL_KEY_GAME_FREQUENCY = "highFrequencyGames";
	public final static String MODEL_KEY_GAME_TRADITIONAL = "traditionalSportGames";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	
		try {
			GameConfig gameConfig = new GameConfig();
			// 1.得到彩种详情
			List<Game> commonGames = gameCacheService.getCommonGameList();
			List<GameDescDto> commonGameDesc = Lists.newArrayList();
			for (Game game : commonGames) {
				
				GameDescDto gameDto = new GameDescDto();
				gameDto.setGame(game);
				gameDto.setDesc(gameConfig.getGameDesc(game.getGameId()));
				gameDto.setTag(gameConfig.getGameTag(game.getGameId()));
				commonGameDesc.add(gameDto);
			}
			
			// 1.高频彩得到彩种详情
			List<GameDescDto> frequencyGameDesc = Lists.newArrayList();
			List<Game> highFrequencyGames = gameCacheService.getHighFrequencyGameList();
			for (Game game : highFrequencyGames) {
				
				GameDescDto gameDto = new GameDescDto();
				gameDto.setGame(game);
				gameDto.setDesc(gameConfig.getGameDesc(game.getGameId()));
				gameDto.setTag(gameConfig.getGameTag(game.getGameId()));
				frequencyGameDesc.add(gameDto);
			}
			
			List<GameDescDto> traditionalGameDesc = Lists.newArrayList();
			List<Game> traditionalGames = gameCacheService.getTradFootballGameList();
			for (Game game : traditionalGames) {
				GameDescDto gameDto = new GameDescDto();
				gameDto.setGame(game);
				gameDto.setDesc(gameConfig.getGameDesc(game.getGameId()));
				gameDto.setTag(gameConfig.getGameTag(game.getGameId()));
				traditionalGameDesc.add(gameDto);
			}
			
			Collections.sort(commonGameDesc, new GameComparator(gameConfig));
			Collections.sort(frequencyGameDesc, new GameComparator(gameConfig));
			Collections.sort(traditionalGameDesc, new GameComparator(gameConfig));
			
			request.setAttribute(MODEL_KEY_GAME_CONFIGS, gameConfig);
			request.setAttribute(MODEL_KEY_GAME_COMMON, commonGameDesc);
			request.setAttribute(MODEL_KEY_GAME_FREQUENCY, frequencyGameDesc);
			request.setAttribute(MODEL_KEY_GAME_TRADITIONAL, traditionalGameDesc);
		} catch (Exception e) {
			LOG.runLog.fatal(e, e);
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
	}
	
	private class GameComparator implements Comparator<Object> {
		
		private GameConfig gameConfig;
		
		GameComparator(GameConfig gameConfig) {
		
			this.gameConfig = gameConfig;
		}
		
		@Override
		public int compare(Object oo1, Object oo2) {
		
			if (oo1 instanceof GameDescDto) {
				GameDescDto o1 = (GameDescDto) oo1;
				GameDescDto o2 = (GameDescDto) oo2;
				return gameConfig.getGameIndex(o1.getGameId()) - gameConfig.getGameIndex(o2.getGameId());
			} else {
				throw new UnsupportedOperationException(oo1.toString());
			}
		}
	}
	
	public static class GameConfig {
		
		Map<String,Object[]> configMap;
		
		GameConfig() {
		
			init();
		}
		
		private void init() {
		
			configMap = Maps.newHashMap();
			try {
				String config = SystemConfigs.get("INDEX_GAME_CONFIG", "ssq:2元可中1000万|热 dlt:3元可中2400万|加奖 qlc:简单易中大奖百万 qxc:2元可中500万 k3gx:好玩易中 k3jl:返奖率高 k3js:竞猜大小 f14:14场比赛猜胜负 f9:9场比赛猜胜负");
				String[] temp = StringUtils.split(config);
				for (int i = 0; i < temp.length; i++) {
					String[] tmp = StringUtils.split(temp[i], ":");
					if (tmp.length != 2) {
						continue;
					}
					String[] gameConfig = StringUtils.split(tmp[1], "|");
					Object[] obj = new Object[gameConfig.length + 1];
					// 次序
					obj[0] = i;
					System.arraycopy(gameConfig, 0, obj, 1, gameConfig.length);
					configMap.put(tmp[0], obj);
				}
			} catch (Exception e) {
			}
		}
		
		public int getGameIndex(String gameId) {
		
			Object[] obj = configMap.get(gameId);
			if (obj != null && obj.length >= 1) {
				return (int) obj[0];
			}
			return 0;
		}
		
		public String getGameDesc(String gameId) {
		
			Object[] obj = configMap.get(gameId);
			if (obj != null && obj.length >= 2) {
				return (String) obj[1];
			}
			return "";
		}
		
		public String getGameTag(String gameId) {
		
			Object[] obj = configMap.get(gameId);
			if (obj != null && obj.length >= 3) {
				return (String) obj[2];
			}
			return "";
		}
	}
}
