package iec104;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dao.DevControlDao;
import iec104.IeShortFloat;
import iec104.IeSinglePointWithQuality;

import iec104.util.ChangeUtils;


public class Client {
	// 定义的是本地远程命令发送帧的接收和发送序号
	public static int receiveSeqNum = 0; //接收序号
	public static int sendSeqNum = 0; // 发送序号，每发送一个后需+1
	public static void main(String[] args) {
		// 记录存储遥控、遥调实时值
		Map<String, Integer> remoteControlValues = new HashMap<String,Integer>();
		Map<String, Double> remoteAdjustVlaues = new HashMap<String,Double>();
		try {
            //系统初始化
            Init.start();
			// 建立对服务端主机的Socket连接
			Socket socket = new Socket("127.0.0.1", 2404);
			// 由Socket对象得到输出流，并构造PrintWriter对象
			OutputStream os = socket.getOutputStream();
			// 启动链路
			os.write(ChangeUtils.hexStringToBytes("680407000000"));
			// 发送总召唤命令
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        byte[] recNum = new byte[2];
                        recNum[0] = (byte) (receiveSeqNum << 1);
                        recNum[1] = (byte) (receiveSeqNum >> 7);
                        String recStr = ChangeUtils.toHexString(recNum);
                        byte[] sendNum = new byte[2];
                        sendNum[0] = (byte) (sendSeqNum << 1);
                        sendNum[1] = (byte) (sendSeqNum >> 7);
                        sendSeqNum += 1;
                        String sendStr = ChangeUtils.toHexString(sendNum);
                        os.write(ChangeUtils.hexStringToBytes("680E"+sendStr+recStr+"64010600010000000014"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ScheduledExecutorService service_A = Executors.newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service_A.scheduleAtFixedRate(runnable, 2, 60, TimeUnit.SECONDS);
            
        	// 设备信息定时获取存入设备类，并进行处理发送（遥测、遥信数据）
            Runnable runnable_db_send = new Runnable() {
    			@Override
    			public void run() {
    				try {
    					// 遥控信息(单点遥控)
    					Iterator address_control = Init.remoteControl.keys();
    					while (address_control.hasNext()) {
							String address = address_control.next().toString();
							String num = Init.remoteControl.getJSONObject(address).getString("num");
							String field = Init.remoteControl.getJSONObject(address).getString("field");
							DevControlDao devDao = new DevControlDao();
							if (!num.equals("") && !field.equals("")) {
								Integer value = devDao.getint(num, field);
								if(remoteControlValues.containsKey(address) && value == remoteControlValues.get(address)) {
									//System.out.println("状态不变");
								} else {
									remoteControlValues.put(address, value);
									System.out.println("信息体地址：" + address + "信息体值："+ value);
									// 起始位
									String start = "68";
									// 长度
									Integer len = 14;
									byte[] lenNum = new byte[1];
									lenNum[0] = (byte) (len >> 0);
									String lenStr = ChangeUtils.toHexString(lenNum);
			                        // 发送序号
			                        byte[] sendNum = new byte[2];
			                        sendNum[0] = (byte) (sendSeqNum << 1);
			                        sendNum[1] = (byte) (sendSeqNum >> 7);
			                        sendSeqNum += 1;
			                        String sendStr = ChangeUtils.toHexString(sendNum);
									// 接收序号
			                        byte[] recNum = new byte[2];
			                        recNum[0] = (byte) (receiveSeqNum << 1);
			                        recNum[1] = (byte) (receiveSeqNum >> 7);
			                        String recStr = ChangeUtils.toHexString(recNum);
			                        // 类型标识
			                        String typeIdStr = "2d";
			                        // 可变结构限定词
			                        String vsqStr = "01";
			                        // 传输原因
			                        String causeOfTransmissionStr = "0600";
			                        // 公共地址
			                        String commAddressStr = "0100";
			                        // 信息体地址
			                        Integer addr = Integer.parseInt(address);
			                        byte[] addrNum = new byte[3];
			                        addrNum[0] = (byte) (addr >> 0) ;
			                        addrNum[1] = (byte) (addr >> 8);
			                        addrNum[2] = (byte) (addr >> 16);
			                        String addrStr = ChangeUtils.toHexString(addrNum);
			                        // 信息值
			                        byte[] valueNum = new byte[1];
			                        valueNum[0] = (byte) (value >> 0);
			                        String valuestr = ChangeUtils.toHexString(valueNum);
			                        os.write(ChangeUtils.hexStringToBytes(start+lenStr+sendStr+recStr+
			                        		typeIdStr+vsqStr+causeOfTransmissionStr+commAddressStr+
			                        		addrStr+valuestr));
			                        System.out.println("发送遥控命令帧："+ start+lenStr+sendStr+recStr+
			                        		typeIdStr+vsqStr+causeOfTransmissionStr+commAddressStr+
			                        		addrStr+valuestr);
			                        Thread.sleep(200);
								}
							}
						}
    					// 遥调 (定值、标度化值)
    					Iterator address_ajust = Init.remoteAdjust.keys();
    					while (address_ajust.hasNext()) {
							String address = address_ajust.next().toString();
							String num = Init.remoteAdjust.getJSONObject(address).getString("num");
							String field = Init.remoteAdjust.getJSONObject(address).getString("field");
							DevControlDao devDao = new DevControlDao();
							if (!num.equals("") && !field.equals("")) {
								Double value = devDao.getdouble(num, field);
								
								if(remoteAdjustVlaues.containsKey(address) && Math.abs(value-remoteAdjustVlaues.get(address))<0.01) {
									//System.out.println("状态不变");
								} else {
									remoteAdjustVlaues.put(address, value);
									System.out.println("信息体地址：" + address + "信息体值："+ value);
									// 起始位
									String start = "68";
									// 长度
									Integer len = 16;
									byte[] lenNum = new byte[1];
									lenNum[0] = (byte) (len >> 0);
									String lenStr = ChangeUtils.toHexString(lenNum);
			                        // 发送序号
			                        byte[] sendNum = new byte[2];
			                        sendNum[0] = (byte) (sendSeqNum << 1);
			                        sendNum[1] = (byte) (sendSeqNum >> 7);
			                        sendSeqNum += 1;
			                        String sendStr = ChangeUtils.toHexString(sendNum);
									// 接收序号
			                        byte[] recNum = new byte[2];
			                        recNum[0] = (byte) (receiveSeqNum << 1);
			                        recNum[1] = (byte) (receiveSeqNum >> 7);
			                        String recStr = ChangeUtils.toHexString(recNum);
			                        // 类型标识
			                        String typeIdStr = "31";
			                        // 可变结构限定词
			                        String vsqStr = "01";
			                        // 传输原因
			                        String causeOfTransmissionStr = "0600";
			                        // 公共地址
			                        String commAddressStr = "0100";
			                        // 信息体地址
			                        Integer addr = Integer.parseInt(address);
			                        byte[] addrNum = new byte[3];
			                        addrNum[0] = (byte) (addr >> 0) ;
			                        addrNum[1] = (byte) (addr >> 8);
			                        addrNum[2] = (byte) (addr >> 16);
			                        String addrStr = ChangeUtils.toHexString(addrNum);
			                        // 信息值2位
			                        byte[] valueNum = new byte[2];
			                        valueNum[0] = (byte) (value.intValue() >> 0);
			                        valueNum[1] = (byte) (value.intValue() >> 8);
			                        String valuestr = ChangeUtils.toHexString(valueNum);
			                        // 品质因素1位 
			                        String QDSStr = "00";
			                        os.write(ChangeUtils.hexStringToBytes(start+lenStr+sendStr+recStr+
			                        		typeIdStr+vsqStr+causeOfTransmissionStr+commAddressStr+
			                        		addrStr+valuestr+QDSStr));
			                        System.out.println("发送遥调命令帧："+ start+lenStr+sendStr+recStr+
			                        		typeIdStr+vsqStr+causeOfTransmissionStr+commAddressStr+
			                        		addrStr+valuestr);
			                        Thread.sleep(200);
								}
							}
						}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		}; 
            ScheduledExecutorService service_send = Executors.newScheduledThreadPool(1);
            // 第二个参数为首次执行的延时时间，第三个参数为结束和下次开始执行的间隔时间
            service_send.scheduleWithFixedDelay(runnable_db_send, 3, 1, TimeUnit.SECONDS);
            
			// 由Socket对象得到输入流，构造相应的BufferedReader对象
			InputStream is = socket.getInputStream();
			while(true){
                try {
                    Apdu apdu = new Apdu(new DataInputStream(is));
                    if (apdu.getApciType() == Apdu.ApciType.I_FORMAT) {
                        Asdu asdu = apdu.getAsdu();
                        //  处理I命令
                        handleData(asdu.getTypeId(),asdu.getInformationObjects());
                        //  返回S确认命令
                        receiveSeqNum = apdu.getSendSeqNumber() + 1;    
                        byte[] recNum = new byte[2];
                        recNum[0] = (byte) (receiveSeqNum << 1);
                        recNum[1] = (byte) (receiveSeqNum >> 7);
                        String recStr = ChangeUtils.toHexString(recNum);
                        os.write(ChangeUtils.hexStringToBytes("68040100" + recStr));
                        System.out.println("确认消息，S类型，下一条的接受序号：" + recStr);
                    }else if (apdu.getApciType() == Apdu.ApciType.STARTDT_ACT) {
                        os.write(ChangeUtils.hexStringToBytes("68040B000000"));
                        System.out.println("确认启动消息，U类型");
                    }else if (apdu.getApciType() == Apdu.ApciType.STOPDT_ACT) {
                        os.write(ChangeUtils.hexStringToBytes("680423000000"));
                        System.out.println("确认停止消息，U类型");
                    }else if (apdu.getApciType() == Apdu.ApciType.TESTFR_ACT) {
                        os.write(ChangeUtils.hexStringToBytes("680483000000"));
                        System.out.println("确认测试消息，U类型");
                    }else{
                    	System.out.println("其它报文：" + apdu.getApciType());
                    }
                }catch (Exception e){
                	System.out.println("异常错误,"+ e);
                    break;
                }
			}
			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
		} catch (Exception e) {
			System.out.println("异常错误,"+ e);
		}
	}
    public static void handleData(int typeId,InformationObject[] infoObjs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    for (int i = 0; i < infoObjs.length; i++) {
                        if(typeId == 1){   //单点遥信处理
                            int firstAddress = infoObjs[i].getInformationObjectAddress();  // 信息地址起始位
                            int len = infoObjs[i].getInformationElements().length;   // 所有信息值长度
                            for (int j = 0; j < len; j++) {
                                int address = firstAddress + j;
                                if(address > 500 || address < 0){
                                	System.out.println("遥信信息超出定义范围：" + address);
                                }
                            	String addr = String.valueOf(address);
                            	String num = Init.remoteSignal.getJSONObject(addr).getString("num");
                            	String field = Init.remoteSignal.getJSONObject(addr).getString("field");
                            	String table_name = Init.remoteSignal.getJSONObject(addr).getString("table_name");
                                Integer value = ((IeSinglePointWithQuality) infoObjs[i].getInformationElements()[j][0]).isOn();
                                if (table_name.equals("pvdigitalquantitydata")) {  // 光伏逆变器数字量
                                	for(int k=0; k<Init.PVD.size(); k++) {
                                		if (Init.PVD.get(k).getPv_num().equals(num)) {
                                			if (field.equals("status_down")) {
                                				Init.PVD.get(k).setStatus_down(value);
                                			} else if (field.equals("status_standby")){
                                				Init.PVD.get(k).setStatus_standby(value);
											} else if (field.equals("status_selftest")){
												Init.PVD.get(k).setStatus_selftest(value);
											} else if (field.equals("status_ongrid")){
												Init.PVD.get(k).setStatus_ongrid(value);
											} else if (field.equals("locking_self")){
												Init.PVD.get(k).setLocking_self(value);
											} else if (field.equals("emergency_stop")){
												Init.PVD.get(k).setEmergency_stop(value);
											} else if (field.equals("remote_local")){
												Init.PVD.get(k).setRemote_local(value);
											} else if (field.equals("reactive_power_compensation")){
												Init.PVD.get(k).setReactive_power_compensation(value);
											} else if (field.equals("smoke_alarm")){
												Init.PVD.get(k).setSmoke_alarm(value);
											} else if (field.equals("DC_lightning_protection")){
												Init.PVD.get(k).setDC_lightning_protection(value);
											} else if (field.equals("AC_lightning_protection")){
												Init.PVD.get(k).setAC_lightning_protection(value);
											} else if (field.equals("PV_reverse_connection")){
												Init.PVD.get(k).setPV_reverse_connection(value);
											} else if (field.equals("PV_insulation_resistance")){
												Init.PVD.get(k).setPV_insulation_resistance(value);
											} else if (field.equals("power_voltage")){
												Init.PVD.get(k).setPower_voltage(value);
											} else if (field.equals("grid_frequency")){
												Init.PVD.get(k).setGrid_frequency(value);
											} else if (field.equals("grid_reverse_order")){
												Init.PVD.get(k).setGrid_reverse_order(value);
											} else if (field.equals("inverter_overload")){
												Init.PVD.get(k).setInverter_overload(value);
											} else if (field.equals("inverter_overheating")){
												Init.PVD.get(k).setInverter_overheating(value);
											} else if (field.equals("ambient_temperature_overheating")){
												Init.PVD.get(k).setAmbient_temperature_overheating(value);
											} else if (field.equals("inverter_short_circuit")){
												Init.PVD.get(k).setInverter_short_circuit(value);
											} else if (field.equals("island_protection")){
												Init.PVD.get(k).setIsland_protection(value);
											} 	
										}
                                	}
                                }else if(table_name.equals("")){
									
								}
                                System.out.println("信息地址："+address+"信息值："+ ((IeSinglePointWithQuality) infoObjs[i].getInformationElements()[j][0]).isOn());
                            }
                        }
                        else if(typeId == 13) {
                        	int firstAddress = infoObjs[i].getInformationObjectAddress();  // 信息地址起始位
                        	int len = infoObjs[i].getInformationElements().length;
                        	for (int j=0; j<len; j++) {
                        		int address = firstAddress + j;
                                if (address < 16385 || address > 17000){
                                    System.out.println("遥测信息超出定义范围：" + address);
                                }
                            	String addr = String.valueOf(address);
                            	String num = Init.remoteMeasure.getJSONObject(addr).getString("num");
                            	String field = Init.remoteMeasure.getJSONObject(addr).getString("field");
                            	String table_name = Init.remoteMeasure.getJSONObject(addr).getString("table_name");
                            	Double value = Double.parseDouble(String.valueOf(((IeShortFloat) infoObjs[i].getInformationElements()[j][0]).getValue()));
	                            if (table_name.equals("pvanalogquantitydata1")) {  // 光伏逆变器模拟量1
                            		for(int k=0; k < Init.PVA1.size(); k++) {
	                            		if(Init.PVA1.get(k).getPv_num().equals(num)) {
	                            			if(field.equals("matrix_volt")) {
	                            				Init.PVA1.get(k).setMatrix_volt(value);
	                            			}
	                            			else if(field.equals("matrix_cur")) {
												Init.PVA1.get(k).setMatrix_cur(value);
											}
	                            			else if(field.equals("matrix_power_in")) {
												Init.PVA1.get(k).setMatrix_power_in(value);
											}
	                            			else if(field.equals("grid_volt_ab")) {
												Init.PVA1.get(k).setGrid_volt_ab(value);
											}
	                            			else if(field.equals("grid_volt_bc")) {
												Init.PVA1.get(k).setGrid_volt_bc(value);
											}
	                            			else if(field.equals("grid_volt_ca")) {
												Init.PVA1.get(k).setGrid_volt_ca(value);
											}
	                            			else if(field.equals("on_grid_cur_a")) {
												Init.PVA1.get(k).setOn_grid_cur_a(value);
											}
	                            			else if(field.equals("on_grid_cur_b")) {
												Init.PVA1.get(k).setOn_grid_cur_b(value);
											}
	                            			else if(field.equals("on_grid_cur_c")) {
												Init.PVA1.get(k).setOn_grid_cur_c(value);
											}
	                            			else if(field.equals("power_factor_a")) {
												Init.PVA1.get(k).setPower_factor_a(value);
											}
	                            			else if(field.equals("power_factor_b")) {
												Init.PVA1.get(k).setPower_factor_b(value);
											}
	                            			else if(field.equals("power_factor_c")) {
												Init.PVA1.get(k).setPower_factor_c(value);
											}
	                            			else if(field.equals("grid_freq")) {
												Init.PVA1.get(k).setGrid_freq(value);
											}
	                            		}
	                            	}
	                            }else if(table_name.equals("pvanalogquantitydata2")){
									for(int k=0; k<Init.PVA2.size(); k++) {
										if(Init.PVA2.get(k).getPv_num().equals(num)) {
											if(field.equals("on_grid_p")) {
	                            				Init.PVA2.get(k).setOn_grid_p(value);
	                            			}
	                            			else if(field.equals("on_grid_q")) {
												Init.PVA2.get(k).setOn_grid_q(value);
											}
	                            			else if(field.equals("on_grid_s")) {
												Init.PVA2.get(k).setOn_grid_s(value);
											}
	                            			else if(field.equals("inv_cabin_temp")) {
												Init.PVA2.get(k).setInv_cabin_temp(value);
											}
	                            			else if(field.equals("day_gen_power")) {
												Init.PVA2.get(k).setDay_gen_power(value);
											}
	                            			else if(field.equals("day_runtime")) {
												Init.PVA2.get(k).setDay_runtime(value);
											}
	                            			else if(field.equals("total_gen_power")) {
												Init.PVA2.get(k).setTotal_gen_power(value);
											}
	                            			else if(field.equals("total_runtime")) {
												Init.PVA2.get(k).setTotal_runtime(value);
											}
	                            			else if(field.equals("co2_reduce")) {
												Init.PVA2.get(k).setCo2_reduce(value);
											}
										}
									}
								}
                                System.out.println("信息地址："+address+"信息值："+ ((IeShortFloat) infoObjs[i].getInformationElements()[j][0]).getValue());
                        	}
                        }else {
                        	System.out.println("类型标识:" + typeId + "未处理");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
