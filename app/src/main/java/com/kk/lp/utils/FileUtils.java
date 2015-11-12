package com.kk.lp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

/**
 * 
 * @Description
 * @author lipeng
 * @version 2015-6-29
 * 
 */

public class FileUtils {
	public static final String CACHE_DIR_PATH = "cache_lp";

	/**
	 * @Description 向文件中写入数据
	 * @author lipeng
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean writeFile(byte[] buffer, String folder, String fileName) throws IOException {
		boolean writeSucc = false;

		File file = getSimpleFile(folder, fileName, true);
		if (file == null) {
			return false;
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			out.flush();
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writeSucc;
	}

	/**
	 * @Description 创建图片缓存路径
	 * @author lipeng
	 * @param cache_dir
	 *            缓存文件夹名称
	 * @return
	 * @return File
	 */
	public static File getCachePath(String cache_dir) {
		File cacheDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(Environment.getExternalStorageDirectory() + cache_dir);
			if (cacheDir != null) {
				if (!cacheDir.mkdirs()) {
					if (!cacheDir.exists()) {
						// 创建失败
					}
				}
			}
		} else {
			// 未挂载SD卡
		}
		return cacheDir;
	}

	/**
	 * @Description 根据文件目录与文件名称创建空文件
	 * @author lipeng
	 * @param filePath
	 *            文件目录
	 * @param fileName
	 *            文件名称
	 * @return
	 * @throws IOException
	 * @return File
	 */
	public static File createEmptyFile(File fileCachePath, String fileName, boolean deleteIfExist) throws IOException {
		File cacheEmptyFile = null;
		if (fileCachePath != null) {
			String filePath = fileCachePath.getAbsolutePath() + File.separator + fileName;
			cacheEmptyFile = new File(filePath);
			// 如果文件不存在，返回创建并返回一个空文件，否则返回已存在的文件
			if (!cacheEmptyFile.exists()) {
				cacheEmptyFile.createNewFile();
			} else {
				if (deleteIfExist) {
					cacheEmptyFile.delete();
					cacheEmptyFile.createNewFile();
				}
			}
		}
		return cacheEmptyFile;
	}

	/**
	 * @Description 获取文件，存在则返回，不存在则返回一个新建的空文件
	 * @author lipeng
	 * @param filePath
	 *            缓存文件夹名称，如：cache/cache_next，为null则使用默认缓存路径
	 * @param fileName
	 *            文件名，为null则返回null
	 * @param deleteIfExist
	 *            true删除已存在的文件同时创建一个空文件，false直接返回已存在的文件，不存在的话返回空文件
	 * @return
	 * @throws IOException
	 * @return File
	 */
	public static File getSimpleFile(String filePath, String fileName, boolean deleteIfExist) throws IOException {
		File file = null;
		if (fileName == null) {
			return null;
		}
		// 获取缓存路径
		File fileCachePath = null;
		if (filePath == null) {
			fileCachePath = FileUtils.getCachePath(CACHE_DIR_PATH);
		} else {
			fileCachePath = getCachePath(filePath);
		}
		file = FileUtils.createEmptyFile(fileCachePath, fileName, deleteIfExist);
		return file;
	}

}
