/*
 * Copyright 2019 Nicolas Maltais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maltaisn.msdfgdx.test.core

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.widget.MsdfLabel
import ktx.style.get
import java.text.NumberFormat


class TestLayout(skin: Skin) : Table(skin) {

    private val style: TestLayoutStyle = skin.get()

    private val fontStyle = FontStyle().apply {
        fontName = FONT_NAMES.first()
        color = FONT_COLORS.first().cpy()
        weight = FontStyle.WEIGHT_REGULAR
        shadowColor.set(1f, 1f, 1f, 0f)
        shadowOffset.set(2f, 2f)
        innerShadowColor.set(0f, 0f, 0f, 0f)
    }

    private val textScrollPane: ScrollPane
    private val labels = mutableListOf<MsdfLabel>()

    init {
        val textTable = Table()
        textTable.pad(20f)
        textTable.background = style.background
        textTable.color = BG_COLORS.first()

        val buttonTable = ButtonTable(skin)

        textScrollPane = ScrollPane(textTable)
        textScrollPane.setOverscroll(false, false)
        add(buttonTable).pad(20f).growY()
        add(textScrollPane).grow()

        val sizeIndicator = buttonTable.addBtn("Size: --") {}
        sizeIndicator.enabled = false

        // Add labels
        repeat(SIZES.size) {
            val label = MsdfLabel(TEXTS.first(), skin, fontStyle)
            label.touchable = Touchable.enabled
            label.addListener(object : InputListener() {
                override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    if (pointer == -1) {
                        label.debug = true
                        sizeIndicator.title = "Size: ${label.fontStyle.size.toInt()} px"
                    }
                }

                override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    if (pointer == -1) {
                        label.debug = false
                        sizeIndicator.title = "Size: --"
                    }
                }
            })

            textTable.add(label).growX().row()
            labels += label
        }
        updateFontStyle()

        // Add action buttons
        buttonTable.apply {
            addEnumBtn("Change text", TEXTS, null) { _, text ->
                for (label in labels) {
                    label.txt = text
                }
            }
            addEnumBtn("Font", FONT_NAMES, FONT_NAMES) { _, fontName ->
                fontStyle.fontName = fontName
                updateFontStyle()
            }
            addValueBtn("Weight", -0.5f, 0.5f, 0f, 0.05f) { _, weight, _ ->
                fontStyle.weight = weight
                updateFontStyle()
            }
            addEnumBtn("Color", FONT_COLORS, FONT_COLOR_NAMES) { _, color ->
                val fontColor = color.cpy()
                fontColor.a = fontStyle.color.a
                fontStyle.color = fontColor
                stage?.debugColor?.set(fontColor)
                updateFontStyle()
            }
            addValueBtn("Opacity", 0f, 1f, 1f, -0.1f,
                    NumberFormat.getPercentInstance()) { _, opacity, _ ->
                fontStyle.color.a = opacity
                updateFontStyle()
            }
            addEnumBtn("Background color", BG_COLORS, BG_COLOR_NAMES) { _, color ->
                textTable.color = color.cpy()
            }
            addToggleBtn("All caps") { _, allCaps ->
                fontStyle.isAllCaps = allCaps
                updateFontStyle()
            }
            addToggleBtn("Draw shadow") { _, shadowDrawn ->
                fontStyle.shadowColor.a = if (shadowDrawn) 1f else 0f
                updateFontStyle()
            }
            addEnumBtn("Shadow color", SHADOW_COLORS, SHADOW_COLOR_NAMES) { _, color ->
                val shadowColor = color.cpy()
                shadowColor.a = fontStyle.shadowColor.a
                fontStyle.shadowColor = shadowColor
                updateFontStyle()
            }
            addValueBtn("Shadow offset X", -4f, 4f, 2f, 0.5f) { _, offset, _ ->
                fontStyle.shadowOffset.x = offset
                updateFontStyle()
            }
            addValueBtn("Shadow offset Y", -4f, 4f, 2f, 0.5f) { _, offset, _ ->
                fontStyle.shadowOffset.y = offset
                updateFontStyle()
            }
            addValueBtn("Shadow smoothing", 0f, 0.5f, 0.1f, 0.1f) { _, smoothing, _ ->
                fontStyle.shadowSmoothing = smoothing
                updateFontStyle()
            }
            addToggleBtn("Draw inner shadow") { _, shadowDrawn ->
                fontStyle.innerShadowColor.a = if (shadowDrawn) 1f else 0f
                updateFontStyle()
            }
            addEnumBtn("Inner shadow color", SHADOW_COLORS, SHADOW_COLOR_NAMES, 1) { _, color ->
                val shadowColor = color.cpy()
                shadowColor.a = fontStyle.innerShadowColor.a
                fontStyle.innerShadowColor = shadowColor
                updateFontStyle()
            }
            addValueBtn("Inner shadow range", 0f, 0.5f, 0.3f, 0.1f) { _, range, _ ->
                fontStyle.innerShadowRange = range
                updateFontStyle()
            }
        }

        buttonTable.add().grow().row()  // For aligning button to the top
    }

    override fun setStage(stage: Stage?) {
        super.setStage(stage)
        stage?.debugColor?.set(fontStyle.color)
        stage?.scrollFocus = textScrollPane
    }

    private fun updateFontStyle() {
        for ((i, label) in labels.withIndex()) {
            val style = FontStyle(fontStyle)
            style.size = SIZES[i]
            label.fontStyle = style
        }
    }


    class TestLayoutStyle {
        lateinit var background: Drawable
    }

    companion object {
        private val SIZES = listOf(
                10f, 12f, 14f, 16f, 18f, 20f,
                24f, 28f, 32f, 36f, 40f,
                50f, 60f, 70f, 80f, 90f, 100f,
                128f, 196f, 256f, 384f, 512f)

        private val TEXTS = listOf("The quick brown fox jumps over the lazy dog",
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
                "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýÿŸþŽžŠš",
                "!\"#\$%&'()*+,-./:;<=>?[\\]^_`{|}~¡¢£€¥§©ª«¬®¯°±²³µ¶·¹º»Œœ¿×")

        private val FONT_NAMES = listOf("roboto", "roboto-sdf", "roboto-bold",
                "roboto-mono", "satisfy", "lora")

        private val FONT_COLORS = listOf(Color.BLACK, Color.WHITE, Color.BLUE, Color.RED)
        private val FONT_COLOR_NAMES = listOf("black", "white", "blue", "red")

        private val BG_COLORS = listOf(Color.WHITE, Color.BLACK, Color.YELLOW, Color.CYAN)
        private val BG_COLOR_NAMES = listOf("white", "black", "yellow", "cyan")

        private val SHADOW_COLORS = listOf(Color.WHITE, Color.BLACK, Color.YELLOW, Color.CYAN)
        private val SHADOW_COLOR_NAMES = listOf("white", "black", "yellow", "cyan")
    }

}
