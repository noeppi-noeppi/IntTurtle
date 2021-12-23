package io.github.noeppi_noeppi.mods.intturtle.content.source;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import io.github.noeppi_noeppi.mods.intturtle.IntTurtle;
import io.github.noeppi_noeppi.mods.intturtle.network.SourceCodeUpdateSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SourceCodeScreen extends Screen {

    public static final int WIDTH = 216;
    public static final int HEIGHT = 184;
    
    @Nullable
    private Component error;
    private long[] memory;
    
    @Nullable
    private List<FormattedCharSequence> displayStr;
    
    private int left = 0;
    private int top = 0;
    
    public SourceCodeScreen(Component title, long[] memory) {
        super(title);
        this.error = memory.length == 0 ? new TranslatableComponent("intturtle.source_code.empty") : null;
        this.memory = memory;
        this.displayStr = null;
    
    }

    @Override
    protected void init() {
        this.left = (this.width - WIDTH) / 2;
        this.top = (this.height - HEIGHT) / 2;

        this.addRenderableWidget(new ExtendedButton(
                this.left + (WIDTH - 160) / 2, this.top + HEIGHT - 22, 160, 20,
                new TranslatableComponent("intturtle.source_code.load_clipboard"),
                b -> {
                    String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
                    try {
                        this.error = null;
                        this.memory = Arrays.stream(clipboard.split("[, ]"))
                                .map(String::strip).filter(str -> !str.isEmpty())
                                .mapToLong(Long::parseLong).toArray();
                        this.displayStr = null;
                        IntTurtle.getNetwork().channel.sendToServer(new SourceCodeUpdateSerializer.Message(this.memory));
                    } catch (Exception e) {
                        this.error = new TextComponent("Invalid Input: " + e.getMessage());
                        this.memory = new long[]{};
                        this.displayStr = null;
                    }
                }) {
                    @Override
                    public int getFGColor() {
                        return 0xFFFFFF;
                    }
                }
        );
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        poseStack.pushPose();
        poseStack.translate(this.left, this.top, 0);
        RenderHelper.renderGuiBackground(poseStack, -3, -3, WIDTH + 6, HEIGHT + 6);
        
        poseStack.translate(0, 0, 500);
        
        //noinspection IntegerDivisionInFloatingPointContext
        this.font.drawShadow(poseStack, this.getTitle(), (WIDTH - this.font.width(this.getTitle())) / 2, 3, 0xFFFFFF);
        
        if (this.error != null) {
            this.font.drawShadow(poseStack, this.error, 5, 9 + this.font.lineHeight, 0xFF4444);
        } else {
            if (this.displayStr == null) {
                String allInts = Arrays.stream(this.memory).limit(1000).mapToObj(Long::toString).collect(Collectors.joining(", "));
                this.displayStr = ComponentRenderUtils.wrapComponents(new TextComponent(allInts), WIDTH - 10, this.font);
            }
            int y = 0;
            for (FormattedCharSequence line : this.displayStr) {
                if (y + 2 + this.font.lineHeight >= HEIGHT - 25) {
                    this.font.draw(poseStack, "...", 5, 9 + this.font.lineHeight + y, 0x000000);
                    break;
                } else {
                    this.font.draw(poseStack, line, 5, 9 + this.font.lineHeight + y, 0x000000);
                    y += (2 + this.font.lineHeight);
                }
            }
        }
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 1000);
        super.render(poseStack, mouseX, mouseY, partialTick);
        poseStack.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
