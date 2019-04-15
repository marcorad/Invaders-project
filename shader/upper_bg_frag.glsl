#version 330


//
// Description : Array and textureless GLSL 2D/3D/4D simplex
//               noise functions.
//      Author : Ian McEwan, Ashima Arts.
//  Maintainer : stegu
//     Lastmod : 20110822 (ijm)
//     License : Copyright (C) 2011 Ashima Arts. All rights reserved.
//               Distributed under the MIT License. See LICENSE file.
//               https://github.com/ashima/webgl-noise
//               https://github.com/stegu/webgl-noise
//

vec3 mod289(vec3 x) {
	return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 mod289(vec4 x) {
	return x - floor(x * (1.0 / 289.0)) * 289.0;
}

vec4 permute(vec4 x) {
	return mod289(((x*34.0)+1.0)*x);
}

vec4 taylorInvSqrt(vec4 r)
{
	return 1.79284291400159 - 0.85373472095314 * r;
}

float snoise(vec3 v)
{
	const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;
	const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);

	// First corner
	vec3 i  = floor(v + dot(v, C.yyy) );
	vec3 x0 =   v - i + dot(i, C.xxx) ;

	// Other corners
	vec3 g = step(x0.yzx, x0.xyz);
	vec3 l = 1.0 - g;
	vec3 i1 = min( g.xyz, l.zxy );
	vec3 i2 = max( g.xyz, l.zxy );

	//   x0 = x0 - 0.0 + 0.0 * C.xxx;
	//   x1 = x0 - i1  + 1.0 * C.xxx;
	//   x2 = x0 - i2  + 2.0 * C.xxx;
	//   x3 = x0 - 1.0 + 3.0 * C.xxx;
	vec3 x1 = x0 - i1 + C.xxx;
	vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y
	vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y

	// Permutations
	i = mod289(i);
	vec4 p = permute( permute( permute(
			i.z + vec4(0.0, i1.z, i2.z, 1.0 ))
			+ i.y + vec4(0.0, i1.y, i2.y, 1.0 ))
			+ i.x + vec4(0.0, i1.x, i2.x, 1.0 ));

	// Gradients: 7x7 points over a square, mapped onto an octahedron.
	// The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)
	float n_ = 0.142857142857; // 1.0/7.0
	vec3  ns = n_ * D.wyz - D.xzx;

	vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)

	vec4 x_ = floor(j * ns.z);
	vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)

	vec4 x = x_ *ns.x + ns.yyyy;
	vec4 y = y_ *ns.x + ns.yyyy;
	vec4 h = 1.0 - abs(x) - abs(y);

	vec4 b0 = vec4( x.xy, y.xy );
	vec4 b1 = vec4( x.zw, y.zw );

	//vec4 s0 = vec4(lessThan(b0,0.0))*2.0 - 1.0;
	//vec4 s1 = vec4(lessThan(b1,0.0))*2.0 - 1.0;
	vec4 s0 = floor(b0)*2.0 + 1.0;
	vec4 s1 = floor(b1)*2.0 + 1.0;
	vec4 sh = -step(h, vec4(0.0));

	vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;
	vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;

	vec3 p0 = vec3(a0.xy,h.x);
	vec3 p1 = vec3(a0.zw,h.y);
	vec3 p2 = vec3(a1.xy,h.z);
	vec3 p3 = vec3(a1.zw,h.w);

	//Normalise gradients
	vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));
	p0 *= norm.x;
	p1 *= norm.y;
	p2 *= norm.z;
	p3 *= norm.w;

	// Mix final noise value
	vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
	m = m * m;
	return 42.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1),
			dot(p2,x2), dot(p3,x3) ) );
}

//	float n = (0.5+0.5*snoise(noisecoord));
//	float c = n > 0.7 ? 0 : 1;
//	float a = 0;
//	float min_attenuation = 10000000;
//	for(int i = 0; i < num_blobs; i++){
//		float dist = length(gl_FragCoord.xy - blob_pos[i]);
//		float attenuation = 1/(dist);
//		min_attenuation = attenuation < min_attenuation ? attenuation : min_attenuation;
//		a += alpha_mult[i]*30 *attenuation * c * (0.75 + n/4);
//	}
//
//	a = a > 0.3 ? 3 * min_attenuation + n :0;


uniform float t;
uniform vec2 blob_pos[32];
uniform float alpha_mult[32];
void main(void)
{

	float noise_pos_detail_x = 0.015;
	float noise_pos_detail_y = 0.019;
	float noise_t_detail = 0.25;
	vec2 noise_pos = vec2(gl_FragCoord.x * noise_pos_detail_x, gl_FragCoord.y * noise_pos_detail_y);
	vec3 noisecoord = vec3( noise_pos, vec3(t*noise_t_detail));
	float n = (0.5+0.5*snoise(noisecoord));
	float a = 0, min_dist = 100000;
	int min_dist_index = 0;
	float c = 1;
	for(int i = 0; i < 32; i++){
		float l = length(gl_FragCoord.xy - blob_pos[i]);
		//		if(l < min_dist) {
		//			min_dist = l;
		//			min_dist_index = i;
		//		}
		if( c != 0 && l < 100 - n * 40){
			float da = alpha_mult[i];
			//da = da > 0.4 ? 0.4 : da;
			a += da;
		}
	}

	//	if(min_dist < 100 - 30 * n){
	//		a =  c == 1 ? 0.3*alpha_mult[min_dist_index] : 0;
	//	} else {
	//		a = 0;
	//	}


	gl_FragColor =  vec4(0,c,0, clamp(a, 0, 0.4));

}
