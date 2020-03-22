package javax.core.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * FTP 文件服务器操作工具类，提供一些上传、下载、删除的方法
 * 
 * @author Tanyongde
 *
 */
public class FtpUtil {
	private static final Logger log = Logger.getLogger(FtpUtil.class);
	private static FTPClient ftp;
	private static FtpUtil ftpUtil = new FtpUtil();
	/**
	 * 对象构造 设置将过程中使用到的命令输出到控制台
	 */
	private FtpUtil() {
		ftp = new FTPClient();
//		ftp.addProtocolCommandListener(new PrintCommandListener(
//				new PrintWriter(System.out)));
	}
	
	public static FtpUtil getInstance() {
		return ftpUtil == null ? new FtpUtil() : ftpUtil;
	}

	/**
	 * 用户FTP账号登录
	 * 
	 * @param url
	 *            FTP地址
	 * @param port
	 *            FTP端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密 码
	 * @return true/false 成功/失败
	 * @throws SocketException
	 * @throws IOException
	 */
	public boolean login(String url, int port, String username, String password)
			throws SocketException, IOException {
		int reply;
		// 1. 连接FTP服务器
		// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		ftp.connect(url, port);

		// 2. 设置编码
		// 下面三行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
		ftp.setControlEncoding("UTF-8");
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");

		// 3. 登录ftp
		ftp.login(username, password);

		// 看返回的值是不是230，如果是，表示登陆成功
		reply = ftp.getReplyCode();

		// 以2开头的返回值就会为真
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			log.info("FTP服务器连接失败!");
			return false;
		}
		log.info("FTP服务器登录成功!");
		return true;
	}

	/**
	 * 释放FTP
	 */
	public void disconnect() {
		if (ftp.isAvailable()) {
			try {
				ftp.logout(); // 退出FTP
				log.info("FTP登录正常退出。");
			} catch (IOException e) {
				log.error("FTP登录退出异常:" + e.getMessage());
			}
		}
		if (ftp.isConnected()) {
			try {
				// 断开连接
				ftp.disconnect();
				log.info("FTP连接正常断开。");
			} catch (IOException e) {
				log.error("FTP断开连接异常:" + e.getMessage());
			}
		}
	}

	/**
	 * FTP文件上传
	 * 
	 * @param localAdr
	 *            上传文件名
	 * @param remoteAdr
	 *            指定的FTP目录
	 * @param input
	 *            上传的文件流对象
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(String localAdr, String remoteAdr,InputStream input) throws IOException {
		// 初始表示上传失败
		boolean success = false;
		// 设置PassiveMode传输
		ftp.enterLocalPassiveMode();
		// 设置FTP文件类型为二进制，如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.setBufferSize(1024*1024);
		/***** 对远程目录的处理 ******/
		String remoteFileName = remoteAdr;
		if (remoteAdr.contains("/")) {
			remoteFileName = remoteAdr.substring(remoteAdr.lastIndexOf("/") + 1);
			String directory = remoteAdr.substring(0, remoteAdr.lastIndexOf("/") + 1);
			if (!directory.equalsIgnoreCase("/") && !ftp.changeWorkingDirectory(directory)) {
				// 如果远程目录不存在，则递归创建远程服务器目录
				int start = 0, end = 0;
				if (directory.startsWith("/")) {
					start = 1;
				} else {
					start = 0;
				}
				end = directory.indexOf("/", start);
				while (true) {
					String subDirectory = remoteAdr.substring(start, end);
					if (!ftp.changeWorkingDirectory(subDirectory)) {
						if (ftp.makeDirectory(subDirectory)) {
							ftp.changeWorkingDirectory(subDirectory);
						} else {
							log.info("创建目录失败");
							return false;
						}
					}
					start = end + 1;
					end = directory.indexOf("/", start);
					// 检查所有目录是否创建完毕
					if (end <= start) {
						break;
					}
				}
			}
		}

		/***** 执行文件上传 ******/
		try {
			// 保存文件remoteFileName
			success = ftp.storeFile(new String(remoteFileName.getBytes("UTF-8"), "iso-8859-1"), input);
		} catch (IOException e) {
			log.info("文件上传失败:" + e.getMessage());
		} finally {
			for (int i = 0; i < remoteAdr.indexOf("/"); i++) {
				ftp.changeWorkingDirectory("../");
			}
			if (input != null) input.close();
			log.info("文件[:" + localAdr + (success ? "]上传成功!" : "上传失败!"));
		}
		return success;
	}

	/**
	 * 删除FTP文件和目录
	 * 
	 * @param remoteAdr
	 *            文件路径
	 * @return true/false 成功/失败
	 */
	public boolean deleteDirectory(String remoteAdr) {
		boolean success = false;
		try {
			 String remoteAdr_ = new String(remoteAdr.getBytes("UTF-8"),
			 "ISO-8859-1");
			// 转到指定上传目录
			ftp.changeWorkingDirectory(remoteAdr);
			FTPFile[] fs = ftp.listFiles(); // 得到目录的相应文件列表
			if (fs.length > 0) {
				success = ftp.removeDirectory(remoteAdr_);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			disconnect();
		}
		return success;
	}

	/**
	 * 删除FTP文件
	 * 
	 * @param remoteAdr
	 *            文件路径
	 * @return true/false 成功/失败
	 */
	public boolean deleteFile(String remoteAdr) {
		boolean success = false;
		try {
			String remoteAdr_ = new String(remoteAdr.getBytes("UTF-8"), "ISO-8859-1");
			// 得到目录的相应文件列表
			FTPFile[] fs = ftp.listFiles(remoteAdr);
			if (fs.length > 0) {
				// remoteAdr_->remoteAdr
				success = ftp.deleteFile(remoteAdr_);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			disconnect();
		}
		return success;
	}

	/**
	 * 下载FTP文件
	 * 
	 * @param remoteremoteAdr
	 *            远程路径
	 * @param localAdr
	 *            远程文件名称
	 * @param os
	 *            OutputStream输出对象
	 * @return true/false 成功/失败
	 */
	public boolean downFile(String remoteremoteAdr, String localAdr, OutputStream os) {
		boolean success = false;
		try {
			// 转移到FTP服务器目录
			ftp.changeWorkingDirectory(remoteremoteAdr);
			// 得到目录的相应文件列表
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ftpFile : fs) {
				if (ftpFile.getName().equals(localAdr)) {
					ftp.retrieveFile(new String(ftpFile.getName().getBytes("UTF-8"), "ISO-8859-1"), os);
				}
			}
			success = true;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return success;
	}

	/**
	 * 判断是否重名的方法
	 * 
	 * @param localAdr
	 *            文件名称
	 * @param fs
	 *            文件列表数组
	 * @return
	 */
	public boolean isDirExist(String localAdr, FTPFile[] fs) {
		for (FTPFile ftpFile : fs) {
			if (ftpFile.getName().equals(localAdr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据重名判断的结果 生成新的文件的名称 更改的重名为 原有名字+[n], n表示数字
	 * 
	 * @param localAdr
	 *            文件名称
	 * @param fs
	 *            FTP文件列表
	 * @return
	 */
	public String rename(String localAdr, FTPFile[] fs) {
		int n = 0;
		// 创建一个可变的字符串对象 即StringBuffer对象，把localAdr值付给该对象
		StringBuffer localAdr_ = new StringBuffer("");
		localAdr_ = localAdr_.append(localAdr);

		// 重名时改名，遍历存在同名的文件
		while (isDirExist(localAdr_.toString(), fs)) {
			n++;
			String a = "[" + n + "]";
			// 最后一出现小数点的位置
			int b = localAdr_.lastIndexOf(".");
			// 最后一次"["出现的位置
			int c = localAdr_.lastIndexOf("[");
			if (c < 0) {
				c = b;
			}

			// 文件的名字
			StringBuffer name = new StringBuffer(localAdr_.substring(0, c));
			// 后缀的名称
			StringBuffer suffix = new StringBuffer(localAdr_.substring(b + 1));
			localAdr_ = name.append(a).append(".").append(suffix);
		}
		return localAdr_.toString();
	}

	public static void main(String[] args) {
		
	}
}
