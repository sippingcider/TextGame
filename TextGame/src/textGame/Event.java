package textGame;

import enums.AreaType;
import enums.PersonType;

public class Event {
	private AreaType aTrigger;
	private PersonType pTrigger;
	private String message;
	
	public Event (AreaType t, String s) {
		setaTrigger(t); 
		setMessage(s);
		setpTrigger(null);
	}
	
	public Event (PersonType p, String s) {
		setaTrigger(null); 
		setMessage(s);
		setpTrigger(p);
	}

	public AreaType getaTrigger() {
		return aTrigger;
	}

	public void setaTrigger(AreaType trigger) {
		aTrigger = trigger;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PersonType getpTrigger() {
		return pTrigger;
	}

	public void setpTrigger(PersonType pTrigger) {
		this.pTrigger = pTrigger;
	}
}
