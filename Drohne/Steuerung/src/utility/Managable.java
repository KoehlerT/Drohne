package utility;

public interface Managable {
	//Interface f�r alle Manager-Klassen
	//Jeder Manager muss seinen Programmteil starten und anzeigen, ob der Thread/ das Programm noch l�uft
	
	
	public void start(); //Startmethode, wird Anfangs aufgerufen, um Programmteil zu starten
	
	public boolean running(); //Gibt den Status des Programmteils zur�ck. false -> angehalten true -> Arbeitet
}
