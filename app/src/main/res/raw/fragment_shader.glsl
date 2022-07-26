precision mediump float;

uniform sampler2D u_Texture;

varying vec2 v_TexCoordinate;


vec4 getCurrentColor()
{
     vec2 flipped_texcoord = vec2(v_TexCoordinate.x, 1.0 - v_TexCoordinate.y);
     return texture2D(u_Texture, flipped_texcoord);
}


void main()
{
      gl_FragColor = getCurrentColor();
}