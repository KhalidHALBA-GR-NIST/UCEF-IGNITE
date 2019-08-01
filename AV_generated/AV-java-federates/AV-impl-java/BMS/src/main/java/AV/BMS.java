package AV;

import org.cpswt.config.FederateConfig;
import org.cpswt.config.FederateConfigParser;
import org.cpswt.hla.InteractionRoot;
import org.cpswt.hla.base.AdvanceTimeRequest;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The BMS type of federate for the federation designed in WebGME.
 *
 */
public class BMS extends BMSBase {
    private final static Logger log = LogManager.getLogger();

    private double currentTime = 0;

    public BMSConfig BMSparameter = new BMSConfig();
    
    CAN BmsCAN = create_CAN();

    public BMS(BMSConfig params) throws Exception {
        super(params);
        BMSparameter.Peak_Voltage = params.Peak_Voltage;
        BMSparameter.Peak_Current = params.Peak_Current;
        BMSparameter.State_Of_Charge = params.State_Of_Charge;
        BMSparameter.State_Of_Health = params.State_Of_Health;
        BMSparameter.Remaining_Capacity = params.Remaining_Capacity;
        BMSparameter.Max_Temperature = params.Max_Temperature;
        BMSparameter.Min_Temperature = params.Min_Temperature;
        BMSparameter.Peak_Current_Limit = params.Peak_Current_Limit;
        BMSparameter.BMSPGN = params.BMSPGN;
        BMSparameter.BMSSPNs = params.BMSSPNs;

        
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

    	
    	
    	
    	
    	BMSparameter.Peak_Current_Limit =Integer.toString(ThreadLocalRandom.current().nextInt(1,3 + 1));
    	
    	
	}
    
    public String Build_SPN(){    	    	   
    	return   BMSparameter.BMSSPNs = BMSparameter.Peak_Voltage  + " " + BMSparameter.Peak_Current  + " " +BMSparameter.State_Of_Charge  + " " +BMSparameter.State_Of_Health  + " " +BMSparameter.Remaining_Capacity + " " + BMSparameter.Max_Temperature + " " + BMSparameter.Min_Temperature  + " " +BMSparameter.Peak_Current_Limit;
        } 
    
    public void Build_and_Send_CAN_Frame(String pgn,String spn)
    
    {
   	

        BmsCAN.set_ID18B(pgn);

        BmsCAN.set_DataField(spn);
        BmsCAN.sendInteraction(getLRC(), currentTime + getLookAhead());

 	   
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
     	       case 2:	   
     	    	   Calculate_power_limits();
     	    	   Build_and_Send_CAN_Frame( BMSparameter.BMSPGN, Build_SPN());  
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
            BMSConfig federateConfig = federateConfigParser.parseArgs(args, BMSConfig.class);
            BMS federate = new BMS(federateConfig);
            federate.execute();
            log.info("Done.");
            System.exit(0);
        } catch (Exception e) {
            log.error(e);
            System.exit(1);
        }
    }
}
