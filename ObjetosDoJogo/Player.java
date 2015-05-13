package ObjetosDoJogo;

import Mapa.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends ObjetoDoMapa {
    
	//Ações
	private static final int PARADO = 0;
	private static final int VIRADO_ANDANDO = 1;
	private static final int VIRADO_ATACANDO = 2;
	private static final int VOLTANDO = 3;
	private static final int VIRANDO = 4;
	private static final int VIRADO_PULANDO = 5;
	private static final int PULANDO = 6;
	private static final int PARANDO_DE_PULAR = 7;
	private static final int RECUPERANDO_PULO = 8;
        
        
        // Animações
	private ArrayList<BufferedImage[]> sprites; 
	private final int[] numFrames = {1, 4, 7, 2, 2 ,2 ,2, 3, 3};
	
	public Player(TileMap tm) {
            super(tm);

            largura = 303;
            altura = 139;
            cLargura = 100;
            cAltura = 135;
            
            x = 150;
            y = 150;
            
            aceleracao = 0.5;
            velocidadeMaxima = 6;
            desaceleracao = 0.4;
            aceleracaoQueda = 0.15;
            velocidadeMaximaQueda = 4.0;
            alturaInicialPulo = -4.8;
            desaceleracaoPulo = 0.3;

            facingRight = true;
            facingLeft = false;
            facingScreen = true;
            atacando = false;


            //Armazenando todas as sprites no array "sprites"
            try {

                BufferedImage imagemSprites = ImageIO.read(getClass().getResourceAsStream("/Resources/Player/player.png"));

                sprites = new ArrayList<BufferedImage[]>();
                for(int i = 0; i < numFrames.length; i++) {

                        BufferedImage[] bi = new BufferedImage[numFrames[i]];

                        for(int j = 0; j < numFrames[i]; j++) 
                            bi[j] = imagemSprites.getSubimage(j * largura,i * altura, largura, altura);	

                        sprites.add(bi);
                }

            }
            catch(Exception e) {
                    e.printStackTrace();
            }

            animation = new Animation();
            
            //Inicialmente, o player está parado
            currentAction = PARADO;
            animation.setFrames(sprites.get(PARADO));
            animation.setDelay(400);
		
	}
	
	
	private void getProximaPosicao() {
		
		// Movimento para a esquerda
                // A velocidade é aumentada até que ela alcance a velocidade máxima
		if(esquerda) {
			dx -= aceleracao;
			if(dx < -velocidadeMaxima)
				dx = -velocidadeMaxima;
			
		}
                
                // Movimento para a direita
                // A velocidade é aumentada até que ela alcance a velocidade máxima
		else if(direita) {
			dx += aceleracao;
			if(dx > velocidadeMaxima) {
				dx = velocidadeMaxima;
			}
		}
                
                /*Se o player não estiver se movimentando para direita ou para a esquerda, o player deve começar a desacelerar até
                  que ele pare */
		else {
			if(dx > 0) {
				dx -= desaceleracao;
				if(dx < 0) 
					dx = 0;
			}
                        
                        
			else if(dx < 0) {
				dx += desaceleracao;
				if(dx > 0) 
					dx = 0;
			}
		}
		
		
		//Só é possível pular se não estiver caindo
		if(pulando && !caindo) {
			dy = alturaInicialPulo;
			caindo = true;	
		}
		
		//Se o player estiver caindo, ele deve acelerar até que ele alcance a velocidade máxima de queda
		if(caindo) {
			dy += aceleracaoQueda;
			
			if(dy > 0) 
                            pulando = false;
                        
			if(dy < 0 && !pulando) dy += desaceleracaoPulo;
			
			if(dy > velocidadeMaximaQueda) dy = velocidadeMaximaQueda;	
		}
		
	}
	
	public void atualizar() {
		
		// Calculando a próxima posição do player
		getProximaPosicao();
                
                //Checando colisões com o mapa
		checaColisaoComOMapa();
                
                //Mudando a posição do player para a posição calculada em getProximaPosicao()
		setPosition(xtemp, ytemp);
		

		
		
		//Escolhendo a animação adequada 
                
                //Se o personagem está olhando para a tela
                if(facingScreen){
                    
                    //ATAQUE PARADO
                    if(atacando){
                        
                        //Virando o personagem para a direita
                        if(currentAction != VIRANDO && currentAction != VIRADO_ATACANDO){
                            currentAction = VIRANDO;
                            animation.setFrames(sprites.get(VIRANDO));
                            animation.setDelay(50);
                        }
                        
                        else if(currentAction == VIRANDO && animation.foiExecutadaUmaVez()){
                            currentAction = VIRADO_ATACANDO;
                            animation.setFrames(sprites.get(VIRADO_ATACANDO));
                            animation.setDelay(50);
                        }
                        
                        else if(currentAction == VIRADO_ATACANDO && animation.foiExecutadaUmaVez()){
                            currentAction = VOLTANDO;
                            animation.setFrames(sprites.get(VOLTANDO));
                            animation.setDelay(50);
                            atacando = false;
                        }
                    }
                    
                    
                    //PARADO
                    else if(!esquerda && !direita){
                        if(currentAction != PARADO){
                            currentAction = PARADO;
                            animation.setFrames(sprites.get(PARADO));
                            animation.setDelay(100);
                        }
                    }
                }
                
                
                //Se movendo
                if(esquerda || direita){
                    if(pulando && !atacando){
                        if(currentAction != VIRADO_PULANDO){
                            currentAction = VIRADO_ATACANDO;
                            animation.setFrames(sprites.get(VIRADO_ATACANDO));
                            animation.setDelay(150);
                        }
                    }
                    
                    else if(atacando){
                        if(currentAction != VIRADO_ATACANDO){
                            currentAction = VIRADO_ATACANDO;
                            animation.setFrames(sprites.get(VIRADO_ATACANDO));
                            animation.setDelay(50);
                        }
                        
                        if(animation.foiExecutadaUmaVez())
                            atacando = false;
                    }
                    
                    else{
                        if(currentAction != VIRADO_ANDANDO){
                            currentAction = VIRADO_ANDANDO;
                            animation.setFrames(sprites.get(VIRADO_ANDANDO));
                            animation.setDelay(100); 
                        }
                    }
                    
                    
                    facingScreen = false;
                }
                
                
                
                
		
                //Atualizando a animação, ou seja, escolhendo o próximo frame que deve ser desenhado
		animation.update();
		
		//Setando a orientação do player
		if(direita) 
                    facingRight = true;
		if(esquerda) 
                    facingRight = false;
                if(!direita && !esquerda)
                    facingScreen = true;
			
	}
	
        @Override
	public void renderizar(Graphics2D g) {
		//Setando a posição do mapa
		setMapPosition();
		
                //Desenhando o player
		super.renderizar(g);
		
	}
	
}
