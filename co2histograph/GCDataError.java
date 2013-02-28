package co2histograph;

public class GCDataError
{
    int time;
    float value;
	public GCDataError(int time, float value) {
		super();
		this.time = time;
		this.value = value;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public String toString() {
		return "[time=" + time + ", value=" + value + "]";
	}
}
