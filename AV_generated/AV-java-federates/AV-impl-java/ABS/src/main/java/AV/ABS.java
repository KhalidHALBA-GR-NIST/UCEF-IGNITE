package AV;

import org.cpswt.config.FederateConfig;
import org.cpswt.config.FederateConfigParser;
import org.cpswt.hla.InteractionRoot;
import org.cpswt.hla.base.AdvanceTimeRequest;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ABS type of federate for the federation designed in WebGME.
 *
 */
public class ABS extends ABSBase {
    private final static Logger log = LogManager.getLogger();

    private double currentTime = 0;

    public ABSConfig ABSparameter = new ABSConfig();
    CAN AbsCAN = create_CAN();
    public ABS(ABSConfig params) throws Exception {
        super(params);
        

        ABSparameter.messageTime= params.messageTime;
        ABSparameter.Wheel_Speed= params.Wheel_Speed;
        ABSparameter.ABS_Event_Status= params.ABS_Event_Status; 
        ABSparameter.Traction_Stability_Torque_Request= params.Traction_Stability_Torque_Request;   
        ABSparameter.VCU_Motor_Torque= params.VCU_Motor_Torque; 
        ABSparameter.VCU_PGN = params.VCU_PGN;
        ABSparameter.Wheel_Speed_Sensors= params.Wheel_Speed_Sensors; 
        ABSparameter.Hydraulic_Valve_Commands= params.Hydraulic_Valve_Commands;
        ABSparameter.ABSPGN= params.ABSPGN;  
        ABSparameter.ABSSPNs= params.ABSSPNs; 
        ABSparameter.Vehicle_Speed= params.Vehicle_Speed;
            
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

public void Wheel_Speed ()
    
    {
   
	  ABSparameter.Wheel_Speed   = Integer.toString(ThreadLocalRandom.current().nextInt(50,90 + 1));

    } 
   
   
   public void ABS_Event_Status  ()
   
   {
	   
	    int length = 10;
	    boolean useLetters = true;
	    boolean useNumbers = false;
	    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
	    ABSparameter.ABS_Event_Status= generatedString;
	   
	  }

    
 public String Build_SPN()
   
   {
	   return ABSparameter.ABSSPNs= ABSparameter.Wheel_Speed +" "+ ABSparameter.Vehicle_Speed +" "+ ABSparameter.ABS_Event_Status+" " + ABSparameter.Traction_Stability_Torque_Request;
   }
    
    
    
    public void Build_and_Send_CAN_Frame(String pgn,String spn)
    
    {

 	
 	   
 
        AbsCAN.set_ID18B(pgn);
        AbsCAN.set_DataField(spn);
        AbsCAN.sendInteraction(getLRC(), currentTime + getLookAhead());
     
 	   
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

      	   case 0:  
      	   Wheel_Speed (); 
      	   Build_and_Send_CAN_Frame( ABSparameter.ABSPGN, Build_SPN());
             break;
      	   
      	   case 6:
      		 ABS_Event_Status  (); 
      	   Build_and_Send_CAN_Frame( ABSparameter.ABSPGN, Build_SPN());  
      	           
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
    }

    public static void main(String[] args) {
        try {
            FederateConfigParser federateConfigParser = new FederateConfigParser();
            ABSConfig federateConfig = federateConfigParser.parseArgs(args, ABSConfig.class);
            ABS federate = new ABS(federateConfig);
            federate.execute();
            log.info("Done.");
            System.exit(0);
        } catch (Exception e) {
            log.error(e);
            System.exit(1);
        }
    }
}
