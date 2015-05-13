package Mapa;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage imagem;
    private int tipo;
    
    //Tipos do tile
    public static final int LIVRE = 1; //Tipo de tile que pode ser ultrapassada por nenhuma entidade do jogo
    public static final int BLOQUEADA = 0; //Tipo de tile que não pode ser ultrapassada por nenhuma entidade do jogo
    
    public Tile(BufferedImage imagem, int tipo){
        this.imagem = imagem;
        this.tipo = tipo;
    }

    //Retorna o tipo deste tile
    public int getTipo() {
        return tipo;
    }

    //Retorna a imagem deste tile
    public BufferedImage getImagem() {
        return imagem;
    } 
    
}
