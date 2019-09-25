package my.call;

import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetMorningCallTxt {

	private static final Logger logger = LoggerFactory.getLogger(ResetMorningCallTxt.class);
	
	   public static void main(String[] args) {
	        String allMorningCall = TxtRead.readFile(); //读取晨报文件
	        String[] eachMorningCall = allMorningCall.split("aaa"); //字符串分隔以后得到每家的晨报
	        logger.info("共要删除{}家晨报",eachMorningCall.length);
	        StringBuilder title = new StringBuilder();
	        //遍历每家的晨报
	        for (int i = 0; i < eachMorningCall.length; i++) {
	        	String morningCall = eachMorningCall[i]; //每家晨报的内容
	        	if(StringUtils.isBlank(morningCall)) {
	        		continue;
	        	}
	        	String row = morningCall.substring(0, morningCall.indexOf("del"));
	        	title.append("aaa").append(row).append("del").append("\t\n\n\n\n");
			}
	        //重写txt文本
	        ResetTxt(title.toString(),  "E:\\stock\\MorningCall.txt");
	    }
	   
	   

	    public static void ResetTxt(String str,String txtPath){
	    	FileWriter writer;
	        try {
	        	writer = new FileWriter(txtPath);
	        	writer.write("");//清空原文件内容
	        	writer.write(str);
	        	writer.flush();
	            writer.close();            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        logger.info("MorningCall txt reset success");
	    }
	    
}
