package ObjetosDoJogo;

import Main.GamePanel;
import Mapa.TileMap;
import Mapa.Tile;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class ObjetoDoMapa {
	
    // Informações sobre o mapa
    protected TileMap tileMap; //O próprio mapa
    protected int tileSize; // Tamanho de uma tile (em pixels)
    protected double xmap; //Posição x do mapa
    protected double ymap; //Posição y do mapa

    // Posição
    protected double x;
    protected double y;
    
    // Vetor
    protected double dx;
    protected double dy;

    // Dimenções
    protected int largura;
    protected int altura;

    // Caixa de colisão
    protected int cLargura;
    protected int cAltura;

    // Colisão
    protected int linhaAtual; // linha em que o objeto se encontra no mapa
    protected int colunaAtual; // coluna em que o objeto se encontra no mapa
    protected double xdest; // coordenada x de destino
    protected double ydest; // coordenada y de destino
    protected double xtemp;
    protected double ytemp;
    protected boolean superiorEsquerdo;
    protected boolean superiorDireito;
    protected boolean inferiorEsquerdo;
    protected boolean inferiorDireito;

    // Animação
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;
    protected boolean facingLeft;
    protected boolean facingScreen;

    // Movimento
    protected boolean esquerda;
    protected boolean direita;
    protected boolean cima;
    protected boolean baixo;
    protected boolean pulando;
    protected boolean caindo;
    protected boolean atacando;

    // Velocidades
    protected double aceleracao;//moveSpeed;
    protected double velocidadeMaxima;//maxSpeed;
    protected double desaceleracao; //stopSpeed
    protected double aceleracaoQueda;//fallSpeed;
    protected double velocidadeMaximaQueda;//maxFallSpeed;
    protected double alturaInicialPulo;//jumpStart;
    protected double desaceleracaoPulo;//stopJumpSpeed;

    // Todo objeto de um mapa deve conter as informações sobre o mapa em que ele se encontra
    public ObjetoDoMapa(TileMap tm) {
            tileMap = tm;
            tileSize = tm.getTileSize(); 
    }

    //Função que verifica se dois objetos do mapa se intersectaram
    public boolean intersects(ObjetoDoMapa o) {
            Rectangle r1 = getRectangle();
            Rectangle r2 = o.getRectangle();
            return r1.intersects(r2);
    }

    //Retorna um retangulo que representa a caixa de colisão deste objeto
    public Rectangle getRectangle() {
            return new Rectangle((int)x - cLargura,(int)y - cAltura,cLargura,cAltura);
    }


    /**
     * Função que calcula se um objeto do mapa encontrou alguma tile bloqueada em
     * algum dos seus cantos
     * @param x : Coordenada x do objeto
     * @param y : Coordenada y do objeto
     */
    public void calculaOsCantos(double x, double y) {

            int tileDaEsquerda = (int)(x - cLargura / 2) / tileSize;
            int tileDaDireita = (int)(x + cLargura / 2 - 1) / tileSize;
            int tileDeCima = (int)(y - cAltura / 2) / tileSize;
            int tileDeBaixo = (int)(y + cAltura / 2 - 1) / tileSize;

            int tl = tileMap.getTileType(tileDeCima, tileDaEsquerda);
            int tr = tileMap.getTileType(tileDeCima, tileDaDireita);
            int bl = tileMap.getTileType(tileDeBaixo, tileDaEsquerda);
            int br = tileMap.getTileType(tileDeBaixo, tileDaDireita);

            superiorEsquerdo = tl == Tile.BLOQUEADA;
            superiorDireito = tr == Tile.BLOQUEADA;
            inferiorEsquerdo = bl == Tile.BLOQUEADA;
            inferiorDireito = br == Tile.BLOQUEADA;	
    }


    /**
     * Função que checa se o objeto colidiu com o mapa
     */
    public void checaColisaoComOMapa() {

            colunaAtual = (int)x / tileSize;
            linhaAtual = (int)y / tileSize;

            xdest = x + dx;
            ydest = y + dy;

            xtemp = x; //x será setado para xtemp assim que o objeto for atualizado
            ytemp = y; //y será setado para ytemp assim que o objeto for atualizado

            //checando se o próximo movimento na direção y gerará uma colisão com o mapa
            calculaOsCantos(x, ydest);

            //Se o objeto estiver se movimentando para cima
            if(dy < 0) {

                    /* Se o objeto encontrar uma tile bloqueada acima dele, isso significa que
                       ele colidiu com o mapa e seu movimento deve ser finalizado*/
                    if(superiorEsquerdo || superiorDireito) {
                            dy = 0; //Finalizando o movimento
                            ytemp = linhaAtual * tileSize + cAltura / 2; //A coordenada y do objeto será setada para o fim da linha atual
                    }

                    //Se objeto não encontrar nenhuma tile acima dele, o movimento deve continuar
                    else 
                        ytemp += dy;

            }

            //Se o objeto estiver se movimentando para baixo
            if(dy > 0) {
                    /* Se o objeto encontrar uma tile bloqueada abaixo dele, isso significa que
                       ele estava caido e agora atingiu o chão, ou seja, seu movimento deve ser finalizado */
                    if(inferiorEsquerdo || inferiorDireito) {
                            dy = 0; // Finalizando o movimento
                            caindo = false; //O objeto não está mais caindo
                            ytemp = (linhaAtual + 1) * tileSize - cAltura / 2; //A coordenada y do objeto será setada para o fim da linha atual
                    }

                    //Se objeto não encontrar nenhuma tile abaixo dele, ele deve continuar caindo
                    else 
                        ytemp += dy;

            }



            //checando se o próximo movimento na direção x gerará uma colisão com o mapa
            calculaOsCantos(xdest, y);

            //Se o objeto estiver se movimentando para a esquerda
            if(dx < 0) {

                /* Se o objeto encontrar uma tile bloqueada à esquerda dele, isso significa que
                    ele colidiu com o mapa e seu movimento deve ser finalizado*/
                    if(superiorEsquerdo || inferiorEsquerdo) {
                            dx = 0; //Finalizando o movimento
                            xtemp = colunaAtual * tileSize + cLargura / 2;//A coordenada x do objeto será setada para o fim da coluna atual
                    }

                    //Se objeto não encontrar nenhuma tile à esquerda dele, o movimento deve continuar
                    else 
                        xtemp += dx;

            }

            //Se o objeto estiver se movimentando para a direita
            if(dx > 0) {
                /* Se o objeto encontrar uma tile bloqueada à direita dele, isso significa que
                    ele colidiu com o mapa e seu movimento deve ser finalizado*/
                    if(superiorDireito || inferiorDireito) {
                            dx = 0; //Finalizando o movimento
                            xtemp = (colunaAtual + 1) * tileSize - cLargura / 2; //A coordenada x do objeto será setada para o fim da coluna atual
                    }

                    //Se objeto não encontrar nenhuma tile à direita dele, o movimento deve continuar
                    else 
                        xtemp += dx;

            }


            //Se o objeto não estiver caindo
            if(!caindo) {
                    //Verificando se, no próximo movimento na direção y, 1 pixel abaixo deste nível, ainda existem tiles bloqueadas.
                    calculaOsCantos(x, ydest + 1);

                    //Se não existirem, o objeto deve começar a cair
                    if(!inferiorEsquerdo && !inferiorDireito) 
                            caindo = true;
            }

    }
	
        
    public int getx() { 
        return (int)x; 
    }

    public int gety() { 
        return (int)y; 
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public int getcLargura() {
        return cLargura;
    }

    public int getcAltura() {
        return cAltura;
    }

    

    public void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
    }
    public void setVector(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
    }

    public void setMapPosition() {
            xmap = tileMap.getX();
            ymap = tileMap.getY();
    }

    public void setEsquerda(boolean esquerda) {
        this.esquerda = esquerda;
    }

    public void setDireita(boolean direita) {
        this.direita = direita;
    }

    public void setCima(boolean cima) {
        this.cima = cima;
    }

    public void setBaixo(boolean baixo) {
        this.baixo = baixo;
    }

    public void setPulando(boolean pulando) {
        this.pulando = pulando;
    }

    public void setAtacando(boolean atacando) {
        this.atacando = atacando;
    }
    
    
        
    
    public void renderizar(Graphics2D g) {
            if(facingRight) 
                g.drawImage(animation.getImage(),(int)(x + xmap - largura / 2),(int)(y + ymap - altura / 2),null);

            else
                g.drawImage(animation.getImage(),(int)(x + xmap + largura / 2),(int)(y + ymap - altura / 2),-largura,altura,null);
    }
}

