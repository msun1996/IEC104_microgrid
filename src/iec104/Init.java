package iec104;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.mchange.v2.c3p0.impl.DbAuth;

import dao.DevControlDao;
import iec104.util.FileUtils;
import net.sf.json.JSONObject;

public class Init {

    public static Properties typeIdProp = null;
    public static Properties causeProp = null;
    public static JSONObject devsinfo = null;
    public static void  start(){
        initBusinessData();
        initdb();
    }

    /* 配置文件解析 */
    public static void initBusinessData(){
        try {
        	// 传送类型及原因
            typeIdProp = FileUtils.loadPropFile("typeId.properties");
            causeProp = FileUtils.loadPropFile("cause.properties");
            // 设备信息
            devsinfo = FileUtils.loadJsonFile("dev.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* 数据库初始化 */
    public static void initdb(){
    	try {
        	// 创建数据库设备信息表操作对象
        	DevControlDao devControlDaoObject = new DevControlDao();
        	// dev.json中录入的设备(根据json文件中的设备去添加、删除数据库中设备)
        	List<String> devs_real = new ArrayList<String>();
        	Iterator devs = devsinfo.keys();
        	while (devs.hasNext()) {
        		String dev_num = devs.next().toString();
        		devs_real.add(dev_num);
        		try {
        			JSONObject devobject = devsinfo.getJSONObject(dev_num);
        			Integer dev_type = devobject.getInt("DEV_TYPE");
        			devControlDaoObject.addDev(dev_num, dev_type);
        		}catch (Exception e) {
    				System.out.println(dev_num+"设备已存在");
    			}
    		}
        	// 获取数据库所有设备,删除json中不存在设备
        	List<String> devs_db = devControlDaoObject.query();
        	for(int i=0; i < devs_db.size(); i++) {
        		String dev_num = devs_db.get(i);
        		if(!devs_real.contains(dev_num)) {
        			devControlDaoObject.delDev(dev_num);
        			System.out.println("删除设备"+dev_num);
        		}
        	}
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	
    	
    }
}

