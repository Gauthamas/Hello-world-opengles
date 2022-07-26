attribute vec4 a_Position;

attribute vec2 a_TexCoordinate;

uniform int u_IsPortrait;

varying vec2 v_TexCoordinate;

void main()
{
            //Image width/height
            float aspect = 512.0/256.0;
            vec2 scale = vec2(aspect, 1.0);
            if(u_IsPortrait == 1)
            {
                scale = vec2(1.0, aspect);
            }
            v_TexCoordinate = scale*(a_TexCoordinate - 0.5)+0.5;
            gl_Position = a_Position;
}