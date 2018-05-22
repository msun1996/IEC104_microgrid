package iec104;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dao.DevControlDao;
import dao.PVAnalogQuantityData1Dao;
import dao.PVAnalogQuantityData2Dao;
import dao.PVDigitalQuantityDataDao;
import iec104.util.FileUtils;
import model.PVAnalogQuantityData1;
import model.PVAnalogQuantityData2;
import model.PVDigitalQuantityData;
import net.sf.json.JSONObject;

public class Init {
	
    public static Properties typeIdProp = null;
    public static Properties causeProp = null;
    public static JSONObject devsinfo = null;
    public static JSONObject remoteSignal = null;
    public static JSONObject remoteMeasure = null;
	// 创建数据库数据对象集合
    public static List<PVDigitalQuantityData> PVD = new ArrayList<PVDigitalQuantityData>();
	public static List<PVAnalogQuantityData1> PVA1 = new ArrayList<PVAnalogQuantityData1>();
	public static List<PVAnalogQuantityData2> PVA2 = new ArrayList<PVAnalogQuantityData2>();
	
    public static void  start(){
        initBusinessData();
        initdb();
        init_db_time_do();
    }

    /* 配置文件解析 */
    public static void initBusinessData(){
        try {
        	// 传送类型及原因
            typeIdProp = FileUtils.loadPropFile("typeId.properties");
            causeProp = FileUtils.loadPropFile("cause.properties");
            // 设备信息
            devsinfo = FileUtils.loadJsonFile("dev.json");
            // 遥信、遥测
            remoteSignal = FileUtils.loadJsonFile("remote_signal.json");
            remoteMeasure = FileUtils.loadJsonFile("remote_measure.json");
            
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
    			JSONObject devobject = devsinfo.getJSONObject(dev_num);
    			Integer dev_type = devobject.getInt("DEV_TYPE");
        		try {
        			devControlDaoObject.addDev(dev_num, dev_type);
        			System.out.println(dev_num+"添加成功");
        		}catch (Exception e) {
        			devControlDaoObject.updateDev(dev_num, dev_type);
    				System.out.println(dev_num+"设备已存在,更新设备类型");
    			}
        		
        		if (dev_type == 1) {  //如果是光伏设备
        			// 光伏数字量对象
        			PVDigitalQuantityData pvd = new PVDigitalQuantityData();
        			pvd.setPv_num(dev_num);
        			PVD.add(pvd);
    				// 光伏逆变器模拟量创建对应设备模拟量对象
        			PVAnalogQuantityData1 pva1 = new PVAnalogQuantityData1();
        			pva1.setPv_num(dev_num);
        			PVA1.add(pva1);
        			PVAnalogQuantityData2 pva2 = new PVAnalogQuantityData2();
        			pva2.setPv_num(dev_num);
        			PVA2.add(pva2);
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
    public static void init_db_time_do() {
    	// 数字量数据定时插入
        Runnable runnable_db_pvd = new Runnable() {
			@Override
			public void run() {
				try {
					for(int i=0; i < PVD.size(); i++) {
						PVDigitalQuantityDataDao pvddao = new PVDigitalQuantityDataDao();
						if (pvddao.get(PVD.get(i).getPv_num()) == null) {
							pvddao.addPVDigitalQuantityData(PVD.get(i));
						}else {
							pvddao.updatePVDigitalQuantityData(PVD.get(i));
						}	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}; 
        // 模拟量数据1定时插入
        Runnable runnable_db_pva1 = new Runnable() {
			@Override
			public void run() {
				try {
					for(int i=0; i < PVA1.size(); i++) {
						PVAnalogQuantityData1Dao pva1dao = new PVAnalogQuantityData1Dao();
						pva1dao.addPVAnalogQuantityData1(PVA1.get(i));
						PVA1.get(i).Empty();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}; 
		// 模拟量数据2定时插入
        Runnable runnable_db_pva2 = new Runnable() {
			@Override
			public void run() {
				try {
					for(int i=0; i < PVA2.size(); i++) {
						PVAnalogQuantityData2Dao pva2dao = new PVAnalogQuantityData2Dao();
						pva2dao.addPVAnalogQuantityData2(PVA2.get(i));
						PVA2.get(i).Empty();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}; 
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable_db_pvd, 0, 1, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(runnable_db_pva1, 0, 10, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(runnable_db_pva2, 0, 10, TimeUnit.SECONDS);
    }
}

