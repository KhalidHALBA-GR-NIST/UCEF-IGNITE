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
public class VCUConfig extends FederateConfig {
    /**
     * The number of operands to generate for the adder.
     */

	@FederateParameter
	 public String VCUPGN;
	@FederateParameter
	 public String VCUSPNs;
	
	
	@FederateParameter
	 public String Motor_Operating_Mode;
	@FederateParameter
	 public String Motor_Torque_cmd;
	@FederateParameter
	 public String Motor_Speed;
	@FederateParameter
	 public String Volt_Cmd;
	@FederateParameter
	 public String Contactor_Override_Commands;
	@FederateParameter 
	 public String Battery_Cooling_Heating_Commands;
	@FederateParameter
	 public String Motor_Cooling_Heating_Commands;
	@FederateParameter
	 public String Inverter_Cooling_Heating_Commands;
	@FederateParameter
	  

	 public String MCU_Motor_Temperature;
	@FederateParameter
	 public String MCU_Inverter_Temperature;
	@FederateParameter
	 public String MCU_Motor_Speed;
	@FederateParameter
	 public String MCU_Motor_Power_Limits;
	@FederateParameter
	  
	  
	 public String BMS_Battery_Peak_Current;
	@FederateParameter
	 public String BMS_Battery_Peak_Voltage;
	@FederateParameter
	 public String BMS_Battery_State_of_charge_and_Health;
	@FederateParameter
	 public String BMS_Battery_Remaining_Capacity;
	@FederateParameter
	 public String BMS_Battery_Max_Min_Cell_Temperature;
	@FederateParameter
	 public String BMS_Battery_Pack_Power_limit;
	@FederateParameter
	public String BMS_Obstacle_Presence_distance;    
	@FederateParameter
	            
	  
	 public String ABS_Wheel_Speed;
	@FederateParameter
	 public String ABS_Vehicle_Speed;
	@FederateParameter
	 public String ABS_Event_Status;
	
	@FederateParameter  
	 public String Accel_Pedal_Position;
	@FederateParameter
	 public String Brake_Pressure;
	
	@FederateParameter
	 public double messageTime;
	@FederateParameter
	 public String COA_Message;

}
