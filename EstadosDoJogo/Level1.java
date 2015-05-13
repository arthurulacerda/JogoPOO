package EstadosDoJogo;

import Main.GamePanel;
import ObjetosDoJogo.Player;
import java.awt.Graphics2D;
import Mapa.*;
import java.awt.event.KeyEvent;


public class Level1 implements Estado {
    private TileMap map;
    private final String localizacaoDaImagem = "/Resources/Mapas/Level1/tiles.png";
    private final String localizacaoDoMapa = "/Resources/Mapas/Level1/level1.txt";
    private final int larguraTiles = 70;
    private final int alturaTiles = 70;
    private final int numTilesColunaImagem = 1;
    private final int numTilesLinhaImagem = 2;
    private Player player;
    
    public Level1(){
        map = new TileMap(localizacaoDaImagem, localizacaoDoMapa, numTilesLinhaImagem, numTilesColunaImagem, larguraTiles, alturaTiles);
        player = new Player(map);
    }
    

    @Override
    public void atualizar() {    
        player.atualizar();
        map.setPosition(GamePanel.LARGURA / 2 - player.getx(),GamePanel.ALTURA / 2 - player.gety());
    }

    @Override
    public void renderizar(Graphics2D g) {
        map.renderizar(g);
        player.renderizar(g);
    }

    


    @Override
    public void keyPressed(int key) {
        switch(key){
            case KeyEvent.VK_LEFT:
                player.setEsquerda(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setDireita(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setPulando(true);
                break;
            case KeyEvent.VK_E:
                player.setAtacando(true);
                break;
        }
    }

    @Override
    public void keyReleased(int key) {
         switch(key){
            case KeyEvent.VK_LEFT:
                player.setEsquerda(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setDireita(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setPulando(false);
                break;
        }  
    }
    
}
