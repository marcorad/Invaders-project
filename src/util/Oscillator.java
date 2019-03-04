package util;

/**An object that has specific waveforms associated with it that can be called to receive a value based on time.
 * The oscillator is centred around zero and can be phase shited in radians and offset in the output, as well symmetrically clipped before offset.
 * @author Marco
 *
 */
public class Oscillator {
	private float freq, amp, offset, phase;
	private OscType type;
	private float ampclamp = Float.POSITIVE_INFINITY;

	/**The type of waveform
	 * @author Marco
	 *
	 */
	public enum OscType{
		SINE, SQUARE, SAW, TRIANGLE
	}	


	/**
	 * @param freq Frequency of oscillation
	 * @param amp Amplitude of oscillation
	 * @param offset Offset in the output
	 * @param phase Phase in radians
	 * @param type Type of oscillator
	 */
	
	public Oscillator(float freq, float amp, float offset, float phase, OscType type) {
		this.freq = freq;
		this.amp = amp;
		this.offset = offset;
		this.phase = phase;
		this.type = type;
	}


	/**Get the value at a certain time
	 * @param elapsed_time The total time elapsed (since the function is periodic)
	 * @return The value at time t
	 */
	public float get(float elapsed_time){
		float y = 0f;
		switch(type){
		case SAW: 
			y = (float) (amp*2*((elapsed_time-phase/(2.f*Math.PI*freq))*freq - Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq) - 0.5f) + offset);
			break;
		case SINE: 
			y = (float) (amp*Util.sinf(elapsed_time*Util.PI*2.0f*freq - phase) + offset); 
			break;
		case SQUARE: 
			y = (((elapsed_time-phase/(2.f*Math.PI*freq))*freq - Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq) - 0.5)) > 0.0 ? amp + offset : -amp + offset;
			break;
		case TRIANGLE: 
			y = (float) (2*amp*Math.abs(2*((elapsed_time-phase/(2.f*Math.PI*freq))*freq-Math.floor((elapsed_time-phase/(2.f*Math.PI*freq))*freq+0.5)))-1.0*amp + offset);
			break;
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


	/**Get the value that the oscillator will be clamped at, both in the positive and negative region, before offset occurs. The allows for clipping of waveforms.
	 * @return The clamp value
	 */
	public float getAmpClamp() {
		return ampclamp;
	}


	/**Set the value that the oscillator will be clamped at, both in the positive and negative region, before offset occurs. The allows for clipping of waveforms.
	 * @param ampclamp The clamp value
	 */
	public void setAmpClamp(float ampclamp) {
		this.ampclamp = ampclamp;
	}
	
	
		

}
