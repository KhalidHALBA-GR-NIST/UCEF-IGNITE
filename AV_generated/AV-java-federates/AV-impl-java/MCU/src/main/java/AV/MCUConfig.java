package AV;

import org.cpswt.config.FederateConfig;
import org.cpswt.config.FederateParameter;

/**
 * An example of how to implement custom configuration options for a federate.
 * 
 * A custom configuration file requires the definition of a class that extends from FederateConfig. Each configuration
 * option must be declared as a public member variable annotated with the FederateParameter annotation.
 * 
 * See {@link InputSource#main(String[])} and {@link InputSource#InputSource(InputSourceConfig)} for how to use the
 * configuration class to initialize a federate.
 */
public class MCUConfig extends FederateConfig {
    /**
     * ABS Measured Wheel Speed.
     */

    @FederateParameter
    public double messageTime ;

    // J1931-71 compliant local Messages
	@FederateParameter
    public String Engine_Speed;         // pgn61444 SPN190
	@FederateParameter
	public String Engine_Temperature;   // pgn65262 SPN110
	@FederateParameter
	public String Motor_Power_Limits;   // pgn ?    SPN ?
	@FederateParameter
	public String Inverter_Temperature; // pgn ?    SPN ? 
    // J1931-71 compliant external Messages
	@FederateParameter
	public String  VCU_Torque_Commands ;

   // Messages sent/received to/from actuators/sensors
	@FederateParameter
	public String Motor_Speed; // see graph in slide 3
   // PGN : multiple PGNs per Federate are possible. we use the Federate name as a placeholder for now.
	@FederateParameter
	public String MCUPGN ;
    // SPNs aggregation : We suppose all SPNs have the same PGN so far, to be modified when the right PGNs are described.
	@FederateParameter
	public String MCUSPNs ; 

   // String BMSSPNs = Peak_Voltage + Peak_Current + State_Of_Charge + State_Of_Health +  Remaining_Capacity + Max_Temperature +Min_Temperature + Peak_Current_Limit  ; 
}
