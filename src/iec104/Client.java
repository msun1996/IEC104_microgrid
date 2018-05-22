package iec104;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import iec104.IeShortFloat;
import iec104.IeSinglePointWithQuality;

import iec104.util.ChangeUtils;


public class Client {
	public static void main(String[] args) {
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
                        os.write(ChangeUtils.hexStringToBytes("680E0000000064010600010000000014"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ScheduledExecutorService service_A = Executors.newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service_A.scheduleAtFixedRate(runnable, 0, 100, TimeUnit.HOURS);
            
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
                        int receiveSeqNum = apdu.getSendSeqNumber() + 1;
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
