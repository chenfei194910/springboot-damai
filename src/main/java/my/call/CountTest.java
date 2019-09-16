package my.call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountTest {

	private static final Logger logger = LoggerFactory.getLogger(CountTest.class);
	
	   public static void main(String[] args) {
	        String mornCallStr = TxtRead.readFile();
	        
	        List<Map> list=new ArrayList<Map>();
	        
	        ArrayList<String> companyList = POITest.readCompanyList();
	        logger.info("开始查找出现的次数");
	        for (int i = 0; i < companyList.size(); i++) {
	        	String compName= companyList.get(i);
	        	Integer countNum = searchCount(mornCallStr, compName);
	        	if(countNum>0) {
	        		logger.info(compName+"    "+countNum);
	        		
	        		Map<String, String> dataMap=new HashMap<String, String>();
	    	        dataMap.put("compName", compName);
	    	        dataMap.put("countNum", String.valueOf(countNum));
	    	        list.add(dataMap);
	        		
	        	}
			}
	        logger.info("查找到的条数："+list.size());
	        
	        WriteExcel.writeExcel(list, 2, "E://stock//writeExcel.xlsx");
//	        StringBuilder resStr = new StringBuilder();
//			for (Entry<String, Integer> entry : countForComp.entrySet()) {
//				resStr.append(entry.getValue()+"    "+entry.getKey() +"\r\n");
//			}
//			outputResult(resStr.toString());
	    }
	 
	 
//	   String str1="上海证券报&&上海证券报9上海证券报上海证券报";
//	   String str2 = "上海证券报";
	    public static int searchCount(String str1, String str2) {
	        int count = 0;
	 
	        while (true) {
	            int i = str1.indexOf(str2);
	            if (i == -1) {
	                break;
	            } else {
	                count++;
	                str1 = str1.substring(i+1);
	            }
	 
	        }
	 
	       return count;
	 
	    }
	    
//	    public static void outputResult(String resStr) {
//	    	logger.info("开始将查找结果写入文件.............");
//            try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
//                
//                /* 读入TXT文件 */  
//                String path = "E:\\stock\\result\\searchResult.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
//
//                /* 写入Txt文件 */  
//                File writename = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件  
//                writename.createNewFile(); // 创建新文件  
//                BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
//                out.write(resStr);
//                out.flush(); // 把缓存区内容压入文件  
//                out.close(); // 最后记得关闭文件  
//      
//            } catch (Exception e) {  
//                e.printStackTrace();  
//            }  
//            logger.info("结果写入文件结束。。。。。。。。");
//	    }
}
