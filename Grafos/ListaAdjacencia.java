package Grafos;

import java.util.*;

public class ListaAdjacencia {

    private int nVertices;
    private List<List<Aresta>>adjacencias;
    private boolean direcionado;
    private boolean[] verticesValidos;

    public ListaAdjacencia(int nVertices, boolean direcionado){
        this.nVertices = nVertices;
        this.direcionado = direcionado;
        this.adjacencias = new ArrayList<>(nVertices);
        for (int i = 0; i < nVertices; i++) {
            adjacencias.add(new ArrayList<>());
        }
        this.verticesValidos = new boolean[nVertices];
        for (boolean vertice: verticesValidos) {
            vertice = true;
        }
    }

    public int getnVertices() {
        return nVertices;
    }

    //Para não 'estragar' a lista de adjacência fazendo seus índices diminuir em um, criei um sistema de 'desativar' o vértice,
    // que pode ser reativado com 'reativaVertice', assim um vértice 'n' pode deixar de existir, mas n+1 permanece no grafo.
    public boolean removeVertice(int num){
        if (num >= nVertices || num < 0)
            return false;
        for (List<Aresta> arestas: adjacencias) {
            for (Aresta aresta: arestas) {
                if (aresta.destino == 3 || aresta.origem ==3 )
                    arestas.remove(aresta);
            }
        }
        verticesValidos[num] = false;
        return true;
    }

    public boolean reativaVertice(int num){
        if (num >= nVertices || num < 0)
            return false;
        if (verticesValidos[num]==true)
            return false;
        verticesValidos[num] = true;
        return true;
    }

    public boolean isConexo(){
        if (nVertices==0)
            return true;

        boolean[] checked = new boolean[nVertices];
        dfs(0,checked);

        for (boolean vertice: checked){
            if (!vertice)
                return false;
        }
        return true;
    }

    public boolean isCompleto(){
        for (int i = 0;i < nVertices; i++){
            if (!verticesValidos[i])
                continue;
            if (adjacencias.get(i).size() < nVertices-1);
            return false;
        }
        return true;
    }

    private void dfs(int vertice, boolean[] checked){
        checked[vertice] = true;

        for (Aresta aresta: adjacencias.get(vertice)) {
            if (!checked[aresta.destino]){
                dfs(aresta.destino, checked);
            }
        }

    }

    public int dijkstra(int origem, int destino){
        int[] peso = new int[nVertices];
        boolean checked[] = new boolean[nVertices];

        Arrays.fill(peso, Integer.MAX_VALUE);

        PriorityQueue<Integer> prio = new PriorityQueue<>(Comparator.comparingInt(vertice -> peso[vertice]));

        peso[origem] = 0;
        int[] pre = new int[adjacencias.size()];
        prio.add(origem);

        while (!prio.isEmpty()) {
            int vertice = prio.poll();

            for (Aresta aresta : adjacencias.get(vertice)) {
                int next = aresta.destino;
                int novoPeso = peso[vertice] + aresta.peso;

                if (novoPeso < peso[next]) {
                    peso[next] = novoPeso;
                    pre[next] = vertice;
                    prio.add(next);
                }
            }
        }
        return peso[destino];

    }

    public int menorPeso(int[] peso,boolean[] checked, PriorityQueue<Integer> prio){
        boolean flagFirst = false;
        int pesoMin = Integer.MAX_VALUE;
        int vertice = 0;
        for (int i = 0; i < nVertices; i++){
            if (!flagFirst){
                pesoMin = peso[i];
                vertice = i;
                flagFirst = true;
            }

            if (peso[i] < pesoMin && !checked[i]){
                pesoMin = peso[i];
                vertice = i;
                prio.add(vertice);
            }

        }
        return vertice;
    }

    public void adicionaAresta(int origem, int destino, int peso){
        Aresta aresta = new Aresta(origem,destino,peso);
        adjacencias.get(origem).add(aresta);
        if (!direcionado){
            Aresta arestaInvertida = new Aresta(destino,origem,peso);
            adjacencias.get(destino).add(arestaInvertida);
        }
    }

    public void adicionaAresta(int origem, int destino){
        adicionaAresta(origem,destino,1);
    }

