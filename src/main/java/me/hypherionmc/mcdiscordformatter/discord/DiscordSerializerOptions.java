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

package me.hypherionmc.mcdiscordformatter.discord;

import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Options for {@link DiscordSerializer}s.
 */
public final class DiscordSerializerOptions {

    public DiscordSerializerOptions(boolean embedLinks, boolean escapeMarkdown, @NotNull Function<KeybindContents, String> keybindProvider, @NotNull Function<TranslatableContents, String> translationProvider) {
        this.embedLinks = embedLinks;
        this.escapeMarkdown = escapeMarkdown;
        this.keybindProvider = keybindProvider;
        this.translationProvider = translationProvider;
    }

    /**
     * Creates the default {@link DiscordSerializerOptions}.
     *
     * @return the default {@link DiscordSerializerOptions}.
     */
    public static DiscordSerializerOptions defaults() {
        return new DiscordSerializerOptions(false, true, KeybindContents::getName, TranslatableContents::getKey);
    }

    /**
     * Makes messages format as [message content](url) when there is a open_url clickEvent (for embeds).
     */
    private final boolean embedLinks;

    /**
     * Escapes Discord formatting codes in the Minecraft message content.
     */
    private final boolean escapeMarkdown;

    /**
     * The translator for {@link KeybindContents}s.
     */
    @NotNull
    private final Function<KeybindContents, String> keybindProvider;

    /**
     * The translator for {@link TranslatableContents}s.
     */
    @NotNull
    private final Function<TranslatableContents, String> translationProvider;

    public boolean isEmbedLinks() {
        return this.embedLinks;
    }

    public boolean isEscapeMarkdown() {
        return this.escapeMarkdown;
    }

    public @NotNull Function<KeybindContents, String> getKeybindProvider() {
        return this.keybindProvider;
    }

    public @NotNull Function<TranslatableContents, String> getTranslationProvider() {
        return this.translationProvider;
    }

    public DiscordSerializerOptions withEmbedLinks(boolean embedLinks) {
        return this.embedLinks == embedLinks ? this : new DiscordSerializerOptions(embedLinks, this.escapeMarkdown, this.keybindProvider, this.translationProvider);
    }

    public DiscordSerializerOptions withEscapeMarkdown(boolean escapeMarkdown) {
        return this.escapeMarkdown == escapeMarkdown ? this : new DiscordSerializerOptions(this.embedLinks, escapeMarkdown, this.keybindProvider, this.translationProvider);
    }

    public DiscordSerializerOptions withKeybindProvider(@NotNull Function<KeybindContents, String> keybindProvider) {
        return this.keybindProvider == keybindProvider ? this : new DiscordSerializerOptions(this.embedLinks, this.escapeMarkdown, keybindProvider, this.translationProvider);
    }

    public DiscordSerializerOptions withTranslationProvider(@NotNull Function<TranslatableContents, String> translationProvider) {
        return this.translationProvider == translationProvider ? this : new DiscordSerializerOptions(this.embedLinks, this.escapeMarkdown, this.keybindProvider, translationProvider);
    }

    public String toString() {
        return "DiscordSerializerOptions(embedLinks=" + this.isEmbedLinks() + ", escapeMarkdown=" + this.isEscapeMarkdown() + ", keybindProvider=" + this.getKeybindProvider() + ", translationProvider=" + this.getTranslationProvider() + ")";
    }
}
