package watabe.alphagone;


import java.awt.Color;
import java.awt.Graphics;
import java.util.stream.IntStream;

import javax.swing.JPanel;

public class GProgress extends JPanel {
	private static final long serialVersionUID = -2137059385355252996L;

	private static final int BOX_WIDTH	= 16;
	private static final int BOX_HEIGHT	= 32;
	private static final int BOX_SPACE	= 8;

	@Override
	public void paint(Graphics g) {
		long now = System.currentTimeMillis();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		IntStream.range(0, 10).forEach(i -> {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect((getWidth() - ((BOX_WIDTH+BOX_SPACE)*10)) / 2 + (BOX_WIDTH+BOX_SPACE) * i, (getHeight()-BOX_HEIGHT) / 2, BOX_WIDTH, BOX_HEIGHT);
		});

		int index = (int)(now / 1000) % 10;
		g.setColor(Color.GRAY);
		g.fillRect((getWidth() - ((BOX_WIDTH+BOX_SPACE)*10)) / 2 + (BOX_WIDTH+BOX_SPACE) * index, (getHeight()-BOX_HEIGHT) / 2, BOX_WIDTH, BOX_HEIGHT);
	}
}
