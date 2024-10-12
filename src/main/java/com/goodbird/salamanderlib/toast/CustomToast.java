package com.goodbird.salamanderlib.toast;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CustomToast implements IToast {
    private final String id;
    private ITextComponent title;
    private List<IReorderingProcessor> messageLines;
    private long lastChanged;
    private boolean changed;
    private final int width;

    public CustomToast(String p_i47488_1_, ITextComponent p_i47488_2_, @Nullable ITextComponent p_i47488_3_) {
        this(p_i47488_1_, p_i47488_2_, nullToEmpty(p_i47488_3_), 160);
    }

    public static CustomToast multiline(Minecraft p_238534_0_, String p_238534_1_, ITextComponent p_238534_2_, ITextComponent p_238534_3_) {
        FontRenderer fontrenderer = p_238534_0_.font;
        List<IReorderingProcessor> list = fontrenderer.split(p_238534_3_, 200);
        int i = Math.max(200, list.stream().mapToInt(fontrenderer::width).max().orElse(200));
        return new CustomToast(p_238534_1_, p_238534_2_, list, i + 30);
    }

    private CustomToast(String p_i232264_1_, ITextComponent p_i232264_2_, List<IReorderingProcessor> p_i232264_3_, int p_i232264_4_) {
        this.id = p_i232264_1_;
        this.title = p_i232264_2_;
        this.messageLines = p_i232264_3_;
        this.width = p_i232264_4_;
    }

    private static ImmutableList<IReorderingProcessor> nullToEmpty(@Nullable ITextComponent p_238537_0_) {
        return p_238537_0_ == null ? ImmutableList.of() : ImmutableList.of(p_238537_0_.getVisualOrderText());
    }

    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return messageLines.size()*16+10;
    }

    public IToast.Visibility render(MatrixStack p_230444_1_, ToastGui p_230444_2_, long p_230444_3_) {
        if (this.changed) {
            this.lastChanged = p_230444_3_;
            this.changed = false;
        }

        p_230444_2_.getMinecraft().getTextureManager().bind(TEXTURE);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        int i = this.width();
        int j = 12;
        if (i == 160 && this.messageLines.size() <= 1) {
            p_230444_2_.blit(p_230444_1_, 0, 0, 0, 64, i, this.height());
        } else {
            int k = this.height() + Math.max(0, this.messageLines.size() - 1) * 12;
            int l = 28;
            int i1 = Math.min(4, k - 28);
            this.renderBackgroundRow(p_230444_1_, p_230444_2_, i, 0, 0, 28);

            for(int j1 = 28; j1 < k - i1; j1 += 10) {
                this.renderBackgroundRow(p_230444_1_, p_230444_2_, i, 16, j1, Math.min(16, k - j1 - i1));
            }

            this.renderBackgroundRow(p_230444_1_, p_230444_2_, i, 32 - i1, k - i1, i1);
        }

        if (this.messageLines == null) {
            p_230444_2_.getMinecraft().font.draw(p_230444_1_, this.title, 18.0F, 12.0F, -256);
        } else {
            p_230444_2_.getMinecraft().font.draw(p_230444_1_, this.title, 18.0F, 7.0F, -256);

            for(int k1 = 0; k1 < this.messageLines.size(); ++k1) {
                p_230444_2_.getMinecraft().font.draw(p_230444_1_, this.messageLines.get(k1), 18.0F, (float)(18 + k1 * 12), -1);
            }
        }

        return p_230444_3_ - this.lastChanged < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }

    private void renderBackgroundRow(MatrixStack p_238533_1_, ToastGui p_238533_2_, int p_238533_3_, int p_238533_4_, int p_238533_5_, int p_238533_6_) {
        int i = p_238533_4_ == 0 ? 20 : 5;
        int j = Math.min(60, p_238533_3_ - i);
        p_238533_2_.blit(p_238533_1_, 0, p_238533_5_, 0, 64 + p_238533_4_, i, p_238533_6_);

        for(int k = i; k < p_238533_3_ - j; k += 64) {
            p_238533_2_.blit(p_238533_1_, k, p_238533_5_, 32, 64 + p_238533_4_, Math.min(64, p_238533_3_ - k - j), p_238533_6_);
        }

        p_238533_2_.blit(p_238533_1_, p_238533_3_ - j, p_238533_5_, 160 - j, 64 + p_238533_4_, j, p_238533_6_);
    }

    public void reset(ITextComponent p_193656_1_, List<IReorderingProcessor> messageLines) {
        this.title = p_193656_1_;
        this.messageLines = messageLines;
        this.changed = true;
    }

    public String getToken() {
        return this.id;
    }

    public static void addOrUpdate(ToastGui p_193657_0_, CustomToast toast) {
        CustomToast systemtoast = p_193657_0_.getToast(CustomToast.class, toast.getToken());
        if (systemtoast == null) {
            p_193657_0_.addToast(toast);
        } else {
            systemtoast.reset(toast.title, toast.messageLines);
        }

    }
}
