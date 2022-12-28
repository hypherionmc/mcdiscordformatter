/*
 * MCDiscordReserializer: A library for transcoding between Minecraft and Discord.
 * Copyright (C) 2018-2022 Vankka
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.hypherionmc.mcdiscordformatter.renderer.implementation;

import me.hypherionmc.mcdiscordformatter.renderer.MinecraftRenderer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * The default implementation for the {@link MinecraftRenderer}.
 */
public class DefaultMinecraftRenderer implements MinecraftRenderer {

    /**
     * The instance of {@link DefaultMinecraftRenderer}.
     */
    public static final DefaultMinecraftRenderer INSTANCE = new DefaultMinecraftRenderer();

    private static final Pattern PATTERN_NEWLINE = Pattern.compile("\n");

    /**
     * Creates a new instance of the {@link DefaultMinecraftRenderer} unless you're extending the class you shouldn't use this.
     *
     * @see #INSTANCE
     */
    public DefaultMinecraftRenderer() {
    }

    @Override
    public IFormattableTextComponent strikethrough(IFormattableTextComponent component) {
        return component.withStyle(TextFormatting.STRIKETHROUGH);
    }

    @Override
    public IFormattableTextComponent underline(IFormattableTextComponent component) {
        return component.withStyle(TextFormatting.UNDERLINE);
    }

    @Override
    public IFormattableTextComponent italics(IFormattableTextComponent component) {
        return component.withStyle(TextFormatting.ITALIC);
    }

    @Override
    public IFormattableTextComponent bold(IFormattableTextComponent component) {
        return component.withStyle(TextFormatting.BOLD);
    }

    @Override
    public IFormattableTextComponent codeString(IFormattableTextComponent component) {
        return component.withStyle(TextFormatting.DARK_GRAY);
    }

    @Override
    public IFormattableTextComponent codeBlock(IFormattableTextComponent component) {
        return component.withStyle(TextFormatting.DARK_GRAY);
    }

    @Override
    public IFormattableTextComponent appendSpoiler(IFormattableTextComponent component, IFormattableTextComponent content) {

        return component.append(new StringTextComponent(StringUtils.repeat("▌", content.getString().length())).withStyle(style ->
                style.withColor(TextFormatting.DARK_GRAY)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content))));
    }

    @Override
    public IFormattableTextComponent appendQuote(IFormattableTextComponent component, IFormattableTextComponent content) {
        IFormattableTextComponent prefix = new StringTextComponent("| ").withStyle(TextFormatting.DARK_GRAY, TextFormatting.BOLD);
        // TODO fix multiline quotes
        // return new LiteralText("").append(prefix).append(component.replaceText(PATTERN_NEWLINE, builder -> builder.append(prefix)));
        return new StringTextComponent("").append(prefix).append(component);
    }

    @Override
    public IFormattableTextComponent appendEmoteMention(IFormattableTextComponent component, String name, String id) {
        return component.append(new StringTextComponent(":" + name + ":"));
    }

    @Override
    public IFormattableTextComponent appendChannelMention(IFormattableTextComponent component, String id) {
        return component.append(new StringTextComponent("<#" + id + ">"));
    }

    @Override
    public IFormattableTextComponent appendUserMention(IFormattableTextComponent component, String id) {
        return component.append(new StringTextComponent("<@" + id + ">"));
    }

    @Override
    public IFormattableTextComponent appendRoleMention(IFormattableTextComponent component, String id) {
        return component.append(new StringTextComponent("<@&" + id + ">"));
    }
}
