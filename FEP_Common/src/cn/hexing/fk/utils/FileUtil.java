package cn.hexing.fk.utils;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 文件操作工具类
 */
public class FileUtil {

	private static Logger log = Logger.getLogger(FileUtil.class);
	
    /**
     * 创建目录。如果父目录不存在，将创建所有父目录
     * @param path 路径名
     * @return 目录对象
     */
    public static File mkdirs(String path) {
        File dir = new File(path);
        if (dir.isFile()) {
            throw new IllegalArgumentException(path + " is not a directory");
        }
        
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return dir;
    }
    
    /**
     * 打开文件。如果文件不存在，则创建之
     * @param path 文件所在目录
     * @param fileName 文件名
     * @return 文件对象
     */
    public static File openFile(String path, String fileName) {
        File dir = mkdirs(path);
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                throw new RuntimeException("Error to open file: " + fileName, ex);
            }
        }
        
        return file;
    }
    
    /**
     * 删除文件
     * @param path 文件所在路径
     * @param fileName 文件名
     */
    public static void deleteFile(String path, String fileName) {
        File file = new File(path, fileName);
        if (file.exists()) {
            file.delete();
        }
    }
    
    /**
     * 取得目录的绝对路径名。如果传入的路径是相对路径，则把用户的当前目录作为其父目录 
     * @param path 路径名。可能是绝对路径或相对路径
     * @return 绝对路径名
     */
    public static String getAbsolutePath(String path) {
        File f = new File(path);
        return f.getAbsolutePath();
    }
    
    /**
     * 取得文件的绝对路径名
     * @param path 文件的存放路径
     * @param fileName 文件名
     * @return 文件的绝对路径名
     */
    public static String getAbsolutePath(String path, String fileName) {
        File dir = mkdirs(getAbsolutePath(path));
        File file = new File(dir, fileName);
        return file.getAbsolutePath();
    }
    
    /**
     * 将对象写入指定文件
     * @param pojo
     * @param file
     * @param isAppend,是追加还是覆盖
     */
    public static void writeObjectToFile(Object pojo,File file,boolean isAppend){
    	ObjectOutputStream oos = null;
      	FileOutputStream  fos = null;
    	try {
    		fos = new FileOutputStream(file,isAppend);
    		if(file.length()>1){
    			//如果文件大于1，则不往文件内写header
    			oos = new AppendableObjectOutputStream(fos);
    		}else{
    			oos = new ObjectOutputStream(fos);
    		}
			oos.writeObject(pojo);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally{
			close(oos);
			close(fos);
		}
    }
    
    public static List<Object> readObjectFromFile(File file){
    	
    	List<Object> result = null;
    	ObjectInputStream ois = null;
    	try {
			ois= new ObjectInputStream(new FileInputStream(file));
			result = new ArrayList<Object>();
			while(true){
				result.add(ois.readObject());
			}
			
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (EOFException e) {
			//到达文件尾，什么也不做。
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		} catch (Exception e ){
			log.error(e.getMessage());
		}finally{
			close(ois);
		}
		return result;
    }
    
    /**
     * 将文件锁住，不允许别的线程打开
     * @param lockFile
     * @return
     */
    public static FileLock tryLockFile(File lockFile){
    	try {
    		
    		if(!lockFile.exists()) return null;
    		
    		//如果文件不存在，下面的代码为创建一个文件出来
            RandomAccessFile randomAccessFile = new RandomAccessFile(
            		lockFile, "rw");

            FileChannel fileChannel = randomAccessFile.getChannel();

            return fileChannel.tryLock();
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * 解锁文件
     * @param fileLock
     */
    public static void unlockFile(FileLock fileLock) {
        try {
            fileLock.release();

            FileChannel fileChannel = fileLock.channel();

            close(fileChannel);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    public static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
        	log.error(e.getMessage());
        } finally {
        	close(inStream);
        	close(outStream);
        	close(in);
        	close(out);
        }
    }
    
    public static void trashferCopy(String srcPath,String srcFileName,String destPath,String destFileName){
    	File srcDir = new File(srcPath);
    	if(!srcDir.exists())
    		throw new RuntimeException(srcPath+" can't found");
    	File srcFile = new File(srcPath+File.separator+srcFileName);
    	if(!srcFile.exists())
    		throw new RuntimeException(srcFile.getAbsolutePath()+" can't found");
    	mkdirs(destPath);
    	File destFile = new File(destPath+File.separator+destFileName);
    	nioTransferCopy(srcFile, destFile);
    }
    
    public static void close(Closeable c){
    	if(null == c) return;
    	try {
			c.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
    }
    
    
    
}
