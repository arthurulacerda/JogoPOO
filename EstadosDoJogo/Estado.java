package EstadosDoJogo;

import java.awt.Graphics2D;

public interface Estado {
    
    public void atualizar();
    public void renderizar(Graphics2D g);
    public void keyPressed(int key);
    public void keyReleased(int key);
}
