package codechicken.enderstorage.client.render;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBVertexBlend;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

import static net.minecraft.client.renderer.GlStateManager.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class RenderCustomEndPortal {
    private static final ResourceLocation end_skyTex = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation end_portalTex = new ResourceLocation("textures/entity/end_portal.png");

    private double surfaceY;
    private double surfaceX1;
    private double surfaceX2;
    private double surfaceZ1;
    private double surfaceZ2;

    FloatBuffer field_40448_a;

    public RenderCustomEndPortal(double y, double x1, double x2, double z1, double z2) {
        surfaceY = y;
        surfaceX1 = x1;
        surfaceX2 = x2;
        surfaceZ1 = z1;
        surfaceZ2 = z2;
        field_40448_a = GLAllocation.createDirectFloatBuffer(16);
    }

    public void render(double posX, double posY, double posZ, float frame, double playerX, double playerY, double playerZ, TextureManager r) {
        if (r == null) {
            return;
        }
        disableLighting();
        Random random = new Random(31100L);
        for (int i = 0; i < 16; i++) {
            pushMatrix();
            float f5 = 16 - i;
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                r.bindTexture(end_skyTex);
                f7 = 0.1F;
                f5 = 65F;
                f6 = 0.125F;
                enableBlend();
                blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            }
            if (i == 1) {
                r.bindTexture(end_portalTex);
                enableBlend(); //GL11.glEnable(GL11.GL_BLEND);
                blendFunc(SourceFactor.ONE, DestFactor.ONE); //GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }
            float f8 = (float) (-(posY + surfaceY));
            float f9 = (float) (f8 + ActiveRenderInfo.getPosition().yCoord);
            float f10 = (float) (f8 + f5 + ActiveRenderInfo.getPosition().yCoord);
            float f11 = f9 / f10;
            f11 = (float) (posY + surfaceY) + f11;
            translate(playerX, f11, playerZ);
            texGen(TexGen.S, 9217);
            texGen(TexGen.T, 9217);
            texGen(TexGen.R, 9217);
            texGen(TexGen.Q, 9216);
            texGen(TexGen.S, 9473, this.func_40447_a(1.0F, 0.0F, 0.0F, 0.0F));
            texGen(TexGen.T, 9473, this.func_40447_a(0.0F, 0.0F, 1.0F, 0.0F));
            texGen(TexGen.R, 9473, this.func_40447_a(0.0F, 0.0F, 0.0F, 1.0F));
            texGen(TexGen.Q, 9474, this.func_40447_a(0.0F, 1.0F, 0.0F, 0.0F));
            enableTexGenCoord(TexGen.S);
            enableTexGenCoord(TexGen.T);
            enableTexGenCoord(TexGen.R);
            enableTexGenCoord(TexGen.Q);
            popMatrix();
            matrixMode(5890);
            pushMatrix();
            loadIdentity();
            translate(0.0F, System.currentTimeMillis() % 0xaae60L / 700000F, 0.0F);
            scale(f6, f6, f6);
            translate(0.5F, 0.5F, 0.0F);
            rotate((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            translate(-0.5F, -0.5F, 0.0F);
            GL11.glTranslated(-playerX, -playerZ, -playerY);
            f9 = f8 + (float) ActiveRenderInfo.getPosition().yCoord;
            translate(((float) ActiveRenderInfo.getPosition().xCoord * f5) / f9, ((float) ActiveRenderInfo.getPosition().zCoord * f5) / f9, -playerY + 20);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            f11 = (random.nextFloat() * 0.5F + 0.1F) * f7;
            float f12 = (random.nextFloat() * 0.5F + 0.4F) * f7;
            float f13 = (random.nextFloat() * 0.5F + 0.5F) * f7;
            if (i == 0) {
                f11 = f12 = f13 = 1.0F * f7;
            }

            buffer.pos(posX + surfaceX1, posY + surfaceY, posZ + surfaceZ1).color(f11, f12, f13, 1.0F).endVertex();
            buffer.pos(posX + surfaceX1, posY + surfaceY, posZ + surfaceZ2).color(f11, f12, f13, 1.0F).endVertex();
            buffer.pos(posX + surfaceX2, posY + surfaceY, posZ + surfaceZ2).color(f11, f12, f13, 1.0F).endVertex();
            buffer.pos(posX + surfaceX2, posY + surfaceY, posZ + surfaceZ1).color(f11, f12, f13, 1.0F).endVertex();

            tessellator.draw();
            popMatrix();
            matrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        }

        disableBlend();
        disableTexGenCoord(TexGen.S);
        disableTexGenCoord(TexGen.T);
        disableTexGenCoord(TexGen.R);
        disableTexGenCoord(TexGen.Q);
        enableLighting();
    }

    private FloatBuffer func_40447_a(float f, float f1, float f2, float f3) {
        field_40448_a.clear();
        field_40448_a.put(f).put(f1).put(f2).put(f3);
        field_40448_a.flip();
        return field_40448_a;
    }
}
