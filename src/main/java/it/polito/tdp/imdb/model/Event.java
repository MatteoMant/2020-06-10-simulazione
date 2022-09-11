package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event>{

	public enum EventType {
		DA_INTERVISTARE,
		FERIE
	}
	
	private int giorno;
	private EventType type;
	private Actor intervistato;
	
	public Event(int giorno, EventType type, Actor intervistato) {
		super();
		this.giorno = giorno;
		this.type = type;
		this.intervistato = intervistato;
	}

	public int getGiorno() {
		return giorno;
	}

	public EventType getType() {
		return type;
	}

	public Actor getIntervistato() {
		return intervistato;
	}

	@Override
	public int compareTo(Event other) {
		return this.getGiorno() - other.getGiorno();
	}
	
}
