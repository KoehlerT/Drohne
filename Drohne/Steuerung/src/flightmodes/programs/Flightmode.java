package flightmodes.programs;

public interface Flightmode {
	
	
	public void onEnable();
	
	public void onDisable();
	
	public void onUpdate(float deltaTime);
	
	public void onCallback();
	
}
