package project.ranking;

public class Score1v1 extends Score<Double> {

	public Score1v1(Double t) {
		super(t);
	}

	public Score1v1(String s) {
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
