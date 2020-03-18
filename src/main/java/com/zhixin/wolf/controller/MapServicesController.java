package com.zhixin.wolf.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhixin.wolf.entity.PersonLocationEntity;
import com.zhixin.wolf.entity.PersonRoundEntity;
import com.zhixin.wolf.entity.RoundInfoEntity;
import com.zhixin.wolf.entity.WSPersonLocationEntity;
import com.zhixin.wolf.entity.WSPersonRoundEntity;
import com.zhixin.wolf.entity.WSRoundInfoEntity;
import com.zhixin.wolf.util.CoordinatesUtils;
import com.zhixin.wolf.util.HttpClientUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class MapServicesController {
	private final Logger logger = LoggerFactory.getLogger(MapServicesController.class);

	@RequestMapping({ "/queryPersonLocations" })
	private List<PersonLocationEntity> queryPersonLocations() {
		List<PersonLocationEntity> result = new ArrayList<PersonLocationEntity>();
		List<WSPersonLocationEntity> wsData = queryWsPersonLocations();

		Map<Integer, List<WSPersonLocationEntity>> wsDataMap = new HashMap<Integer, List<WSPersonLocationEntity>>();
		for (WSPersonLocationEntity wsPersonLocationEntity : wsData) {
			if (wsDataMap.containsKey(wsPersonLocationEntity.getCurrentRoomId())) {
				((List<WSPersonLocationEntity>) wsDataMap.get(wsPersonLocationEntity.getCurrentRoomId())).add(wsPersonLocationEntity);
			} else {
				List<WSPersonLocationEntity> tempList = new ArrayList<WSPersonLocationEntity>();
				tempList.add(wsPersonLocationEntity);
				wsDataMap.put(wsPersonLocationEntity.getCurrentRoomId(), tempList);
			}
		}
		for (Integer key : wsDataMap.keySet()) {
			result.addAll(CoordinatesUtils.execWsPersonLacation((List<WSPersonLocationEntity>) wsDataMap.get(key), key));
		}
		return result;
	}

	private List<WSPersonLocationEntity> queryWsPersonLocations() {
		List<WSPersonLocationEntity> temp = new ArrayList<WSPersonLocationEntity>();
		if (CoordinatesUtils.isTest) {
			Integer[] rooms = CoordinatesUtils.getRoomIds();
			int num = new Random().nextInt(11) + 5;
			for (int i = 0; i < num; i++) {
				WSPersonLocationEntity wsple = new WSPersonLocationEntity();
				wsple.setSerialId(Integer.valueOf(new Random().nextInt(999) + 1));
				wsple.setPersonName("人员" + (new Random().nextInt(98) + 1));
				wsple.setInTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date(System.currentTimeMillis() - (new Random().nextInt(9999000) + 1000))));
				wsple.setCaseType("案由" + (new Random().nextInt(999) + 1));
				wsple.setCuffNo(new Random().nextInt(899999) + 100000 + "");
				wsple.setCurrentRoomId(rooms[new Random().nextInt(rooms.length)]);
				wsple.setPersonSex(Integer.valueOf(new Random().nextInt(2) + 1));
				temp.add(wsple);
			}
			
		}else {
			String resultJson = HttpClientUtil.query(CoordinatesUtils.currentUrl);
			this.logger.info("接口返回数据" + resultJson);
			temp = JSONArray.parseArray(resultJson, WSPersonLocationEntity.class);
		}
		return temp;
	}

	@RequestMapping({ "/queryPersonRoundDataBySerialId" })
	private PersonRoundEntity queryPersonRoundDataBySerialId(HttpServletRequest request) throws Exception {
		this.logger.info("获取到参数" + request.getParameter("serialId"));
		PersonRoundEntity result = new PersonRoundEntity();
		WSPersonRoundEntity wsData = queryWsPersonRounds(
				Integer.valueOf(Integer.parseInt(request.getParameter("serialId").toString())));
		if (wsData != null) {
			result.setPersonSex(wsData.getPersonSex());
			result.setCuffNo(wsData.getCuffNo());
			result.setPersonName(wsData.getPersonName());
			result.setRounds(wsRoundTo2DRound(wsData.getWsries()));
		}
		return result;
	}

	private WSPersonRoundEntity queryWsPersonRounds(Integer serialId) {
		WSPersonRoundEntity temp = new WSPersonRoundEntity();
		if (CoordinatesUtils.isTest) {
			temp.setPersonName("人员" + (new Random().nextInt(98) + 1));
			temp.setCuffNo(new Random().nextInt(899999) + 100000 + "");
			temp.setPersonSex(Integer.valueOf(new Random().nextInt(2) + 1));
			Integer[] rooms = CoordinatesUtils.getRoomIds();
			int num = new Random().nextInt(11) + 5;
			List<WSRoundInfoEntity> wsries = new ArrayList<WSRoundInfoEntity>();
			for (int i = 0; i < num; i++) {
				WSRoundInfoEntity wsrie = new WSRoundInfoEntity();
				wsrie.setRoomId(rooms[new Random().nextInt(rooms.length)]);
				wsrie.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date(System.currentTimeMillis() - (new Random().nextInt(9999000) + 1000))));
				wsrie.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date(System.currentTimeMillis() - (new Random().nextInt(9999000) + 1000))));
				wsries.add(wsrie);
			}
			temp.setWsries(wsries);
		} else {
			String resultJson = HttpClientUtil.query(CoordinatesUtils.roundUrl + serialId);
			this.logger.info("接口返回数据" + resultJson);
			temp = (WSPersonRoundEntity) JSONObject.parseObject(resultJson, WSPersonRoundEntity.class);
		}
		return temp;
	}

	private List<RoundInfoEntity> wsRoundTo2DRound(List<WSRoundInfoEntity> wsRoundInfoEntities) throws Exception {
		List<RoundInfoEntity> ries = new ArrayList<RoundInfoEntity>();
		for (WSRoundInfoEntity wsRoundInfoEntity : wsRoundInfoEntities) {
			if (CoordinatesUtils.getCoordinatesConfigByRoomId(wsRoundInfoEntity.getRoomId()) != null) {
				RoundInfoEntity roundInfoEntity = new RoundInfoEntity();
				roundInfoEntity.setStartTime(wsRoundInfoEntity.getStartTime());
				roundInfoEntity.setEndTime(wsRoundInfoEntity.getEndTime());
				roundInfoEntity.setRoomName(
						CoordinatesUtils.getCoordinatesConfigByRoomId(wsRoundInfoEntity.getRoomId()).getRoomName());
				roundInfoEntity.setX(
						CoordinatesUtils.getCoordinatesConfigByRoomId(wsRoundInfoEntity.getRoomId()).getCenterX());
				roundInfoEntity.setY(
						CoordinatesUtils.getCoordinatesConfigByRoomId(wsRoundInfoEntity.getRoomId()).getCenterY());
				ries.add(roundInfoEntity);
			} else {
				throw new Exception("未找到RoomId为[" + wsRoundInfoEntity.getRoomId() + "]的坐标参数配置");
			}
		}
		return ries;
	}
	
}
