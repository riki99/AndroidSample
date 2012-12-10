package com.satton.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * java.io APIのユーティリティクラス。
 */
public class IOUtil {

	// ----------------------------------------------------------------------
	//  I/O Basic
	// ----------------------------------------------------------------------

	/**
	 * ディレクトリ作成します。
	 *
	 * @param path ディレクトリを示すパス
	 */
	public static boolean mkdirs(String path) {
		if (path == null) {
			return false;
		}
		File logfile = new File(path);
		if (!logfile.exists()) {
			boolean b = logfile.mkdirs();
			return b;
		}
		return true;
	}

	/**
	 * 指定したディレクトリ[directoryPath]が示すファイルまたはディレクトリを完全に削除します。
	 * java.io.Fileクラスのdelete()メソッドは、ディレクトリにファイルが存在する場合、削除することはできません。
	 * そのため、このメソッドはサブディレクトリから再帰的にファイルを削除して行きます。
	 *
	 * @param deleteDir 削除対象のファイルまたはディレクトリ
	 */
	public static void delete(File deleteDir) {
		if (!deleteDir.exists()) {
			return;
		}
		File[] childs = deleteDir.listFiles();
		if (childs != null) {
			for (int i = 0; i < childs.length; i++) {
				File child = childs[i];
				if (child.isDirectory()) {
					delete(child);
				}
				if (child.delete()) {
				}
			}
		}
		if (deleteDir.delete()) {
		}
	}

	/**
	 * ファイル名から拡張子を取り除いた名前を返します。
	 *
	 * @param fileName ファイル名
	 * @return ファイル名
	 */
	public static String getPreffix(String fileName) {
		if (fileName == null)
			return null;
		fileName = new File(fileName).getName();
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
	}

	/**
	 *
	 * @param dir
	 */
	public static void ls(File dir, boolean recursive) {
		System.out.println(String.format("dir	%s", dir.getAbsolutePath()));
		File[] arry = dir.listFiles();
		if (arry != null) {
			for (File file : arry) {
				if (recursive && file.isDirectory()) {
					ls(file, recursive);
				}
				System.out.println(String.format("	%s", file.getName()));
			}
		} else {
			System.out.println("	nothing.");
		}
	}

	// ----------------------------------------------------------------------
	//  I/O Read
	// ----------------------------------------------------------------------

