#!/bin/bash

lines=$(cat names.txt)

assets_path="../src/main/resources/assets/vanillaextension"
data_path="../src/main/resources/data/vanillaextension"
blank_assets_path="./blanks/assets/vanillaextension"
blank_data_path="./blanks/data/vanillaextension"

for line in ${lines}; do
  sed "s/blank/${line}/g" "${blank_assets_path}/blockstates/blank_trapdoor.json" > "${assets_path}/blockstates/${line}_trapdoor.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_trapdoor_bottom.json" > "${assets_path}/models/block/${line}_trapdoor_bottom.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_trapdoor_open.json" > "${assets_path}/models/block/${line}_trapdoor_open.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_trapdoor_top.json" > "${assets_path}/models/block/${line}_trapdoor_top.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/item/blank_trapdoor.json" > "${assets_path}/models/item/${line}_trapdoor.json"

  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/trapdoors/blank_trapdoor.json" > "${data_path}/advancements/recipes/trapdoors/${line}_trapdoor.json"
  sed "s/blank/${line}/g" "${blank_data_path}/loot_tables/blocks/blank_trapdoor.json" > "${data_path}/loot_tables/blocks/${line}_trapdoor.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/trapdoors/blank_trapdoor.json" > "${data_path}/recipes/trapdoors/${line}_trapdoor.json"

  echo "${line}_trapdoor"
done
