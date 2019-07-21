package ts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class LevelTxttoCsv {

	static ArrayList<ArrayList<String>> list = new ArrayList<>();
	static int order = 0;
	public static void main(String[] args) throws IOException {
		String level="level3";
		//String level = "sharedassets2.assets";
		String filepath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Twilight Struggle\\TwilightStruggle_Data\\Unity_Assets_Files\\"+level+"\\";
		//String filepath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Twilight Struggle\\TwilightStruggle_Data\\Unity_Assets_Files\\leveladdon\\"+level+"\\";
		for (int i = 6926; i <= 9237; i++) {
			//list.clear();
			order = i;
			readFile(filepath, "unnamed asset-"+level+"-" + i + "-MonoBehaviour.txt");
			//writeCsv(filepath, "level1-" + i + ".csv");//不全
		}
		writeCsv(filepath, level+"-total.csv");

	}

	private static void readFile(String path, String name) {
		String pathname = path + name;
		try (FileInputStream fis = new FileInputStream(pathname);
				InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
				// FileReader reader = new FileReader(pathname);//不用这个，不支持UTF-8
				BufferedReader br = new BufferedReader(reader)) {
			String line = null;
			//String pattern = "displayName";// level0
			//String pattern2 = "displayDescription";// level0
			//String pattern = "m_cardName";// level1_UI_CardGallery
			//String pattern2 = "m_history";// level1_UI_CardGallery
			String pattern3 = "m_Text";// level1 level2 level3
			//Pattern r = Pattern.compile(pattern);
			//Pattern r2 = Pattern.compile(pattern2);
			Pattern r3 = Pattern.compile(pattern3);
			String matchstring = "";
			int i = 0;
			while ((line = br.readLine()) != null) {
				//Matcher m = r.matcher(line);
				//Matcher m2 = r2.matcher(line);
				Matcher m3 = r3.matcher(line);
				if (m3.find()) {// | m2.find() | m.find()
					matchstring = line.substring(line.indexOf("\"") + 1, line.length() - 1);
					if (ts.Tools.wordfilter(matchstring)) {
						ArrayList<String> strlist = new ArrayList<String>();
						//strlist.add(ts.Tools.crypt(matchstring));
						i++;
						strlist.add(order+"_"+i);
						strlist.add(matchstring);
						list.add(strlist);
						//System.out.println(matchstring);
						// System.out.println(line.indexOf("\""));
					}
				}
			}
			System.out.println(list.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeCsv(String path, String name) throws IOException {
		if (list.size() >= 1) {
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
}
