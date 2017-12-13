package com.job.perfomance.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

public class PerformanceReport {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String newFile = (String) args[0];
		 //String newFile = "jobs_api.jtl";
		File fileName = new File(newFile);
		
		try {
			
			List tempList = FileUtils.readLines(fileName);
			Map<String, String> startEndTimeMap = calculateStartEndTimes(tempList);
			Map<String, Double> averageTimeMap = calculateAverageTimes(tempList);
			writeToFile(startEndTimeMap, averageTimeMap);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeToFile(Map<String, String> startEndTimeMap, Map<String, Double> averageTimeMap) {
		// TODO Auto-generated method stub
		File newFile = new File("PerformanceMetrics.log");
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

			for (Map.Entry<String, String> entry : startEndTimeMap.entrySet()) {
				Bw.write(entry.getKey());
				Bw.write(entry.getValue());
				Bw.write("\n");
			}

			for (Map.Entry<String, Double> entry : averageTimeMap.entrySet()) {
				Bw.write(entry.getKey());
				Bw.write(entry.getValue().toString());
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

	private static Map<String, Double> calculateAverageTimes(List tempList) {
		// TODO Auto-generated method stub
		Set<String> uniqueLableSet = new HashSet<String>();
		for (int i = 1; i < tempList.size(); i++) {
			String Line = (String) tempList.get(i);
			if (Line != null && !Line.isEmpty()) {
				uniqueLableSet.add(Line.split(",")[2]);
			}
		}
		Map<String, Double> averageTimeMap = new HashMap<String, Double>();
		Iterator itr = uniqueLableSet.iterator();
		while (itr.hasNext()) {
			String url = (String) itr.next();
			int count = 0;
			int lapsed = 0;
			double average = 0;
			for (int j = 1; j < tempList.size(); j++) {

				String Line = (String) tempList.get(j);
				if (Line != null && !Line.isEmpty() && Line.contains(url)) {
					count++;
					int temp = Integer.valueOf(Line.split(",")[1]);
					lapsed = lapsed + temp;
				}
			}
			average = lapsed / count;
			averageTimeMap.put(url + " : ", average);
			// System.out.println(url +" : "+count+ " : " + average);
		}
		return averageTimeMap;

	}

	private static Map<String, String> calculateStartEndTimes(List tempList) {
		// TODO Auto-generated method stub

		String firstLine = (String) tempList.get(1);
		String endLine = (String) tempList.get(tempList.size() - 1);
		Map<String, String> tempMap = new HashMap();
		Timestamp startTime = getTimestamp(Long.valueOf(firstLine.split(",")[0]));
		Timestamp endTime = getTimestamp(Long.valueOf(endLine.split(",")[0]));
		System.out.println(startTime.toString());
		tempMap.put("Start Time : ", startTime.toString() + " GMT");
		tempMap.put("End Time : ", endTime.toString() + " GMT");

		return tempMap;

	}

	private static Timestamp getTimestamp(Long valueOf) {
		// TODO Auto-generated method stub
		Date date = new Date(valueOf);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String formatted = format.format(date);
		Timestamp timeStamp = Timestamp.valueOf(formatted);
		return timeStamp;

	}

}
