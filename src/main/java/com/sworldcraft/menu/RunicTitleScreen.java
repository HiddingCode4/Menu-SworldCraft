package com.sworldcraft.menu;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.gui.modlist.ModListScreen;

public final class RunicTitleScreen extends TitleScreen {
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(SworldcraftMenu.ID, "textures/gui/background.png");
    private static final Identifier LOGO = Identifier.fromNamespaceAndPath(SworldcraftMenu.ID, "textures/gui/logo.png");
    private final List<RunicButton> buttons = new ArrayList<>();
    private final long started = System.nanoTime();
    private int logoX, logoY, logoWidth, logoHeight;

    @Override
    protected void init() {
        // Keep Minecraft's own account restrictions, demo mode and multiplayer warning flow.
        super.init();
        Button single = findVanilla("menu.singleplayer");
        if (single == null) single = findVanilla("menu.playdemo");
        Button multi = findVanilla("menu.multiplayer");
        clearWidgets();
        buttons.clear();
        int bh = Math.max(16, Math.min(24, (height - 72) / 8));
        int gap = Math.max(3, bh / 5);
        int bw = Math.min(220, width - 32);
        int total = 5 * bh + 4 * gap + 7;
        int y = Math.max(38, Math.min((int)(height * 0.42), height - total - 24));
        int x = (width - bw) / 2;
        logoWidth = Math.min((int)(width * 0.66), (int)((y - 9) * (2172.0 / 724.0)));
        logoHeight = (int)(logoWidth * (724.0 / 2172.0));
        logoX = (width - logoWidth) / 2;
        logoY = Math.max(2, (y - logoHeight) / 2 - 1);
        Button solo = single;
        RunicButton soloButton = add(x, y, bw, bh,
                solo == null ? Component.translatable("menu.singleplayer") : solo.getMessage(), b -> { if (solo != null) solo.onPress(((RunicButton)b).input()); });
        soloButton.active = solo != null && solo.active;
        y += bh + gap;
        Button multiplayer = multi;
        RunicButton multiButton = add(x, y, bw, bh, Component.translatable("menu.multiplayer"),
                b -> { if (multiplayer != null) multiplayer.onPress(((RunicButton)b).input()); });
        multiButton.active = multiplayer != null && multiplayer.active;
        if (!multiButton.active) multiButton.setTooltip(net.minecraft.client.gui.components.Tooltip.create(
                Component.translatable("title.multiplayer.disabled")));
        y += bh + gap;
        add(x, y, bw, bh, Component.literal("Mods"), b -> minecraft.gui.setScreen(ModListScreen.create(this)));
        y += bh + gap;
        add(x, y, bw, bh, Component.translatable("menu.options"), b -> minecraft.gui.setScreen(new OptionsScreen(this, minecraft.options)));
        y += bh + gap + 7;
        int square = bh;
        add(x, y, square, bh, Component.literal("L"), b -> minecraft.gui.setScreen(new LanguageSelectScreen(this, minecraft.options, minecraft.getLanguageManager())))
                .setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("options.language")));
        add(x + square + gap, y, bw - 2 * (square + gap), bh, Component.translatable("menu.quit"), b -> minecraft.stop());
        add(x + bw - square, y, square, bh, Component.literal("A"), b -> minecraft.gui.setScreen(new AccessibilityOptionsScreen(this, minecraft.options)))
                .setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("options.accessibility.title")));
    }

    private Button findVanilla(String key) {
        Component label = Component.translatable(key);
        return children().stream().filter(c -> c instanceof Button)
                .map(c -> (Button)c).filter(b -> matchesLabel(b, label)).findFirst().orElse(null);
    }

    private boolean matchesLabel(Button button, Component label) {
        // 26.3 substitutes the label of inactive buttons. Match the underlying active label
        // without changing whether the original action is allowed.
        boolean wasActive = button.active;
        try {
            button.active = true;
            return button.getMessage().equals(label);
        } finally {
            button.active = wasActive;
        }
    }

    private RunicButton add(int x, int y, int w, int h, Component label, Button.OnPress action) {
        RunicButton b = new RunicButton(x, y, w, h, label, action);
        buttons.add(b);
        return addRenderableWidget(b);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor g, int mx, int my, float dt) {
        // Cover instead of stretching: preserve the background's proportions on ultrawide screens.
        float scale = Math.max(width / 1672f, height / 941f);
        int w = (int)Math.ceil(1672 * scale), h = (int)Math.ceil(941 * scale);
        g.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, (width - w) / 2, (height - h) / 2, 0, 0, w, h, w, h);
        g.fillGradient(0, 0, width, height, 0x15081018, 0x65050C15);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float dt) {
        extractBackground(g, mx, my, dt);
        // Gentle time-based particles; respect Minecraft's reduced lightning flashes preference.
        double seconds = (System.nanoTime() - started) / 1_000_000_000.0;
        if (!minecraft.options.hideLightningFlash().get()) {
            for (int i = 0; i < 18; i++) {
                double phase = seconds * (0.008 + (i % 4) * 0.002) + i * 0.0618;
                int px = (int)(width * (0.68 + 0.29 * ((i * 0.381966) % 1)) + Math.sin(seconds * .4 + i) * 3);
                int py = (int)(height * (1 - (phase % 1)));
                g.fill(px - 1, py - 1, px + 2, py + 2, 0x185DFFFF);
                g.fill(px, py, px + 1, py + 1, 0xAA91FFFF);
            }
        }
        g.blit(RenderPipelines.GUI_TEXTURED, LOGO, logoX, logoY, 0, 0, logoWidth, logoHeight, logoWidth, logoHeight);
        for (RunicButton button : buttons) button.extractRenderState(g, mx, my, dt);
        g.text(font, "Minecraft 26.3 | NeoForge", 5, height - 12, 0xFFCBD5DB);
        String credit = "Sworldcraft";
        g.text(font, credit, width - font.width(credit) - 5, height - 12, 0xFFCBD5DB);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        // Do not forward clicks to the vanilla Realms notification overlay we do not draw.
        for (RunicButton button : buttons) {
            if (button.mouseClicked(event, doubleClick)) {
                setFocused(button);
                if (event.button() == 0) setDragging(true);
                return true;
            }
        }
        return false;
    }

    @Override public void tick() { }
    @Override public boolean shouldCloseOnEsc() { return false; }
}
