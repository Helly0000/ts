package ts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class HelloWorld {
	static ArrayList<ArrayList<String>> list = new ArrayList<>();
	//Ctrl+Shift+f 自动缩进
	/**
	 * Encodes a string 2 MD5
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws NoSuchAlgorithmException
	 */
	public static String crypt(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}

	public static void main(String[] args) throws IOException {
		// System.out.println("Hello World!");
		readFile("C:\\Users\\Helly\\eclipse-workspace\\ts\\src\\ts\\", "input.txt");
		writeCsv("C:\\Users\\Helly\\eclipse-workspace\\ts\\src\\ts\\", "test.csv");
	}

	public static void readFile(String path, String name) {
		String pathname = path + name;
		try (FileInputStream fis = new FileInputStream(pathname);
				InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
				// FileReader reader = new FileReader(pathname);//不用这个，不支持UTF-8
				BufferedReader br = new BufferedReader(reader)) {
			String line = null;
			String pattern = "displayName";
			String pattern2 = "displayDescription";
			Pattern r = Pattern.compile(pattern);
			Pattern r2 = Pattern.compile(pattern2);
			String matchstring = "";
			while ((line = br.readLine()) != null) {

				Matcher m = r.matcher(line);
				Matcher m2 = r2.matcher(line);

				if (m.find() | m2.find()) {
					matchstring = line.substring(line.indexOf("\"") + 1, line.length() - 1);
					ArrayList<String> strlist = new ArrayList<String>();
					strlist.add(crypt(matchstring));
					strlist.add(matchstring);
					list.add(strlist);
					// System.out.println(matchstring);
					// System.out.println(line.indexOf("\""));
				}
			}
			System.out.println(list.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeCsv(String path, String name) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + name));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			writer.write("\uFEFF");// BOM?//读取的是没有BOM的，但是上传到ParaTranz的csv最好有BOM
			for (ArrayList<?> c : list) {
				csvPrinter.printRecord(Arrays.asList(c).get(0));
			}
			csvPrinter.flush();
		}
	}
}
