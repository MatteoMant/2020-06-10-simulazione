package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulatore {

	// Parametri della simulazione
	private int MAX_GIORNI;

	// Valori in uscita (output della simulazione)
	private Set<Actor> intervistati;
	private int numPause;
	private String generePrecedente;

	// Stato del mondo
	private Graph<Actor, DefaultWeightedEdge> grafo;

	// Coda degli eventi
	private PriorityQueue<Event> queue;

	public Simulatore(Graph<Actor, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public void init(int n) {
		this.MAX_GIORNI = n;
		this.intervistati = new HashSet<>();
		this.numPause = 0; // inizialmente il numero di pause è 0
		this.generePrecedente = null;

		// creo la coda
		this.queue = new PriorityQueue<>();

		if (this.MAX_GIORNI == 0) {
			return;
		}
		// adesso pre-carico la coda con l'intervista che verrà fatta nel primo giorno
		Actor intervistato = selezionaIntervistato(this.grafo.vertexSet());
		this.generePrecedente = intervistato.getGender();
		this.intervistati.add(intervistato);
		this.queue.add(new Event(1, EventType.DA_INTERVISTARE, intervistato));
	}

	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		if (e.getGiorno() >= this.MAX_GIORNI) {
			return;
		}
		switch (e.getType()) {
		case DA_INTERVISTARE:

			double caso = Math.random(); // tiro un numero a caso

			Actor intervistato;

			if (caso < 0.6) {
				intervistato = selezionaIntervistato(this.grafo.vertexSet());
			} else {
				intervistato = selezionaVicino(e.getIntervistato());
			}

			this.intervistati.add(intervistato);

			if (intervistato.getGender().equals(this.generePrecedente)) {
				double pausa = Math.random();

				if (pausa <= 0.90) {
					this.queue.add(new Event(e.getGiorno() + 1, EventType.FERIE, intervistato));
				} else {
					this.queue.add(new Event(e.getGiorno() + 1, EventType.DA_INTERVISTARE, intervistato));
				}
			} else {
				this.queue.add(new Event(e.getGiorno() + 1, EventType.DA_INTERVISTARE, intervistato));
			}

			this.generePrecedente = intervistato.getGender();

			break;

		case FERIE:

			this.generePrecedente = null;

			this.queue.add(new Event(e.getGiorno() + 1, EventType.DA_INTERVISTARE, e.getIntervistato()));
			this.numPause++;

			break;
		}
	}

	private Actor selezionaIntervistato(Set<Actor> vertexSet) {
		Set<Actor> candidati = new HashSet<>(vertexSet);
		candidati.removeAll(this.intervistati);

		if (candidati.size() == 0) {
			return null;
		}

		int scelto = (int) (Math.random() * candidati.size());

		return (new ArrayList<Actor>(candidati)).get(scelto);
	}

	private Actor selezionaVicino(Actor attore) {
		List<Actor> vicini = Graphs.neighborListOf(this.grafo, attore);
		vicini.removeAll(this.intervistati); // dopo aver tolto gli intervistati posso calcolare il massimo

		if (vicini.size() == 0) {
			// in questo caso il vertice è isolato oppure tutti i suoi adiacenti sono già
			// stati intervistati
			return null;
		}

		double max = 0.0;
		for (Actor a : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(attore, a));
			if (peso > max) {
				max = peso;
			}
		}
		// dopo aver calcolato il massimo creo una lista di vicini che hanno questo peso
		// "max"
		List<Actor> migliori = new ArrayList<>();
		for (Actor a : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(attore, a));
			if (peso == max) {
				migliori.add(a); // aggiungo il vertice alla lista dei vertici "migliori"
			}
		}

		int scelto = (int) (Math.random() * migliori.size());
		return migliori.get(scelto); // restituisco un elemento a caso da questa lista
	}

	public Set<Actor> getIntervistati() {
		return intervistati;
	}

	public int getNumPause() {
		return numPause;
	}

}
