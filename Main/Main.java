package Main;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static void main(String[] args)  {

        JFrame window = new JFrame("Jogo");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.pack();
        
        window.setVisible(true);  
    }

}
//jsioadjiosajdoijsaoidsa
