package Grafos;

public class ListaAdjacenciasMain {
    public static void main(String[] args) {
        ListaAdjacencia grafo = new ListaAdjacencia(7,true);

        grafo.adicionaAresta(1,2,130);
        grafo.adicionaAresta(2,3,70);
        grafo.adicionaAresta(3,4,80);
        grafo.adicionaAresta(4,5,70);
        grafo.adicionaAresta(5,6,50);

        //Simplifiquei implementando apenas o caminho que já havia calculado como mais rápido

        System.out.println("Lisboa a Braga:"+grafo.dijkstra(1,6));


    }
}
