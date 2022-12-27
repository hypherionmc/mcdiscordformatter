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

package me.hypherionmc.mcdiscordformatter.renderer;

import me.hypherionmc.mcdiscordformatter.minecraft.MinecraftSerializerOptions;
import me.hypherionmc.mcdiscordformatter.rules.DiscordMarkdownRules;
import dev.vankka.simpleast.core.TextStyle;
import dev.vankka.simpleast.core.node.Node;
import dev.vankka.simpleast.core.node.StyleNode;
import dev.vankka.simpleast.core.node.TextNode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Interface for rendering formatting {@link Node}s into Minecraft
 * {@link MutableComponent}s for standard {@link TextStyle}s.
 */
public interface MinecraftRenderer extends MinecraftNodeRenderer {

    @Override
    default MutableComponent render(MutableComponent MutableComponent, Node<Object> node, MinecraftSerializerOptions<MutableComponent> serializerOptions,
                                    Function<Node<Object>, MutableComponent> renderWithChildren) {
        if (node instanceof TextNode) {
            MutableComponent = Component.literal(((TextNode<Object>) node).getContent());
        } else if (node instanceof StyleNode) {
            List<TextStyle> styles = new ArrayList<>(((StyleNode<?, TextStyle>) node).getStyles());
            for (TextStyle style : styles) {
                switch (style.getType()) {
                    case STRIKETHROUGH:
                        MutableComponent = strikethrough(MutableComponent);
                        break;
                    case UNDERLINE:
                        MutableComponent = underline(MutableComponent);
                        break;
                    case ITALICS:
                        MutableComponent = italics(MutableComponent);
                        break;
                    case BOLD:
                        MutableComponent = bold(MutableComponent);
                        break;
                    case CODE_STRING:
                        MutableComponent = codeString(MutableComponent);
                        ((StyleNode<?, TextStyle>) node).getStyles().remove(style);
                        break;
                    case CODE_BLOCK:
                        MutableComponent = codeBlock(MutableComponent);
                        ((StyleNode<?, TextStyle>) node).getStyles().remove(style);
                        break;
                    case QUOTE:
                        MutableComponent content = Component.empty();
                        for (Node<Object> objectNode : serializerOptions.getParser().parse(style.getExtra().get("content"),
                                new DiscordMarkdownRules.QuoteState(true),
                                serializerOptions.getRules(),
                                serializerOptions.isDebuggingEnabled())) {
                            content = content.append(renderWithChildren.apply(objectNode));
                        }

                        MutableComponent = appendQuote(MutableComponent, content);
                        break;
                    case SPOILER:
                        content = Component.empty();
                        for (Node<Object> objectNode : serializerOptions.getParser().parse(style.getExtra().get("content"),
                                null, serializerOptions.getRules(), serializerOptions.isDebuggingEnabled())) {
                            content = content.append(renderWithChildren.apply(objectNode));
                        }

                        MutableComponent = appendSpoiler(MutableComponent, content);
                        break;
                    case MENTION_EMOJI:
                        MutableComponent = appendEmoteMention(MutableComponent, style.getExtra().get("name"), style.getExtra().get("id"));
                        break;
                    case MENTION_CHANNEL:
                        MutableComponent = appendChannelMention(MutableComponent, style.getExtra().get("id"));
                        break;
                    case MENTION_USER:
                        MutableComponent = appendUserMention(MutableComponent, style.getExtra().get("id"));
                        break;
                    case MENTION_ROLE:
                        MutableComponent = appendRoleMention(MutableComponent, style.getExtra().get("id"));
                        break;
                    default:
                        break;
                }
            }
        }

        return MutableComponent;
    }

    /**
     * Renders the provided {@link MutableComponent} as strikethrough.
     *
     * @param part the {@link MutableComponent} to render as strikethrough
     * @return the strikethrough {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent strikethrough(@NotNull MutableComponent part);

    /**
     * Renders the provided {@link MutableComponent} as underlined.
     *
     * @param part the {@link MutableComponent} to render as underlined
     * @return the underlined {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent underline(@NotNull MutableComponent part);

    /**
     * Renders the provided {@link MutableComponent} as italics.
     *
     * @param part the {@link MutableComponent} to render as italics
     * @return the italics {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent italics(@NotNull MutableComponent part);

    /**
     * Renders the provided {@link MutableComponent} as bold.
     *
     * @param part the {@link MutableComponent} to render as bold
     * @return the bold {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent bold(@NotNull MutableComponent part);

    /**
     * Renders the provided {@link MutableComponent} as a code string.
     *
     * @param part the {@link MutableComponent} to render the code string to
     * @return the code stringed {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent codeString(@NotNull MutableComponent part);

    /**
     * Renders the provided {@link MutableComponent} as a code block.
     *
     * @param part the {@link MutableComponent} to render as a code block
     * @return the code blocked {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent codeBlock(@NotNull MutableComponent part);

    /**
     * Renders the spoiler and appends it to the provided {@link MutableComponent}.
     *
     * @param MutableComponent the {@link MutableComponent} to render the spoiler to
     * @param content   the content of the spoiler
     * @return the spoiler'ed {@link MutableComponent} or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent appendSpoiler(@NotNull MutableComponent MutableComponent, @NotNull MutableComponent content);

    /**
     * Adds the required formatting for quotes to the provided {@link MutableComponent}.
     *
     * @param MutableComponent the {@link MutableComponent} to render to
     * @param content   the content of the quote
     * @return the {@link MutableComponent} with the quote rendered or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent appendQuote(@NotNull MutableComponent MutableComponent, @NotNull MutableComponent content);

    /**
     * Renders a emote mention and appends it to the provided {@link MutableComponent}.
     *
     * @param MutableComponent the {@link MutableComponent} to render to
     * @param name      the name of the emote
     * @param id        the id of the emote
     * @return the {@link MutableComponent} with emote rendered or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent appendEmoteMention(@NotNull MutableComponent MutableComponent, @NotNull String name, @NotNull String id);

    /**
     * Renders a channel mention and appends it to the provided {@link MutableComponent}.
     *
     * @param MutableComponent the {@link MutableComponent} to render to
     * @param id        the id of the channel
     * @return the {@link MutableComponent} with the channel mention rendered or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent appendChannelMention(@NotNull MutableComponent MutableComponent, @NotNull String id);

    /**
     * Renders a user mention and appends it to the provided {@link MutableComponent}.
     *
     * @param MutableComponent the {@link MutableComponent} to render to
     * @param id        the id of the user
     * @return the {@link MutableComponent} with the user mention rendered or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent appendUserMention(@NotNull MutableComponent MutableComponent, @NotNull String id);

    /**
     * Renders a role mention and appends it to the provided {@link MutableComponent}.
     *
     * @param MutableComponent the {@link MutableComponent} to render to
     * @param id        the id of the role
     * @return the {@link MutableComponent} with the role mention rendered or {@code null} if this renderer does not process that kinds of styles
     */
    @Nullable
    MutableComponent appendRoleMention(@NotNull MutableComponent MutableComponent, @NotNull String id);
}
