/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;

public class RenderUtils {

    public static void drawRect(final PoseStack poseStack, final int x1, final int y1, final int x2, final int y2, final int color) {
        int newLeft = x1;
        int newRight = x2;
        if (newLeft < newRight) {
            int[] swap = swap(newLeft, newRight);
            newLeft = swap[0];
            newRight = swap[1];
        }
        int newTop = y1;
        int newBottom = y2;
        if (newTop < newBottom) {
            int[] swapped = swap(newTop, newBottom);
            newTop = swapped[0];
            newBottom = swapped[1];
        }
        Matrix4f matrix = poseStack.last().pose();
        Tesselator tessellation = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellation.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        vertex(newRight, newBottom, newLeft, color, matrix, bufferbuilder);
        vertex(newLeft, newTop, newRight, color, matrix, bufferbuilder);
        tessellation.end();
        RenderSystem.enableTexture();
    }

    private static int[] swap(final int a, final int b) {
        return new int[]{b, a};
    }

    private static void vertex(final float x1, final float y1, final float x2, final int color, final Matrix4f matrix, final BufferBuilder bufferbuilder) {
        bufferbuilder.vertex(matrix, x2, y1, 0.0F)
                .color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF).endVertex();
        bufferbuilder.vertex(matrix, x1, y1, 0.0F)
                .color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF).endVertex();
    }

}