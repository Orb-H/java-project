package project.ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class Ranking<T> {

	File f;
	HashMap<String, Score<T>> score = new HashMap<>();
	boolean ascending = true;
	String name;
	Class<? extends Score<T>> cl;

	public Ranking(String filename, String name, Class<? extends Score<T>> cl) {
		f = new File(filename);
		this.name = name;
		this.cl = cl;
	}

	public void onEnable() {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String s;
				while ((s = br.readLine()) != null) {
					try {
						save(s, cl.getDeclaredConstructor(String.class).newInstance(br.readLine()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void save(String name, Score<T> point) {
		score.put(name, point);
	}

	public void saveIfHigher(String name, Score<T> point) {
		if (score.containsKey(name)) {
			if (score.get(name).compare(point) < 0) {
				save(name, point);
			}
		} else
			save(name, point);
	}

	Comparator<Entry<String, Score<T>>> c = new Comparator<Entry<String, Score<T>>>() {

		@Override
		public int compare(Entry<String, Score<T>> o1, Entry<String, Score<T>> o2) {
			return (ascending ? 1 : -1) * o1.getValue().compare(o2.getValue());
		}

	};

	public void onDisable() {
		Stream<Entry<String, Score<T>>> stream = score.entrySet().stream().sorted(c);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f));
			Iterator<Entry<String, Score<T>>> ite = stream.iterator();
			while (ite.hasNext()) {
				Entry<String, Score<T>> ent = ite.next();
				pw.println(ent.getKey());
				pw.println(ent.getValue());
				pw.println("-----");
				pw.flush();
			}
			pw.close();
		} catch (IOException e) {
		}
	}

	public String format() {
		return "";
	}

}
