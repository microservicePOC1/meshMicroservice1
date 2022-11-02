package co.services.sample.models;



import lombok.EqualsAndHashCode;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@ToString
@EqualsAndHashCode
public class EnvMap implements Serializable {

	private Map<String, String> msConfigMap;
	
    public EnvMap(Map<String, String> linkedHashMap) {
    	super();
    	this.msConfigMap = linkedHashMap;
		// TODO Auto-generated constructor stub
	}

    public Map<String, String> getMsConfigMap() {
    	return this.msConfigMap;
    }
    public void setMsConfigMap(Map<String, String> linkedHashMap) {
    	this.msConfigMap = linkedHashMap;
    }


    

   /*const AircraftRosterEntry = {
        operator: '',
        acsn: '',
        tailNumber: '',
        thrust: '',
    }*/


}
