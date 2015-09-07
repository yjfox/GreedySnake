package GreedySnake;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class MainFrame extends JFrame implements KeyListener
{
    JTextPane j;
    public MainFrame ()
    {
        super ("title");
        setBounds (0, 0, 640, 480);
        setDefaultCloseOperation (EXIT_ON_CLOSE);
        j = new JTextPane ();
        j.setBackground(Color.BLACK);
        j.setForeground(Color.LIGHT_GRAY);
        j.setFont(new Font("courier", Font.BOLD, 15));
        j.addKeyListener (this);
        getContentPane ().add (j);
        setVisible (true);
    }
    public void keyPressed (KeyEvent arg0)
    {
        // skip it, just need to implement it

    }

    public void keyReleased (KeyEvent arg0)
    {
        // skip it, just need to implement it
    }

    public void keyTyped (KeyEvent arg0)
    {
        char c = arg0.getKeyChar ();
        // do your stuff;
        print ((char) (c + 1) + " ");
    }
    private void print (String s)
    {
        j.setText (j.getText () + s);
    }
    private void println (String s)
    {
        j.setText (j.getText () + s + "\n");
    }
    /**
     * @param args
     */
//    public static void main (String[] args)
//    {
//        new MainFrame ();
//
//    }
}
