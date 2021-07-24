#!/bin/bash

lines=$(cat names.txt)

assets_path="../src/main/resources/assets/vanillaextension"
data_path="../src/main/resources/data/vanillaextension"
blank_assets_path="./blanks/assets/vanillaextension"
blank_data_path="./blanks/data/vanillaextension"

for line in ${lines}; do
  sed "s/blank/${line}/g" "${blank_assets_path}/blockstates/blank_slab.json" > "${assets_path}/blockstates/${line}_slab.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_slab.json" > "${assets_path}/models/block/${line}_slab.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_slab_top.json" > "${assets_path}/models/block/${line}_slab_top.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/item/blank_slab.json" > "${assets_path}/models/item/${line}_slab.json"

  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/slabs/blank_slab.json" > "${data_path}/advancements/recipes/slabs/${line}_slab.json"
  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/blocks/blank_from_blank_slab.json" > "${data_path}/advancements/recipes/slabs/${line}_from_${line}_slab.json"
  sed "s/blank/${line}/g" "${blank_data_path}/loot_tables/blocks/blank_slab.json" > "${data_path}/loot_tables/blocks/${line}_slab.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/slabs/blank_slab.json" > "${data_path}/recipes/slabs/${line}_slab.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/blocks/blank_from_blank_slab.json" > "${data_path}/recipes/blocks/${line}_from_${line}_slab.json"

  echo "${line}_slab"
done
