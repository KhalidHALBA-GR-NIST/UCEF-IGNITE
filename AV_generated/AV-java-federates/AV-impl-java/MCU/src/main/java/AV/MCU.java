package AV;

import org.cpswt.config.FederateConfig;
import org.cpswt.config.FederateConfigParser;
import org.cpswt.hla.InteractionRoot;
import org.cpswt.hla.base.AdvanceTimeRequest;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The MCU type of federate for the federation designed in WebGME.
 *
 */
public class MCU extends MCUBase {
    private final static Logger log = LogManager.getLogger();

    private double currentTime = 0;

 public MCUConfig MCUparameter = new MCUConfig();
    
    CAN McuCAN = create_CAN();

    public MCU(MCUConfig params) throws Exception {
        super(params);
        MCUparameter.Engine_Speed = params.Engine_Speed ;
        MCUparameter.Engine_Temperature = params.Engine_Temperature ;
        MCUparameter.Motor_Power_Limits = params.Motor_Power_Limits ;
        MCUparameter.Inverter_Temperature = params.Inverter_Temperature ;
        MCUparameter.VCU_Torque_Commands = params.VCU_Torque_Commands ;
        MCUparameter.Motor_Speed = params.Motor_Speed ;
        MCUparameter.MCUPGN = params.MCUPGN ;
        MCUparameter.MCUSPNs = params.MCUSPNs ;
        MCUparameter.messageTime = params.messageTime ;

        
        
    }
    private void checkReceivedSubscriptions() {

        InteractionRoot interaction = null;
        while ((interaction = getNextInteractionNoWait()) != null) {
            if (interaction instanceof CAN) {
                handleInteractionClass((CAN) interaction);
            }
            else {
                log.debug("unhandled interaction: {}", interaction.getClassName());
            }
        }
     }

    
    
 public void Calculate_power_limits()
    
    {	
    	MCUparameter.Motor_Power_Limits=Integer.toString(ThreadLocalRandom.current().nextInt(40, 80 + 1));  
    }

    
    
    
    public void Sense_speed()

    {
    	MCUparameter.Engine_Speed       = Integer.toString(ThreadLocalRandom.current().nextInt(600, 1000 + 1)); 
    }

    
    public String Build_SPN()
    
    {  
    	return MCUparameter.MCUSPNs = MCUparameter.Engine_Temperature + " " + MCUparameter.Engine_Speed  + " " +MCUparameter.Motor_Power_Limits  + " " +  MCUparameter.Inverter_Temperature  ;
    	
    }
     
    
    public void AV_Log()
    
    {
    	
    String VCU_Torque_Message =  "VCU_Torque_Commands : " + MCUparameter.VCU_Torque_Commands + " & current time is  " + currentTime + "  message time is " + MCUparameter.messageTime +  " case " + (int)MCUparameter.messageTime % 10 ;
    log.info("VCU_Torque_Commands "+VCU_Torque_Message);
    
    }
    
    
    
    public void Build_and_Send_CAN_Frame(String pgn,String spn)
    
    { 
    McuCAN.set_ID18B(MCUparameter.MCUPGN);
    McuCAN.set_DataField(MCUparameter.MCUSPNs);
    McuCAN.sendInteraction(getLRC(), currentTime + getLookAhead());
    }
    
    
    
    
    
    
//    
//    
//          Engine rpm= 3000
//    		Gear ratio=1.5 []
//    		Axle ratio = 4 [3.94 corolla 2006]
//    		Tyre size = 12 inch radius
//
//    		The formula for calculating vehicle speed is:
//    		Car or automobile speed in consistent unit,
//    		V = (engine rpm * wheel tyre perimeter)/(gear ratio * axle ratio)
//
//    		Or, V = (engine rpm * 3.14 * tyre diameter ) / ( gear ratio * wheel axle ratio)
//
//    		For the given values,
//    		V  = (3000 rpm* 3.14 * 2 * 12 inch) / ( 1.5 * 4)
//          
//    		Or, V (kmph)= (3000 * 60 rph * 3.14 * 2 * 12 * 25.4 * 0.000001 km)/(1.5 * 4)
//
//    		Or, V = 57.42 kmph
//    
    
    
    
    
    
// HP = Torque x RPM ÷ 5252
// toyota corolla 2006 : HP ~ 140
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void Control_Torque()
    
