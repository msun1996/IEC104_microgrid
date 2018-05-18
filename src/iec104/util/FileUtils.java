package iec104.util;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/* @Description 加载property配置文件 */
public class FileUtils {
    public static Properties loadPropFile(String filePath) throws Exception{
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        InputStreamReader isr = new InputStreamReader(is, "GBK");
        Properties properties = new Properties();
        try {
            properties.load(isr);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            is.close();
            isr.close();
        }
        return properties;
    }

    public static JSONObject loadJsonFile(String filePath) throws Exception{
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer jsonStrBuff = new StringBuffer();
        String brStr = null;
        while ((brStr =br.readLine()) != null){
            jsonStrBuff.append(brStr);
        }
        return JSONObject.fromObject(jsonStrBuff.toString());
    }
}