	/**
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileByte(String filepath) throws IOException {
		FileInputStream fis = new FileInputStream(filepath);
		return readStreamByte(fis);
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

	// ----------------------------------------------------------------------
	//  I/O Write
	// ----------------------------------------------------------------------

	/**
	 * filepath へ Byte データを書き込ます
	 *
	 * @param filepath ファイルパス
	 * @param data Byte データ
	 * @throws IOException
	 */
	public static void writeFileByte(String filepath, byte[] data)
			throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filepath);
			fos.write(data);
			fos.flush();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	// ----------------------------------------------------------------------
	//  I/O Copy
	// ----------------------------------------------------------------------

	/**
	 * コピー元のパス srcPath からコピー先のパス destPath へファイルのコピー行います。
	 *
	 * @param srcPath コピー元のパス
	 * @param destPath コピー先のパス
	 * @param bufferSize データの読み込みバッファサイズ(KB)です。
	 * @throws IOException 何らかの入出力処理例外が発生した場合
	 */
	public static void copyStream(String srcPath, String destPath, int bufferSize)
			throws IOException {
		InputStream in = new FileInputStream(srcPath);
		OutputStream os = new FileOutputStream(destPath);
		copyStream(in, os, bufferSize, null);
	}

	/**
	 * 入力ストリームから出力ストリームへデータの書き込みを行います。
	 * 尚、書き込み終了後、入力・出力ストリームを閉じます。
	 * データの読み込みバッファサイズは100KBです。
	 *
	 * @param in 入力ストリーム
	 * @param os 出力ストリーム
	 * @throws IOException 何らかの入出力処理例外が発生した場合
	 */
	public static void copyStream(InputStream in, OutputStream os)
			throws IOException {
		copyStream(in, os, 100, null);
	}

	/**
	 * 入力ストリームから出力ストリームへデータの書き込みを行います。
	 * 尚、コピー処理終了後、入力・出力ストリームを閉じます。
	 *
	 * @param in 入力ストリーム
	 * @param os 出力ストリーム
	 * @param bufferSize データの読み込みバッファサイズ(KB)です。
	 * @throws IOException 何らかの入出力処理例外が発生した場合
	 */
	public static void copyStream(InputStream in, OutputStream os, int bufferSize, DownloadCallback callback)
			throws IOException {
		int len = -1;
		byte[] b = new byte[bufferSize * 1024];
		try {
			while ((len = in.read(b, 0, b.length)) != -1) {
				os.write(b, 0, len);
				if (callback != null) {
					callback.onProgress(len);
				}

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

	public static class DownloadCallback {
		public long maxLength = 0;

		public void onProgress(int readLength) {
		}

	}

	/**
	 * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ
	 * ファイルのコピーを行います。
	 * コピー処理にはFileChannel#transferToメソッドを利用します。
	 * 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
	 *
	 * @param srcPath コピー元のパス
	 * @param destPath コピー先のパス
	 * @throws IOException 何らかの入出力処理例外が発生した場合
	 */
	public static void copyTransfer(String srcPath, String destPath)
			throws IOException {

		FileChannel srcChannel = null;
		FileChannel destChannel = null;
		try {
			srcChannel = new
					FileInputStream(srcPath).getChannel();
			destChannel = new
					FileOutputStream(destPath).getChannel();
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		} finally {
			if (srcChannel != null) {
				srcChannel.close();
			}
			if (destChannel != null) {
				destChannel.close();
			}
		}
	}

	/**
	 * url で指定されたファイルをダウンロードする
	 *
	 * @param url
	 * @param saveFile
	 * @throws Exception
	 */
	public static void download(URL url, File saveFile, IOUtil.DownloadCallback callback) throws Exception {
		if (callback == null) {
			throw new IllegalStateException();
		}
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		http.connect();
//			LogUtil.d(String.format("URL=%s	Content-length: %s	savePath=%s",
//					url, http.getContentLength() / 1024 * 1024, saveFile.getAbsolutePath()));

		if (callback != null) {
			callback.maxLength = http.getContentLength();
		}
		InputStream in = http.getInputStream();
		IOUtil.copyStream(in, new FileOutputStream(saveFile), 1024, callback);

	}

	// ----------------------------------------------------------------------
	//  serializetion
	// ----------------------------------------------------------------------
	/**
	 * オブジェクト[object]を、 指定したパス[path]に、XMLファイルとして保存します。
	 *
	 * @param path
	 *            オブジェクトを保存するパス。 存在しない場合可能であれば作成します。
	 * @param object
	 *            保存するオブジェクト。
	 * @throws IOException
	 *             指定されたパス名で示されるファイルが開けなかった場合
	 */
	public static void writeXML(String path, Object object) throws IOException {
		writeXML(new File(path), object);
	}

	public static void writeXML(File file, Object object) throws IOException {
		if (!file.getParentFile().exists()) {
			throw new IllegalArgumentException();
		}
		XStream xs = new XStream(new DomDriver());
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			xs.toXML(object, output);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}


	// ----------------------------------------------------------------------
	//  I/O その他
	// ----------------------------------------------------------------------

	public static long getFileSize(File file) {
		long size = 0;
		if (file.isFile()) {
			size = file.length();
		} else {
			File[] childs = file.listFiles();
			for (int i = 0; i < childs.length; i++) {
				if (childs[i].isFile()) {
					size += childs[i].length();
				} else {
					size += getFileSize(childs[i]);
				}
			}
		}
		return size;
	}

}
