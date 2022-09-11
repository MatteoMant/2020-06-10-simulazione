package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	
	// risultati del simulatore
	private int numPause;
	private Set<Actor> intervistati;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		dao.loadAllActors(idMap);
	}
	
	public void creaGrafo(String genre) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, this.dao.listAllActorsWithGenre(genre));
		
		// Aggiunta degli archi
		for (Adiacenza a : this.dao.listAllAdiacenzeWithGenre(genre, idMap)) {
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public void simula(int n) {
		Simulatore sim = new Simulatore(this.grafo);
		sim.init(n);
		sim.run();
		this.numPause = sim.getNumPause();
		this.intervistati = sim.getIntervistati();
	}
	
	public List<Actor> getAttoriSimili(Actor attore){
		List<Actor> attori = new LinkedList<Actor>();
		GraphIterator<Actor, DefaultWeightedEdge> iteratore = new DepthFirstIterator<Actor, DefaultWeightedEdge>(this.grafo, attore);
		while (iteratore.hasNext()) {
			attori.add(iteratore.next());
		}

		return attori;
	}
	
	public List<String> getAllGenres(){
		return this.dao.listAllGenres();
	}
	
	public Set<Actor> getAllActors(){
		return this.grafo.vertexSet();
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}

	public int getNumPause() {
		return numPause;
	}

	public Set<Actor> getIntervistati() {
		return intervistati;
	}

}