    {	
    	
    	
    	
    	
    	
    }
     
    
    
    
    
    
    
    
    
    private void execute() throws Exception {
        if(super.isLateJoiner()) {
            log.info("turning off time regulation (late joiner)");
            currentTime = super.getLBTS() - super.getLookAhead();
            super.disableTimeRegulation();
        }

        /////////////////////////////////////////////
        // TODO perform basic initialization below //
        /////////////////////////////////////////////

        AdvanceTimeRequest atr = new AdvanceTimeRequest(currentTime);
        putAdvanceTimeRequest(atr);

        if(!super.isLateJoiner()) {
            log.info("waiting on readyToPopulate...");
            readyToPopulate();
            log.info("...synchronized on readyToPopulate");
        }

        ///////////////////////////////////////////////////////////////////////
        // TODO perform initialization that depends on other federates below //
        ///////////////////////////////////////////////////////////////////////

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

            ////////////////////////////////////////////////////////////////////////////////////////
            // TODO send interactions that must be sent every logical time step below.
            // Set the interaction's parameters.
            //
            //    CAN vCAN = create_CAN();
            //    vCAN.set_ACKslot( < YOUR VALUE HERE > );
            //    vCAN.set_CRC( < YOUR VALUE HERE > );
            //    vCAN.set_DLC( < YOUR VALUE HERE > );
            //    vCAN.set_DataField( < YOUR VALUE HERE > );
            //    vCAN.set_EndOfFrame( < YOUR VALUE HERE > );
            //    vCAN.set_ID11B( < YOUR VALUE HERE > );
            //    vCAN.set_ID18B( < YOUR VALUE HERE > );
            //    vCAN.set_IDE( < YOUR VALUE HERE > );
            //    vCAN.set_Parameter( < YOUR VALUE HERE > );
            //    vCAN.set_RTR( < YOUR VALUE HERE > );
            //    vCAN.set_ReservedBit1( < YOUR VALUE HERE > );
            //    vCAN.set_ReservedBit2( < YOUR VALUE HERE > );
            //    vCAN.set_SRR( < YOUR VALUE HERE > );
            //    vCAN.set_StartOfFrame( < YOUR VALUE HERE > );
            //    vCAN.set_actualLogicalGenerationTime( < YOUR VALUE HERE > );
            //    vCAN.set_federateFilter( < YOUR VALUE HERE > );
            //    vCAN.set_originFed( < YOUR VALUE HERE > );
            //    vCAN.set_sourceFed( < YOUR VALUE HERE > );
            //    vCAN.sendInteraction(getLRC(), currentTime + getLookAhead());
            //
            ////////////////////////////////////////////////////////////////////////////////////////

            checkReceivedSubscriptions();
            int osd = (int)(currentTime/2) % 10;

      	   switch (osd)
      	   {

      	           
      	   case 4:
      
              Sense_speed();
              Calculate_power_limits();
              Build_and_Send_CAN_Frame( MCUparameter.MCUPGN, Build_SPN());
              
              break;
              
              
      	   case 9:
      		   
      		   checkReceivedSubscriptions();
      		   AV_Log();
      		   Control_Torque();
      		   break;

      	   }
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
        //////////////////////////////////////////////////////////////////////////
        // TODO implement how to handle reception of the interaction            //
        //////////////////////////////////////////////////////////////////////////
   
    	String delims = "[ ]+";
    	String[] CSPNs = interaction.get_DataField().split(delims);
    	switch (interaction.get_ID18B()) {
            case "VCU":
            	MCUparameter.VCU_Torque_Commands      = CSPNs[1];
            	MCUparameter.messageTime=interaction.getTime();
       
                break;   
                
                
            
        }
    
    }

    public static void main(String[] args) {
        try {
            FederateConfigParser federateConfigParser = new FederateConfigParser();
            MCUConfig federateConfig = federateConfigParser.parseArgs(args, MCUConfig.class);
            MCU federate = new MCU(federateConfig);
            federate.execute();
            log.info("Done.");
            System.exit(0);
        } catch (Exception e) {
            log.error(e);
            System.exit(1);
        }
    }
}
