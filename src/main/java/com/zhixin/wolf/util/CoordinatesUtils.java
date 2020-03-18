package com.zhixin.wolf.util;

import com.zhixin.wolf.entity.CoorinatesConfigEntity;
import com.zhixin.wolf.entity.PersonLocationEntity;
import com.zhixin.wolf.entity.WSPersonLocationEntity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
@ConfigurationProperties(prefix = "properties")
@PropertySource("classpath:conf.properties")
public class CoordinatesUtils
  implements ServletContextAware, Runnable
{
  private static final Logger logger = LoggerFactory.getLogger(CoordinatesUtils.class);
  private static Map<Integer, CoorinatesConfigEntity> coorinatesMap = null;
  public static String roundUrl;
  public static String currentUrl;
  private static Integer imgW;
  private static Integer imgH;
  private static Integer imgI;
  public static boolean isTest;
  
  
  private void init()
    throws Exception
  {
    if (imgW == null) {
      imgW = Integer.valueOf(30);
    }
    if (imgH == null) {
      imgH = Integer.valueOf(30);
    }
    if (imgI == null) {
      imgI = Integer.valueOf(5);
    }
    coorinatesMap = new HashMap<Integer, CoorinatesConfigEntity>();
    ClassPathResource classPathResource = new ClassPathResource("coordinates");
    InputStream inputStream = classPathResource.getInputStream();
    BufferedReader in2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    String tempData = "";
    int i = 0;
    while ((tempData = in2.readLine()) != null)
    {
      i++;
      if (tempData.indexOf("#") != 0)
      {
        String[] tempStrArr = tempData.split("@");
        try
        {
          CoorinatesConfigEntity configEntity = new CoorinatesConfigEntity();
          configEntity.setRoomId(Integer.valueOf(Integer.parseInt(tempStrArr[0])));
          configEntity.setRoomName(tempStrArr[1]);
          configEntity.setLeftTopX(Integer.valueOf(Integer.parseInt(tempStrArr[2].split(",")[0])));
          configEntity.setLeftTopY(Integer.valueOf(Integer.parseInt(tempStrArr[2].split(",")[1])));
          configEntity.setRightTopX(Integer.valueOf(Integer.parseInt(tempStrArr[3].split(",")[0])));
          configEntity.setRightTopY(Integer.valueOf(Integer.parseInt(tempStrArr[3].split(",")[1])));
          configEntity.setLeftDownX(Integer.valueOf(Integer.parseInt(tempStrArr[4].split(",")[0])));
          configEntity.setLeftDownY(Integer.valueOf(Integer.parseInt(tempStrArr[4].split(",")[1])));
          configEntity.setRightDownX(Integer.valueOf(Integer.parseInt(tempStrArr[5].split(",")[0])));
          configEntity.setRightDownY(Integer.valueOf(Integer.parseInt(tempStrArr[5].split(",")[1])));
          configEntity.setCenterX(Integer.valueOf(Integer.parseInt(tempStrArr[6].split(",")[0])));
          configEntity.setCenterY(Integer.valueOf(Integer.parseInt(tempStrArr[6].split(",")[1])));
          configEntity.setMainNumR(Integer.valueOf((configEntity.getRightDownX().intValue() - configEntity.getLeftTopX().intValue() + imgI.intValue()) / (imgW.intValue() + imgI.intValue())));
          configEntity.setMainNumC(Integer.valueOf((configEntity.getLeftDownY().intValue() - configEntity.getLeftTopY().intValue() + imgI.intValue()) / (imgH.intValue() + imgI.intValue())));
          coorinatesMap.put(configEntity.getRoomId(), configEntity);
        }
        catch (Exception e)
        {
          logger.info("[error]定位坐标配置第" + i + "行数据有误【" + tempData + "】错误信息：" + e);
          throw new Exception("定位坐标配置第" + i + "行数据有误【" + tempData + "】错误信息：" + e);
        }
      }
    }
    logger.info("================获取到定位信息配置start=======================");
    for (Integer key : coorinatesMap.keySet()) {
      logger.info(((CoorinatesConfigEntity)coorinatesMap.get(key)).toString());
    }
    logger.info("================获取到定位配置信息end=========================");
  }
  
  public void setServletContext(ServletContext servletContext)
  {
    run();
  }
  
  public void run()
  {
    try
    {
      init();
      System.out.println("1:"+roundUrl+"\n"+"2:"+currentUrl+"\n"+"3:"+imgH+"\n"+"4:"+imgW+"\n"+"5:"+imgI);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public static Integer[] getRoomIds()
  {
    return (Integer[])coorinatesMap.keySet().toArray(new Integer[0]);
  }
  
  private static Integer[][] coordinateAlgorithm(Integer size, Integer roomId)
  {
    Integer[][] result = new Integer[size.intValue()][2];
    
    CoorinatesConfigEntity cce = (CoorinatesConfigEntity)coorinatesMap.get(roomId);
    if (cce != null)
    {
      if ((size.intValue() != 1) && (isRoomRule(cce)))
      {
        if (size.intValue() == 2)
        {
          if (cce.getMainNumR().intValue() > 1)
          {
            result[0][0] = Integer.valueOf(cce.getCenterX().intValue() - (imgW.intValue() + imgI.intValue()) / 2);
            result[0][1] = Integer.valueOf(cce.getCenterY().intValue() - imgH.intValue() / 2);
            result[1][0] = Integer.valueOf(cce.getCenterX().intValue() + (imgW.intValue() + imgI.intValue()) / 2);
            result[1][1] = Integer.valueOf(cce.getCenterY().intValue() - imgH.intValue() / 2);
          }
          else if (cce.getMainNumC().intValue() > 1)
          {
            result[0][0] = Integer.valueOf(cce.getCenterX().intValue() - imgW.intValue() / 2);
            result[0][1] = Integer.valueOf(cce.getCenterY().intValue() - (imgH.intValue() + imgI.intValue()) / 2);
            result[1][0] = Integer.valueOf(cce.getCenterX().intValue() - imgW.intValue() / 2);
            result[1][1] = Integer.valueOf(cce.getCenterY().intValue() + (imgH.intValue() + imgI.intValue()) / 2);
          }
          else
          {
            for (int i = 0; i < size.intValue(); i++)
            {
              result[i][0] = cce.getCenterX();
              result[i][1] = cce.getCenterX();
            }
          }
        }
        else if (size.intValue() == 3)
        {
          if ((cce.getMainNumR().intValue() > 1) && (cce.getMainNumC().intValue() > 1))
          {
            result[0][0] = cce.getCenterX();
            result[0][1] = Integer.valueOf(cce.getCenterY().intValue() - imgH.intValue());
            result[1][0] = Integer.valueOf(cce.getCenterX().intValue() - (imgW.intValue() + imgI.intValue()) / 2);
            result[1][1] = cce.getCenterY();
            result[2][0] = Integer.valueOf(cce.getCenterX().intValue() + (imgW.intValue() + imgI.intValue()) / 2);
            result[2][1] = cce.getCenterY();
          }
          else if ((cce.getMainNumC().intValue() < 2) && (cce.getMainNumR().intValue() > 2))
          {
            result[0][0] = Integer.valueOf(cce.getCenterX().intValue() - imgW.intValue());
            result[0][1] = cce.getCenterY();
            result[1][0] = cce.getCenterX();
            result[1][1] = cce.getCenterY();
            result[2][0] = Integer.valueOf(cce.getCenterX().intValue() + imgW.intValue());
            result[2][1] = cce.getCenterY();
          }
          else if ((cce.getMainNumC().intValue() > 2) && (cce.getMainNumR().intValue() < 2))
          {
            result[0][0] = cce.getCenterX();
            result[0][1] = Integer.valueOf(cce.getCenterY().intValue() - imgH.intValue());
            result[1][0] = cce.getCenterX();
            result[1][1] = cce.getCenterY();
            result[2][0] = cce.getCenterX();
            result[2][1] = Integer.valueOf(cce.getCenterY().intValue() + imgH.intValue());
          }
          else
          {
            for (int i = 0; i < size.intValue(); i++)
            {
              result[i][0] = cce.getCenterX();
              result[i][1] = cce.getCenterX();
            }
          }
        }
        else if (size.intValue() == 4)
        {
          if ((cce.getMainNumR().intValue() > 1) && (cce.getMainNumC().intValue() > 1))
          {
            result[0][0] = Integer.valueOf(cce.getCenterX().intValue() - (imgW.intValue() + imgI.intValue()) / 2);
            result[0][1] = Integer.valueOf(cce.getCenterY().intValue() - (imgH.intValue() + imgI.intValue()) / 2);
            result[1][0] = Integer.valueOf(cce.getCenterX().intValue() - (imgW.intValue() + imgI.intValue()) / 2);
            result[1][1] = Integer.valueOf(cce.getCenterY().intValue() + (imgH.intValue() + imgI.intValue()) / 2);
            result[2][0] = Integer.valueOf(cce.getCenterX().intValue() + (imgW.intValue() + imgI.intValue()) / 2);
            result[2][1] = Integer.valueOf(cce.getCenterY().intValue() - (imgH.intValue() + imgI.intValue()) / 2);
            result[3][0] = Integer.valueOf(cce.getCenterX().intValue() + (imgW.intValue() + imgI.intValue()) / 2);
            result[3][1] = Integer.valueOf(cce.getCenterY().intValue() + (imgH.intValue() + imgI.intValue()) / 2);
          }
          else if ((cce.getMainNumC().intValue() < 2) && (cce.getMainNumR().intValue() > 3))
          {
            result[0][0] = Integer.valueOf(cce.getCenterX().intValue() - (imgW.intValue() + imgI.intValue()) / 2);
            result[0][1] = cce.getCenterY();
            result[1][0] = Integer.valueOf(cce.getCenterX().intValue() + (imgW.intValue() + imgI.intValue()) / 2);
            result[1][1] = cce.getCenterY();
            result[2][0] = Integer.valueOf(cce.getCenterX().intValue() + ((imgW.intValue() + imgI.intValue()) / 2 + (imgW.intValue() + imgI.intValue())));
            result[2][1] = cce.getCenterY();
            result[3][0] = Integer.valueOf(cce.getCenterX().intValue() - ((imgW.intValue() + imgI.intValue()) / 2 + (imgW.intValue() + imgI.intValue())));
            result[3][1] = cce.getCenterY();
          }
          else if ((cce.getMainNumC().intValue() > 2) && (cce.getMainNumR().intValue() < 2))
          {
            result[0][0] = cce.getCenterX();
            result[0][1] = Integer.valueOf(cce.getCenterY().intValue() - (imgH.intValue() + imgI.intValue()) / 2);
            result[1][0] = cce.getCenterX();
            result[1][1] = Integer.valueOf(cce.getCenterY().intValue() + (imgH.intValue() + imgI.intValue()) / 2);
            result[2][0] = cce.getCenterX();
            result[2][1] = Integer.valueOf(cce.getCenterY().intValue() + ((imgH.intValue() + imgI.intValue()) / 2 + (imgH.intValue() + imgI.intValue())));
            result[3][0] = cce.getCenterX();
            result[3][1] = Integer.valueOf(cce.getCenterY().intValue() - ((imgH.intValue() + imgI.intValue()) / 2 + (imgH.intValue() + imgI.intValue())));
          }
          else
          {
            for (int i = 0; i < size.intValue(); i++)
            {
              result[i][0] = cce.getCenterX();
              result[i][1] = cce.getCenterX();
            }
          }
        }
        else
        {
          int rowNums = (size.intValue() - 1) / cce.getMainNumR().intValue() + 1;
          if (cce.getMainNumC().intValue() >= rowNums)
          {
            int tempSize = size.intValue();
            int bj = 0;
            for (int j = 0; j < rowNums; j++)
            {
              int kSize = cce.getMainNumR().intValue() > tempSize ? tempSize : cce.getMainNumR().intValue();
              for (int k = 0; k < kSize; k++)
              {
                result[bj][0] = Integer.valueOf(cce.getLeftTopX().intValue() + imgI.intValue() + (imgW.intValue() + imgI.intValue()) * k);
                result[bj][1] = Integer.valueOf(cce.getLeftTopY().intValue() + imgI.intValue() + (imgH.intValue() + imgI.intValue()) * j);
                tempSize--;
                bj++;
              }
            }
          }
          else
          {
            int tempSize = size.intValue();
            int bj = 0;
            int tempNum = (rowNums - 1) / cce.getMainNumC().intValue();
            int tempI = 10;
            for (int i = 0; i < tempNum; i++) {
              for (int j = 0; j < cce.getMainNumC().intValue(); j++) {
                for (int k = 0; k < cce.getMainNumR().intValue(); k++)
                {
                  result[bj][0] = Integer.valueOf(cce.getLeftTopX().intValue() + (imgW.intValue() + imgI.intValue()) * k + imgI.intValue() + i * tempI);
                  result[bj][1] = Integer.valueOf(cce.getLeftTopY().intValue() + (imgH.intValue() + imgI.intValue()) * j + imgI.intValue());
                  tempSize--;
                  bj++;
                }
              }
            }
            if (tempSize > 0)
            {
              int tempRowNums = (tempSize - 1) / cce.getMainNumR().intValue() + 1;
              for (int j = 0; j < tempRowNums; j++)
              {
                int kSize = cce.getMainNumR().intValue() > tempSize ? tempSize : cce.getMainNumR().intValue();
                for (int k = 0; k < kSize; k++)
                {
                  result[bj][0] = Integer.valueOf(cce.getLeftTopX().intValue() + (imgW.intValue() + imgI.intValue()) * k + imgI.intValue() + tempNum * tempI);
                  result[bj][1] = Integer.valueOf(cce.getLeftTopY().intValue() + (imgH.intValue() + imgI.intValue()) * j + imgI.intValue());
                  tempSize--;
                  bj++;
                }
              }
            }
          }
        }
      }
      else {
        for (int i = 0; i < size.intValue(); i++)
        {
          result[i][0] = cce.getCenterX();
          result[i][1] = cce.getCenterY();
        }
      }
    }
    else {
      try
      {
        throw new Exception("获取roomID为【" + roomId + "】坐标系错误");
      }
      catch (Exception e)
      {
        e.printStackTrace();
        logger.info(e.toString());
      }
    }
    return result;
  }
  
  private static boolean isRoomRule(CoorinatesConfigEntity cce)
  {
    int num = 10;
    boolean topFlag = (cce.getLeftTopX().intValue() < cce.getRightTopX().intValue()) && (Math.abs(cce.getLeftTopY().intValue() - cce.getRightTopY().intValue()) < num);
    boolean downFlag = (cce.getLeftDownX().intValue() < cce.getRightDownX().intValue()) && (Math.abs(cce.getLeftDownY().intValue() - cce.getRightDownY().intValue()) < num);
    boolean leftFlag = (cce.getLeftTopY().intValue() < cce.getLeftDownY().intValue()) && (Math.abs(cce.getLeftTopX().intValue() - cce.getLeftDownX().intValue()) < num);
    boolean rigthFlag = (cce.getRightTopY().intValue() < cce.getRightDownY().intValue()) && (Math.abs(cce.getRightTopX().intValue() - cce.getRightDownX().intValue()) < num);
    logger.info("结果：topFlag【" + topFlag + "】 downFlag【" + downFlag + "】  leftFlag【" + leftFlag + "】 rigthFlag【" + rigthFlag + "】");
    if ((topFlag) && (downFlag) && (leftFlag) && (rigthFlag)) {
      return true;
    }
    return false;
  }
  
  public static List<PersonLocationEntity> execWsPersonLacation(List<WSPersonLocationEntity> wsples, Integer roomId)
  {
    List<PersonLocationEntity> result = new ArrayList<PersonLocationEntity>();
    Integer[][] coordinatesArr = coordinateAlgorithm(Integer.valueOf(wsples.size()), roomId);
    for (int i = 0; i < wsples.size(); i++)
    {
      WSPersonLocationEntity wsple = (WSPersonLocationEntity)wsples.get(i);
      if (wsple != null)
      {
        PersonLocationEntity ple = new PersonLocationEntity();
        ple.setPersonSex(wsple.getPersonSex());
        ple.setCurrentRoomId(wsple.getCurrentRoomId());
        ple.setCuffNo(wsple.getCuffNo());
        ple.setCurrentRoomName(((CoorinatesConfigEntity)coorinatesMap.get(roomId)).getRoomName());
        ple.setInTimeStr(wsple.getInTimeStr());
        ple.setPersonName(wsple.getPersonName());
        ple.setSerialId(wsple.getSerialId());
        ple.setX(coordinatesArr[i][0]);
        ple.setY(coordinatesArr[i][1]);
        ple.setCaseType(wsple.getCaseType());
        result.add(ple);
      }
    }
    return result;
  }
  
  public static CoorinatesConfigEntity getCoordinatesConfigByRoomId(Integer roomId)
  {
    return (CoorinatesConfigEntity)coorinatesMap.get(roomId);
  }
	
	@Value("${roundUrl}")
	public void setRoundUrl(String roundUrl) {
		CoordinatesUtils.roundUrl = roundUrl;
	}
	
	@Value("${currentUrl}")
	public void setCurrentUrl(String currentUrl) {
		CoordinatesUtils.currentUrl = currentUrl;
	}
	
	@Value("${imgW}")
	public void setImgW(Integer imgW) {
		CoordinatesUtils.imgW = imgW;
	}
	
	@Value("${imgH}")
	public void setImgH(Integer imgH) {
		CoordinatesUtils.imgH = imgH;
	}
	
	@Value("${imgI}")
	public void setImgI(Integer imgI) {
		CoordinatesUtils.imgI = imgI;
	}
	
	@Value("${isTest}")
	public void setIsTest(boolean isTest) {
		CoordinatesUtils.isTest = isTest;
	}

}
