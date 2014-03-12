package cn.hexing.fk.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * @author gaoll
 * 
 * @time 2013-2-3 上午10:43:52
 * 
 * @info FTP远程文件读取,
 */
public class FtpFileReader {
	private  String fileDir = "./Upgrade/";
	private FTPClient ftpClient;

	public FtpFileReader() {}

	public void closeServer() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public byte[] readFile(String url, // FTP服务器hostname
			int port,// FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String remotePath,// FTP服务器上的相对路径
			String fileName,// 文件名
			String fileDir
	){
		this.fileDir = fileDir;
		try {
			String fileContent=readFile(url, port, username, password, remotePath, fileName);
			return HexDump.toArray(fileContent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String readFile(String url, // FTP服务器hostname
			int port,// FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String remotePath,// FTP服务器上的相对路径
			String fileName// 文件名
	) throws InterruptedException {
		String result = "";
		boolean flag = false;
 			flag = downFile(url, 21, username, password, remotePath,
					fileName, fileDir);
		if (flag) {
			try {
				File file = new File(fileDir+fileName);
				FileInputStream stream = new FileInputStream(file);
				byte[] buffer = new byte[(int)file.length()];
				stream.read(buffer);
				result = HexDump.toHex(buffer);
				stream.close();
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * Description: 从FTP服务器下载文件
	 * 
	 * @Version1.0
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */

	public  boolean downFile(String url, // FTP服务器hostname
			int port,// FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String remotePath,// FTP服务器上的相对路径
			String fileName,// 要下载的文件名
			String localPath// 下载后保存到本地的路径
	) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.setConnectTimeout(1000);
			ftp.setDataTimeout(2000);
			ftp.connect(url, port);
			//从win平台下读取文件到linux
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath);
					if(!localFile.exists()){
						localFile.mkdirs();
					}
					localFile=new File(localPath + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
					break;
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	public static void main(String[] args) throws InterruptedException {
		 FtpFileReader ftp = new FtpFileReader();
		 System.out.println(ftp.readFile("172.16.251.239",21,"hexing", "hexing",
				   "ftp\\fep","PrepayMeterV02.bin"));
		try {
//			boolean flag = downFile("172.16.251.239", 21, "hexing", "hexing",
//					"ftp\\fep", "update.tbz", "D:/");
//			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
