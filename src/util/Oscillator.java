package util;

public class Oscillator {
	private float freq, amp, offset, phase;
	public OscType type;

	public enum OscType{
		SINE, SQUARE, SAW, TRIANGLE
	}
	
	

	//phase in seconds
	/**	 * 
	 * @param freq frequency of oscillation
	 * @param amp amplitude of oscillation
	 * @param offset offset in the output
	 * @param phase phase in radians
	 * @param type type of oscillator
	 */
	
	public Oscillator(float freq, float amp, float offset, float phase, OscType type) {
		this.freq = freq;
		this.amp = amp;
		this.offset = offset;
		this.phase = phase;
		this.type = type;
	}


	/**
	 * @param elapsed_time the total time elapsed (since the function is periodic
	 * @return the value at time t
	 */
	public float get(float elapsed_time){
		switch(type){
		case SAW: 
			return (float) (amp*2*((elapsed_time-phase/(2.f*Math.PI*freq))*freq - Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq) - 0.5f) + offset);
		case SINE: 
			return (float) (amp*Math.sin(elapsed_time*Math.PI*2.0*freq - phase) + offset); 
		case SQUARE: 
			return (((elapsed_time-phase/(2.f*Math.PI*freq))*freq - Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq) - 0.5)) > 0.0 ? amp + offset : -amp + offset;
		case TRIANGLE: 
			return (float) (2*amp*Math.abs(2*((elapsed_time-phase/(2.f*Math.PI*freq))*freq-Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq+0.5)))-1.0*amp + offset);
		}
		return 0.0f;
	}


	public float getFreq() {
		return freq;
	}


	public void setFreq(float freq) {
		this.freq = freq;
	}


	public float getAmp() {
		return amp;
	}


	public void setAmp(float amp) {
		this.amp = amp;
	}


	public float getOffset() {
		return offset;
	}


	public void setOffset(float offset) {
		this.offset = offset;
	}


	public float getPhase() {
		return phase;
	}


	public void setPhase(float phase) {
		this.phase = phase;
	}


	public OscType getType() {
		return type;
	}


	public void setType(OscType type) {
		this.type = type;
	}
		

}
