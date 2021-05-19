#!/bin/bash

lines=$(cat names.txt)

assets_path="../src/main/resources/assets/vanillaextension"
data_path="../src/main/resources/data/vanillaextension"
blank_assets_path="./blanks/assets/vanillaextension"
blank_data_path="./blanks/data/vanillaextension"

for line in ${lines}; do
  sed "s/blank/${line}/g" "${blank_assets_path}/blockstates/blank_fence.json" > "${assets_path}/blockstates/${line}_fence.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_inventory.json" > "${assets_path}/models/block/${line}_fence_inventory.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_post.json" > "${assets_path}/models/block/${line}_fence_post.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_side.json" > "${assets_path}/models/block/${line}_fence_side.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/item/blank_fence.json" > "${assets_path}/models/item/${line}_fence.json"

  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/fences/blank_fence.json" > "${data_path}/advancements/recipes/fences/${line}_fence.json"
  sed "s/blank/${line}/g" "${blank_data_path}/loot_tables/blocks/blank_fence.json" > "${data_path}/loot_tables/blocks/${line}_fence.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/fences/blank_fence.json" > "${data_path}/recipes/fences/${line}_fence.json"

  echo "${line}_fence"
done