    public void mostrarLista(){
        for (int i = 0; i < nVertices; i++) {
            System.out.print("Vértice "+i+":");
            for (Aresta aresta: adjacencias.get(i)) {
                System.out.print("("+aresta.destino+", Peso:"+aresta.peso+")");
            }
            System.out.println();
        }
    }

    public void removeAresta(int origem, int destino){
        List<Aresta> arestaOrigem = adjacencias.get(origem);
        for (Aresta aresta : arestaOrigem){
            if (aresta.destino == destino){
                arestaOrigem.remove(aresta);
                break;
            }
        }
        if (!direcionado){
            List<Aresta> arestaDestino = adjacencias.get(destino);
            for (Aresta aresta : arestaDestino){
                if (aresta.destino == origem){
                    arestaDestino.remove(aresta);
                    break;
                }
            }
        }
    }

    public void ListaVertice(int v){
        List<Aresta> arestasVertice = adjacencias.get(v);
        for (Aresta aresta: arestasVertice){
            System.out.print("("+aresta.destino+", Peso:"+aresta.peso+")");
        }
        System.out.println();
    }

    public boolean isAdjacente(int origem, int destino){
        List<Aresta> arestasOrigem = adjacencias.get(origem);
        for (Aresta aresta: arestasOrigem){
            if (aresta.destino==destino)
                return true;
        }
        return false;
    }

    public void buscaEmProfundidade(int verticeInicial) {
        boolean[] visitado = new boolean[nVertices];
        Stack<Integer> pilha = new Stack<>();

        visitado[verticeInicial] = true;
        pilha.push(verticeInicial);

        System.out.println("Resultado da busca em profundidade a partir do vértice " + verticeInicial + ":");
        while (!pilha.isEmpty()) {
            int verticeAtual = pilha.pop();
            System.out.print(verticeAtual + " ");

            for (Aresta aresta : adjacencias.get(verticeAtual)) {
                int vizinho = aresta.destino;
                if (!visitado[vizinho]) {
                    visitado[vizinho] = true;
                    pilha.push(vizinho);
                }
            }
        }
        System.out.println();
    }

    private int isEuleriano(){
        for (int i = 0; i < nVertices; i++) {
            if (adjacencias.get(i).size() % 2 != 0) {
                int count = 0;
                for (int j = 0; j < nVertices; j++) {
                    if (adjacencias.get(i).size() % 2 != 0)
                        count++;
                    if (count > 2)
                        return -1;
                }
                if (count == 2)
                    return 0;
                return -1;
            }
        }
        return 1;
    }

    public void ehEuleriano(){
        int valor = isEuleriano();
        if (valor==1)
            System.out.println("É Euleriano");
        else if (valor==0)
            System.out.println("É Semi-Euleriano");
        else
            System.out.println("Não é Euleriano");
    }

    private int isHamiltoniano() {
        int[] path = new int[nVertices];
        Stack<Integer> pilha = new Stack<>();

        for (int i = 0; i < nVertices; i++) {
            path[i] = -1;
        }

        path[0] = 0;
        pilha.push(0);

        int result = cicle(path, pilha, 1);

        if (result != 0) {
            for (int i : path) {
                System.out.print(i + " ");
            }
        }
        return result;
    }

    private int cicle(int[] caminho, Stack<Integer> pilha, int pos) {
        if (pos == nVertices) {
            int ultimoVertice = caminho[pos - 1];
            int primeiroVertice = caminho[0];
            if (isAdjacente(ultimoVertice, primeiroVertice)) {
                return 1;
            } else {
                return 0;
            }
        }
        for (int i = 1; i < nVertices; i++) {
            if (isAdjacente(pilha.peek(), i) && !pilha.contains(i)) {

                caminho[pos] = i;
                pilha.push(i);

                int result = cicle(caminho, pilha, pos + 1);
                if (result != -1) {
                    return result;
                }
                caminho[pos] = -1;
                pilha.pop();
            }
        }
        return -1;
    }

    public void ehHamiltoniano(){
        int valor = isHamiltoniano();
        if (valor==1)
            System.out.println("É Hamiltoniano");
        else if (valor==0)
            System.out.println("É Semi-Hamiltoniano");
        else
            System.out.println("Não é Hamiltoniano");
    }

}
