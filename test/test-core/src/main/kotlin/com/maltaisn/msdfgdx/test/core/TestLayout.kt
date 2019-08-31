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
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.widget.MsdfLabel
import ktx.actors.onClick
import ktx.log.info


class TestLayout(skin: Skin) : Table(skin) {

    init {

        add(MsdfLabel("Test MSDF text", skin, FontStyle().apply {
            fontName = "roboto"
            size = 128f
            color = Color.BLACK
            weight = FontStyle.WEIGHT_REGULAR
        })).expand().row()

        val btn = MsdfButton(skin, "Test button")
        btn.onClick { info { "Button clicked" } }
        add(btn).expand()
    }

}
