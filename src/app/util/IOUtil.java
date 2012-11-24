package app.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.StringTokenizer;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * java.ioAPIのユーティリティクラス。
 */
public class IOUtil {


	/**
	 * @param path
	 * @throws IOException
	 *             ディレクトリが作成されない場合
	 */
	public static void mkdirs(String path) {
		if (!StringUtil.isExist(path)) {
			return;
		}
		File logfile = new File(path);
		if (!logfile.exists()) {
			// System.out.println("ディレクトリがありませんので作成します。");
			boolean b = logfile.mkdirs();
			if (b) {
				// System.out.println("ディレクトリが作成されました。");
			} else {
				throw new RuntimeException(path + " ディレクトリが作成されません！！");
			}
		}
	}


	public static String rename(String filePath, String add) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			throw new IllegalArgumentException();
		}
		String name = StringUtil.getPreffix(file.getName());
		String ext = StringUtil.getSuffix(filePath);
		return file.getParent() + File.separator + name + add + "." + ext;
	}

	/**
	 * java.beans.XMLEncoderを使用し、オブジェクト[object]を、 指定したパス[path]に、XMLファイルとして保存します。
	 * ※オブジェクト[object]がJavaBeans慣例に適合している場合、 プライベートなフィールドのデータも保存できます。
	 * ※保存したXMLファイルはjava.beans.XMLDecoderで復元する事ができます。
	 *
	 * @param path
	 *            オブジェクトを保存するパス。 存在しない場合可能であれば作成します。
	 * @param object
	 *            保存するオブジェクト。
	 * @throws IOException
	 *             指定されたパス名で示されるファイルが開けなかった場合
	 */
	public static void writeXML(String path, Object object) throws IOException {
		writeXML(path, object, null);
	}

	public static void writeXML(String path, Object object, byte[] secretKey)
			throws IOException {
		XStream xs = new XStream(new DomDriver());
		OutputStream output = null;
		try {
			output = new FileOutputStream(path);
			xs.toXML(object, output);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * 指定したパス[path]のXMLファイルから、オブジェクトを復元します。
	 * ※XMLファイルはjava.beans.XMLEncoderで保存している必要があります。
	 *
	 * @param path
	 *            オブジェクトが保存されているパス。
	 * @throws FileNotFoundException
	 *             指定されたパス名で示されるファイルが存在しない場合
	 */
	public static Object readXML(String path, byte[] secretKey)
			throws IOException {
		XStream xs = new XStream(new DomDriver());
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			return xs.fromXML(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStreamByte(InputStream in) throws IOException {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			int len = -1;
			byte[] b = new byte[100 * 1024];
			while ((len = in.read(b, 0, b.length)) != -1) {
				bos.write(b, 0, len);
			}
			return bos.toByteArray();
		} finally {
			if (in != null) {
				in.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * コピー元のパス[srcPath]からコピー先のパス[destPath]へファイルのコピー を行います。
	 *
	 * @param srcPath
	 *            コピー元のパス
	 * @param destPath
	 *            コピー先のパス
	 * @param bufferSize
	 *            データの読み込みバッファサイズ(KB)です。
	 * @throws IOException
	 *             何らかの入出力処理例外が発生した場合
	 */
	public static void copyStream(String srcPath, String destPath,
			int bufferSize) throws IOException {
		InputStream in = new FileInputStream(srcPath);
		OutputStream os = new FileOutputStream(destPath);
		copyStream(in, os, bufferSize);
	}

	/**
	 * 入力ストリームから出力ストリームへデータの書き込みを行います。 尚、書き込み終了後、入力・出力ストリームを閉じます。
	 * データの読み込みバッファサイズは100KBです。
	 *
	 * @param in
	 *            入力ストリーム
	 * @param os
	 *            出力ストリーム
	 * @throws IOException
	 *             何らかの入出力処理例外が発生した場合
	 */
	public static void copyStream(InputStream in, OutputStream os)
			throws IOException {
		copyStream(in, os, 100);
	}

	/**
	 * 入力ストリームから出力ストリームへデータの書き込みを行います。 尚、コピー処理終了後、入力・出力ストリームを閉じます。
	 *
	 * @param in
	 *            入力ストリーム
	 * @param os
	 *            出力ストリーム
	 * @param bufferSize
	 *            データの読み込みバッファサイズ(KB)です。
	 * @throws IOException
	 *             何らかの入出力処理例外が発生した場合
	 */
	public static void copyStream(InputStream in, OutputStream os,
			int bufferSize) throws IOException {
		int len = -1;
		byte[] b = new byte[bufferSize * 1024];
		try {
			while ((len = in.read(b, 0, b.length)) != -1) {
				os.write(b, 0, len);
			}
			os.flush();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void copyChannel(String fromPath, String toPath,
			int bufferSize) throws IOException {
		FileChannel srcChannel = new FileInputStream(fromPath).getChannel();
		FileChannel destChannel = new FileOutputStream(toPath).getChannel();
		try {
			// バッファの生成
			ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize * 1024);
			while (true) {
				buffer.clear();
				// ファイルから読みこみ
				// 読みこんだバイト数分だけ position が移動
				// 最後まで読みこんだ場合、戻り値が -1 になる
				if (srcChannel.read(buffer) < 0) {
					break;
				}
				buffer.flip();
				destChannel.write(buffer);
			}
		} finally {
			srcChannel.close();
			destChannel.close();
		}
	}

	/**
	 * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ ファイルのコピーを行います。
	 * コピー処理にはFileChannel#transferToメソッドを利用します。 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
	 *
	 * @param srcPath
	 *            コピー元のパス
	 * @param destPath
	 *            コピー先のパス
	 * @throws IOException
	 *             何らかの入出力処理例外が発生した場合
	 */
	public static void copyTransfer(String srcPath, String destPath)
			throws IOException {

		FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
		FileChannel destChannel = new FileOutputStream(destPath).getChannel();
		try {
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		} finally {
			srcChannel.close();
			destChannel.close();
		}

	}

	// ---『 Wright/ 』------------------------------------------------------

	public static void writeFileLine(File file, String writedata)
			throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(writedata);
			fw.flush();
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
	}

	/**
	 * @param filepath
	 * @param data
	 * @throws IOException
	 */
	public static void writeFileByte(String filepath, byte[] data)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(filepath);
		try {
			fos.write(data);
			fos.flush();
		} finally {
			fos.close();
		}
	}

	/**
	 * パスからファイル名を返します。 例 ： D:/ApacheTomcat4.0/webapp/information
	 *
	 * @param path
	 * @return 上記の例ではinformationが返る。
	 */
	public static String getFileName(String path) {
		Vector vectors = new Vector();
		StringTokenizer token = new StringTokenizer(path, "\\/");
		while (token.hasMoreTokens()) {
			vectors.add(token.nextToken());
		}
		return ((String) vectors.lastElement());
	}

	public static boolean isImageFile(String name) {
		return name.matches(".*\\.(jpg|jpeg|gif|png)");
	}
}
