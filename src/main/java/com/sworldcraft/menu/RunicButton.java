package com.sworldcraft.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.client.input.InputWithModifiers;

/** Standard button input, narration, focus and sound with a custom stone skin. */
final class RunicButton extends Button {
    RunicButton(int x, int y, int width, int height, Component label, OnPress action) {
        super(x, y, width, height, label, action, DEFAULT_NARRATION);
    }

    private InputWithModifiers lastInput;
    InputWithModifiers input() { return lastInput; }
    @Override
    public void onPress(InputWithModifiers input) {
        lastInput = input;
        onPress.onPress(this);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor g, int mx, int my, float dt) {
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        boolean selected = active && isHoveredOrFocused();
        int edge = selected ? 0xFF65F5F3 : 0xFF7B8288;
        if (selected) {
            g.fill(x - 2, y - 2, x + w + 2, y + h + 2, 0x254CE7EC);
            g.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0x554CE7EC);
        }
        g.fill(x, y + 2, x + w, y + h + 2, 0xAA000000);
        g.fill(x, y, x + w, y + h, 0xFF151B20);
        g.fillGradient(x + 2, y + 2, x + w - 2, y + h - 2,
                selected ? 0xF02C6068 : 0xEF44494E, selected ? 0xF015333B : 0xF024292E);
        // Stable pixel grain (no frame-dependent flicker).
        for (int i = 4; i < w - 4; i += 7) {
            int n = (i * 13 + x * 3) & 7;
            g.fill(x + i, y + 4 + n, x + i + 4, y + 6 + n, 0x164F6267);
            g.fill(x + i + 1, y + h - 7 - n / 2, x + i + 5, y + h - 5 - n / 2, 0x15101720);
        }
        g.fill(x + 1, y, x + w - 1, y + 1, edge);
        g.fill(x, y + 1, x + 1, y + h - 1, edge);
        g.fill(x + w - 1, y + 1, x + w, y + h - 1, selected ? edge : 0xFF20262B);
        g.fill(x + 1, y + h - 1, x + w - 1, y + h, selected ? edge : 0xFF11171C);
        if (w > 70) {
            int rune = selected ? 0xFF8EFFFF : 0xFF3A969C;
            for (int side : new int[]{x + 5, x + w - 8}) {
                g.fill(side, y + 5, side + 3, y + 6, rune);
                g.fill(side, y + 5, side + 1, y + 10, rune);
                g.fill(side + 2, y + 8, side + 3, y + 12, rune);
            }
        }
        var font = Minecraft.getInstance().font;
        int color = !active ? 0xFF899296 : selected ? 0xFFE4FFFF : 0xFFE0E5E8;
        g.centeredText(font, getMessage(), x + w / 2, y + (h - 8) / 2, color);
    }
}
