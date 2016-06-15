package implementacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class Grafo {
	int aux;
	int numVertices;
	int numArestas;
	int matrizAdjacente [][];
	int memoriza[][]; 
	Random numAleatorio = new Random();
	int [] explorado;           /* vetor para armazenar se o vertice foi explorado pelo DFS*/
	int [] ciclo;               /* ciclo atual */
	int min;                    /* usado para selecionar o menor ciclo */
	int [] melhorCiclo;         /* melhor ciclo encontrado */
	int nivel;                  /* profundidade alcancada pelo DFS*/
	
	/*-----------------------------------------*/
	//Método construtor: cria gráfico de n vértices limpo
	public Grafo (int n, int m){
		nivel=0;  
		numVertices = n;
		numArestas = m;
		explorado = new int[numVertices];
	    ciclo = new int[numVertices];
	    melhorCiclo = new int[numVertices];
        min = 1000000;
		matrizAdjacente = new int [n][n];
		aux = (int)Math.pow(2, numVertices)-1;
		memoriza = new int [numVertices][aux];
		for (int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				matrizAdjacente[i][j] = 999999;
		for (int i = 0; i < numVertices; i++)
			for(int j = 0; j < aux; j++) 
				memoriza[i][j] = -1;
		for(int i=0; i<numVertices;i++)
            explorado[i]=0;   
	}
	/*-----------------------------------------*/
	//Gera grafo aleatório com numArestas 
	void gerarGrafoAleatório (){
		for(int i = 0; i < numArestas; i++)
			gerarAresta();
	}
	/*-----------------------------------------*/
	//função que gera aresta aleatória e adiciona no grafo
	void gerarAresta (){
		int v1 = -1;
		int v2 = -1;
		boolean arestaAcrescentada = false;
		//Adicionando nova aresta
		do{
			//Grafo simples
			while(v1 == v2){
				//vértice 1
				v1 = numAleatorio.nextInt(numVertices);
				//System.out.println("Aresta 1: " + v1);
				//vértice 2
				v2 = numAleatorio.nextInt(numVertices);
				//System.out.println("Aresta 2: " + v2);
			}
			if(matrizAdjacente[v1][v2] == 999999){
				//valor de custo aleatório entre 1 e 10 para nova aresta
				//nextInt gera números pseudoaleatórios uniformemente distribuído
				matrizAdjacente[v1][v2] = 1 + numAleatorio.nextInt(10); 
				matrizAdjacente[v2][v1] = matrizAdjacente[v1][v2];
				//System.out.println("Custo: " + matrizAdjacente[v1][v2]);
				arestaAcrescentada = true;
			}
			else{
				v1 = v2 = -1;
			}
		//Do-while termina quando uma nova aresta for adicionada
		}while(arestaAcrescentada == false);	
	}
	/*-----------------------------------------*/
	//imprime a matriz de adjacências
	void imprimirMatriz (){
		for (int i = 0; i < numVertices; i++) {
		    for (int j = 0; j < numVertices; j++) {
		        System.out.print(matrizAdjacente[i][j]+" ");
		    }
		    System.out.print("\n");
		}
	}
	
	void imprimirMatrizMemoriza (){
		for (int i = 0; i < numVertices; i++) {
		    for (int j = 0; j < 10; j++) {
		        System.out.print(memoriza[i][j]+" ");
		    }
		    System.out.print("\n");
		}
	}
	//TPS com programação dinâmica
	//c é cidade atual
	//b -> cidades visitadas
	int TSP (int c, int b){
		if(b == ((int)Math.pow(2,numVertices)-1))
			return matrizAdjacente[c][0];
		else if (memoriza[c][b]!=-1)
			return memoriza[c][b];
		int resposta = 999999; //infinito
		for(int i = 1; i < numVertices; i++){
			if ((int)(b/Math.pow(2,i)%2)==0){
				int a = TSP(i, b + (int) Math.pow(2, i));
				if(matrizAdjacente[c][i] + a < resposta){
					resposta = matrizAdjacente[c][i] + a;			 
				}	
			}
		}
		memoriza[c][b] = resposta;		
	    return resposta;
	}
	/*-----------------------------------------*/
	private int medeciclo(int[] t) {
        int i;
        int l=0;
        
        for(i=0;i<numVertices-1;i++)
            l=l+matrizAdjacente[t[i]][t[i+1]];
        
        l=l+matrizAdjacente[t[numVertices-1]][t[0]];
        
        return l;
    }
	/*-----------------------------------------*/
    private void dfs(int v, int nivel) {
        int i,j,dist;
        explorado[v] = 1;
        ciclo[nivel] = v;
        if(nivel==(numVertices-1)){
            /* completou um ciclo */
            dist = medeciclo(ciclo);
            if(dist < min){
                min = dist;
                for(j=0; j<numVertices; j++)
                    melhorCiclo[j]=ciclo[j];
            }
        }
        for(i=0;i<numVertices;i++){
            if(explorado[i]!=1){
                dfs(i,nivel+1);
                explorado[i]=0;
            }
        }
    }
    /*-----------------------------------------*/
	public static void main(String[] args) {
		//teste	
		int distanciaTotal;
		Grafo g1 = new Grafo(10, 45);// aqui você pode mudar as quantidades de vértice e de arestas
		g1.gerarGrafoAleatório();
		g1.imprimirMatriz();
		
		Date horaInicioPD = new Date();
		long horaInicioLongPD = horaInicioPD.getTime();
		distanciaTotal = g1.TSP(0,1);
		Date horaFinalPD = new Date();
		long horaFinalLongPD = horaFinalPD.getTime();
		long tempoExecucaoPD = (horaFinalLongPD - horaInicioLongPD);
		System.out.println("PD: Tempo de execução = " + tempoExecucaoPD +" milisegundos");
		
		//g1.imprimirMatrizMemoriza();
		System.out.println("Distância Total(Programção Dinâmica) = " + distanciaTotal);
		
		Date hi = new Date();
		long horaInicio = hi.getTime();
		g1.dfs(0,0);
		Date hf = new Date();
		long horaFinal = hf.getTime();
		long tempoExecucao2 = (horaFinal - horaInicio);
		System.out.println("Tempo de execução = " + tempoExecucao2 + " milisegundos");
		System.out.println("Melhor Ciclo pelo método força bruta:");
		for(int i=0;i<g1.numVertices;i++) {
            if(i>0)
            	System.out.print("-");
            System.out.print(g1.melhorCiclo[i]);
        }
		int contador = 0;
		for (int i = 0; i < g1.numVertices; i++)
			for(int j = 0; j < g1.aux; j++)
				if(g1.memoriza[i][j]!=-1)
					contador++;
		System.out.println("");
		System.out.println("Elementos de memória PD = " + contador);
		
		
		
	}
}
