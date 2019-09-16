package my.call;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtRead {
	
	private static final Logger logger = LoggerFactory.getLogger(TxtRead.class);

	/**
	 * 读入TXT文件
	 */
	public static String readFile() {
		logger.info("开始读取早报");
		StringBuilder sb = new StringBuilder();
		String pathname = "E:\\stock\\MorningCall.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的MorningCall.txt文件
		// 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
		// 不关闭文件会导致资源的泄露，读写文件都同理
		// Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
		try (
				InputStreamReader isr = new InputStreamReader(new FileInputStream(pathname), "utf-8");
				BufferedReader br = new BufferedReader(isr); // 建立一个对象，它把文件内容转成计算机能读懂的语言
		) {
			String line;

			// 网友推荐更加简洁的写法
			while ((line = br.readLine()) != null) {
				// 一次读入一行数据
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("早报共计："+sb.length());
		return sb.toString();
	}

//	public static void main(String[] args) {
//		System.out.println(readFile());
//	}

}
