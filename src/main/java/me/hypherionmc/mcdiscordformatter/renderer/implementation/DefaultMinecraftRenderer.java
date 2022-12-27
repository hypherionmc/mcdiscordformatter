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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

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
    public MutableComponent strikethrough(MutableComponent component) {
        return component.withStyle(ChatFormatting.STRIKETHROUGH);
    }

    @Override
    public MutableComponent underline(MutableComponent component) {
        return component.withStyle(ChatFormatting.UNDERLINE);
    }

    @Override
    public MutableComponent italics(MutableComponent component) {
        return component.withStyle(ChatFormatting.ITALIC);
    }

    @Override
    public MutableComponent bold(MutableComponent component) {
        return component.withStyle(ChatFormatting.BOLD);
    }

    @Override
    public MutableComponent codeString(MutableComponent component) {
        return component.withStyle(ChatFormatting.DARK_GRAY);
    }

    @Override
    public MutableComponent codeBlock(MutableComponent component) {
        return component.withStyle(ChatFormatting.DARK_GRAY);
    }

    @Override
    public MutableComponent appendSpoiler(MutableComponent component, MutableComponent content) {
        return component.append(new TextComponent("▌".repeat(content.getString().length())).withStyle(style ->
                style.withColor(ChatFormatting.DARK_GRAY)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, content))));
    }

    @Override
    public MutableComponent appendQuote(MutableComponent component, MutableComponent content) {
        MutableComponent prefix = new TextComponent("| ").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD);
        // TODO fix multiline quotes
        // return new LiteralText("").append(prefix).append(component.replaceText(PATTERN_NEWLINE, builder -> builder.append(prefix)));
        return new TextComponent("").append(prefix).append(component);
    }

    @Override
    public MutableComponent appendEmoteMention(MutableComponent component, String name, String id) {
        return component.append(new TextComponent(":" + name + ":"));
    }

    @Override
    public MutableComponent appendChannelMention(MutableComponent component, String id) {
        return component.append(new TextComponent("<#" + id + ">"));
    }

    @Override
    public MutableComponent appendUserMention(MutableComponent component, String id) {
        return component.append(new TextComponent("<@" + id + ">"));
    }

    @Override
    public MutableComponent appendRoleMention(MutableComponent component, String id) {
        return component.append(new TextComponent("<@&" + id + ">"));
    }
}
