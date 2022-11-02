package com.service.models;



import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LookupResponse implements Serializable {

	/**
	 * 
	 */
	public LookupResponse(String msg, boolean b) {
		super();
		this.responseMsg=msg;
		this.isNameAvailable=b;
		// TODO Auto-generated constructor stub
	}
	private String responseMsg;
	private boolean isNameAvailable;
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public boolean isNameAvailable() {
		return isNameAvailable;
	}
	public void setNameAvailable(boolean isNameAvailable) {
		this.isNameAvailable = isNameAvailable;
	}

	

    

   /*const AircraftRosterEntry = {
        operator: '',
        acsn: '',
        tailNumber: '',
        thrust: '',
    }*/


}