#!/bin/bash

lines=$(cat names.txt)

assets_path="../src/main/resources/assets/vanillaextension"
data_path="../src/main/resources/data/vanillaextension"
blank_assets_path="./blanks/assets/vanillaextension"
blank_data_path="./blanks/data/vanillaextension"

for line in ${lines}; do
  sed "s/blank/${line}/g" "${blank_assets_path}/blockstates/blank_fence_gate.json" > "${assets_path}/blockstates/${line}_fence_gate.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_gate.json" > "${assets_path}/models/block/${line}_fence_gate.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_gate_open.json" > "${assets_path}/models/block/${line}_fence_gate_open.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_gate_wall.json" > "${assets_path}/models/block/${line}_fence_gate_wall.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/block/blank_fence_gate_wall_open.json" > "${assets_path}/models/block/${line}_fence_gate_wall_open.json"
  sed "s/blank/${line}/g" "${blank_assets_path}/models/item/blank_fence_gate.json" > "${assets_path}/models/item/${line}_fence_gate.json"

  sed "s/blank/${line}/g" "${blank_data_path}/advancements/recipes/fence_gates/blank_fence_gate.json" > "${data_path}/advancements/recipes/fence_gates/${line}_fence_gate.json"
  sed "s/blank/${line}/g" "${blank_data_path}/loot_tables/blocks/blank_fence_gate.json" > "${data_path}/loot_tables/blocks/${line}_fence_gate.json"
  sed "s/blank/${line}/g" "${blank_data_path}/recipes/fence_gates/blank_fence_gate.json" > "${data_path}/recipes/fence_gates/${line}_fence_gate.json"

  echo "${line}"
done
