package util;

public class Oscillator {
	private float freq, amp, offset, phase;
	private OscType type;
	private float ampclamp = Float.POSITIVE_INFINITY;

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
		float y = 0f;
		switch(type){
		case SAW: 
			y = (float) (amp*2*((elapsed_time-phase/(2.f*Math.PI*freq))*freq - Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq) - 0.5f) + offset);
		case SINE: 
			y = (float) (amp*Math.sin(elapsed_time*Math.PI*2.0*freq - phase) + offset); 
		case SQUARE: 
			y = (((elapsed_time-phase/(2.f*Math.PI*freq))*freq - Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq) - 0.5)) > 0.0 ? amp + offset : -amp + offset;
		case TRIANGLE: 
			y = (float) (2*amp*Math.abs(2*((elapsed_time-phase/(2.f*Math.PI*freq))*freq-Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq+0.5)))-1.0*amp + offset);
		}
		return Util.clamp(y, offset-ampclamp, offset+ampclamp);
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


	public float getAmpClamp() {
		return ampclamp;
	}


	public void setAmpClamp(float ampclamp) {
		this.ampclamp = ampclamp;
	}
	
	
		

}
