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

import dev.vankka.simpleast.core.node.Node;
import me.hypherionmc.mcdiscordformatter.minecraft.MinecraftSerializerOptions;
import net.minecraft.util.text.IFormattableTextComponent;

import java.util.function.Function;

/**
 * Interface for rendering {@link Node}s into Minecraft {@link net.minecraft.text.MutableComponent}s.
 */
public interface MinecraftNodeRenderer extends NodeRenderer<IFormattableTextComponent> {

    /**
     * Renders the given {@link Node} onto the provided
     * {@link MutableComponent} using the given
     * {@link MinecraftSerializerOptions}.
     *
     * @param baseComponent      the input component to apply the node to
     * @param node               the node
     * @param serializerOptions  the serializer options for this render
     * @param renderWithChildren a function to allow rendering a node recursively
     * @return the new component with the node applied to it
     */
    IFormattableTextComponent render(IFormattableTextComponent baseComponent, Node<Object> node, MinecraftSerializerOptions<IFormattableTextComponent> serializerOptions,
                       Function<Node<Object>, IFormattableTextComponent> renderWithChildren);
}
