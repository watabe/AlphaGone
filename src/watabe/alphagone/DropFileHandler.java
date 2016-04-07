package watabe.alphagone;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.TransferHandler;


public class DropFileHandler extends TransferHandler {
	private static final long serialVersionUID = -4166217132731661793L;

	private List<FileDropper> fileDropperes = new ArrayList<>();

	public void addFileDropper(FileDropper fileDropper) {
		fileDropperes.add(fileDropper);
	}

	/**
	 * ドロップされたものを受け取るか判断 (ファイルのときだけ受け取る)
	 */
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDrop()) {
			// ドロップ操作でない場合は受け取らない
	        return false;
	    }

		if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			// ドロップされたのがファイルでない場合は受け取らない
	        return false;
	    }

		return true;
	}

	/**
	 * ドロップされたファイルを受け取る
	 */
	@Override
	public boolean importData(TransferSupport support) {
		// 受け取っていいものか確認する
		if (!canImport(support)) {
	        return false;
	    }

		// ドロップ処理
		try {
			Transferable t = support.getTransferable();
			// ファイルを受け取る
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
			List<File> droppedAllFiles = new ArrayList<>();

			files.stream()
				.filter(f -> f.isDirectory() || f.getName().toUpperCase().endsWith("PNG"))
				.forEach(f -> droppedAllFiles.addAll(digFiles(f)));

			fileDropperes.stream().forEach(fd -> fd.dropped(droppedAllFiles));
		} catch(UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	private List<File> digFiles(File rootFile) {
		List<File> files = new ArrayList<>();

		if(rootFile.isDirectory()) {
			Arrays.stream(rootFile.listFiles(f -> f.isDirectory() || f.getName().toUpperCase().endsWith("PNG")))
				.forEach(c -> files.addAll(digFiles(c)));
		} else {
			files.add(rootFile);
		}

		return files;
	}
}
