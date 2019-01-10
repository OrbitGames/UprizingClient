package net.minecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class OpenGlHelper
{
    public static boolean openGL21;

    /**
     * An OpenGL constant corresponding to GL_TEXTURE0, used when setting data pertaining to auxiliary OpenGL texture
     * units.
     */
    public static int defaultTexUnit;

    /**
     * An OpenGL constant corresponding to GL_TEXTURE1, used when setting data pertaining to auxiliary OpenGL texture
     * units.
     */
    public static int lightmapTexUnit;
    public static boolean field_153197_d;
    public static int field_153198_e;
    public static int field_153199_f;
    public static int field_153200_g;
    public static int field_153201_h;
    public static int field_153202_i;
    public static int field_153203_j;
    public static int field_153204_k;
    public static int field_153205_l;
    public static int field_153206_m;
    private static int field_153212_w;
    public static boolean framebufferSupported;
    private static boolean field_153213_x;
    private static boolean field_153214_y;
    public static int GL_LINK_STATUS;
    public static int GL_COMPILE_STATUS;
    public static int GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER;
    public static boolean anisotropicFilteringSupported;
    public static int anisotropicFilteringMax;
    private static boolean field_153215_z;
    private static boolean openGL14;
    public static boolean field_153211_u;
    public static boolean shadersSupported;
    private static String field_153196_B = "";
    public static float lastBrightnessX = 0.0F;
    public static float lastBrightnessY = 0.0F;
    private static final String __OBFID = "CL_00001179";

    /**
     * Initializes the texture constants to be used when rendering lightmap values
     */
    public static void initializeTextures()
    {
        Config.initDisplay();
        ContextCapabilities var0 = GLContext.getCapabilities();
        field_153215_z = var0.GL_ARB_multitexture && !var0.OpenGL13;

        if (field_153215_z)
        {
            field_153196_B = field_153196_B + "Using multitexturing ARB.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
        }
        else
        {
            field_153196_B = field_153196_B + "Using GL 1.3 multitexturing.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
        }

        field_153211_u = var0.GL_EXT_blend_func_separate && !var0.OpenGL14;
        openGL14 = var0.OpenGL14 || var0.GL_EXT_blend_func_separate;
        framebufferSupported = openGL14 && (var0.GL_ARB_framebuffer_object || var0.GL_EXT_framebuffer_object || var0.OpenGL30);

        if (framebufferSupported)
        {
            field_153196_B = field_153196_B + "Using framebuffer objects because ";

            if (var0.OpenGL30)
            {
                field_153196_B = field_153196_B + "OpenGL 3.0 is supported and separate blending is supported.\n";
                field_153212_w = 0;
                field_153198_e = 36160;
                field_153199_f = 36161;
                field_153200_g = 36064;
                field_153201_h = 36096;
                field_153202_i = 36053;
                field_153203_j = 36054;
                field_153204_k = 36055;
                field_153205_l = 36059;
                field_153206_m = 36060;
            }
            else if (var0.GL_ARB_framebuffer_object)
            {
                field_153196_B = field_153196_B + "ARB_framebuffer_object is supported and separate blending is supported.\n";
                field_153212_w = 1;
                field_153198_e = 36160;
                field_153199_f = 36161;
                field_153200_g = 36064;
                field_153201_h = 36096;
                field_153202_i = 36053;
                field_153204_k = 36055;
                field_153203_j = 36054;
                field_153205_l = 36059;
                field_153206_m = 36060;
            }
            else if (var0.GL_EXT_framebuffer_object)
            {
                field_153196_B = field_153196_B + "EXT_framebuffer_object is supported.\n";
                field_153212_w = 2;
                field_153198_e = 36160;
                field_153199_f = 36161;
                field_153200_g = 36064;
                field_153201_h = 36096;
                field_153202_i = 36053;
                field_153204_k = 36055;
                field_153203_j = 36054;
                field_153205_l = 36059;
                field_153206_m = 36060;
            }
        }
        else
        {
            field_153196_B = field_153196_B + "Not using framebuffer objects because ";
            field_153196_B = field_153196_B + "OpenGL 1.4 is " + (var0.OpenGL14 ? "" : "not ") + "supported, ";
            field_153196_B = field_153196_B + "EXT_blend_func_separate is " + (var0.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            field_153196_B = field_153196_B + "OpenGL 3.0 is " + (var0.OpenGL30 ? "" : "not ") + "supported, ";
            field_153196_B = field_153196_B + "ARB_framebuffer_object is " + (var0.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            field_153196_B = field_153196_B + "EXT_framebuffer_object is " + (var0.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }

        anisotropicFilteringSupported = var0.GL_EXT_texture_filter_anisotropic;
        anisotropicFilteringMax = (int)(anisotropicFilteringSupported ? GL11.glGetFloat(34047) : 0.0F);
        field_153196_B = field_153196_B + "Anisotropic filtering is " + (anisotropicFilteringSupported ? "" : "not ") + "supported";

        if (anisotropicFilteringSupported)
        {
            field_153196_B = field_153196_B + " and maximum anisotropy is " + anisotropicFilteringMax + ".\n";
        }
        else
        {
            field_153196_B = field_153196_B + ".\n";
        }

        GameSettings.Options.ANISOTROPIC_FILTERING.setValueMax((float)anisotropicFilteringMax);
        openGL21 = var0.OpenGL21;
        field_153213_x = openGL21 || var0.GL_ARB_vertex_shader && var0.GL_ARB_fragment_shader && var0.GL_ARB_shader_objects;
        field_153196_B = field_153196_B + "Shaders are " + (field_153213_x ? "" : "not ") + "available because ";

        if (field_153213_x)
        {
            if (var0.OpenGL21)
            {
                field_153196_B = field_153196_B + "OpenGL 2.1 is supported.\n";
                field_153214_y = false;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            }
            else
            {
                field_153196_B = field_153196_B + "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                field_153214_y = true;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            }
        }
        else
        {
            field_153196_B = field_153196_B + "OpenGL 2.1 is " + (var0.OpenGL21 ? "" : "not ") + "supported, ";
            field_153196_B = field_153196_B + "ARB_shader_objects is " + (var0.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            field_153196_B = field_153196_B + "ARB_vertex_shader is " + (var0.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            field_153196_B = field_153196_B + "ARB_fragment_shader is " + (var0.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }

        shadersSupported = framebufferSupported && field_153213_x;
        field_153197_d = GL11.glGetString(GL11.GL_VENDOR).toLowerCase().contains("nvidia");
    }

    public static boolean func_153193_b()
    {
        return shadersSupported;
    }

    public static String func_153172_c()
    {
        return field_153196_B;
    }

    public static int glGetProgrami(int p_153175_0_, int p_153175_1_)
    {
        return field_153214_y ? ARBShaderObjects.glGetObjectParameteriARB(p_153175_0_, p_153175_1_) : GL20.glGetProgrami(p_153175_0_, p_153175_1_);
    }

    public static void glAttachShader(int p_153178_0_, int p_153178_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glAttachObjectARB(p_153178_0_, p_153178_1_);
        }
        else
        {
            GL20.glAttachShader(p_153178_0_, p_153178_1_);
        }
    }

    public static void glDeleteShader(int p_153180_0_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glDeleteObjectARB(p_153180_0_);
        }
        else
        {
            GL20.glDeleteShader(p_153180_0_);
        }
    }

    public static int glCreateShader(int p_153195_0_)
    {
        return field_153214_y ? ARBShaderObjects.glCreateShaderObjectARB(p_153195_0_) : GL20.glCreateShader(p_153195_0_);
    }

    public static void glShaderSource(int p_153169_0_, ByteBuffer p_153169_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glShaderSourceARB(p_153169_0_, p_153169_1_);
        }
        else
        {
            GL20.glShaderSource(p_153169_0_, p_153169_1_);
        }
    }

    public static void glCompileShader(int p_153170_0_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glCompileShaderARB(p_153170_0_);
        }
        else
        {
            GL20.glCompileShader(p_153170_0_);
        }
    }

    public static int glGetShaderi(int p_153157_0_, int p_153157_1_)
    {
        return field_153214_y ? ARBShaderObjects.glGetObjectParameteriARB(p_153157_0_, p_153157_1_) : GL20.glGetShaderi(p_153157_0_, p_153157_1_);
    }

    public static String glGetShaderInfoLog(int p_153158_0_, int p_153158_1_)
    {
        return field_153214_y ? ARBShaderObjects.glGetInfoLogARB(p_153158_0_, p_153158_1_) : GL20.glGetShaderInfoLog(p_153158_0_, p_153158_1_);
    }

    public static String glGetProgramInfoLog(int p_153166_0_, int p_153166_1_)
    {
        return field_153214_y ? ARBShaderObjects.glGetInfoLogARB(p_153166_0_, p_153166_1_) : GL20.glGetProgramInfoLog(p_153166_0_, p_153166_1_);
    }

    public static void glUseProgram(int p_153161_0_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUseProgramObjectARB(p_153161_0_);
        }
        else
        {
            GL20.glUseProgram(p_153161_0_);
        }
    }

    public static int glCreateProgram()
    {
        return field_153214_y ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }

    public static void glDeleteProgram(int p_153187_0_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glDeleteObjectARB(p_153187_0_);
        }
        else
        {
            GL20.glDeleteProgram(p_153187_0_);
        }
    }

    public static void glLinkProgram(int p_153179_0_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glLinkProgramARB(p_153179_0_);
        }
        else
        {
            GL20.glLinkProgram(p_153179_0_);
        }
    }

    public static int glGetUniformLocation(int p_153194_0_, CharSequence p_153194_1_)
    {
        return field_153214_y ? ARBShaderObjects.glGetUniformLocationARB(p_153194_0_, p_153194_1_) : GL20.glGetUniformLocation(p_153194_0_, p_153194_1_);
    }

    public static void glUniform1(int p_153181_0_, IntBuffer p_153181_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform1ARB(p_153181_0_, p_153181_1_);
        }
        else
        {
            GL20.glUniform1(p_153181_0_, p_153181_1_);
        }
    }

    public static void glUniform1i(int p_153163_0_, int p_153163_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform1iARB(p_153163_0_, p_153163_1_);
        }
        else
        {
            GL20.glUniform1i(p_153163_0_, p_153163_1_);
        }
    }

    public static void glUniform1(int p_153168_0_, FloatBuffer p_153168_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform1ARB(p_153168_0_, p_153168_1_);
        }
        else
        {
            GL20.glUniform1(p_153168_0_, p_153168_1_);
        }
    }

    public static void glUniform2(int p_153182_0_, IntBuffer p_153182_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform2ARB(p_153182_0_, p_153182_1_);
        }
        else
        {
            GL20.glUniform2(p_153182_0_, p_153182_1_);
        }
    }

    public static void glUniform2(int p_153177_0_, FloatBuffer p_153177_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform2ARB(p_153177_0_, p_153177_1_);
        }
        else
        {
            GL20.glUniform2(p_153177_0_, p_153177_1_);
        }
    }

    public static void glUniform3(int p_153192_0_, IntBuffer p_153192_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform3ARB(p_153192_0_, p_153192_1_);
        }
        else
        {
            GL20.glUniform3(p_153192_0_, p_153192_1_);
        }
    }

    public static void glUniform3(int p_153191_0_, FloatBuffer p_153191_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform3ARB(p_153191_0_, p_153191_1_);
        }
        else
        {
            GL20.glUniform3(p_153191_0_, p_153191_1_);
        }
    }

    public static void glUniform4(int p_153162_0_, IntBuffer p_153162_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform4ARB(p_153162_0_, p_153162_1_);
        }
        else
        {
            GL20.glUniform4(p_153162_0_, p_153162_1_);
        }
    }

    public static void glUniform4(int p_153159_0_, FloatBuffer p_153159_1_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniform4ARB(p_153159_0_, p_153159_1_);
        }
        else
        {
            GL20.glUniform4(p_153159_0_, p_153159_1_);
        }
    }

    public static void glUniformMatrix2(int p_153173_0_, boolean p_153173_1_, FloatBuffer p_153173_2_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniformMatrix2ARB(p_153173_0_, p_153173_1_, p_153173_2_);
        }
        else
        {
            GL20.glUniformMatrix2(p_153173_0_, p_153173_1_, p_153173_2_);
        }
    }

    public static void glUniformMatrix3(int p_153189_0_, boolean p_153189_1_, FloatBuffer p_153189_2_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniformMatrix3ARB(p_153189_0_, p_153189_1_, p_153189_2_);
        }
        else
        {
            GL20.glUniformMatrix3(p_153189_0_, p_153189_1_, p_153189_2_);
        }
    }

    public static void glUniformMatrix4(int p_153160_0_, boolean p_153160_1_, FloatBuffer p_153160_2_)
    {
        if (field_153214_y)
        {
            ARBShaderObjects.glUniformMatrix4ARB(p_153160_0_, p_153160_1_, p_153160_2_);
        }
        else
        {
            GL20.glUniformMatrix4(p_153160_0_, p_153160_1_, p_153160_2_);
        }
    }

    public static int glGetAttribLocation(int p_153164_0_, CharSequence p_153164_1_)
    {
        return field_153214_y ? ARBVertexShader.glGetAttribLocationARB(p_153164_0_, p_153164_1_) : GL20.glGetAttribLocation(p_153164_0_, p_153164_1_);
    }

    public static void func_153171_g(int p_153171_0_, int p_153171_1_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glBindFramebuffer(p_153171_0_, p_153171_1_);
                    break;

                case 1:
                    ARBFramebufferObject.glBindFramebuffer(p_153171_0_, p_153171_1_);
                    break;

                case 2:
                    EXTFramebufferObject.glBindFramebufferEXT(p_153171_0_, p_153171_1_);
            }
        }
    }

    public static void func_153176_h(int p_153176_0_, int p_153176_1_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glBindRenderbuffer(p_153176_0_, p_153176_1_);
                    break;

                case 1:
                    ARBFramebufferObject.glBindRenderbuffer(p_153176_0_, p_153176_1_);
                    break;

                case 2:
                    EXTFramebufferObject.glBindRenderbufferEXT(p_153176_0_, p_153176_1_);
            }
        }
    }

    public static void func_153184_g(int p_153184_0_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glDeleteRenderbuffers(p_153184_0_);
                    break;

                case 1:
                    ARBFramebufferObject.glDeleteRenderbuffers(p_153184_0_);
                    break;

                case 2:
                    EXTFramebufferObject.glDeleteRenderbuffersEXT(p_153184_0_);
            }
        }
    }

    public static void func_153174_h(int p_153174_0_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glDeleteFramebuffers(p_153174_0_);
                    break;

                case 1:
                    ARBFramebufferObject.glDeleteFramebuffers(p_153174_0_);
                    break;

                case 2:
                    EXTFramebufferObject.glDeleteFramebuffersEXT(p_153174_0_);
            }
        }
    }

    public static int func_153165_e()
    {
        if (!framebufferSupported)
        {
            return -1;
        }
        else
        {
            switch (field_153212_w)
            {
                case 0:
                    return GL30.glGenFramebuffers();

                case 1:
                    return ARBFramebufferObject.glGenFramebuffers();

                case 2:
                    return EXTFramebufferObject.glGenFramebuffersEXT();

                default:
                    return -1;
            }
        }
    }

    public static int func_153185_f()
    {
        if (!framebufferSupported)
        {
            return -1;
        }
        else
        {
            switch (field_153212_w)
            {
                case 0:
                    return GL30.glGenRenderbuffers();

                case 1:
                    return ARBFramebufferObject.glGenRenderbuffers();

                case 2:
                    return EXTFramebufferObject.glGenRenderbuffersEXT();

                default:
                    return -1;
            }
        }
    }

    public static void func_153186_a(int p_153186_0_, int p_153186_1_, int p_153186_2_, int p_153186_3_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glRenderbufferStorage(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;

                case 1:
                    ARBFramebufferObject.glRenderbufferStorage(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;

                case 2:
                    EXTFramebufferObject.glRenderbufferStorageEXT(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
            }
        }
    }

    public static void func_153190_b(int p_153190_0_, int p_153190_1_, int p_153190_2_, int p_153190_3_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glFramebufferRenderbuffer(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;

                case 1:
                    ARBFramebufferObject.glFramebufferRenderbuffer(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;

                case 2:
                    EXTFramebufferObject.glFramebufferRenderbufferEXT(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
            }
        }
    }

    public static int func_153167_i(int p_153167_0_)
    {
        if (!framebufferSupported)
        {
            return -1;
        }
        else
        {
            switch (field_153212_w)
            {
                case 0:
                    return GL30.glCheckFramebufferStatus(p_153167_0_);

                case 1:
                    return ARBFramebufferObject.glCheckFramebufferStatus(p_153167_0_);

                case 2:
                    return EXTFramebufferObject.glCheckFramebufferStatusEXT(p_153167_0_);

                default:
                    return -1;
            }
        }
    }

    public static void func_153188_a(int p_153188_0_, int p_153188_1_, int p_153188_2_, int p_153188_3_, int p_153188_4_)
    {
        if (framebufferSupported)
        {
            switch (field_153212_w)
            {
                case 0:
                    GL30.glFramebufferTexture2D(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;

                case 1:
                    ARBFramebufferObject.glFramebufferTexture2D(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;

                case 2:
                    EXTFramebufferObject.glFramebufferTexture2DEXT(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
            }
        }
    }

    /**
     * Sets the current lightmap texture to the specified OpenGL constant
     */
    public static void setActiveTexture(int par0)
    {
        if (field_153215_z)
        {
            ARBMultitexture.glActiveTextureARB(par0);
        }
        else
        {
            GL13.glActiveTexture(par0);
        }
    }

    /**
     * Sets the current lightmap texture to the specified OpenGL constant
     */
    public static void setClientActiveTexture(int par0)
    {
        if (field_153215_z)
        {
            ARBMultitexture.glClientActiveTextureARB(par0);
        }
        else
        {
            GL13.glClientActiveTexture(par0);
        }
    }

    /**
     * Sets the current coordinates of the given lightmap texture
     */
    public static void setLightmapTextureCoords(int par0, float par1, float par2)
    {
        if (field_153215_z)
        {
            ARBMultitexture.glMultiTexCoord2fARB(par0, par1, par2);
        }
        else
        {
            GL13.glMultiTexCoord2f(par0, par1, par2);
        }

        if (par0 == lightmapTexUnit)
        {
            lastBrightnessX = par1;
            lastBrightnessY = par2;
        }
    }

    public static void glBlendFunc(int p_148821_0_, int p_148821_1_, int p_148821_2_, int p_148821_3_)
    {
        if (openGL14)
        {
            if (field_153211_u)
            {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
            else
            {
                GL14.glBlendFuncSeparate(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
        }
        else
        {
            GL11.glBlendFunc(p_148821_0_, p_148821_1_);
        }
    }

    public static boolean isFramebufferEnabled()
    {
        return !Config.isFastRender() && (framebufferSupported && Minecraft.getInstance().gameSettings.fboEnable);
    }
}