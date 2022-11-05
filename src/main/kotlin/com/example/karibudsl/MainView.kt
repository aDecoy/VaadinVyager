/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example.karibudsl

import com.example.demo2.voyager.MomentLoop
import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
class MainView : KComposite() {
    private lateinit var nameField: TextField
    private lateinit var greetButton: Button
    private lateinit var bigDiv: Div
    private lateinit var forwardSpeedLabel: Label
    private lateinit var backSpeedLabel: Label


    private lateinit var turnSpeedLabel: Label
    private lateinit var turnDegreeLabel: Label
    val momentLoop = MomentLoop()



    // The main view UI definition
    private val root = ui {
        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        verticalLayout(classNames = "centered-content") {

            // Use TextField for standard text input
            nameField = textField("Your name")

            // Use Button for a clickable button
            greetButton = button("Say hello") {
                setPrimary(); addClickShortcut(Key.ENTER)
            }
//            bigDiv = div {  }
//            bigDiv.setSizeFull()
//            bigDiv.setHeightFull()
        }
    }

    init {
        // attach functionality to the UI components.
        // It's a good practice to keep UI functionality separated from UI definition.

        // Button click listeners can be defined as lambda expressions
        greetButton.onLeftClick {
            Notification.show("Hello, ${nameField.value}")
        }
        nameField.addKeyPressListener {keypressedEvent ->
            val key = keypressedEvent.key
            println("pressed $key")

            val chr : Char = key.keys.get(0).toCharArray().get(0) //.toString().toCharArray().get(0)

            momentLoop.inputCharTilAction(chr)
        }
    }
}
