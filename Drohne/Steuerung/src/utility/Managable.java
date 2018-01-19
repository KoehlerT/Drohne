package utility;

public interface Managable {
	//Interface für alle Manager-Klassen
	//Jeder Manager muss seinen Programmteil starten und anzeigen, ob der Thread/ das Programm noch läuft
	
	
	public void start(); //Startmethode, wird Anfangs aufgerufen, um Programmteil zu starten
	
	public boolean running(); //Gibt den Status des Programmteils zurück. false -> angehalten true -> Arbeitet
}
