package my.call;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitTxtTest {

	private static final Logger logger = LoggerFactory.getLogger(SplitTxtTest.class);
	
	   public static void main(String[] args) {
	        String allMorningCall = TxtRead.readFile(); //读取晨报文件
	        String[] eachMorningCall = allMorningCall.split("aaa"); //字符串分隔以后得到每家的晨报
	        logger.info("共{}家晨报",eachMorningCall.length);
	        ArrayList<String> companyList = POITest.readCompanyList(); //所有上市公司的名字
	        logger.info("共{}家上市公司",companyList.size());
	        List<String> appearList = new ArrayList<String>(); //整个研报中推荐的上市公司
	        //遍历每家的晨报
	        for (int i = 0; i < eachMorningCall.length; i++) {
	        	String morningCall = eachMorningCall[i]; //每家晨报的内容
	        	
		        for (int j = 0; j < companyList.size(); j++) {
		        	String compName= companyList.get(j); //上市公司名字
		        	//返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1
		        	Integer countNum = morningCall.indexOf(compName); 
		        	if (countNum != -1) {
		        		appearList.add(compName);
		        	}
				}
			}
	        List<Map> list=new ArrayList<Map>(); //需要输出的结果集
    		
    		Map<String, String> excelTitleMap=new HashMap<String, String>(); //excel标题
    		excelTitleMap.put("compName", "公司名称");
    		excelTitleMap.put("countNum", "推荐的家数");
	        list.add(excelTitleMap);
	        
	        Map<String, Integer> compMap = new HashMap<String, Integer>();
	        for (String temp : appearList) {
	            Integer count = compMap.get(temp);
	            compMap.put(temp, (count == null) ? 1 : count + 1);
	        }
	        
	        Map<String, Integer> sorted = new HashMap<String, Integer>();
	        sorted = sortByValue(compMap);
	        for(Map.Entry<String, Integer> entry : sorted.entrySet()) {
	        	Map<String, String> dataMap=new HashMap<String, String>();
	        	dataMap.put("compName", entry.getKey());
	        	dataMap.put("countNum", String.valueOf(entry.getValue()));
	        	list.add(dataMap);	        	
	        }
	        
	        //结果输出到excel
	        WriteExcel.writeExcel(list, 2, "E://stock//writeExcel.xlsx");

	    }
	 
	 
	   /**
	    * 查找出现在晨报中的上市公司
	    * @Title: searchCompany
	    * @Description: TODO
	    * @param: str1 晨报
	    * @param: str2上市公司名称
	    * @return: 
	    * @throws:
	    */
	    public static int searchCompany(String str1, String str2) {
	        int count = 0;
	        int i = str1.indexOf(str2);
            if (i == -1) {
            	count = 0;
            } else {
                count++;
            }
            return count;
	    }
	    
	    /**
	     * 根据map的value对map排序
	     * @Title: sortByValue
	     * @Description: TODO
	     * @param: 
	     * @return: Map<K,V>  
	     * @throws:
	     */
	    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	        Map<K, V> result = new LinkedHashMap<>();
	        Stream<Entry<K, V>> st = map.entrySet().stream();
	  
	        st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));
	  
	        return result;
	    }
	    
}
