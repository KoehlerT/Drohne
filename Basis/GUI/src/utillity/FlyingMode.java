package utillity;

public enum FlyingMode {
	FORCESTOP, FORCEDOWN, MANUAL, AUTOMATIC;
	
	@Override
	public String toString(){
		switch(this) {
		case FORCESTOP: return "Not Aus";
		case FORCEDOWN: return "Landung";
		case MANUAL: return "Manuell";
		case AUTOMATIC: return "Automatisch";
		default: return "";
		}
	}
}
