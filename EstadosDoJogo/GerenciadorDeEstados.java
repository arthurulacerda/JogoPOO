package EstadosDoJogo;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GerenciadorDeEstados {
    private ArrayList<Estado> estadosDoJogo;
    private int estadoAtual; //Atual estados em que o jogo se encontra
    
    //Constantes que identificam os estados do jogo
    private final int MENU = 0;
    private final int LEVEL1 = 0;
    
    public GerenciadorDeEstados(){
        estadosDoJogo = new ArrayList<>();
        estadosDoJogo.add(new Level1());
        //adicionar os estados
        estadoAtual = LEVEL1; // Quando o jogo é iniciado, ele começa a executar o menu
    }
    
    //Atualiza o estado atual
    public void atualizar(){
        estadosDoJogo.get(estadoAtual).atualizar();
    }
    
    //Renderiza o estado atual
    public void renderizar(Graphics2D g){
        estadosDoJogo.get(estadoAtual).renderizar(g);
    }
    
    //Notifica o estado atual de que uma tecla foi pressionada
    public void keyPressed(int key){
        estadosDoJogo.get(estadoAtual).keyPressed(key);
    }
    
    //Notifica o estado atual de que uma tecla foi liberada
    public void keyReleased(int key){
        estadosDoJogo.get(estadoAtual).keyReleased(key);
    }
}
