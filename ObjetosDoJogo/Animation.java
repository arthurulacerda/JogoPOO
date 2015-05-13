package ObjetosDoJogo;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int frameAtual; //Posição do frame atual
	
	private long tempoInicial;//Tempo em que o frame anterior foi desenhado
	private long delay; //Delay entre os frames da animação
	
	private boolean executadaUmaVez; //Será verdadeiro se todos os frames da animação já foram desenhados uma vez
	
	public Animation() {
		executadaUmaVez = false;
	}
	
        /**
         * Função que muda os frames de uma animação
         * @param frames : Vetor de imagem contendo todos os frames da animação
         */
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		frameAtual = 0; //Primeiro frame
		tempoInicial = System.nanoTime();
		executadaUmaVez = false;
	}
	
        /**
         * Função que modifica o delay entre os frames da animação
         * @param d : delay em milissegundos
         */
	public void setDelay(long d){ 
            delay = d; 
        }
        
        /**
         * Seta o frame inicial para i
         * @param i 
         */
	public void setFrame(int i) { 
            frameAtual = i; 
        }
	
        
	public void update() {
		
            //Se o delay é igual a -1, a animação ainda não contém nenhum frame    
            if(delay == -1) 
                return;
            
            //Tempo desde que o frame anterior foi desenhado
            long elapsed = (System.nanoTime() - tempoInicial) / 1000000;
            
            /* Se o tempo desde que o frame anterior foi desenhado for maior do que o delay, 
               frameAtual é incrementada para garantir que o próximo frame será desenhado */
            if(elapsed > delay) {
                    frameAtual++;
                    tempoInicial = System.nanoTime();
            }
            
            /* Se todos os frames da animação já foram desenhados, frameAtual recebe 0 para garantir 
               que a animação se repita quantas vezes forem necessárias */
            if(frameAtual == frames.length) {
                    frameAtual = 0;
                    executadaUmaVez = true;
            }
		
	}
	
        //Retorna a posição do frame atual
	public int getFrame() { 
            return frameAtual; 
        }
        
        //Retorna o frame atual
	public BufferedImage getImage() { 
            return frames[frameAtual]; 
        }
        
        
	public boolean foiExecutadaUmaVez() { 
            return executadaUmaVez; 
        }
	
}
