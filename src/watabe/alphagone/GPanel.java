package watabe.alphagone;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class GPanel extends JPanel {
	private static final long serialVersionUID = -5787974717252435953L;

	private static final int WIDTH	= 384;
	private static final int HEIGHT	= 256;

	private static final int RECT_WIDTH		= 64;
	private static final int RECT_HEIGHT	= 64;

	public GPanel() {
		super();
		this.setSize(WIDTH, HEIGHT);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.BLACK);
	}

	@Override
	public void paint(Graphics g) {
//		super.print(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D)g;
		float dash[] = {10.0f, 1.0f};
		BasicStroke stroke = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		g2.setStroke(stroke);
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawRoundRect((WIDTH-RECT_WIDTH)/2, (HEIGHT-RECT_HEIGHT)/2-32, RECT_WIDTH, RECT_HEIGHT, 5, 10);

		g.setColor(Color.DARK_GRAY);
		drawStringCenter(g, "Drop Your Model Here", getWidth(), 192);
	}

	public static void drawStringCenter(Graphics g, String text, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle rectText = fm.getStringBounds(text, g).getBounds();
		x=(x-rectText.width)/2;
		y=y-rectText.height/2+fm.getMaxAscent();
		
        g.drawString(text, x, y);
	}
}
