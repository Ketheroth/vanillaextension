#!/bin/bash

lines=$(cat names.txt)

assets_path="../src/main/resources/assets/vanillaextension"
data_path="../src/main/resources/data/vanillaextension"
blank_assets_path="./blanks/assets/vanillaextension"
blank_data_path="./blanks/data/vanillaextension"

for line in ${lines}; do
  sed "s/blank/${line}/g" "${blank_assets_path}/blockstates/blank_stairs.json" > "${assets_path}/blockstates/${line}_stairs.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_stairs.json" > "${assets_path}/models/block/${line}_stairs.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_stairs_inner.json" > "${assets_path}/models/block/${line}_stairs_inner.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_stairs_outer.json" > "${assets_path}/models/block/${line}_stairs_outer.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/item/blank_stairs.json" > "${assets_path}/models/item/${line}_stairs.json"

  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/stairs/blank_stairs.json" > "${data_path}/advancements/recipes/stairs/${line}_stairs.json"
  sed "s/blank/${line}/g" "${blank_data_path}/loot_tables/blocks/blank_stairs.json" > "${data_path}/loot_tables/blocks/${line}_stairs.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/stairs/blank_stairs.json" > "${data_path}/recipes/stairs/${line}_stairs.json"

  echo "${line}_stairs"
done
