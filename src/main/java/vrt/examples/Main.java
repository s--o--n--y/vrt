package vrt.examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main extends JComponent {
    public static void main(String[] args) {

        JFrame frame = new JFrame();

        final JButton activate = new JButton("Show");
        frame.add(activate);

        frame.pack();
        frame.setVisible(true);

        final Main glass = new Main(frame);
        frame.setGlassPane(glass);

        activate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                glass.setVisible(true);
            }
        });
    }

    JFrame frame;

    Point cursor;

    public Main(JFrame frame) {
        this.frame = frame;
        cursor = new Point();
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evt) {
                cursor = new Point(evt.getPoint());
                Main.this.repaint();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                Main.this.setVisible(false);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Container root = frame.getContentPane();
        myPaint(root, g);
    }

    private void myPaint(Component comp, Graphics g) {
        int x = comp.getX();
        int y = comp.getY();
        g.translate(x, y);
        cursor.translate(-x, -y);
        if (comp.contains(cursor)) {
            String cls_name = comp.getClass().getName();
            g.setColor(Color.black);
            g.drawString(cls_name, 0, 10);
        }
        if (comp instanceof Container) {
            Container cont = (Container) comp;
            for (int i = 0; i < cont.getComponentCount(); i++) {
                Component child = cont.getComponent(i);
                myPaint(child, g);
            }
        }

        cursor.translate(x, y);
        g.translate(-x, -y);
    }

}