package project.ranking;

public class Score2v2 extends Score<Double> {

	public Score2v2(Double t) {
		super(t);
	}

	public Score2v2(String s) {
		this(Double.parseDouble(s));
	}

	@Override
	public int compare(Score<Double> s) {
		return value.compareTo(s.value);
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
