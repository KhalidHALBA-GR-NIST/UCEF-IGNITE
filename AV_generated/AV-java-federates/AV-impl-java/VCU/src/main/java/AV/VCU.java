package AV;

import org.cpswt.config.FederateConfig;
import org.cpswt.config.FederateConfigParser;
import org.cpswt.config.FederateParameter;
import org.cpswt.hla.InteractionRoot;
import org.cpswt.hla.base.AdvanceTimeRequest;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.*; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The VCU type of federate for the federation designed in WebGME.
 *
 */
public class VCU extends VCUBase {
	
	
	
//	      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName("192.168.56.1");
//	      byte[] sendData = new byte[1024];
//	      String sentence = inFromUser.readLine();
//	      sendData = sentence.getBytes();
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
//	      clientSocket.send(sendPacket);
//	      clientSocket.close();
	
	
	
	
	
	
	
	
	
	
	
	
//	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//	      DatagramSocket clientSocket = new DatagramSocket();
//	      InetAddress IPAddress = InetAddress.getByName("192.168.56.1");
//	      byte[] sendData = new byte[1024];
////	      byte[] receiveData = new byte[1024];
//	      String sentence = inFromUser.readLine();
//	      sendData = sentence.getBytes();
//	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
//	      clientSocket.send(sendPacket);
//	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//	      clientSocket.receive(receivePacket);
//	      String modifiedSentence = new String(receivePacket.getData());
//	      System.out.println("FROM SERVER:" + modifiedSentence);
//	      clientSocket.close();
//	
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    private final static Logger log = LogManager.getLogger();

    private double currentTime = 0;
    String status = "0";
    
    public VCUConfig VCUparameter = new VCUConfig();
    
    CAN VcuCAN = create_CAN();
    
    Queue<String> s = new LinkedList<String>();
    Queue<Double> t = new LinkedList<Double>();
	
    
    Queue<String> s1 = new LinkedList<String>();
    Queue<Double> t1 = new LinkedList<Double>();
    
    
    public VCU(VCUConfig params) throws Exception {
        super(params);
        

   	 VCUparameter.VCUPGN= params.VCUPGN;
   	
   	 VCUparameter.VCUSPNs= params.VCUSPNs;
   	
   	
   	
   	 VCUparameter.Motor_Operating_Mode= params.Motor_Operating_Mode;
   	
   	 VCUparameter.Motor_Torque_cmd= params.Motor_Torque_cmd;
   	
   	 VCUparameter.Motor_Speed= params.Motor_Speed;
   	
   	 VCUparameter.Volt_Cmd= params.Volt_Cmd;
   	
   	 VCUparameter.Contactor_Override_Commands= params.Contactor_Override_Commands;
   	 
   	 VCUparameter.Battery_Cooling_Heating_Commands= params.Battery_Cooling_Heating_Commands;
   	
   	 VCUparameter.Motor_Cooling_Heating_Commands= params.Motor_Cooling_Heating_Commands;
   	
   	 VCUparameter.Inverter_Cooling_Heating_Commands= params.Inverter_Cooling_Heating_Commands;
   	
   	  

   	 VCUparameter.MCU_Motor_Temperature= params.MCU_Motor_Temperature;
   	
   	 VCUparameter.MCU_Inverter_Temperature= params.MCU_Inverter_Temperature;
   	
   	 VCUparameter.MCU_Motor_Speed= params.MCU_Motor_Speed;
   	
   	 VCUparameter.MCU_Motor_Power_Limits= params.MCU_Motor_Power_Limits;
   	
   	  
   	  
   	 VCUparameter.BMS_Battery_Peak_Current= params.BMS_Battery_Peak_Current;
  	  
  	 VCUparameter.BMS_Obstacle_Presence_distance= params.BMS_Obstacle_Presence_distance;
   	
   	 VCUparameter.BMS_Battery_Peak_Voltage= params.BMS_Battery_Peak_Voltage;
   	
   	 VCUparameter.BMS_Battery_State_of_charge_and_Health= params.BMS_Battery_State_of_charge_and_Health;
   	
   	 VCUparameter.BMS_Battery_Remaining_Capacity= params.BMS_Battery_Remaining_Capacity;
   	
   	 VCUparameter.BMS_Battery_Max_Min_Cell_Temperature= params.BMS_Battery_Max_Min_Cell_Temperature;
   	
   	 VCUparameter.BMS_Battery_Pack_Power_limit= params.BMS_Battery_Pack_Power_limit;
   	
   	            
   	  
   	 VCUparameter.ABS_Wheel_Speed= params.ABS_Wheel_Speed;
   	
   	 VCUparameter.ABS_Vehicle_Speed= params.ABS_Vehicle_Speed;
   	
   	 VCUparameter.ABS_Event_Status= params.ABS_Event_Status;
   	
   	  
   	 VCUparameter.Accel_Pedal_Position= params.Accel_Pedal_Position;
   	
   	 VCUparameter.Brake_Pressure= params.Brake_Pressure;
   	
   	
     VCUparameter.messageTime=params.messageTime;
   
    }
 
