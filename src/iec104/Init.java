package iec104;

import java.util.Properties;

import iec104.util.FileUtils;
import net.sf.json.JSONObject;

public class Init {

    public static Properties typeIdProp = null;
    public static Properties causeProp = null;

    public static void  start(){
        initBusinessData();
    }

    /* 配置文件解析 */
    public static void initBusinessData(){
        try {
        	// 传送类型及原因
            typeIdProp = FileUtils.loadPropFile("typeId.properties");
            causeProp = FileUtils.loadPropFile("cause.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

