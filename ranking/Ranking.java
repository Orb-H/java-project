package project.ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class Ranking<T> {

	File f;
	HashMap<String, Score<T>> score = new HashMap<>();
	boolean ascending = false;
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

	public void saveIfHigher(String name, T point) {
		try {
			Score s = cl.getDeclaredConstructor(point.getClass()).newInstance(point);
			if (score.containsKey(name)) {
				if (score.get(name).compare(s) < 0) {
					save(name, s);
				}
			} else
				save(name, s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Comparator<Entry<String, Score<T>>> c = new Comparator<Entry<String, Score<T>>>() {

		@Override
		public int compare(Entry<String, Score<T>> o1, Entry<String, Score<T>> o2) {
			return (ascending ? 1 : -1) * o1.getValue().compare(o2.getValue());
		}

	};

	public void store() {
		Stream<Entry<String, Score<T>>> stream = getScore();
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

	public void onDisable() {
		store();
	}

	public Stream<Entry<String, Score<T>>> getScore() {
		return score.entrySet().stream().sorted(c);
	}

	public String[] format() {
		Iterator<Entry<String, Score<T>>> it = getScore().iterator();
		Entry<String, Score<T>> cur;
		String[] s = new String[21];
		s[0] = "RANKING      NAME        SCORE     ";
		for (int i = 1; i <= 10; i++) {
			cur = it.hasNext() ? it.next() : null;
			s[2 * i - 1] = "  " + lengthen(i + "", 2) + "        " + lengthen(cur != null ? cur.getKey() : "", 6)
					+ "     " + (cur != null ? String.format("%9.3f", cur.getValue().value) : "         ") + "   ";
			s[2 * i] = "-----------------------------------";
		}
		return s;

	}

	public String lengthen(String s, int len) {
		while (s.length() < len) {
			s = " " + s;
		}
		return s;
	}

}
