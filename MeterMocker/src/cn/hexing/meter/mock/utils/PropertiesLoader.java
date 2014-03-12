package cn.hexing.meter.mock.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

public class PropertiesLoader {
	public static final void loadPropertieFile2SystemProperties(){
		String curPath = System.getProperty("user.dir");
		String propFilePattern = ".*\\.properties";
		final Pattern pattern = Pattern.compile(propFilePattern);
		final FilenameFilter filter = new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return pattern.matcher(name).matches();
			}
		};
		searchPropertiesFile(curPath,filter);
		searchPropertiesFile(curPath+File.separator+"cfg",filter);
		searchPropertiesFile(curPath+File.separator+"config",filter);
		searchPropertiesFile(curPath+File.separator+"configuration",filter);
		searchPropertiesFile(curPath+File.separator+"bin",filter);
		searchPropertiesFile(curPath+File.separator+"classess",filter);
	}
	
	private static final void searchPropertiesFile(String dirPath, FilenameFilter filter){
		try{
			File f = new File(dirPath);
			if( !f.exists() || !f.isDirectory() )
				return;
			
			File[] pfs = f.listFiles(filter);
			for( File pf: pfs ){
				Properties props = new Properties();
				props.load(new FileInputStream(pf));
				Enumeration<?> pnames = props.propertyNames();
				while(pnames.hasMoreElements()){
					String propName = (String)pnames.nextElement();
					String propValue = props.getProperty(propName);
					System.setProperty(propName, propValue);
					System.out.println("add sys propertie:("+propName+","+propValue+")");
				}
			}
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	public static void main(String[] args) {
		PropertiesLoader.loadPropertieFile2SystemProperties();
	}
}
