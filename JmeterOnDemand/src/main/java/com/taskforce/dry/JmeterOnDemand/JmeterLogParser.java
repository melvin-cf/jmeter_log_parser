package com.taskforce.dry.JmeterOnDemand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class JmeterLogParser {

	public static void main(String[] args) {

		String newFile = (String) args[0];
		File fileName = new File(newFile);
		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<String> finalList = new ArrayList<String>();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			List tempList = FileUtils.readLines(fileName);
			Iterator itr = tempList.iterator();
			while (itr.hasNext()) {
				String temp = (String) itr.next();
				if (temp.contains("Active:")) {
					// System.out.println(temp);
					String[] strArray = temp.split("\\s");
					StringBuffer str = new StringBuffer("TimeStamp: " + strArray[0] + " " + strArray[1] + " ");
					str.append(strArray[strArray.length - 6] + strArray[strArray.length - 5] + " ");
					str.append(strArray[strArray.length - 4] + strArray[strArray.length - 3] + " ");
					str.append(strArray[strArray.length - 2] + strArray[strArray.length - 1] + " ");
					finalList.add(str.toString());

				}
			}
			writeToFile(finalList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeToFile(ArrayList<String> finalList) {
		// TODO Auto-generated method stub
		File newFile = new File("Debug.csv");
		FileWriter fw = null;
		BufferedWriter Bw = null;

		try {
			if (newFile.delete()) {
				newFile.createNewFile();
			}
			if (fw == null) {
				fw = new FileWriter(newFile, true);
			}
			if (Bw == null) {
				Bw = new BufferedWriter(fw);
			}
			for (int k = 0; k < finalList.size(); k++) {

				Bw.write(finalList.get(k));
				Bw.write("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (Bw != null) {
					Bw.flush();
					Bw.close();
				}

				if (fw != null) {
					fw.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
