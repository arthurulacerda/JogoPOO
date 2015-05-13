package Mapa;


import Main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileMap {
    private int[][] mapa; //Matriz de inteiros que representa o mapa
    
    private Tile tiles[][]; //Conjunto de tiles que podem ser usadas para desenhar este mapa
    
    private String localizacaoDaImagem; //Localização da imagem que contém todas as tiles que podem ser utilizadas neste mapa
    
    private String localizacaoDoMapa; //Localização do arquivo de texto que contém as informações sobre o mapa
    
    private int numTilesColunaImagem; // Número de tiles por coluna na imagem que contém todas as tiles
    private int numTilesLinhaImagem;// Número de tiles por linha na imagem que contém todas as tiles
    public int larguraTiles; //Largura de cada tile ( em pixels )
    public int alturaTiles; //Altura de cada tile ( em pixels )
    
    private int x; // Posição x do mapa
    private int y; // Posição y do mapa
    
    private int numLinhas;
    private int numColunas;

    public TileMap(String localizacaoDaImagem, String localizacaoDoMapa, int numTilesLinhaImagem, int numTilesColunaImagem, int larguraTiles, int alturaTiles) {
        this.localizacaoDaImagem = localizacaoDaImagem;
        this.numTilesColunaImagem = numTilesColunaImagem;
        this.numTilesLinhaImagem = numTilesLinhaImagem;
        this.larguraTiles = larguraTiles;
        this.alturaTiles = alturaTiles;
        
        carregaTiles(localizacaoDaImagem);
        carregaMapa(localizacaoDoMapa);
    }
    
    
    /**
     * Função que lê uma imagem e a recorta em várias subimagens.
     * Cada subimagem é uma tile.
     * Todas as tiles serão armazenadas em uma matriz
     * @param localizacao : Localização da imagem que contém todas as tiles
     */
    private void carregaTiles(String localizacao){
        try {
            tiles = new Tile[numTilesLinhaImagem][ numTilesColunaImagem];
            BufferedImage imagem = ImageIO.read(getClass().getResourceAsStream(localizacao)); //Lendo a imagem que contém todas as tiles
            BufferedImage tile;

            
            for(int linha = 0; linha < numTilesLinhaImagem ; linha++){
                for(int coluna = 0; coluna < numTilesColunaImagem ; coluna++){
                    /* Foi definido que:
                       Tiles na linha 1 são do tipo BLOQUEADA(0)
                       Tiles na linha 2 são do tipo LIVRE(1) */
                    
                    //Recortando um tile da imagem
                    tile = imagem.getSubimage(coluna * larguraTiles, linha * alturaTiles, larguraTiles, alturaTiles);
                    
                    //Armazenando o tile no vetor de tiles
                    tiles[linha][coluna] = new Tile(tile,linha);
                }
            }
            
        } catch (IOException ex) {
            System.out.println("Erro ao tentar carregar a imagem "+localizacao);
            ex.printStackTrace();
        }
  
    }
    
    
    private void carregaMapa(String localizacao){
        try {
            int linhas, colunas;
            String[] partes;
            String delim = ",";
            String linha;
            BufferedReader br = new BufferedReader(new FileReader(getClass().getResource(localizacao).toString().substring(6)));
            
            //Lendo a quantidade de linhas e colunas que o mapa terá
            linha = br.readLine();
            partes = linha.split(delim);
            numLinhas = Integer.parseInt(partes[0]);
            numColunas = Integer.parseInt(partes[1]);
            mapa = new int[numLinhas][numColunas];
            
            //Lendo as demais informações do mapa
            for(int i = 0; i < numLinhas ; i++){
                linha = br.readLine();
                partes = linha.split(delim);
                
                for(int j = 0; j < numColunas ; j++)
                    mapa[i][j] = Integer.parseInt(partes[j]);
                
            }
            
            
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao tentar carregar o mapa "+localizacao);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Erro ao tentar ler informações do arquivo "+localizacao);
            ex.printStackTrace();
        }
        
        for(int i = 0; i < numLinhas ; i++){
                for(int j = 0; j < numColunas ; j++)
                    System.out.print(mapa[i][j]+",");
                System.out.println();
        }
    }
    
    
    
    
    
    public void renderizar(Graphics2D g){
        for(int linha = 0; linha < numLinhas ; linha++){
            
            for(int coluna = 0; coluna < numColunas ; coluna++){
                int lc = mapa[linha][coluna];
                if(lc == 0) lc = 2;
                int l = lc/ numTilesLinhaImagem;
                int c = lc % numTilesLinhaImagem;
                if(c != 0) c--;
                g.drawImage(tiles[l][c].getImagem(),x + coluna*larguraTiles, y + linha*alturaTiles, larguraTiles, alturaTiles, null); 
                
            }
        } 
    }
    
    
   public void setPosition(int x, int y){
        this.x = x;
        this.y = y;

        if(this.y < -360) this.y = -360;
        if(this.x > 0) this.x = 0;
    }
    
    
    public Tile getTile(int linha, int coluna){
        int lc = mapa[linha][coluna];
        int l = lc/ numTilesLinhaImagem;
        int c = lc % numTilesLinhaImagem;
        if(c != 0) c--;
        return tiles[l][c];
    }
    
    public int getTileType(int linha, int coluna){
        int lc = mapa[linha][coluna];
        if(lc == 0) lc = 2;
        int l = lc/ numTilesLinhaImagem;
        int c = lc % numTilesLinhaImagem;
        if(c != 0) c--;
        return tiles[l][c].getTipo();
    }
    
    public int getTileRow(int y){
        return y/ alturaTiles;
    }
    
    public int getTileCol(int x){
        return x/ larguraTiles;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
    public int getTileSize(){
        return alturaTiles;
    }
}
