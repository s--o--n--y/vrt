package vrt.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class VrtPoint extends Point {

	boolean hit = false;

	public VrtPoint(Point p) {
		super(p);
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public void paintSearchPoint(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x - 10, y - 10, 20, 20);
	}

	public void paintHitPoint(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(x - 10, y - 10, 20, 20);
	}

	public void paintMissedPoint(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(x - 10, y - 10, 20, 20);
	}

}