    private void checkReceivedSubscriptions() {

        InteractionRoot interaction = null;
        if ((interaction = getNextInteractionNoWait()) != null) {
            if (interaction instanceof CAN) {
                handleInteractionClass((CAN) interaction);
            }
            else {
                log.debug("unhandled interaction: {}", interaction.getClassName());
            }
        }
        
        else {
        	
        	if(s.size()==0)
        	
        	{
            s.add("0");
            t.add(0.0);
        	}
        	else 
        	{	
        	s.add(s.peek());
            t.add(t.peek());
        	}
        	}
     }
    
    
    
    
    
    
    
    public void TorqueRequest(Double speed , String status) 
    {
    	
    	VCUparameter.Motor_Torque_cmd = Double.toString(speed) + "  " + status;
    	
    }
    
    
    public String Build_SPN()
    {
 	//  return VCUparameter.VCUSPNs= VCUparameter.Motor_Operating_Mode + " " + VCUparameter.Motor_Torque_cmd + " " +VCUparameter.Motor_Speed + " " + VCUparameter.Volt_Cmd+ " " + VCUparameter.Contactor_Override_Commands+ " " + VCUparameter.Battery_Commands+ " " + VCUparameter.Torque_Commands +" "+VCUparameter.InverterCoolingAndHeatingCommands ;
   	  return VCUparameter.VCUSPNs= VCUparameter.Motor_Operating_Mode + " " + VCUparameter.Motor_Torque_cmd ;
    }
    public void Build_and_Send_CAN_Frame(String pgn, String spn){
 	      VcuCAN.set_ID18B(pgn);
 	      VcuCAN.set_DataField(spn);
 	      VcuCAN.sendInteraction(getLRC(), currentTime + getLookAhead());    
    }
   public void AV_Log()
    {
    String ABS_traction_Stability_torque_request_Message ="VCU OBSTACLE" +  VCUparameter.BMS_Obstacle_Presence_distance  +  "ABS Speed : " + VCUparameter.ABS_Wheel_Speed + " ABS_Event_Status    : " + VCUparameter.ABS_Event_Status+ " & current time is  " + currentTime+ "  message time is " + VCUparameter.messageTime +  " case " +(int)VCUparameter.messageTime % 10 ;
//    log.info(ABS_traction_Stability_torque_request_Message);  	           
    }
   public void DC_Reader(int speedline)
   {
	   
	   
	   
	   
//		try {
//		      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//		      DatagramSocket clientSocket = new DatagramSocket();
//		      InetAddress IPAddress = InetAddress.getByName("192.168.56.1");
//		      byte[] sendData = new byte[1024];				      
//		      sendData = (VCUparameter.BMS_Obstacle_Presence_distance).getBytes();
//		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//		      clientSocket.send(sendPacket);
//		  	System.out.println("speed" + VCUparameter.BMS_Obstacle_Presence_distance + " time " + currentTime);
//		      clientSocket.close();
//         } catch (Exception e) {
//         System.out.println(e);
//         }
	   
	   
//	   
//	   
//	   
//	   
//	   
//	   
	   
	   //int i = 0;
//			BufferedReader reader;
			try {
//				reader = new BufferedReader(new FileReader(
//						"/home/vagrant/Desktop/AVCOA/AV_generated/AV-java-federates/AV-impl-java/VCU/src/main/java/AV/FTP75.txt"));
//				String line = reader.readLine();
//				String line32 = reader.
				
				String speed = Files.readAllLines(Paths.get("/home/vagrant/Desktop/AVCOA/AV_generated/AV-java-federates/AV-impl-java/VCU/src/main/java/AV/FTP75.txt")).get(speedline);
				
//				System.out.println("speed" + speed + " time " + speedline);
				
//				while (line != null) {
//					i++;
//					try {
//			           Thread.sleep(1000);
//			           } catch (Exception e) {
//			           System.out.println(e);
//			           }
//					System.out.println("speed" + line + " time " + i);
					try {
//					      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					      DatagramSocket clientSocket = new DatagramSocket();
					      InetAddress IPAddress = InetAddress.getByName("192.168.56.1");
					      byte[] sendData = new byte[1024];		
					      if(VCUparameter.BMS_Obstacle_Presence_distance.equals("true") )
					      {
					    	  
					    	  
					    	  speed = "0";
					    	  
					    	  
					    	  System.out.println(" obstacle detected stopping car" + speed + " time " + speedline);
					      sendData = speed.getBytes();
					      }
					      else
					      {
					    	  
					    	  System.out.println(" obstacle NOT detected " + speed + " time " + speedline);
					    	  
					      sendData = speed.getBytes();
					      }
					      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
					      clientSocket.send(sendPacket);
					      clientSocket.close();
			           } catch (Exception e) {
			           System.out.println(e);
			           }
//					line = reader.readLine();
//				}
//				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			
			
			
			
			
			
//			
//			int i = 0;
//			BufferedReader reader;
//			try {
//				reader = new BufferedReader(new FileReader(
//						"/home/vagrant/Desktop/AVCOA/AV_generated/AV-java-federates/AV-impl-java/VCU/src/main/java/AV/FTP75.txt"));
//				String line = reader.readLine();
////				String line32 = reader.
//				
//				String line32 = Files.readAllLines(Paths.get("/home/vagrant/Desktop/AVCOA/AV_generated/AV-java-federates/AV-impl-java/VCU/src/main/java/AV/FTP75.txt")).get(50);
//				
//				System.out.println("speed" + line32 + " time " + i);
//				
//				while (line != null) {
//					i++;
//					try {
//			           Thread.sleep(1000);
//			           } catch (Exception e) {
//			           System.out.println(e);
//			           }
//					System.out.println("speed" + line + " time " + i);
//					try {
//					      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//					      DatagramSocket clientSocket = new DatagramSocket();
//					      InetAddress IPAddress = InetAddress.getByName("192.168.56.1");
//					      byte[] sendData = new byte[1024];		
//					      if(VCUparameter.BMS_Obstacle_Presence_distance == "true" )
//					      {
//					      sendData = VCUparameter.BMS_Obstacle_Presence_distance.getBytes();
//					      }
//					      else
//					      {
//					      sendData = line.getBytes();
//					      }
//					      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
//					      clientSocket.send(sendPacket);
//					      clientSocket.close();
//			           } catch (Exception e) {
//			           System.out.println(e);
//			           }
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
   }  
   public void VCU_Commands()
    
    {
     
   if(s.size()>1)
    {

 
	String sp1= s.poll();
    String sp2= s.peek();
	Double tm1= t.poll();
    Double tm2= t.peek(); 
    
    Double delta = Double.parseDouble(sp2)-Double.parseDouble(sp1);
    
    if(delta>0)
    {
    status = "Accelerating" ;	
    TorqueRequest(Double.parseDouble(sp2) , status);	
    }
    else if (delta<0)
    	
    {
    	
    	status = "Braking" ;	
    	TorqueRequest(Double.parseDouble(sp2) , status);	
    }
    
    else
    	
    {
    	if(Double.parseDouble(sp2)>0)
    	{
    	status = "Coasting" ;
    	TorqueRequest(Double.parseDouble(sp2) , status);	
    	}
    	else
    	{
    	status = "PARKED";
    	TorqueRequest(Double.parseDouble(sp2) , status);	
    	}
    	
    }
    
    String diff = "speed(t-1)=  " +sp1+ " time(t-1)= " + tm1 + "  speed(t)=  " + sp2 + " time(t)= " + tm2 + "  Scenario  "  +status+  " cuRRent logical time  " + currentTime + " current simulation time  " + currentTime ;
//    log.info(diff);
	}
    }
    private void execute() throws Exception {
        if(super.isLateJoiner()) {
            currentTime = super.getLBTS() - super.getLookAhead();
            super.disableTimeRegulation();
        }
        AdvanceTimeRequest atr = new AdvanceTimeRequest(currentTime);
        putAdvanceTimeRequest(atr);

        if(!super.isLateJoiner()) {
            readyToPopulate();
        }
        if(!super.isLateJoiner()) {
            log.info("waiting on readyToRun...");
            readyToRun();
            log.info("...synchronized on readyToRun");
        }
        startAdvanceTimeThread();
        log.info("started logical time progression");
        while (!exitCondition) {
            atr.requestSyncStart();
            enteredTimeGrantedState();
            checkReceivedSubscriptions();
            log.info(" notif as received " + VCUparameter.BMS_Obstacle_Presence_distance);
            VCU_Commands();
            Build_and_Send_CAN_Frame( VCUparameter.VCUPGN, Build_SPN());
//            DC_Reader();  
	 	 	   int osd = (int)(currentTime) % 20;
	    	   switch (osd)
	    	   {

     	
	     	   case 0:
	     	      DC_Reader((int)(currentTime/20));
	     		  log.info(" OSD 0" + Integer.toString(osd) + " currentime " + Double.toString(currentTime));
	     		  break;
//	     	   
//     	
//
//	     	     	
//	     	   case 1:
//	     	      DC_Reader();
//	     		  log.info(" OSD 1" + Integer.toString(osd) + " currentime " + Double.toString(currentTime));
//	     		  break;
//	     	   
//     	
//	     	   case 2:
//		     	      DC_Reader();
//		     		  log.info(" OSD 2" + Integer.toString(osd) + " currentime " + Double.toString(currentTime));
//		     		  break;
//     	
//     	
//		     	    	
//	     	   case 3:
//		     	      DC_Reader();
//		     		  log.info(" OSD 3" + Integer.toString(osd) + " currentime " + Double.toString(currentTime));
//		     		  break;
//		     		  
		     		  
		     		  
//	    	   case 2:
//	    		//   DC_Reader();
//	    		   break;
//	    	   case 3:
//	    		   DC_Reader();
//	    		   break;
//	    	   case 4:
//	    		   DC_Reader();
//	    		   break;
//	    	   case 5:
//	    		   DC_Reader();
//	    		   break;
//	    	   case 6:
//	    		   DC_Reader();
//	    		   break;
	    	   case 7:
	             //  checkReceivedSubscriptions();
	               AV_Log();
	              	break;     	
	    	   case 8:
    	      	   Build_and_Send_CAN_Frame( VCUparameter.VCUPGN, Build_SPN());
	    	           break;
	    	   }

            ////////////////////////////////////////////////////////////////////////////////


            ////////////////////////////////////////////////////////////////////////////////////////
            // TODO break here if ready to resign and break out of while loop
            ////////////////////////////////////////////////////////////////////////////////////////


            if (!exitCondition) {
                currentTime += super.getStepSize();
                AdvanceTimeRequest newATR = new AdvanceTimeRequest(currentTime);
                putAdvanceTimeRequest(newATR);
                atr.requestSyncEnd();
                atr = newATR;
            }
        }
 
        // call exitGracefully to shut down federate
        
        exitGracefully();

        ////////////////////////////////////////////////////////////////////////////////////////
        // TODO Perform whatever cleanups needed before exiting the app
        ////////////////////////////////////////////////////////////////////////////////////////
    }

    private void handleInteractionClass(CAN interaction) {
   
    	String delims = "[ ]+";
    	String[] CSPNs = interaction.get_DataField().split(delims);
    switch (interaction.get_ID18B()) {
   	               
    case "COA":  
    	VCUparameter.COA_Message = interaction.get_DataField();  
    	VCUparameter.messageTime = interaction.getTime();
       s.add(VCUparameter.COA_Message);
       t.add(VCUparameter.messageTime);

    	break; 
    	
    case "ABS":  
 	   VCUparameter.ABS_Wheel_Speed               = CSPNs[0];
 	   VCUparameter.ABS_Event_Status     = CSPNs[2];
 	   VCUparameter.messageTime=interaction.getTime();
 	   
         break;  
    	
     case "BMS":  
    	 VCUparameter.BMS_Obstacle_Presence_distance               = CSPNs[0];
  	   VCUparameter.messageTime=interaction.getTime();
  	   
          break;      	
    		
    }

    }
    
    public static void main(String[] args) {
        try {
            FederateConfigParser federateConfigParser = new FederateConfigParser();
            VCUConfig federateConfig = federateConfigParser.parseArgs(args, VCUConfig.class);
            VCU federate = new VCU(federateConfig);
            federate.execute();
//            log.info("Done.");
            System.exit(0);
        } catch (Exception e) {
            log.error(e);
            System.exit(1);
        }
    }
}
