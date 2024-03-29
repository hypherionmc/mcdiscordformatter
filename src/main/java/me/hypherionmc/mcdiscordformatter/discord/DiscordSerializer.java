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

import me.hypherionmc.mcdiscordformatter.text.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.*;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * DiscordSerializer, for serializing from Minecraft {@link MutableComponent}s to Discord messages.
 *
 * @author Vankka
 *
 * @see DiscordSerializerOptions
 * @see me.hypherionmc.mcdiscordformatter.rules.DiscordMarkdownRules
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DiscordSerializer {

    /**
     * Default instance of the DiscordSerializer, incase that's all you need.
     * Using {@link DiscordSerializer#setDefaultOptions(DiscordSerializerOptions)} is not allowed.
     */
    public static final DiscordSerializer INSTANCE = new DiscordSerializer() {
        @Override
        public void setDefaultOptions(DiscordSerializerOptions defaultOptions) {
            throw new UnsupportedOperationException("Cannot modify public instance");
        }

        @SuppressWarnings("deprecation")
        @Override
        @Deprecated
        public void setKeybindProvider(Function<KeybindContents, String> provider) {
            throw new UnsupportedOperationException("Cannot modify public instance");
        }

        @SuppressWarnings("deprecation")
        @Override
        @Deprecated
        public void setTranslationProvider(Function<MutableComponent, String> provider) {
            throw new UnsupportedOperationException("Cannot modify public instance");
        }
    };

    /**
     * The default {@link DiscordSerializerOptions} to use for this serializer.
     */
    private DiscordSerializerOptions defaultOptions;
    private Function<KeybindContents, String> keybindProvider;
    private Function<MutableComponent, String> translationProvider;

    /**
     * Constructor for creating a serializer, which {@link DiscordSerializerOptions#defaults()} as defaults.
     */
    public DiscordSerializer() {
        this(DiscordSerializerOptions.defaults());
    }

    /**
     * Constructor for creating a serializer, with the specified {@link DiscordSerializerOptions} as defaults.
     *
     * @param defaultOptions the default serializer options (can be overridden on serialize)
     * @see DiscordSerializerOptions#defaults()
     * @see DiscordSerializerOptions#DiscordSerializerOptions(boolean, boolean, Function, Function)
     */
    public DiscordSerializer(@NotNull DiscordSerializerOptions defaultOptions) {
        this.defaultOptions = defaultOptions;
        this.translationProvider = defaultOptions.getTranslationProvider();
        this.keybindProvider = defaultOptions.getKeybindProvider();
    }

    /**
     * Returns the keybind provider for this serializer.
     *
     * @return keybind provider, a KeybindComponent to String function
     * @deprecated Use {@link #getDefaultOptions()} {@link DiscordSerializerOptions#getKeybindProvider()}
     */
    @Deprecated
    public Function<KeybindContents, String> getKeybindProvider() {
        return keybindProvider;
    }

    /**
     * Sets the keybind provider for this serializer.
     *
     * @param provider a KeybindComponent to String function
     * @deprecated Use {@link #setDefaultOptions(DiscordSerializerOptions)} {@link DiscordSerializerOptions#withKeybindProvider(Function)}
     */
    @Deprecated
    public void setKeybindProvider(Function<KeybindContents, String> provider) {
        keybindProvider = provider;
    }

    /**
     * Returns the translation provider for this serializer.
     *
     * @return keybind provider, a TranslatableComponent to String function
     * @deprecated Use {@link #getDefaultOptions()} {@link DiscordSerializerOptions#getTranslationProvider()}
     */
    @Deprecated
    public Function<MutableComponent, String> getTranslationProvider() {
        return translationProvider;
    }

    /**
     * Sets the translation provider for this serializer.
     *
     * @param provider a TranslationComponent to String function
     * @deprecated Use {@link #setDefaultOptions(DiscordSerializerOptions)} {@link DiscordSerializerOptions#withTranslationProvider(Function)}
     */
    @Deprecated
    public void setTranslationProvider(Function<MutableComponent, String> provider) {
        translationProvider = provider;
    }

    /**
     * Serializes a {@link MutableComponent} to Discord formatting (markdown) with this serializer's {@link DiscordSerializer#getDefaultOptions() default options}.<br/>
     * Use {@link DiscordSerializer#serialize(MutableComponent, DiscordSerializerOptions)} to fine tune the serialization options.
     *
     * @param component The text component from a Minecraft chat message
     * @return Discord markdown formatted String
     */
    public String serialize(@NotNull final MutableComponent component) {
        DiscordSerializerOptions options = getDefaultOptions();
        if (keybindProvider != null) {
            options = options.withKeybindProvider(keybindProvider);
        }
        if (translationProvider != null) {
            options = options.withTranslationProvider(translationProvider);
        }
        return serialize(component, options);
    }

    /**
     * Serializes a MutableComponent (from a chat message) to Discord formatting (markdown).
     *
     * @param component     The text component from a Minecraft chat message
     * @param embedLinks    Makes messages format as [message content](url) when there is an open_url clickEvent (for embeds)
     * @return Discord markdown formatted String
     * @deprecated Use {@link #serialize(MutableComponent, DiscordSerializerOptions)} {@link DiscordSerializerOptions#withEmbedLinks(boolean)}
     */
    @Deprecated
    public String serialize(@NotNull final MutableComponent component, boolean embedLinks) {
        return serialize(component, defaultOptions.withEmbedLinks(embedLinks));
    }

    /**
     * Serializes MutableComponent (from a chat message) to Discord formatting (markdown).
     *
     * @param component         The text component from a Minecraft chat message
     * @param serializerOptions The options to use for this serialization
     * @return Discord markdown formatted String
     * @see DiscordSerializerOptions#defaults()
     * @see DiscordSerializerOptions#DiscordSerializerOptions(boolean, boolean, Function, Function)
     */
    public String serialize(@NotNull final MutableComponent component, @NotNull final DiscordSerializerOptions serializerOptions) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Text> texts = getTexts(new LinkedList<>(), component, new Text(), serializerOptions);
        for (Text text : texts) {
            String content = text.getContent();
            if (content.isEmpty()) {
                // won't work
                continue;
            }

            if (text.isBold()) {
                stringBuilder.append("**");
            }
            if (text.isStrikethrough()) {
                stringBuilder.append("~~");
            }
            if (text.isItalic()) {
                stringBuilder.append("_");
            }
            if (text.isUnderline()) {
                stringBuilder.append("__");
            }

            if (serializerOptions.isEscapeMarkdown()) {
                content = content.replace("(?<!\\\\)(?:\\\\\\\\)*\\*", "\\*")
                        .replace("(?<!\\\\)(?:\\\\\\\\)*~", "\\~")
                        .replace("(?<!\\\\)(?:\\\\\\\\)*_", "\\_")
                        .replace("(?<!\\\\)(?:\\\\\\\\)*`", "\\`")
                        .replace("(?<!\\\\)(?:\\\\\\\\)*\\|", "\\|");
            }

            stringBuilder.append(ChatFormatting.stripFormatting(content));

            if (text.isUnderline()) {
                stringBuilder.append("__");
            }
            if (text.isItalic()) {
                stringBuilder.append("_");
            }
            if (text.isStrikethrough()) {
                stringBuilder.append("~~");
            }
            if (text.isBold()) {
                stringBuilder.append("**");
            }

            stringBuilder.append("\u200B"); // zero width space
        }
        int length = stringBuilder.length();
        return length < 1 ? "" : stringBuilder.substring(0, length - 1);
    }

    private LinkedList<Text> getTexts(@NotNull final List<Text> input, @NotNull final MutableComponent component,
                                      @NotNull final Text text, @NotNull final DiscordSerializerOptions serializerOptions) {
        LinkedList<Text> output = new LinkedList<>(input);

        String content;

        // TODO maybe fix?
        if (component.getContents() instanceof KeybindContents) {
            KeybindContents keybindComponent = (KeybindContents)component.getContents();
            content = keybindProvider.apply(keybindComponent);
        } else if (component.getContents() instanceof ScoreContents) {
            ScoreContents scoreText = (ScoreContents)component.getContents();
            content = scoreText.getObjective();
        } else if (component.getContents() instanceof SelectorContents) {
            SelectorContents selectorText = (SelectorContents) component.getContents();
            content = selectorText.getPattern();
        } else if (component.getContents() instanceof PlainTextContents.LiteralContents) {
            content = component.getString();
        } else if (component.getContents() instanceof TranslatableContents) {
            content = translationProvider.apply(component);
        } else {
            content = "";
        }


        ClickEvent clickEvent = component.getStyle().getClickEvent();
        if (serializerOptions.isEmbedLinks() && clickEvent != null && clickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
            text.setContent("[" + content + "](" + clickEvent.getValue() + ")");
        } else {
            text.setContent(content);
        }

        if (component.getStyle().isBold()) {
            text.setBold(true);
        }
        text.setBold(component.getStyle().isBold());
        text.setItalic(component.getStyle().isItalic());
        text.setUnderline(component.getStyle().isUnderlined());
        text.setStrikethrough(component.getStyle().isStrikethrough());

        if (!output.isEmpty()) {
            Text previous = output.getLast();
            // if the formatting matches (color was different), merge the text objects to reduce length
            if (text.formattingMatches(previous)) {
                output.removeLast();
                text.setContent(previous.getContent() + text.getContent());
            }
        }
        output.add(text);

        for (Component child : component.getSiblings()) {
            Text next = text.clone();
            next.setContent("");
            output = getTexts(output, (MutableComponent) child, next, serializerOptions);
        }

        return output;
    }

    public DiscordSerializerOptions getDefaultOptions() {
        return this.defaultOptions;
    }

    public void setDefaultOptions(DiscordSerializerOptions defaultOptions) {
        this.defaultOptions = defaultOptions;
    }
}
