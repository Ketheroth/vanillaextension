#!/bin/bash

lines=$(cat names.txt)

assets_path="../src/main/resources/assets/vanillaextension"
data_path="../src/main/resources/data/vanillaextension"
blank_assets_path="./blanks/assets/vanillaextension"
blank_data_path="./blanks/data/vanillaextension"

for line in ${lines}; do
  sed "s/blank/${line}/g" "${blank_assets_path}/blockstates/blank_wall.json" > "${assets_path}/blockstates/${line}_wall.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_wall_inventory.json" > "${assets_path}/models/block/${line}_wall_inventory.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_wall_post.json" > "${assets_path}/models/block/${line}_wall_post.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_wall_side.json" > "${assets_path}/models/block/${line}_wall_side.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_wall_side_tall.json" > "${assets_path}/models/block/${line}_wall_side_tall.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/item/blank_wall.json" > "${assets_path}/models/item/${line}_wall.json"

  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/walls/blank_wall.json" > "${data_path}/advancements/recipes/walls/${line}_wall.json"
  sed "s/blank/${line}/g" "${blank_data_path}/loot_tables/blocks/blank_wall.json" > "${data_path}/loot_tables/blocks/${line}_wall.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/walls/blank_wall.json" > "${data_path}/recipes/walls/${line}_wall.json"

  echo "${line}_wall"
done
