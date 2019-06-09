package project.ranking;

public abstract class Score<T> {

	T value;

	public Score(T t) {
		value = t;
	}

	public abstract int compare(Score<T> s);

	public abstract String toString();
}
