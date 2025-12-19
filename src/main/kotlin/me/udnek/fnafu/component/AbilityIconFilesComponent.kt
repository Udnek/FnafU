package me.udnek.fnafu.component

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.fnafu.component.kit.Kit
import net.kyori.adventure.key.Key


class AbilityIconFilesComponent(val kit: Kit) : AutoGeneratingFilesItem.Generated() {

    override fun getDefinition(itemModel: Key): JsonObject {
        return JsonParser.parseString(
            replacePlaceHolders(
                """
                {
                  "model": {
                    "type": "minecraft:select",
                    "property": "minecraft:display_context",
                    "cases": [
                      {
                        "when": "gui",
                        "model": {
                          "type": "minecraft:model",
                          "model": "%model_path%"
                        }
                      },
                      {
                        "when": "firstperson_righthand",
                        "model": {
                          "type": "minecraft:model",
                          "model": "fnafu:item/animatronic/%playerClass%/hand"
                        }
                      },
                      {
                        "when": "firstperson_lefthand",
                        "model": {
                          "type": "minecraft:model",
                          "model": "fnafu:item/animatronic/%playerClass%/hand"
                        }
                      }
                    ],
                    "fallback": {
                      "type": "minecraft:empty"
                    }
                  }
                }
                """.replace("%playerClass%", kit.key.value()), itemModel
            )
        ) as JsonObject
    }
}

