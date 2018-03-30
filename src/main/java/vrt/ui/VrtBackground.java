package vrt.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class VrtBackground extends JComponent {

	Color pointColor = Color.yellow;

	@Override
	public void paint(Graphics g) {
		// paint my contents first...
		// then, make sure lightweight children paint
		super.paint(g);
		Dimension size = getSize();
		// diameter
		int d = Math.min(size.width, size.height);
		int x = (size.width - d) / 2;
		int y = (size.height - d) / 2;

		g.setColor(new Color(55, 60, 215));

		g.fillRect(0, 0, size.width, size.height);
		// draw circle (color already set to foreground)
		pointColor = new Color((int) Math.random() * 255, 255, 0);
		g.setColor(pointColor);
		int ovalDiameter = d / 18;
		g.fillOval(size.width / 2 - ovalDiameter / 2, size.height / 2
				- ovalDiameter / 2, ovalDiameter, ovalDiameter);
		// g.drawOval(x, y, d, d);

		// glassPane.repaint();

	}

}
