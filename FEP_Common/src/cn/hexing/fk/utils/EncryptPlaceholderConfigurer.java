package cn.hexing.fk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

public class EncryptPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private String encryptFileName = null;
	private List<String> encryptPropNames = new ArrayList<String>();
	
	@Override
	protected void convertProperties(Properties props) {
		Pattern p = Pattern.compile("[0-9a-fA-F]+");
		Enumeration<?> propertyNames = props.propertyNames();
		while (propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
			boolean isEncryptProp = false;
			for(String epname : encryptPropNames ){
				if( epname.equalsIgnoreCase(propertyName)){
					isEncryptProp = true;
					break;
				}
			}
			if( isEncryptProp ){
				String propertyValue = props.getProperty(propertyName);
				if( propertyValue.length() % 8 ==0 ){
					Matcher matcher = p.matcher(propertyValue);
					if( matcher.matches() ){
						try{
							String newValue = DESEncryptUtil.decrypt(propertyValue);
							props.setProperty(propertyName, newValue);
							System.out.println(propertyName+"="+newValue);
						}catch(Exception e){
							
						}
					}
				}
			}
		}
		super.convertProperties(props);
	}

	@Override
	protected String convertPropertyValue(String originalValue) {
		return super.convertPropertyValue(originalValue);
	}

	@Override
	public void setLocations(Resource[] locations) {
		if( null != encryptFileName ){
			for(Resource r: locations ){
				try{
					File f = r.getFile();
					if( encryptFileName.equalsIgnoreCase(f.getName() )){
						handlePropertiesFile(f);
					}
				}catch(Exception e){
					
				}
			}
		}
		super.setLocations(locations);
	}
	
	private void handlePropertiesFile(File propFile) throws Exception{
		Pattern p = Pattern.compile("[0-9a-fA-F]+");
		BufferedReader reader = new BufferedReader(new FileReader(propFile));
		ArrayList<String> lines = new ArrayList<String>();
		boolean modified = false;
		try{
			String line = null;
			while( null != (line=reader.readLine()) ){
				line = line.trim();
				if( line.startsWith("#")){
					lines.add(line);
					continue;
				}
				int index = line.indexOf("=");
				if( index<=0 ){
					lines.add(line);
					continue;
				}
				String propName = line.substring(0,index);
				String propValue = line.substring(index+1);
				boolean tobeEncrypt = false;
				for(String epname : encryptPropNames ){
					if( epname.equalsIgnoreCase(propName)){
						tobeEncrypt = true;
						break;
					}
				}

				if( tobeEncrypt ){
					Matcher matcher = p.matcher(propValue);
					if( propValue.length() % 8 ==0 && matcher.matches() ){
						//Is encrypted successfully.
						lines.add(line);
						continue;
					}
					else{
						try{
							String newValue = DESEncryptUtil.encrypt(propValue);
							System.out.println(propValue+" encrypt as: "+newValue);
							lines.add(propName+"="+newValue);
							modified = true;
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				else{
					lines.add(line);
				}
			}
			if( modified ){
				reader.close();
				reader = null;
				PrintWriter writer = new PrintWriter(propFile);
				
				for(String oneline : lines )
					writer.println(oneline);
				writer.flush();
				writer.close();
				writer = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if( null != reader )
				reader.close();
		}
	}

	public void setEncryptFileName(String encryptFileName) {
		this.encryptFileName = encryptFileName;
	}

	public void setEncryptPropNames(List<String> encryptPropNames) {
		this.encryptPropNames = encryptPropNames;
	}
}
