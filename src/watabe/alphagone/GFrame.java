package watabe.alphagone;


import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GFrame extends JFrame implements FileDropper {
	private static final long serialVersionUID = 7447251232006309536L;

	private final GPanel panel = new GPanel();
	private final GProgress progress = new GProgress();

	private final DropFileHandler dropFileHandler = new DropFileHandler();

	private final List<File> convertTargetList = Collections.synchronizedList(new ArrayList<>());

	public GFrame() {
		super("GONE");

		this.getContentPane().setLayout(new BorderLayout());

		this.getContentPane().add(panel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setTransferHandler(dropFileHandler);

		dropFileHandler.addFileDropper(this);

		this.pack();
	}

	File savePath = null;

	@Override
	public void dropped(List<File> files) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int selected = fileChooser.showSaveDialog(this);
		switch(selected) {
		case JFileChooser.APPROVE_OPTION :
			savePath = fileChooser.getSelectedFile();
			System.out.println(savePath);
			break;

		case JFileChooser.CANCEL_OPTION :
			break;

		case JFileChooser.ERROR_OPTION :
			break;
		}

		SwingUtilities.invokeLater(() -> {
			this.getContentPane().remove(panel);
			this.getContentPane().add(progress, BorderLayout.CENTER);
			progress.updateUI();
			this.getContentPane().repaint();
		});

		convertTargetList.clear();
		convertTargetList.addAll(files);

		Thread convertProcessThread = new Thread(() -> {
			convertTargetList.parallelStream().forEach(f -> {
				System.out.println("Convert start: " + f.getAbsolutePath());

				// バッチ処理
//				try { Thread.sleep(1000*10); } catch(Exception e) {}
				if(savePath != null) {
					String save = savePath.getAbsolutePath() + f.getName();

					try {
						BufferedImage original = ImageIO.read(f);
						BufferedImage rgbImage = copyImage(original);
						BufferedImage alphaImage = copyImage(original);
						for(int x = 0; x < original.getWidth(); x++) {
							for(int y = 0; y < original.getHeight(); y++) {
								int rgba1 = rgbImage.getRGB(x, y);
								rgbImage.setRGB(x, y, rgba1|0xFF000000);

								int rgba2 = alphaImage.getRGB(x, y);
								alphaImage.setRGB(x, y, rgba2&0xFF000000);
							}
						}

						File save1 = new File(save + "_rgb.png");
						ImageIO.write(rgbImage, "png", save1);
						System.out.println(save1.getAbsolutePath());

						File save2 = new File(save + "_a.png");
						ImageIO.write(alphaImage, "png", save2);
						System.out.println(save2.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				System.out.println("Convert end: " + f.getAbsolutePath());
			});

			SwingUtilities.invokeLater(() -> {
				this.getContentPane().remove(progress);
				this.getContentPane().add(panel, BorderLayout.CENTER);
				progress.updateUI();
				this.getContentPane().repaint();
			});
		});
		convertProcessThread.start();

		Thread repaintThread = new Thread(() -> {
			while(true) {
				progress.updateUI();
				this.getContentPane().repaint();

				try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }

				if(Arrays.asList(this.getContentPane().getComponents()).contains(panel)) {
					break;
				}
			}
		});
		repaintThread.start();
	}

	static BufferedImage copyImage(BufferedImage src){
		BufferedImage dist=new BufferedImage(
				src.getWidth(), src.getHeight(), src.getType());
		dist.setData(src.getData());
		return dist;
	}
}
