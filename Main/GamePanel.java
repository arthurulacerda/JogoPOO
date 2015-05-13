package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import EstadosDoJogo.GerenciadorDeEstados;


public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static final int LARGURA = 1280;
    public static final int ALTURA = 720;
    Thread thread; 
    private final int FPS = 60; // Quadros por segundos
    private final int tempoPorFrame = 1000/FPS; // Tempo mínimo que cada quadro deve gastar para ser desenhado
    BufferedImage imagem; //Imagem que armazena o quadro atual antes ser desenhado no painel
    Graphics2D g; //Usado para desenhar na imagem
    GerenciadorDeEstados gs; 
    
    public GamePanel(){
        super();
        this.setPreferredSize(new Dimension(LARGURA, ALTURA)); // Definindo o tamanho do painel
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);
        this.setVisible(true);
    }
    
    
    /**
     * A função addNotify é chamada quando GamePanel é adicionado à janela principal.
     * Ela será usada para criar um nova thread assim que isso acontecer.
     */
    @Override
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {

        //Inicializando variáveis
        imagem = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) imagem.getGraphics();
        gs = new GerenciadorDeEstados();
        
        
        long tempoInicial; //Tempo antes do quadro atual ser desenhado
        long tempoFinal; //Tempo após o quadro ser desenhado
        long tempoDeEspera; //Tempo em que o while loop deve esperar antes de começar a desenhar o próximo frame
        
        
        // GAME LOOP
        while(true){
            tempoInicial = System.nanoTime(); // tempo inicial em nanossegundos
            
            //atualizando, renderizando e desenhando o novo quadro
            atualizar();
            renderizar();
            desenhar();
            
            tempoFinal = (System.nanoTime() - tempoInicial)/1000000; //tempo final em milissegundos
            tempoDeEspera = tempoPorFrame - tempoFinal; //tempo de espera em milissegundos
            
            if(tempoDeEspera > 0){
                try {
                    Thread.sleep(tempoDeEspera);
                
                } catch (InterruptedException ex) {
                    System.out.println("A thread do painel foi interrompida inesperadamente.");
                    ex.printStackTrace();
                }
            }
        }
        
    }
    
    //Atualiza a posição do mapa, do personagem, dos inimigos e dos projéteis
    private void atualizar(){
        gs.atualizar();
    }
    
    //Desenha o mapa, o personagem, os inimigos e os projéteis na imagem
    private void renderizar(){
        gs.renderizar(g);
    }
    
    //Desenha a imagem no painel
    private void desenhar(){
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(imagem, 0, 0, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        

    }

    @Override
    public void keyPressed(KeyEvent e) {
        gs.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gs.keyReleased(e.getKeyCode());
    }
}
